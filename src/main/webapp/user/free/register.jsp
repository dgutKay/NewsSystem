<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>register.jsp</title>
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

	function emailCheck() {
		var pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$", "i"); //创建模式对象
		var str1 = $("#email").val(); //获取文本框的内容

		if (str1.length >= 8 && pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#emailSpan").html("Ok");
			return true;
		} else {
			$("#emailSpan").html("The format of the E-mail is wrong!");
			return false;
		}
	}

	function check() {
		if (valName() && valPassword() && passwordSame()) return true; // 提交
		else return false; // 阻止提交
	}

	$(document).ready(function() {

		$("#name").blur(function() {
			valName();
		})

		$("#password").blur(function() {
			valPassword();
		})

		$("#email").blur(function() {
			emailCheck();
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
			else if (!emailCheck())
				alert("电子邮箱格式错误!"); //阻止提交	    	    	
			else if ($("#checkCode").val() == "")
				alert("必须输入验证码！"); //
			else { //客户端数据验证通过
				$.ajax({ //验证码检测
					url : "/NewsSystem/servlet/UserServlet?condition=register",
					type : "post",
					data : $("#form").serialize(), //serialize():搜集表单元素数据，并形成查询字符串
					dataType : "json",
					cache : false,
					error : function(textStatus, errorThrown) { //ajax请求失败时，将会执行此回调函数
						alert("系统ajax交互错误: " + textStatus);
					},
					success : function(data, textStatus) { //ajax请求成功时，会执行此回调函数
						if (data.result == 1) { //注册成功
							var newHtml = "<p align='center'>注册成功！</p>";
							$("#myDiv").html(newHtml);
						} else if (data.result == 0) { //数据库操作失败
							alert("数据库操作失败！");
						} else if (data.result == -1) { //有同名用户
							alert("有同名用户！");
							$("#namespan").html("用户名已注册，请换一个用户名！");
						} else if (data.result == -10) { //emai已被注册
							alert("emai已被注册！");
							$("#emailSpan").html("emai已被注册，请换一个email！");
						} else if (data.result == -11) { //重名用户且email被注册
							alert("有同名用户且emai已被注册！");
							$("#namespan").html("用户名已注册，请换一个用户名！");
							$("#emailSpan").html("emai已被注册，请换一个email！");
						} else if (data.result == -3) { //服务器端验证图片验证码不存在
							alert("验证码不存在！请点击验证码生成新的验证码");
							$("#checkCode").val(""); //清空文本框
						} else if (data.result == -4) { //验证码错误
							alert("验证码错误，请重新输入验证码！");
							$("#checkCode").val("");
						}
					}
				});
			}
		});
	});
</script>
</head>

<body>
	<div id="myDiv">
		<form action="/NewsSystem/servlet/UserServlet?condition=register"
			id="form" name="form" method="post" onsubmit="return check()">
			<div class="center" style="width:600px;margin-top:40px">
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
						<td align="right">E-mail:</td>
						<td><input type="text" name="email" id="email"
							onBlur="emailCheck()"><span id="emailSpan"></span></td>
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
						<td align="right">Verification Code:</td>
						<td><input type="text" name="checkCode" id="checkCode"><img
							id="checkImg"
							src="/NewsSystem/servlet/ImageCheckCodeServlet?rand=-1"
							class="hand" /></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="button"
							name="button" id="button" value="register" /></td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
</html>
