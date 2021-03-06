<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="/NewsSystem/css/1.css" rel="stylesheet" type="text/css">

<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#frameDiv").height($("#leftDiv").outerHeight());

		$("#leftDiv li").hover( //新闻类别的鼠标事件，改变字体颜色
			function() { //鼠标移动到li上时
				$(this).css("color", "red");
			},
			function() { //鼠标从li上移开时
				$(this).css("color", "black");
			}
		);

		$("#leftDiv li").click(function() {
			var label = $(this).text();

			//jquery的load（）方法：载入远程 HTML文件代码并插入至 DOM 中。这里将返回的网页插入到rightDiv中
			if (label === "显示个人信息")
				$("#rightDiv").load("/NewsSystem/servlet/UserServlet?condition=showUserInformation");
			else if (label === "修改个人信息")
				$("#rightDiv").load("/NewsSystem/servlet/UserServlet?condition=changeUserInformation");
			else if (label === "修改密码")
				$("#rightDiv").load("/NewsSystem/user/manage/changePassword.jsp");
			else if (label === "浏览用户")
				$("#rightDiv").load("/NewsSystem/servlet/UserServlet?condition=showPage&page=1&pageSize=2");
			else if (label === "审查信息")
				$("#rightDiv").load("/NewsSystem/servlet/UserServlet?condition=check&page=1&pageSize=2");
			else if (label === "查询用户")
				$("#rightDiv").load("/NewsSystem/manager/searchUser.jsp");
			else if (label === "删除用户")
				$("#rightDiv").load("/NewsSystem/servlet/UserServlet?condition=delete&page=1&pageSize=2");
			else if (label === "批量添加用户")
				$("#rightDiv").load("/NewsSystem/manager/userBatchAdd.jsp");
			else if (label === "管理新闻")
				$("#rightDiv").load("/NewsSystem/servlet/NewsServlet?condition=manage&page=1&pageSize=2");
			else if (label === "添加新闻")
				window.open("/NewsSystem/news/manage/addNews.jsp", '_blank');
			else if (label === "年度新闻数")
				$("#rightDiv").load("/NewsSystem/statistic/articleNumberByMonthInAYear.jsp");
			else if (label === "各年新闻数") {
				$("#rightDiv").html("正在处理！");
				$.post("/NewsSystem/servlet/StatisticServlet?condition=articleNumberByMonthInAYearEveryYear", {},
					function(data, textStatus, jqXHR) {
						if (data != null) {
							if (data.result == 1) {
								alert("success!");
								$("#rightDiv").html("<br><br><a href='" + data.redirectUrl + "'>各年度新闻发布情况</a>");
							} else {
								$("#rightDiv").html(data.message);
							}
						}
					}, "json");
			} else if (label === "生成静态html") {
				$("#rightDiv").html("正在处理！");
				$.post("/NewsSystem/servlet/AutoGeneratorServlet?condition=newsHtml", {},
					function(data, textStatus, jqXHR) {
						if (data != null) {
							if (data.result > 0) {
								$("#rightDiv").html("<br><br><a href='" + data.redirectUrl + "'>成功！</a>");
							} else {
								$("#rightDiv").html(data.message);
							}
						}
					}, "json");
			} else if (label === "各年评论前十") {
				$("#rightDiv").html("正在处理！");
				$.post("/NewsSystem/servlet/StatisticServlet?condition=firstTenCommentNumberAYearEveryYear", {},
					function(data, textStatus, jqXHR) {
						if (data != null) {
							if (data.result == 1) {
								alert("success!");
								$("#rightDiv").html("<br><br><a href='" + data.redirectUrl + "'>各年评论前十情况</a>");
							} else {
								$("#rightDiv").html(data.message);
							}
						}
					}, "json");
			} else if (label === "数据库备份") {
				$("#rightDiv").load("/NewsSystem/manager/databaseBackup.jsp");
			} else if (label === "数据库还原") {
				$("#rightDiv").load("/NewsSystem/servlet/DatabaseServlet?condition=getAll");
			}
		});
	});
</script>
</head>
<div class="newsShowByType_frame center" id="frameDiv">
	<div class="newsShowByType_left" id="leftDiv">
		<ul style="list-style-type: none;">
			<li>显示个人信息</li>
			<li>修改个人信息</li>
			<li>修改密码</li>
			<c:if test="${sessionScope.user.type=='manager'}">
				<br>
				<li>浏览用户</li>
				<li>审查信息</li>
				<li>查询用户</li>
				<li>删除用户</li>
				<li>批量添加用户</li>
				<br>
				<li>管理新闻</li>
				<br>
				<li>年度新闻数</li>
				<li>各年新闻数</li>
				<li>各年评论前十</li>
				<li>生成静态html</li>
				<br>
				<li>数据库备份</li>
				<li>数据库还原</li>
			</c:if>
			<c:if test="${sessionScope.user.type=='newsAuthor'}">
				<br>
				<li>添加新闻</li>
				<li>管理新闻</li>
			</c:if>
		</ul>
	</div>
	<div class="manageMain_right" id="rightDiv"></div>
	<div class="clear"></div>
</div>
</body>
</html>
