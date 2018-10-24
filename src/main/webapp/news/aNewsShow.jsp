<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>aNewsShow.jsp</title>
<link href="/NewsSystem/css/1.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	//将传过来的时间转换成毫秒
	function getDateTimeStamp(dateStr) {
		return Date.parse(dateStr.replace(/-/gi, "/"));
	}

	//生成要展示的评论时间
	function displayTime(data) {
		var str = data;

		//将字符串转换成时间格式
		var timePublish = new Date(str);
		var timeNow = new Date();
		var minute = 1000 * 60;
		var hour = minute * 60;
		var day = hour * 24;
		var month = day * 30;
		var diffValue = timeNow - timePublish;
		var diffMonth = diffValue / month;
		var diffWeek = diffValue / (7 * day);
		var diffDay = diffValue / day;
		var diffHour = diffValue / hour;
		var diffMinute = diffValue / minute;

		if (diffValue < 0)
			alert("Wrong time!");
		else if (diffMonth > 3) { //时间太久，显示评论实际时间
			result = timePublish.getFullYear() + "-";
			result += timePublish.getMonth() + "-";
			result += timePublish.getDate();
		} else if (diffMonth > 1)
			result = parseInt(diffMonth) + "月前";
		else if (diffWeek > 1)
			reuslt = parseInt(diffWeek) + "周前";
		else if (diffDay > 1)
			result = parseInt(diffDay) + "天前";
		else if (diffHour > 1)
			result = parseInt(diffHour) + "小时前";
		else if (diffMinute > 1)
			result = parseInt(diffMinute) + "分钟前";
		else
			result = "刚刚发表";

		return result;
	}

	//生成显示一条评论需要的信息，主要包括：1、生成显示一条评论的html代码；2、更新评论分页信息。showAComment会在评论展示、添加时被重用多次
	function showAComment(user, comment, pageInfo) {
		var time = displayTime(getDateTimeStamp(comment.time));
		var commentUserViews = ""; //展示回复内容的html


		commentUserViews += "<div style='margin-bottom: 10px;'>\
            		<div>\
            			<div class='commentIcon'>\
							<img width='35' src=" + user.headIconUrl + ">\
						</div>\
						<div class='comment1'>\
							<div class='commentAuthor'>" + user.name + "</div>\
							<div class='commentTime'>" + time + "</div>\
						</div>\
						<div class='comment2'>\
							<div class='comment3'>\
								<a class='commentReplay' href='javascript:void(0);' onclick='model(" + comment.commentId + ",this);'>回复</a>\
							</div>\
							<div class='comment4'>\
								<span class='commentPraiseText'>第" + comment.stair + "楼</span>\
								<a class='commentPraiseA' href='javascript:void(0);' onclick='praise(" + comment.commentId + "," + comment.newsId + ",this);'></a>\
								<span class='commentPraiseText'>" + comment.praise + "</span>\
							</div>\
						</div>\
					</div>\
					<div class='clear'>\
						<div class='comment5'>\
							<div class='commentContent'>" + comment.content + "</div>\
						</div>\
					</div>\
				</div>";
		//设置分页信息
		$("#newsId").val(comment.newsId);
		$("#page").val(pageInfo.page);
		$("#pageSize").val(pageInfo.pageSize);
		$("#totalPageCount").val(pageInfo.totalPageCount);
		$("#allRecordCount").val(pageInfo.allRecordCount);

		//跳转到评论起始处
		$("html,body").scrollTop($(".commentsHead").offset().top);

		return commentUserViews;
	}

	//上一页或下一页的评论
	function changeCommentPage() {
		$.ajax({
			url : "/NewsSystem/servlet/CommentServlet?condition=showComment",
			type : "post",
			data : $("#myForm").serialize(),
			dataType : "json",
			error : function() {
				alert("Wrong!");
			},
			success : function(data) {
				if (data != null) {
					var commentUserViews = "";
					for (var i = 1; i < data.length; i++) {
						let user = {
							"headIconUrl" : data[i].headIconUrl,
							"name" : data[i].userName
						};
						let comment = {
							"commentId" : data[i].commentId,
							"stair" : data[i].stair,
							"newsId" : data[i].newsId,
							"praise" : data[i].praise,
							"content" : data[i].content,
							"time" : data[i].time
						};
						commentUserViews += showAComment(user, comment, data[0]);
					}
					$("#commentViews #showCommentDiv").html(commentUserViews);
				}
			}
		});
	}

	$(document).ready(function() {
		//提交用户对文章的评论
		$("#submit").click(function() {
			$.ajax({
				type : "post",
				dataType : "json",
				url : "/NewsSystem/servlet/CommentServlet?condition=addComment",
				data : $("#myForm").serialize(),
				success : function(data) {
					if (data != null) {
						var commentUserViews = showAComment(data[0], data[1], data[2]);
						$("#commentViews #showCommentDiv").prepend(commentUserViews);
						$("#textarea").val("");
					}
					else
						alert("Wrong!");
				},
				error : function() {
					alert("Limited authority! Please login!");
				}
			});
		});

		$("#previous").click(function() {
			var page = parseInt($("#page").val());
			if (page != 1) {
				page--;
				$("#page").val(page.toString());
			}
			changeCommentPage();
		});

		$("#next").click(function() {
			var pageCount = parseInt($("#totalPageCount").val());
			var page = parseInt($("#page").val());
			if (page < pageCount) {
				page++;
				$("#page").val(page.toString());
			}
			changeCommentPage();
		});
	});

	function praise(commentId, newsId, thisobj) { //thisobj表示当前被点击的元素（标签）对象
		$.ajax({
			type : "post",
			dataType : "json",
			url : "/NewsSystem/servlet/CommentServlet?condition=praise",
			data : {
				"newsId" : newsId,
				"commentId" : commentId
			},
			success : function(data) {
				$(thisobj).next().text(data); //next（）：下一个兄弟节点，text(data)：设置元素内部文字内容为data
			},
			error : function() {
				alert("Limited authority! Please login!");
			}
		});
	}

	//回复用户的评论（对评论的评论）
	function subcomment() {
		$.ajax({
			type : "post",
			dataType : "json",
			url : "/NewsSystem/servlet/CommentServlet?condition=addComment",
			data : {
				"content" : $("#content").val(),
				"newsId" : $("#newsId").val(),
				"page" : $("#page").val(),
				"pageSize" : $("#pageSize").val(),
				"allRecordCount" : $("#allRecordCount").val(),
				"commentId" : $("#commentId").val()
			},
			success : function(data) {
				if (data != null) {
					var commentUserViews = showAComment(data[0], data[1], data[2]); //展示回复内容的html
					$('#commentViews #showCommentDiv').prepend(commentUserViews); //prepend():向每个匹配的元素  内部 前置内容。这是向所有匹配元素内部的 开始处 插入内容的最佳方式。
					$('#model').remove(); //删除添加回复的界面
				} else alert("Wrong!");
			},
			error : function() {
				alert("Limited authority! Please login!");
			}
		});
	}

	//取消回复用户的评论
	function cancel() {
		$("#model").remove();
	}

	function model(commentId, thisobj) {
		$("#model").remove(); //删除已存在的
		var subComment = "<div id='model'><div class='modelContent'><table><tbody><tr><td colspan='2'><textarea name='content' cols='60' rows='8' id='content' required></textarea></td></tr><tr><td align='center'><input type='button' name='submit' id='submit' value='提交' onclick='subcomment();'></td><td align='center'><input type='submit' onclick='cancel();' value='取消'></td></tr></tbody></table></div><input type='hidden' name='newsId' id='newsId' value='${param.newsId}'><input type='hidden' name='page' id='page' value='${param.page}'><input type='hidden' name='pageSize' id='pageSize' value='${param.pageSize}'><input type='hidden' name='commentId' id='commentId'><input type='hidden' name='totalPageCount' id='totalPageCount' value='${param.totalPageCount}'></div>";
		$(thisobj).parent().parent().parent().parent().append(subComment); //在当前评论下方显示  添加回复的界面
		$("#commentId").val(commentId);
	}
</script>
</head>
<body>
	<form action="" method="post" id="myForm">
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

			<div class="center" style="width:500px">
				<textarea name="content" cols="72" rows="8" id="textarea" required></textarea>
			</div>
			<p>
			<div class="center" style="width:50px;height:80px;">
				<br> <input type="button" name="submit" id="submit"
					value="  提 交 评 论  ">
			</div>
			<input type="hidden" name="newsId" id="newsId"
				value="${param.newsId}"> <input type="hidden" name="page"
				id="page" value="${param.page}"> <input type="hidden"
				name="pageSize" id="pageSize" value="${param.pageSize}"> <input
				type="hidden" name="totalPageCount" id="totalPageCount"
				value="${requestScope.pageInformation.totalPageCount}" /> <input
				type="hidden" name="allRecordCount" id="allRecordCount"
				value="${requestScope.pageInformation.allRecordCount}">
		</div>
	</form>

	<div class="commentsHead center">最新评论</div>
	<div id="commentViews">
		<div class="center" style="width:600px" id='showCommentDiv'>
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
								<a class="commentReplay" href="javascript:void(0);"
									onclick="model(${commentUserView.commentId},this);"> 回复 </a>
							</div>
							<div class="comment4">
								<span class="commentPraiseText">第${commentUserView.stair}楼</span>
								<a class="commentPraiseA" href="javascript:void(0);"
									onclick="praise(${commentUserView.commentId},${param.newsId},this);">
								</a><span class="commentPraiseText">${commentUserView.praise}</span>
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
		</div>
	</div>
	<div class="commentsHead center" style="text-align:center;">
		<a href="javascript:void(0);" id="previous">Previous</a> <a
			href="javascript:void(0);" id="next">Next</a>
	</div>
</body>
</html>