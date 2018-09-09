<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>register.jsp</title>
<script type="text/javascript">
	function valName() {
		var pattern = new RegExp("^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$", "i"); // 创建模式对象
		var name = document.getElementById('name').value; // 获取文本框的内容
		var namespan = document.getElementById("namespan");
		if (name == null || name == "") {
			namespan.innerHTML = "*Can not be empty";
			return false;
		} else if (name.length >= 8 && pattern.test(name)) { // pattern.test() 模式如果匹配，会返回true，不匹配返回false
			namespan.innerHTML = "Ok";
			return true;
		} else {
			namespan.innerHTML = "*User name requires at least 8 characters, starting with letters, ending with letters or numbers, and can have - and _.";
			return false;
		}
	}

	function valPassword() {
		var pattern = /^(\w){6,20}$/;
		var password = document.getElementById("password").value;
		var passwordspan = document.getElementById("passwordspan");
		if (password == null || password == "") {
			passwordspan.innerHTML = "*Can not be empty";
			return false;
		} else if (password.match(pattern) == null) {
			passwordspan.innerHTML = "*Password can only enter 6-20 letters, numbers or underscore.";
			return false;
		} else {
			passwordspan.innerHTML = "Ok";
			return true;
		}
	}

	function passwordSame() {
		var password = document.getElementById("password").value;
		var confirmPassword = document.getElementById("confirmPassword").value;
		var confirmPasswordSpan = document.getElementById("confirmPasswordSpan");
		if (confirmPassword == null || confirmPassword == "") {
			confirmPasswordSpan.innerHTML = "*Can not be empty";
			return false;
		} else if (password == confirmPassword) {
			confirmPasswordSpan.innerHTML = "Ok";
			return true;
		} else {
			confirmPasswordSpan.innerHTML = "*The two passwords are different.";
			return false;
		}
	}

	function check() {
		if (valName() && valPassword() && passwordSame()) return true; // 提交
		else return false; // 阻止提交
	}
</script>
</head>

<body>
	<form action="/NewsSystem/servlet/UserServlet?condition=register"
		method="post" onsubmit="return check()">
		<div class="center" style="width:400px;margin-top:40px">
			<table border="0" align="center" cellpadding="5" cellspacing="0">
				<tr>
					<td colspan="2" align="center">Register</td>
				</tr>
				<tr>
					<td align="right">Type:</td>
					<td><select name="type">
							<option value="user">User</option>
							<option value="newsAuthor">News Author</option>
							<option value="manager">Manager</option>
					</select></td>
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
					<td align="right">Confirm Password:</td>
					<td><input type="password" name="confirmPassword"
						id="confirmPassword" onBlur="passwordSame()"><span
						id="confirmPasswordSpan"></span></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit"
						value="submit" /></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>
