<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link href="/NewsSystem/css/1.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//提交用户对文章的评论
		$("#addCommentForm").on("submit", function(ev) {
			var url = "/NewsSystem/servlet/CommentServlet?condition=addComment";
			$("#page").val("1");
			var data = $("#pageInfo").serialize() + "&" + $("#addCommentForm").serialize();
			//jQuery.post(url, [data], [callback], [type])
			//callback:发送成功时回调函数。
			//type:返回内容格式，xml, html, script, json, text, _default。
			$.post(url, data, function(data, textStatus) {
				if (textStatus == "success") {
					$("#showComment").html(data);
					$("#addCommentTextarea").val(""); //清空内容
				} else
					alert("添加失败！");
			}, "html");

			ev.preventDefault();
		});
	});

	function getOnePage(type, orderFieldName) {
		var page = $("#page");

		pageValue = parseInt(page.val());
		if (type == "first") {
			page.val("1");
		} else if (type == "pre") {
			pageValue -= 1;
			page.val(pageValue.toString());
		} else if (type == "next") {
			pageValue += 1;
			page.val(pageValue.toString());
		} else if (type == "last") {
			page.val($("#totalPageCount").val());
		}

		//提交
		$("#showComment").load("/NewsSystem/servlet/CommentServlet?condition=showComment", $("#pageInfo").serialize());
	}

	function praise(commentId, thisobj) { //thisobj表示当前被点击的元素（标签）对象
		$.ajax({
			type : "post",
			dataType : "json",
			url : "/NewsSystem/servlet/CommentServlet?condition=praise&commentId=" + commentId,
			data : $("#pageInfo").serialize(),
			success : function(data) {
				if (data != null) {
					if (data.result > 0) {
						$(thisobj).next().html(data.result); //next（）：下一个兄弟节点，text(data)：设置元素内部文字内容为data
					} else alert("点赞失败！");
				}
			},
			error : function() {
				alert("无权限！请登录！");
			}
		});
	}

	function model(commentId) {
		$("#model").show();
		$("#replyCommentForm").on("submit", function(ev) {
			var url = "/NewsSystem/servlet/CommentServlet?condition=addComment" + "&commentId=" + commentId;
			$("#page").val("1");
			var data = $("#pageInfo").serialize() + "&" + $("#replyCommentForm").serialize();

			$("#showComment").load(url, data);
			ev.preventDefault();
		});
	}
</script>
</head>
<body>
	<!--新闻内容 -->
	<div class="center" style="width:800px;margin-top:30px;">
		<table width="800" border="0">
			<tbody>
				<tr>
					<td class="newsCaption">${requestScope.news.caption}</td>
				<tr>
					<td align="center" height="50">作者：${requestScope.news.author}&nbsp;
						&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; <myTag:LocalDateTimeTag
							dateTime="${requestScope.news.newsTime}" type="YMDHM" />
					</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
				<tr>
					<td>${requestScope.news.content}</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
			</tbody>
		</table>
	</div>

	<!--回复评论 -->
	<div id="addComment">
		<div class="center" style="width:800px;margin-top:30px;">
			<form id="addCommentForm">
				<div class="center" style="width:500px">
					<textarea name="content" cols="72" rows="8" id="addCommentTextarea"
						required></textarea>
				</div>
				<p>
				<div class="center" style="width:50px;height:80px;">
					<br> <input type="submit" value="  提 交 评 论  ">
				</div>
			</form>
		</div>
	</div>

	<div id="showComment">
		<form id="showCommnentForm">
			<div class="center" style="width:600px">
				<a name="commentsStart"></a>
				<div class="commentsHead">最新评论</div>
				<c:forEach items="${requestScope.commentUserViews}"
					var="commentUserView">
					<div style='margin-bottom: 10px;'>
						<div>
							<div class="commentIcon">
								<img width="35" src="${commentUserView.headIconUrl}">
							</div>
							<div class="comment1">
								<div class="commentAuthor">${commentUserView.userName}</div>
								<div class="commentTime">
									<myTag:TimestampTag dateTime="${commentUserView.time}"
										type="latest" />
								</div>
							</div>
							<div class="comment2">
								<div class="comment3">
									<a class="commentReply" href="javascript:void(0);"
										onclick="model(${commentUserView.commentId});"> 回复 </a>
								</div>
								<div class="comment4">
									<span class="commentPraiseText">第${commentUserView.stair}楼</span>
									<a class="commentPraiseA" href="javascript:void(0);"
										onclick="praise(${commentUserView.commentId},this);"> </a><span
										class="commentPraiseText">${commentUserView.praise}</span>
								</div>
							</div>
						</div>
						<div class="clear">
							<div class="comment5 ">
								<div class="commentContent">${commentUserView.content}</div>
							</div>
						</div>
					</div>
				</c:forEach>
				<div class="commentsHead" style="text-align:center;">
					<table align="center" cellpadding="5" cellspacing="0" width="600px">
						<tr>
							<c:if test="${requestScope.pageInformation.page>1 }">
								<td><a href="javascript:void(0);"
									onclick="getOnePage('first','');">First</a>
							</c:if>
							<c:if test="${requestScope.pageInformation.page>1 }">
								<td><a href="javascript:void(0);"
									onclick="getOnePage('pre','');">Previous</a>
							</c:if>
							<c:if
								test="${requestScope.pageInformation.page< requestScope.pageInformation.totalPageCount }">
								<td><a href="javascript:void(0);"
									onclick="getOnePage('next','');">Next</a>
							</c:if>
							<c:if
								test="${requestScope.pageInformation.page< requestScope.pageInformation.totalPageCount }">
								<td><a href="javascript:void(0);"
									onclick="getOnePage('last','');">Last</a>
							</c:if>
						</tr>
						<tr align="center">
							<td colspan="4">total:
								${requestScope.pageInformation.totalPageCount} pages</td>
						</tr>
					</table>
				</div>
			</div>
		</form>

		<div id="model" style="display: none;">
			<form id="replyCommentForm">
				<div class="modelContent">
					<table>
						<tbody>
							<tr>
								<td colspan="2"><textarea name="content" cols="60" rows="8"
										id="content" required></textarea></td>
							</tr>
							<tr>
								<td align="center"><input type="submit" name="submit"
									id="submit" value="提交"></td>
								<td align="center"><input type="button"
									onclick="$('#model').hide();" value="取消"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</form>
		</div>

		<form id="pageInfo">
			<input type="hidden" name="newsId" id="newsId"
				value="${param.newsId}"> <input type="hidden" name="page"
				id="page" value="${param.page}"> <input type="hidden"
				name="pageSize" id="pageSize" value="${param.pageSize}"> <input
				type="hidden" name="totalPageCount" id="totalPageCount"
				value="${requestScope.pageInformation.totalPageCount}" /> <input
				type="hidden" name="allRecordCount" id="allRecordCount"
				value="${requestScope.pageInformation.allRecordCount}">
		</form>
	</div>
</body>
</html>