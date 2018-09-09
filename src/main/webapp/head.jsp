<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="/NewsSystem/css/1.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

</script>
</head>
<body>
	<div class="div-out">
		<div class="logRight">
			<div class="logRightInner">
				<c:if test="${!(empty sessionScope.user) }">
					<a href="/NewsSystem/user/manageUIMain/manageMain.jsp">Manage</a>&nbsp;&nbsp;&nbsp;
					</c:if>

				<a href="/NewsSystem/index.jsp">Home Page</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:choose>
					<c:when test="${empty sessionScope.user}">
						<a href="/NewsSystem/user/free/login.jsp">Login In</a>&nbsp;&nbsp;&nbsp;
							&nbsp;<a href="/NewsSystem/user/free/register.jsp">Register</a>
					</c:when>
					<c:otherwise>
					    	${sessionScope.user.name}&nbsp;&nbsp;
					    	<a href="/NewsSystem/servlet/UserServlet?condition=exit">Logout</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<div class="clear"></div>
</body>
</html>
