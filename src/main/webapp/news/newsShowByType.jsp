<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<link href="/NewsSystem/css/1.css" rel="stylesheet" type="text/css">
<meta charset="utf-8">
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	//更新新闻列表
	function refreshNews(data) {
		if (data != null) {
			$("#newsShowByTypeUL").empty(); //清空新闻列表的所有内容
			var newsShow = "";

			for (var i = 2; i < data.length; i++) {
				newsShow += "<li><a href='/NewsSystem/servlet/NewsServlet?condition=showANews&newsId=" + data[i].newsId + "&page=1&pageSize=5' target='_blank'>" + data[i].caption + "</a></li>";
			}
			$("#newsShowByTypeUL").html(newsShow);
			//更新分页信息
			$("#page").val(data[0].page);
			$("#pageSize").val(data[0].pageSize);
			$("#totalPageCount").val(data[0].totalPageCount);
			$("#allRecordCount").val(data[0].allRecordCount);
			$("#newsType").val(data[1]);
		}
	}

	function changeNewsByType() {
		$.ajax({
			url : "/NewsSystem/servlet/NewsServlet?condition=showNewsByNewsTypeAjax",
			type : "post",
			data : $("#myform").serialize(),
			dataType : "json",
			cache : false,
			error : function() {
				alert("Wrong!");
			},
			success : function(data) {
				refreshNews(data);
			}
		});
	}

	$(document).ready(function() {
		$("#frameDiv").height($("#leftDiv").outerHeight());

		$("#leftDiv li").hover(
			function() {
				$(this).css("color", "red");
			},
			function() {
				$(this).css("color", "black");
			}
		);

		$("#leftDiv li").click(function() {
			var label = $(this).text();

			if (label == "全部")
				label = "all";

			$.ajax({
				url : "/NewsSystem/servlet/NewsServlet?condition=showNewsByNewsTypeAjax",
				type : "post",
				data : {
					"newsType" : label,
					"page" : "1",
					"pageSize" : "10"
				},
				dataType : "json",
				cache : false,
				error : function() {
					alert("Wrong!");
				},
				success : function(data) {
					refreshNews(data);
				}
			});
		});

		$("#previous").click(function() {
			var page = parseInt($("#page").val());
			if (page != 1) {
				page--;
				$("#page").val(page.toString());
			}
			changeNewsByType();
		});

		$("#next").click(function() {
			var pageCount = parseInt($("#totalPageCount").val());
			var page = parseInt($("#page").val());
			if (page < pageCount) {
				page++;
				$("#page").val(page.toString());
			}
			changeNewsByType();
		});
	});
</script>
</head>

<body>
	<div class="center" style="width:810px">
		<form action="" id="myform" method="post">
			<div class="newsShowByType_frame center" id="frameDiv">
				<div class="newsShowByType_left" id="leftDiv">
					<ul style="list-style-type: none;">
						<li>全部</li>
						<li>国际</li>
						<li>社会</li>
						<li>体育</li>
						<li>科学</li>
						<li>汽车</li>
					</ul>
				</div>
				<div class="newsShowByType_rightTop" id="newsShowByType">
					<div>
						<ul id="newsShowByTypeUL">
							<c:forEach items="${requestScope.newses}" var="news">
								<li><a
									href="/NewsSystem/servlet/NewsServlet?condition=showANews&newsId=${news.newsId}&page=1&pageSize=5"
									target="_blank">${news.caption}</a></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="newsShowByType_rightBottom">
					<div class="center" style="width:150px;">
						<a id="previous" href="javascript:void(0);">上一页</a> <a id="next"
							href="javascript:void(0);">下一页</a>
					</div>
				</div>
			</div>

			<input type="hidden" name="page" id="page"
				value="${requestScope.pageInformation.page}"> <input
				type="hidden" name="pageSize" id="pageSize"
				value="${requestScope.pageInformation.pageSize}"> <input
				type="hidden" name="totalPageCount" id="totalPageCount"
				value="${requestScope.pageInformation.totalPageCount}"> <input
				type="hidden" name="allRecordCount" id="allRecordCount"
				value="${requestScope.pageInformation.allRecordCount}"> <input
				type="hidden" name="newsType" id="newsType"
				value="${requestScope.newsType}">
		</form>
	</div>
</body>
</html>