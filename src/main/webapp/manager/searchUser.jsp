<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>searchUser.jsp</title>
</head>

<body>
	<form
		action="/NewsSystem/servlet/UserServlet?condition=search&page=1&pageSize=2"
		method="post">
		<table border="0" align="center" cellpadding="5" cellspacing="0">
			<tr>
				<td colspan="2" align="center">Search</td>
			</tr>
			<tr>
				<td align="right">Type:</td>
				<td><select name="type">
						<option value="all">All</option>
						<option value="user">User</option>
						<option value="newsAuthor">News Author</option>
						<option value="manager">Manager</option>
				</select></td>
			<tr>
				<td align="right">Name:</td>
				<td><input type="text" name="name" id="name"></td>
			</tr>
			<tr>
				<td align="right">Usability:</td>
				<td><select name="usability">
						<option value="all">All</option>
						<option value="use">Use</option>
						<option value="stop">Stop</option>
				</select></td>
			</tr>
			<tr>
				<td align="right">Register Date:</td>
				<td>Between <input type="date" name="lowDate"> and <input
					type="date" name="upDate"></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit"
					value="submit"></td>
			</tr>
		</table>
	</form>
</body>
</html>
