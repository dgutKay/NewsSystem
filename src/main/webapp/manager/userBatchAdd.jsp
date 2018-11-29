<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<title>userBatchAdd.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#myform").on("submit", function(event) {
			var formdata = new FormData($("#myform")[0]);
			$.ajax({
				type : "post",
				dataType : "json",
				url : "/NewsSystem/servlet/UserServlet?condition=batchAdd",
				data : formdata,
				cache : false,
				async : false,
				processData : false,
				contentType : false,
				success : function(data) {
					if (data != null) {
						if (data.result == 1) {
							$("#rightDiv").html(data.message + "<a href='" + data.redirectUrl + "'>初始密码文件<a>");
						} else {
							$("#rightDiv").html("aaaaa" + data.message);
						}
					} else {
						$("#rightDiv").html("异常！");
					}
				},
				error : function() {
					$("#rightDiv").html("异常！");
				}
			});
			event.preventDefault();
		});
	});
</script>
</head>

<body>
	文件格式为: 第一列是用户名称（要求符合本系统注册时的用户名要求，且不能有重复）
	<br>
	<br>
	<form action="" id="myform" method="post">
		<table align="center" style="width:500px;">
			<tr>
				<td>excel文件上传:</td>
				<td><input id="myFile" name="myFile" type="file"
					accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
					<input type="submit" value="提交" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
