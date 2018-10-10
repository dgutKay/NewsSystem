<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>login.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<style type="text/css">
.hand {
	cursor: pointer;
	//
	鼠标变成手的形状
}
</style>
<script type="text/javascript">
	function valName() {
		var str = document.getElementById("name").value;
		if (str == null || str == "") {
			document.getElementById("namespan").innerHTML = "*Can not be empty";
			return false;
		} else {
			document.getElementById("namespan").innerHTML = "*ok";
			return true;
		}
	}

	function valPassword() {
		var str = document.getElementById("password").value;
		if (str == null || str == "") {
			document.getElementById("passwordspan").innerHTML = "*Can not be empty";
			return false;
		} else {
			document.getElementById("passwordspan").innerHTML = "*ok";
			return true;
		}
	}

	function check() {
		if (valName() && valPassword())
			return true;
		else
			return false;
	}

	$(document).ready(function() {

		$("#name").blur(function() {
			valName();
		})

		$("#password").blur(function() {
			valPassword();
		})

		$("#checkImg").click(function() { //为id是checkImg的标签绑定  鼠标单击事件  的处理函数
			//$(selector).attr(attribute,value)  设置被选元素的属性值
			//网址后加如一个随机值rand，表示了不同的网址，防止缓存导致的图片内容不变
			$("#checkImg").attr("src", "/NewsSystem/servlet/ImageCheckCodeServlet?rand=" + Math.random());
		});

		$("#button").click(function() {
			if (!valName())
				alert("用户名不符合规范!"); //阻止提交
			else if (!valPassword())
				alert("密码不符合规范!"); //阻止提交 	    	
			else if ($("#checkCode").val() == "")
				alert("必须输入验证码！");
			else $("#form").submit();
		});
	});
</script>
</head>

<body>
	<form action="/NewsSystem/servlet/UserServlet?condition=login" id="form" name="form"
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
					<td align="right">Verification Code:</td>
					<td><input type="text" name="checkCode" id="checkCode"><img
						id="checkImg"
						src="/NewsSystem/servlet/ImageCheckCodeServlet?rand=-1"
						class="hand" /></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button"
						name="button" id="button" value="login" /></td>
				</tr>
				<tr>
					<td align="center"><a href="register.jsp">register</a></td>
					<td align="right"><a href="findPassword.jsp">forget
							password</a></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>
