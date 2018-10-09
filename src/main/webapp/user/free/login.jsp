<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>login.jsp</title>
<script type="text/javascript">
	function valName() {
		var str = document.getElementById("name").value;
		if (str == null || str == "") {
			document.getElementById("namespan").innerHTML = "*Can not be empty";
			return false;
		} else
			return true;
	}

	function valPassword() {
		var str = document.getElementById("password").value;
		if (str == null || str == "") {
			document.getElementById("passwordspan").innerHTML = "*Can not be empty";
			return false;
		} else
			return true;
	}

	function check() {
		if (valName() && valPassword())
			return true;
		else
			return false;
	}
</script>
</head>

<body>
	<form action="/NewsSystem/servlet/UserServlet?condition=login"
		method="post" onsubmit="return check()">
		<div class="center" style="width:400px;margin-top:40px">
			<table border="0" align="center" cellpadding="5" cellspacing="0">
				<tr>
					<td colspan="2" align="center">Login In</td>
				</tr>
				<tr>
					<td align="right">Name:</td>
					<td><input type="text" name="name" id="name"
						onBlur="valName()"><span id="namespan"></span></td>
				</tr>
				<tr>
					<td align="right">Password:</td>
					<td><input type="password" name="password" id="password"
						onBlur="valPassword()"><span id="passwordspan"></span></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="login" /></td>
				</tr>
				<tr>
					<td align="center"><a href="register.jsp">register</a></td>
					<td align="right"><a href="findPassword.jsp">forget password</a>
					</td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>
