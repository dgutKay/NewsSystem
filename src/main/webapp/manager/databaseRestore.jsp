<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>databaseRestore.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#backup").click(function() {
			$.ajax({
				type : "post",
				dataType : "json",
				url : "/NewsSystem/servlet/DatabaseServlet?condition=restore",
				data : $("#restoreForm").serialize(),
				success : function(message) {
					if (message != null) {
						$("#rightDiv").html("操作结束。" + message.message);
					} else {
						$("#rightDiv").html("服务器异常！");
					}
				},
				error : function() {
					$("#rightDiv").html("服务器异常！无法连接！");
				}
			});
		});
	});
</script>
</head>

<body>
	<form id="restoreForm">
		<select name="databasebackupId" size="20" required>
			<c:forEach items="${requestScope.databasebackups}"
				var="databasebackup">
				<option value="${databasebackup.databasebackupId}">${databasebackup.name}</option>
			</c:forEach>
		</select> <input type="button" id="backup" value=" 开 始 还 原 " />
	</form>
</body>
</head>
</html>