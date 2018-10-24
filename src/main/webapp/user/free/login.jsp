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
		var name = $("#name").val();
		if (name == null || name == "") {
			$("#namespan").html("*Can not be empty");
			return false;
		} else {
			$("#namespan").html("Ok");
			return true;
		}
	}

	function valPassword() {
		var password = $("#password").val();
		if (password == null || password == "") {
			$("#passwordspan").html("*Can not be empty");
			return false;
		} else {
			$("#passwordspan").html("Ok");
			return true;
		}
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
				alert("必须输入验证码！");else {
				$.ajax({ //验证码检测
					url : "/NewsSystem/servlet/UserServlet?condition=login",
					type : "post",
					data : $("#form").serialize(), //serialize():搜集表单元素数据，并形成查询字符串
					dataType : "json",
					cache : false,
					error : function(textStatus, errorThrown) { //ajax请求失败时，将会执行此回调函数
						alert("系统ajax交互错误: " + textStatus);
					},
					success : function(data, textStatus) { //ajax请求成功时，会执行此回调函数
						if (data.result == 1) {
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
	<form action="/NewsSystem/servlet/UserServlet?condition=login"
		id="form" name="form" method="post">
		<div class="center" style="width:400px;margin-top:40px">
			<table border="0" align="center" cellpadding="5" cellspacing="0">
				<tr>
					<td colspan="2" align="center">Login In</td>
				</tr>
				<tr>
					<td align="right">Name:</td>
					<td><input type="text" name="name" id="name"><span
						id="namespan"></span></td>
				</tr>
				<tr>
					<td align="right">Password:</td>
					<td><input type="password" name="password" id="password"><span
						id="passwordspan"></span></td>
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
