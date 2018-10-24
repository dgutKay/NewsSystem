<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>changeUserInformation.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#sex").val("${requestScope.userInformation.sex}");

		function getFileURL(file) {
			if (window.createObjectURL != undefined) { // basic
				return window.createObjectURL(file);
			} else if (window.URL != undefined) { // mozilla(firefox)
				return window.URL.createObjectURL(file);
			} else if (window.webkitURL != undefined) {
				return window.webkitURL.createObjectURL(file);
			}
		}

		$("#myFile").change(function() {
			var fileUrl = getFileURL(this.files[0]);
			if (fileUrl) {
				$("#myImage").attr("src", fileUrl);
			}
		});

		$("#submit").click(function() {
			var formdata = new FormData($("#form")[0]);
			$.ajax({
				url : "/NewsSystem/servlet/UserServlet?condition=changeInformation",
				type : "post",
				data : formdata,
				dataType : "json",
				async : false,
				cache : false,
				processData : false,
				contentType : false,
				error : function(textStatus, errorThrown) {
					alert("系统ajax交互错误: " + textStatus);
				},
				success : function(data) {
					if (data != null) {
						alert(data.message);
						$("#rightDiv").load(data.redirectUrl);
					}
					else $("#rightDiv").html("Wrong!");
				}
			});
		})
	})
</script>
</head>

<body>
	<form enctype="multipart/form-data" id="form" name="form" method="post">
		<table border="1" align="center" cellpadding="5" cellspacing="0">
			<tr>
				<td colspan="2" align="center">Change Information</td>
			</tr>
			<tr>
				<td align="right">Head Icon:</td>
				<td><img src="${sessionScope.user.headIconUrl }" height="100"
					width="100" /> <input type="file" id="myFile" name="myFile"><br>
					<br>Preview:<img id="myImage" height="100" width="100" /></td>
			</tr>
			<c:if test="${sessionScope.user.type=='user' }">
				<tr>
					<td align="right">Gender:</td>
					<td><select id="sex" name="sex">
							<option value="男">男</option>
							<option value="女">女</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">Hobby:</td>
					<td><input type="text" name="hobby"
						value="${requestScope.userInformation.hobby }"></td>
				</tr>
			</c:if>
			<tr>
				<td colspan="2" align="center"><input type="button"
					value="submit" id="submit" /></td>
			</tr>
		</table>
	</form>
</body>
</html>