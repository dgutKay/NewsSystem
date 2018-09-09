<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>My JSP 'manage.jsp' starting page</title>
<script type="text/javascript">
	function getOnePage(type, orderFieldName) {
		var page = document.getElementById("page");
		var totalPageCount = document.getElementById("totalPageCount");
		var order = document.getElementById('order');
		var orderField = document.getElementById("orderField");

		if (orderFieldName != "") { //切换排序
			orderField.value = orderFieldName; //设置排序字段名
			if (order.value == "asc")
				order.value = "desc";
			else
				order.value = "asc";

			page.value = 1;
		}

		pageValue = parseInt(page.value);
		if (type == "first") {
			page.value = 1;
		} else if (type == "pre") {
			pageValue -= 1;
			page.value = pageValue.toString();
		} else if (type == "next") {
			pageValue += 1;
			page.value = pageValue.toString();
		} else if (type == "last") {
			page.value = totalPageCount.value;
		}

		//提交
		document.getElementById('myform').submit();
	}

	function deleteANews(id) {
		obj = document.getElementById("ids");
		obj.value = id;

		document.getElementById("myform").action = "/NewsSystem/servlet/NewsServlet?condition=delete";
		document.getElementById('myform').submit();
	}

	function editANews(id) {
		document.getElementById('myform').setAttribute("target", "_blank");
		document.getElementById("myform").action = "/NewsSystem/servlet/NewsServlet?condition=edit&newsId=" + id;
		document.getElementById('myform').submit();
	}
</script>
</head>

<body>
	<form action="/NewsSystem/servlet/NewsServlet?condition=manage"
		id="myform" method="post">
		<table border="1" align="center" cellpadding="5" cellspacing="0">
			<tr bgcolor='lightyellow'>
				<td><a href='javascript:void(0);'
					onclick="getOnePage('','newsId');">Id</a></td>
				<td>Caption</td>
				<td>Author</td>
				<td>News Date</td>
				<td>Delete</td>
				<td>Edit</td>
			</tr>
			<c:forEach items="${requestScope.newses }" var="news">
				<tr>
					<td>${news.newsId }</td>
					<td><a
						href="/NewsSystem/servlet/NewsServlet?condition=showANews&newsId=${news.newsId }&page=1&pageSize=2">${news.caption }</a></td>
					<td>${news.author }</td>
					<td>${news.newsTime }</td>
					<td><a href="javascript:void(0);"
						onclick="deleteANews(${news.newsId});">delete</a></td>
					<td><a href="javascript:void(0);"
						onclick="editANews(${news.newsId});">edit</a></td>
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
				<td>total: ${requestScope.pageInformation.totalPageCount} pages</td>
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
