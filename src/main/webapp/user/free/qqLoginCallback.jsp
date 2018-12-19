<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>qqLoginCallback.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="http://connect.qq.com/qc_jssdk.js" data-appid="101504585"
	data-redirecturi="http://220a27a0.nat123.cc:19537/news2/user/public/qqLoginCallback.jsp"></script>
<script type="text/javascript">
	$(document).ready(function() {
		if (QC.Login.check()) {
			QC.api("get_user_info")
				.success(function(s) { //成功回调
					QC.Login.getMe(function(openId, accessToken) {
						var url = "/NewsSystem/servlet/UserServlet?condition=qqLogin";
						url += "&nickname=" + s.data.nickname;
						url += "&openId=" + openId;
						url += "&accessToken=" + accessToken;

						window.parent.window.location.href = url; //在当前窗口中打开窗口
					})
				})
				.error(function(f) { //失败回调
					alert("获取用户信息失败！登录失败！");
					location.href = "/NewsSystem/user/free/login.jsp";
				})
				.complete(function(c) { //完成请求回调
					//	alert("获取用户信息完成！");
				});
		} else {
			alert("请登录！");
			location.href = "/NewsSystem/user/free/login.jsp";
		}
	});
</script>
</head>
<body>
	<div>
		<h3>数据传输中，请稍后...</h3>
	</div>
</body>
</html>
