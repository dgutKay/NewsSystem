<!DOCTYPE html>
<html lang="cn">
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />

<link rel="stylesheet"
	href="/NewsSystem/plugin/bootstrap-4.2.1-dist/css/bootstrap.min.css" />

<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script src="/NewsSystem/plugin/bootstrap-4.2.1-dist/js/popper.min.js"></script>
<script
	src="/NewsSystem/plugin/bootstrap-4.2.1-dist/js/bootstrap.min.js"></script>

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
			$.post(
				url,
				data,
				function(data, textStatus) {
					if (textStatus == "success") {
						$("#showComment").html(data);
						$("#addCommentTextarea").val(""); //清空内容
					} else alert("添加失败！");
				},
				"html"
			);

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
		$("#showComment").load(
			"/NewsSystem/servlet/CommentServlet?condition=showComment",
			$("#pageInfo").serialize()
		);
	}

	function praise(commentId, thisobj) {
		//thisobj表示当前被点击的元素（标签）对象
		$.ajax({
			type : "post",
			dataType : "json",
			url : "/NewsSystem/servlet/CommentServlet?condition=praise&commentId=" +
				commentId,
			data : $("#pageInfo").serialize(),
			success : function(data) {
				if (data != null) {
					if (data.result > 0) {
						$(thisobj)
							.next()
							.html(data.result); //next（）：下一个兄弟节点，text(data)：设置元素内部文字内容为data
					} else alert("点赞失败！");
				}
			},
			error : function() {
				alert("无权限！");
			}
		});
	}

	function model(commentId) {
		$("#model").modal("show");
		$("#replyCommentForm").on("submit", function(ev) {
			var url = "/NewsSystem/servlet/CommentServlet?condition=addComment" +
				"&commentId=" +
				commentId;
			$("#page").val("1");
			var data = $("#pageInfo").serialize() +
			"&" +
			$("#replyCommentForm").serialize();

			$("#showComment").load(url, data, function() {
				//关闭模态框
				$("#model").modal("hide");
			});
			ev.preventDefault();
		});
	}
</script>
</head>
<body>
	<!--新闻内容 -->
	<div class="container">
		<div class="row">
			<div class="col text-center m-3">
				<h4>${news.caption}</h4>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col text-center mb-2">
				作者：${news.author}&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
				<myTag:LocalDateTimeTag dateTime="${news.newsTime}" type="YMDHM" />
			</div>
		</div>
	</div>

	<div class="container border-top border-bottom">
		<div class="row">
			<div class="col">${news.content}</div>
		</div>
	</div>

	<!--回复评论 -->
	<!--justify-content-center：内容居中 ；  col-auto：栅格内容居中 -->
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-10 col-md-8 col-auto m-3">
				<form id="addCommentForm">
					<div class="form-group">
						<textarea class="form-control" name="content"
							id="addCommentTextarea" rows="4" required></textarea>
					</div>
					<!--text-center：将button居中 -->
					<div class="text-center form-group">
						<button type="submit" class="btn btn-primary mb-2">提 交 评
							论</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="showComment">
		<div class="container">
			<div class="row justify-content-center">
				<div class="col-md-10 col-auto">

					<div>评论</div>
					<#list commentUserViews as commentUserView>
					<!--media表示为Media Object组件 -->
					<div class="media border-top mt-2 pt-1">
						<img class="mr-3" style="width:3rem"
								src="${commentUserView.headIconUrl}"
								title="${commentUserView.userName}">
						<div class="media-body">
							<!--clearfix：清除浮动 ；  float-left：utilities工具中的float，往左边浮动-->
							<div class="clearfix">
								<div class="float-left mr-2">${commentUserView.userName}</div>
								<div class="float-left">${commentUserView.time}</div>
							</div>

							<!--float-right：utilities工具中的float，往右边浮动-->
							<div class="float-right">
								<!--span是行内标签，可以与其他内容同一行，此处用span的目的是为了作为容器，控制水平间距 -->
								<span class="mr-2">第${commentUserView.stair}楼</span> <a
									class="mr-2" href="javascript:void(0);"
									onclick="praise(this,${commentUserView.commentId},${news.newsId});">
									<img src="/NewsSystem/upload/images/praise.png">
								</a> <span class="mr-2">${commentUserView.praise}</span> <a
									href="javascript:void(0);"
									onclick="model(${commentUserView.commentId});">回复</a>
							</div>

							<div>${commentUserView.content}</div>
						</div>
					</div>
					</#list>

						<!--nav标签和之内的ul以及pagination样式，表示分页组件Pagination-->
						<nav class="mt-5">
							<ul class="pagination justify-content-center">
								<#if (pageInformation.page > 1) >
								<li class="page-item"><a class="page-link"
									href="javascript:void(0);" onclick="getOnePage('first','');">首页</a>
								</li></#if> <#if (pageInformation.page > 1) >
								<li class="page-item"><a class="page-link"
									href="javascript:void(0);" onclick="getOnePage('pre','');">上一页</a>
								</li></#if> <#if (pageInformation.page <
								pageInformation.totalPageCount) >
								<li class="page-item"><a class="page-link"
									href="javascript:void(0);" onclick="getOnePage('next','');">下一页</a>
								</li></#if> <#if (pageInformation.page <
								pageInformation.totalPageCount) >
								<li class="page-item"><a class="page-link"
									href="javascript:void(0);" onclick="getOnePage('last','');">尾页</a>
								</li></#if>
							</ul>
							<ul class="pagination justify-content-center">
								总共：${pageInformation.totalPageCount} 页
							</ul>
						</nav>

						<form id="pageInfo">
							<input type="hidden" name="newsId" id="newsId"
								value="${news.newsId}" /> <input type="hidden" name="page"
								id="page" value="${pageInformation.page}" /> <input
								type="hidden" name="pageSize" id="pageSize"
								value="${pageInformation.pageSize}" /> <input type="hidden"
								name="totalPageCount" id="totalPageCount"
								value="${pageInformation.totalPageCount}" /> <input
								type="hidden" name="allRecordCount" id="allRecordCount"
								value="${pageInformation.allRecordCount}" />
						</form>
					</div>
				</div>
			</div>
		</div>


		<div class="container">
			<div class="row">
				<div class="col text-center m-3">
					<!--modal组件  模态框-->
					<div id="model" class="modal" tabindex="-1" role="dialog">
						<div class="modal-dialog" role="document">
							<div class="modal-content">

								<div class="modal-header">
									<h5 class="modal-title">回复评论</h5>
									<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>

								<form id="replyCommentForm">
									<div class="modal-body">
										<!--w-100为Utilities的Sizing组件  表示宽度100%-->
										<textarea class="w-100 border" name='content' rows='8'
											id='content' required></textarea>
									</div>
									<div class="modal-footer">
										<button type="submit" class="btn btn-primary" id="submit">提交</button>
										<button type="button" class="btn btn-primary"
											data-dismiss="modal">取消</button>
									</div>
								</form>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
