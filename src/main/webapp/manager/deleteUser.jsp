<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>deleteUser.jsp</title>
<script type="text/javascript">
	function checkAll(obj) {
		var checkboxs = document.getElementsByName('aCheckbox');
		for (var i = 0; i < checkboxs.length; i++) {
			checkboxs[i].checked = obj.checked;
		}
	}

	function deleteUser() {
		var checkboxs = document.getElementsByName("aCheckbox");
		var ids = "";
		// 拼接id为：1,2,
		for (var i = 0; i < checkboxs.length; i++) {
			if (checkboxs[i].checked == true)
				ids += checkboxs[i].value + ",";
		}
		if (ids.length < 1) {
			alert("Please select at least one user that needs to be deleted!");
			return false;
		}
		ids = ids.substring(0, ids.length - 1); // 删除最后的逗号
		document.getElementById('ids').value = ids;
		document.getElementById("myform").submit();
	}

	function getOnePage(type, orderFieldName) {
		// var url;
		var page = document.getElementById("page");
		// var pageSize = document.getElementById("pageSize");
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
</script> 
</head>

<body>
	<form action="/NewsSystem/servlet/UserServlet?condition=delete"
		id="myform" method="post">
		<table border="1" align="center">
			<tr bgcolor='lightyellow'>
				<td><input type="checkbox" id="checkboxAll"
					onchange="checkAll(this);"></td>
				<td><a href='javascript:void(0);'
					onclick="getOnePage('','userId');">Id</a></td>
				<td>User Type</td>
				<td>User Name</td>
				<td>Register Date</td>
				<td><a href='javascript:void(0);'
					onclick="getOnePage('','usability');"></a>Usability</td>
			</tr>
			<c:forEach items="${requestScope.users }" var="user">
				<tr>
					<td><input type="checkbox" name="aCheckbox"
						value="${user.userId }"></td>
					<td>${user.userId }</td>
					<td>${user.type }</td>
					<td>${user.name }</td>
					<td>${user.registerDate }</td>
					<td>${user.usability }</td>
				</tr>
			</c:forEach>
		</table>
		<table border="1" align="center">
			<tr>
				<td><input type="button" value="Delete Selected"
					onclick="deleteUser();"></td>
				<c:if test="${requestScope.pageInformation.page >1 }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('first','');">First</a>
				</c:if>
				<c:if test="${requestScope.pageInformation.page >1 }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('pre','');">Previous</a>
				</c:if>
				<c:if
					test="${requestScope.pageInformation.page < requestScope.pageInformation.totalPageCount }">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('next','');">Next</a>
				</c:if>
				<c:if
					test="${requestScope.pageInformation.page < requestScope.pageInformation.totalPageCount }">
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
