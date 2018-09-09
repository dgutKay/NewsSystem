<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>changePassword.jsp</title>
<script type="text/javascript">
	function valPassword(input, span) {
		var inputValue = document.getElementById(input).value;
		var spanObj = document.getElementById(span);
		var pattern = /^(\w){6,20}$/;

		if (inputValue == null || inputValue == "") {
			spanObj.innerHTML = "*Can not be empty";
			return false;
		} else if (inputValue.match(pattern) == null) {
			spanObj.innerHTML = "*Password can only enter 6-20 letters, numbers or underscore.";
			return false;
		} else {
			spanObj.innerHTML = "Ok";
			return true;
		}
	}

	function passwordDifferent(input1, input2, span2) {
		var input1Value = document.getElementById(input1).value;
		var input2Value = document.getElementById(input2).value;
		var span2Obj = document.getElementById(span2);

		if (input2Value == null || input2Value == "") {
			span2Obj.innerHTML = "*Can not be empty";
			return false;
		} else if (input1Value == input2Value) {
			span2Obj.innerHTML = "*Please choose a password that you haven't used before.";
			return false;
		} else {
			span2Obj.innerHTML = "Ok";
			return true;
		}
	}

	function passwordSame(input1, input2, span2) {
		var input1Value = document.getElementById(input1).value;
		var input2Value = document.getElementById(input2).value;
		var span2Obj = document.getElementById(span2);

		if (input2Value == null || input2Value == "") {
			span2Obj.innerHTML = "*Can not be empty";
			return false;
		} else if (input1Value == input2Value) {
			span2Obj.innerHTML = "Ok";
			return true;
		} else {
			span2Obj.innerHTML = "*The two passwords are different.";
			return false;
		}
	}

	function check() {
		if (valPassword('curPassword', 'curPasswordSpan') && valPassword('newPassword', 'newPasswordSpan') && passwordSame('newPassword', 'confirmPassword', 'confirmPasswordSpan') && passwordDifferent('curPassword', 'newPassword', 'newPasswordSpan')) return true; // 提交
		else return false; // 阻止提交 
	}
</script>
</head>

<body>
	<form action="/NewsSystem/servlet/UserServlet?condition=changePassword"
		method="post" onsubmit="return check()">
		<table border="0" align="center" cellpadding="5" cellspacing="0">
			<tr>
				<td colspan="2" align="center">Change Password</td>
			</tr>
			<tr>
				<td align="right">Current Password:</td>
				<td><input type="password" name="curPassword" id="curPassword"
					onBlur="valPassword('curPassword','curPasswordSpan')"><span
					id="curPasswordSpan"></span></td>
			</tr>
			<tr>
				<td align="right">New Password:</td>
				<td><input type="password" name="newPassword" id="newPassword"
					onBlur="valPassword('newPassword','newPasswordSpan')"><span
					id="newPasswordSpan"></span></td>
			</tr>
			<tr>
				<td align="right">Confirm Password:</td>
				<td><input type="password" name="confirmPassword"
					id="confirmPassword"
					onBlur="passwordSame('newPassword','confirmPassword','confirmPasswordSpan')"><span
					id="confirmPasswordSpan"></span></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit"
					value="submit" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
