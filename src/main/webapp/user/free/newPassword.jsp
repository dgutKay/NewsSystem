<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>newPassword.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
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

	$(document).ready(function() { //资源加载后才执行的 代码，就放到这个函数中，jquery能保证网页所有资源（html代码，图片，js文件，css文件等）都加载后，才执行此函数
		$("#password").blur(function() { //为id是userName的标签绑定  失去焦点事件  的处理函数
			passwordCheck();
		});

		$("#confirmPassword").blur(function() { //为id是userName的标签绑定  失去焦点事件  的处理函数
			passwordSame();
		});

		$("#button").click(function() {
			if (!passwordCheck())
				alert("密码格式错误！"); //阻止提交
			else if (!passwordSame())
				alert("两次输入的密码不相同！");else { //客户端数据验证通过
				$.ajax({ //验证码检测
					url : "/NewsSystem/servlet/UserServlet?condition=newPassword",
					type : "post",
					data : $("#form").serialize(), //serialize():搜集表单元素数据，并形成查询字符串
					dataType : "json",
					cache : false,
					error : function(textStatus, errorThrown) { //ajax请求失败时，将会执行此回调函数
						alert("系统ajax交互错误: " + textStatus);
					},
					success : function(data, textStatus) { //ajax请求成功时，会执行此回调函数
						if (data.result == 1) { //修改密码成功
							var newHtml = "修改密码成功！<br/>" + "<a href='login.jsp'>登录</a><br/>" + "<a href='/NewsSystem/index.jsp'>返回前端主页</a>";
							$("#div").html(newHtml);
						} else if (data.result == -1) { //修改密码失败
							alert("修改密码失败！");
						} else if (data.result == -2) {
							alert("修改密码超时！");
							location.href = "findPassword.jsp";
						} else if (data.result == -3) { //随机数错误
							alert("无权限修改密码！");
						}
					}
				});
			}
		});
	});
</script>
</head>

<body>
	<div id="div">
		<form action="/NewsSystem/servlet/UserServlet?condition=newPassword"
			id="form" name="form" method="post" onsubmit="return check()">
			<div class="center" style="width:600px;margin-top:40px">
				<table border="0" align="center" cellpadding="5" cellspacing="0">
					<tr>
						<td colspan="2" align="center">Change Password</td>
					</tr>
					<tr>
						<td align="right">New Password:</td>
						<td><input type="password" name="password" id="password"
							onBlur="valPassword()"><span id="passwordspan"></span></td>
					</tr>
					<tr>
						<td align="right">Confirm New Password:</td>
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
		</form>
	</div>
</body>
</html>
