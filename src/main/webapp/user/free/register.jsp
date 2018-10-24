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
		var name = $("#name").val(); // 获取文本框的内容

		if (name == null || name == "") {
			$("#namespan").html("*Can not be empty");
			return false;
		} else if (name.length >= 8 && pattern.test(name)) { // pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#namespan").html("Ok");
			return true;
		} else {
			$("#namespan").html("*User name requires at least 8 characters, starting with letters, ending with letters or numbers, and can have - and _.");
			return false;
		}
	}

	function valPassword() {
		var pattern = /^(\w){6,20}$/;
		var password = $("#password").val();

		if (password == null || password == "") {
			$("#passwordspan").html("*Can not be empty");
			return false;
		} else if (password.match(pattern) == null) {
			$("#passwordspan").html("*Password can only enter 6-20 letters, numbers or underscore.");
			return false;
		} else {
			$("#passwordspan").html("Ok");
			return true;
		}
	}

	function passwordSame() {
		var password = $("#password").val();
		var confirmPassword = $("#confirmPassword").val();

		if (confirmPassword == null || confirmPassword == "") {
			$("#confirmPasswordSpan").html("*Can not be empty");
			return false;
		} else if (password == confirmPassword) {
			$("#confirmPasswordSpan").html("Ok");
			return true;
		} else {
			$("#confirmPasswordSpan").html("*The two passwords are different.");
			return false;
		}
	}

	function emailCheck() {
		var pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$", "i"); //创建模式对象
		var email = $("#email").val(); //获取文本框的内容

		if (email.length >= 8 && pattern.test(email)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#emailSpan").html("Ok");
			return true;
		} else {
			$("#emailSpan").html("The format of the E-mail is wrong!");
			return false;
		}
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
				alert("必须输入验证码！");
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
							alert(data.message);
							window.location.href = data.redirectUrl; //跳转网页
						} else alert(data.message);
					}
				});
			}
		});
	});
</script>
</head>

<body>
	<div id="myDiv">
		<form id="form" name="form" method="post">
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
						<td><input type="text" name="name" id="name"><span
							id="namespan"></span></td>
					</tr>
					<tr>
						<td align="right">E-mail:</td>
						<td><input type="text" name="email" id="email"><span
							id="emailSpan"></span></td>
					</tr>
					<tr>
						<td align="right">Password:</td>
						<td><input type="password" name="password" id="password"><span
							id="passwordspan"></span></td>
					</tr>
					<tr>
						<td align="right">Confirm Password:</td>
						<td><input type="password" name="confirmPassword"
							id="confirmPassword"><span id="confirmPasswordSpan"></span></td>
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
