<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>showUser.jsp</title>
<script type="text/javascript">
	function getOnePage(type, orderFieldName) {
		var page = $("#page");
		var totalPageCount = $("#totalPageCount");
		var order = $("#order");
		var orderField = $("#orderField");

		if (orderFieldName != "") { //切换排序
			orderField.val(orderFieldName); //设置排序字段名
			if (order.val() == "asc")
				order.val("desc");
			else
				order.val("asc");

			page.val("1");
		}

		pageValue = parseInt(page.val());
		if (type == "first") {
			page.val("1");
		} else if (type == "pre") {
			pageValue -= 1;
			page.val(pageValue.toString());
		} else if (type == "next") {
			pageValue += 1;
			page.val(pageValue.toString());
		} else if (type == "last") {
			page.val(totalPageCount.val());
		}

		//提交
		$("#rightDiv").load("/NewsSystem/servlet/UserServlet?condition=showPage", $("#myform").serialize());
	}
</script>
</head>

<body>
	<form action="" id="myform" method="post">
		<table border="1" align="center" cellpadding="5" cellspacing="0">
			<tr bgcolor='lightyellow'>
				<td><a href='javascript:void(0);'
					onclick="getOnePage('','userId');">Id</a></td>
				<td>User Type</td>
				<td>User Name</td>
				<td>Register Date</td>
				<td>Usability</td>
			</tr>
			<c:forEach items="${requestScope.users }" var="user">
				<tr>
					<td>${user.userId }</td>
					<td>${user.type }</td>
					<td>${user.name }</td>
					<td>${user.registerDate }</td>
					<td>${user.usability }</td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<table border="1" align="center" cellpadding="5" cellspacing="0">
			<tr>
				<c:if test="${requestScope.pageInformation.page>1 }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('first','');">First</a>
				</c:if>
				<c:if test="${requestScope.pageInformation.page>1 }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('pre','');">Previous</a>
				</c:if>
				<c:if
					test="${requestScope.pageInformation.page< requestScope.pageInformation.totalPageCount }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('next','');">Next</a>
				</c:if>
				<c:if
					test="${requestScope.pageInformation.page< requestScope.pageInformation.totalPageCount }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('last','');">Last</a>
				</c:if>
				<td>total:${requestScope.pageInformation.totalPageCount}pages</td>
			</tr>
		</table>
		<input type="hidden" name="page" id="page"
			value="${requestScope.pageInformation.page }"> <input
			type="hidden" name="pageSize" id="pageSize"
			value="${requestScope.pageInformation.pageSize }"> <input
			type="hidden" name="totalPageCount" id="totalPageCount"
			value="${requestScope.pageInformation.totalPageCount}"> <input
			type="hidden" name="allRecordCount" id="allRecordCount"
			value="${requestScope.pageInformation.allRecordCount}"> <input
			type="hidden" name="orderField" id="orderField"
			value="${requestScope.pageInformation.orderField}"> <input
			type="hidden" name="order" id="order"
			value="${requestScope.pageInformation.order}"> <input
			type="hidden" name="ids" id="ids"
			value="${requestScope.pageInformation.ids}"> <input
			type="hidden" name="searchSql" id="searchSql"
			value="${requestScope.pageInformation.searchSql}"> <input
			type="hidden" name="result" id="result"
			value="${requestScope.pageInformation.result}">
	</form>
</body>
</html>
