<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>manage.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="/NewsSystem/plugin/DataTables/datatables.css">
<script type="text/javascript" charset="utf8"
	src="/NewsSystem/plugin/DataTables/datatables.js">
</script>
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
		$("#rightDiv").load("/NewsSystem/servlet/NewsServlet?condition=manage", $("#myform").serialize());
	}

	function deleteANews(id) {
		$("#ids").val(id);
		$("#rightDiv").load("/NewsSystem/servlet/UserServlet?condition=delete", $("#myform").serialize());
	}

	function editANews(id) {
		$("#ids").val(id);
		$("#myform").attr("target", "_blank");
		$("#myform").attr("action", "/NewsSystem/servlet/NewsServlet?condition=edit").submit();
	}

</script>
</head>

<body>
	<form id="myform" method="post">
		<table border="1" align="center" cellpadding="5" cellspacing="0" id="myTable">
			<tr bgcolor='lightyellow'>
				<td><a href='javascript:void(0);'
					onclick="getOnePage('','newsId');">Id</a></td>
				<td>标题</td>
				<td>作者</td>
				<td>时间</td>
				<td>删除</td>
				<td>修改</td>
			</tr>
			<c:forEach items="${requestScope.newses }" var="news">
				<tr>
					<td  width="5%">${news.newsId }</td>
					<td width="30%"><a
						href="/NewsSystem/servlet/NewsServlet?condition=showANews&newsId=${news.newsId }&page=1&pageSize=2">${news.caption }</a></td>
					<td width="15%">${news.author }</td>
					<td width="20%">${news.newsTime }</td>
					<td width="10%"><a href="javascript:void(0);"
						onclick="deleteANews(${news.newsId});">删除</a></td>
					<td width="10%"><a href="javascript:void(0);"
						onclick="editANews(${news.newsId});">修改</a></td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<table border="1" align="right" cellpadding="5" cellspacing="0">
			<tr>
				<c:if test="${requestScope.pageInformation.page>1 }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('first','');">首页</a>
				</c:if>
				<c:if test="${requestScope.pageInformation.page>1 }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('pre','');">上一页</a>
				</c:if>
				<c:if
					test="${requestScope.pageInformation.page< requestScope.pageInformation.totalPageCount }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('next','');">下一页</a>
				</c:if>
				<c:if
					test="${requestScope.pageInformation.page< requestScope.pageInformation.totalPageCount }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('last','');">尾页</a>
				</c:if>
				<td>总共：${requestScope.pageInformation.totalPageCount} 页</td>
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
