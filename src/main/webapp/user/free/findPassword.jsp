<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>findPassword.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	var type = "email";
	//验证
	function check() {
		var pattern;
		if (type == "email")
			pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$", "i"); //创建模式对象
		else
			pattern = new RegExp("^1[3|4|5|8][0-9]\d{4,8}$", "i"); //创建模式对象

		var str1 = $("#wayInput").val(); //获取文本框的内容

		if (pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#wayInputSpan").html("ok");
			return true;
		} else {
			if (type == "email")
				$("#wayInputSpan").html("email格式不正确！");
			else
				$("#wayInputSpan").html("手机号码格式不正确！");

			return false;
		}
	}


	$(document).ready(function() { //资源加载后才执行的 代码，就放到这个函数中，jquery能保证网页所有资源（html代码，图片，js文件，css文件等）都加载后，才执行此函数

		$("#byEmail").click(function() {
			type = "email";
			$("#wayName").text("E-mail:");
		});

		$("#byTelephone").click(function() {
			type = "telephone";
			$("#wayName").text("Telephone:");
		});

		$("#wayInput").blur(function() {
			check();
		});

		$("#button").click(function() {
			if (!check())
				alert("输入格式错误！"); //阻止提交
			else if ($(type == "telephone" && "#checkCode").val() == "")
				alert("必须输入验证码！"); //
			else { //客户端数据验证通过
				$.ajax({
					url : "/NewsSystem/servlet/UserServlet?condition=findPassword&type=" + type,
					type : "post",
					data : $("#form").serialize(), //serialize():搜集表单元素数据，并形成查询字符串
					dataType : "json",
					cache : false,
					error : function(textStatus, errorThrown) { //ajax请求失败时，将会执行此回调函数
						alert("系统ajax交互错误: 222" + textStatus);
					},
					success : function(data, textStatus) { //ajax请求成功时，会执行此回调函数
						if (data.result == -1) { //-1发送邮件失败
							alert("发送邮件失败！");
						} else if (data.result == -2) { //-2邮箱未注册过
							alert("邮箱未注册过！");
						} else if (data.result == 1) { //发送邮件成功
							alert("请登录邮箱查看邮件，按邮件提示操作找回密码！");
						}
					}
				});
			}
		});
	});
</script>
</head>

<body>
	<div class="center" style="width:600px;margin-top:40px">
		<form id="form" name="form">

			<table border="0" align="center" cellpadding="5" cellspacing="0">
				<tr>
					<td colspan="2" align="center">find password</td>
				</tr>
				<tr>
					<td align="right">choose the way:</td>
					<td><input name="radio" type="radio" id="byEmail"
						value="byEmail" checked> <label for="byEmail">E-mail</label>
						<input type="radio" name="radio" id="byTelephone"
						value="byTelephone"> <label for="byTelephone">Telephone</label></td>
				</tr>
				<tr>
					<td align="right"><span id="wayName">E-mail:</span></td>
					<td><input name="wayInput" type="text" id="wayInput"
						accesskey="p" tabindex="2" size="30" maxlength="30" /> <span
						id="wayInputSpan"></span></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button"
						name="button" id="button" value="confirm" /></td>
				</tr>
			</table>

		</form>
	</div>
</body>
</html>
