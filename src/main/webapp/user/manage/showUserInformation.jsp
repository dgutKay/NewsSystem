<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>showUserInformation.jsp</title>
</head>

<body>
	<table border="1" align="center" cellpadding="5" cellspacing="0">
		<tr>
			<td colspan="2" align="center">User Information</td>
		</tr>
		<tr>
			<td align="right">User Type:</td>
			<td>${sessionScope.user.type }</td>
		</tr>
		<tr>
			<td align="right">User Name:</td>
			<td>${sessionScope.user.name }</td>
		</tr>
		<tr>
			<td align="right">Head Icon:</td>
			<td><img src="${sessionScope.user.headIconUrl }" height="100"
				width="100" /></td>
		</tr>
		<tr>
			<td align="right">Register Date:</td>
			<td>${sessionScope.user.registerDate }</td>
		</tr>
		<c:if test="${sessionScope.user.type=='user' }">
			<tr>
				<td align="right">Gender:</td>
				<td>${requestScope.userInformation.sex }</td>
			</tr>
			<tr>
				<td align="right">Hobby:</td>
				<td>${requestScope.userInformation.hobby }</td>
			</tr>
		</c:if>
	</table>
</body>
</html>
