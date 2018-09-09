<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>changeUserInformation.jsp</title>
</head>

<body>
	<form
		action="/NewsSystem/servlet/UserServlet?condition=changeInformation"
		method="post" enctype="multipart/form-data">
		<table border="1" align="center" cellpadding="5" cellspacing="0">
			<tr>
				<td colspan="2" align="center">Change Information</td>
			</tr>
			<tr>
				<td align="right">Head Icon:</td>
				<td><img src="${sessionScope.user.headIconUrl }" height="100" width="100" />
					<input type="file" id="myFile" name="myFile" onchange="preview()"><br>
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
				<td colspan="2" align="center"><input type="submit"
					value="submit" /></td>
			</tr>
		</table>
	</form>
</body>
</html>

<script type="text/javascript">

	var sex = document.getElementById("sex");
	for (var i = 0; i < sex.options.length; i++) {
		if (sex.options[i].value == "${requestScope.userInformation.sex}") {
			sex.options[i].selected = true;
			break;
		}
	}

	function preview() {
		var preview = document.getElementById('myImage');
		var file = document.getElementById("myFile").files[0];
		var reader = new FileReader();

		if (file) reader.readAsDataURL(file);
		else
			preview.src = "";

		reader.onloadend = function() {
			preview.src = reader.result;
		}
	}
</script>
