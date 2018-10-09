<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>newPassword.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">







</script>
</head>

<body>
	<form id="form" name="form"
		action="/NewsSystem/servlet/UserServlet?condition=newPassword"
		method="post">
		<div class="center" style="width:400px;margin-top:40px">
			<table border="0" align="center" cellpadding="5" cellspacing="0">
				<tr>
					<td colspan="2" align="center">Enter New Password</td>
				</tr>
				<tr>
					<td align="right">Password:</td>
					<td><input type="password" name="password" id="password"
						onBlur="valPassword()"><span id="passwordspan"></span></td>
				</tr>
				<tr>
					<td align="right">Confirm Password:</td>
					<td><input type="password" name="confirmPassword"
						id="confirmPassword" onBlur="passwordSame()"><span
						id="confirmPasswordSpan"></span></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button"
						name="button" id="button" value="confirm" /></td>
				</tr>
			</table>
		</div>
		<input type="hidden" name="rand" id="rand" value="${param.rand}">
	</form>
</body>
</html>
