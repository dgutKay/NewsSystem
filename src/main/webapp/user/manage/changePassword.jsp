<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>changePassword.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	function valPassword(input, span) {
		var inputValue = $("#" + input).val();
		var pattern = /^(\w){6,20}$/;

		if (inputValue == null || inputValue == "") {
			$("#" + span).html("*Can not be empty");
			return false;
		} else if (inputValue.match(pattern) == null) {
			$("#" + span).html("*Password can only enter 6-20 letters, numbers or underscore.");
			return false;
		} else {
			$("#" + span).html("Ok");
			return true;
		}
	}

	function passwordDifferent(input1, input2, span2) {
		var input1Value = $("#" + input1).val();
		var input2Value = $("#" + input2).val();

		if (input2Value == null || input2Value == "") {
			$("#" + span2).html("*Can not be empty");
			return false;
		} else if (input1Value == input2Value) {
			$("#" + span2).html("*Please choose a password that you haven't used before.");
			return false;
		} else {
			$("#" + span2).html("Ok");
			return true;
		}
	}

	function passwordSame(input1, input2, span2) {
		var input1Value = $("#" + input1).val();
		var input2Value = $("#" + input2).val();

		if (input2Value == null || input2Value == "") {
			$("#" + span2).html("*Can not be empty");
			return false;
		} else if (input1Value == input2Value) {
			$("#" + span2).html("Ok");
			return true;
		} else {
			$("#" + span2).html("*The two passwords are different.");
			return false;
		}
	}



	$(document).ready(function() {
		$("#button").click(function() {
			if (valPassword('curPassword', 'curPasswordSpan') && valPassword('newPassword', 'newPasswordSpan') && passwordSame('newPassword', 'confirmPassword', 'confirmPasswordSpan') && passwordDifferent('curPassword', 'newPassword', 'newPasswordSpan')) {
				$.ajax({
					url : "/NewsSystem/servlet/UserServlet?condition=changePassword",
					type : "post",
					data : $("#form").serialize(),
					dataType : "json",
					cache : false,
					error : function() {
						alert("error!");
					},
					success : function(data) {
						if (data != null)
							//alert(data.message);
							$("#rightDiv").html(data.message);
						else
							//alert(data.message);
							$("#rightDiv").html("Wrong!");
					}
				});
			}
			else return false; // 阻止提交 
		});
	});
</script>
</head>

<body>
	<form action="" id="form" name="form" method="post">
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
				<td colspan="2" align="center"><input type="button"
					name="button" id="button" value="confirm" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
