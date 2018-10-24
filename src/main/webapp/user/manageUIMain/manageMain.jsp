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
			else if (label === "管理新闻")
				$("#rightDiv").load("/NewsSystem/servlet/NewsServlet?condition=manage&page=1&pageSize=2");
			else if (label === "添加新闻")
				window.open("/NewsSystem/news/manage/addNews.jsp", '_blank');
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
				<br>
				<li>管理新闻</li>
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
