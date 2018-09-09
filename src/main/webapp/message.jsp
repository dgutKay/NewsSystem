<%@ page language="java" import="java.util.*,tools.Message"
	pageEncoding="UTF-8"%>
<%
	Message message = (Message) request.getAttribute("message");
	response.setHeader("refresh", message.getRedirectTime() + ";URL=" + message.getRedirectUrl());
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>message.jsp</title>
</head>
<body>
	<div class="center" style="width:300px;" align="center">
		<table width="300" border="0" align="center" style="margin:50px;">
			<tr>
				<td>${requestScope.message.message }</td>
			</tr>
			<c:if test="${requestScope.message.redirectTime < 10}">
				<tr>
					<td>It will jump to other page after
						${requestScope.message.redirectTime } seconds.</td>
				</tr>
				<tr>
					<td>If it didn't jump, please click <a
						href="${requestScope.message.redirectUrl} ">here</a>.
					</td>
				</tr>
			</c:if>
			<c:if test="${requestScope.message.redirectTime >= 10}">
				<tr>
					<td><a href="javascript:void(0);" onclick="history.go(-1);">Back</a>
					</td>
				</tr>
			</c:if>
		</table>
	</div>
</body>
</html>
