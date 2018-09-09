<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>

<body>

	<a href="/NewsSystem/user/free/register.jsp" target='_blank'>注册</a>
	<br>
	<br>
	<a href="/NewsSystem/user/free/login.jsp" target='_blank'>登录</a>
	<br>
	<br>
	<br> 管理员
	<br>
	<a
		href="/NewsSystem/servlet/UserServlet?condition=showPage&page=1&pageSize=2"
		target='_blank'>浏览用户</a>
	<br>
	<br>
	<a
		href="/NewsSystem/servlet/UserServlet?condition=check&page=1&pageSize=2"
		target='_blank'>审查用户</a>
	<br>
	<br>
	<a href="/NewsSystem/manager/searchUser.jsp" target='_blank'>查询用户</a>
	<br>
	<br>
	<a
		href="/NewsSystem/servlet/UserServlet?condition=delete&page=1&pageSize=2"
		target='_blank'>删除用户</a>
	<br>
	<br>
	<br> 用户
	<br>
	<a href="/NewsSystem/user/manage/changePassword.jsp" target='_blank'>修改密码</a>
	<br>
	<br>
	<a href="/NewsSystem/servlet/UserServlet?condition=showUserInformation"
		target='_blank'>显示个人信息</a>
	<br>
	<br>
	<a
		href="/NewsSystem/servlet/UserServlet?condition=changeUserInformation"
		target='_blank'>修改个人信息</a>
	<br>
	<br> 新闻
	<br>
	<a href="/NewsSystem/news/manage/addNews.jsp" target='_blank'>添加新闻</a>
	<br>
	<br>
	<a
		href="/NewsSystem/servlet/NewsServlet?condition=manage&page=1&pageSize=2"
		target='_blank'>管理新闻</a>
	<br>
	<br>
	<a
		href="/NewsSystem/servlet/NewsServlet?condition=showNews&page=1&pageSize=2"
		target='_blank'>显示新闻</a>
	<br>
	<br>


</body>
</html>
