<%@ page language="java" import="java.util.*,tools.WebProperties"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>edit.jsp</title>
<script src='<%=WebProperties.config.getString("ueditConfigJs")%>'
	type="text/javascript"></script>
<script src='<%=WebProperties.config.getString("ueditJs")%>'
	type="text/javascript"></script>
<script src='<%=WebProperties.config.getString("ueditLang")%>'
	type="text/javascript"></script>
<script type="text/javascript">
	function valEmpty(input) {
		var inputValue = document.getElementById(input).value;
		if (inputValue == null || inputValue == "") {
			document.getElementById(input + "Span").innerHTML = "*Can not be empty";
			return false;
		}
		return true;
	}

	function check() {
		result = valEmpty('caption');
		result = valEmpty('author') && result;
		result = valEmpty('newsTime') && result;

		if (result)
			return true; //提交
		else
			return false; //阻止提交
	}
</script>
</head>

<body>
	<form action="/NewsSystem/servlet/NewsServlet?condition=editANews"
		method="post" name="myform" onsubmit="return check()">
		<table border="0" align="center" cellpadding="5" cellspacing="0">
			<tr>
				<td colspan="2" align="center">Edit News</td>
			</tr>
			<tr>
				<td align="right">Caption:</td>
				<td><input type="text" name="caption" id="caption"
					value="${requestScope.news.caption}" onBlur="valEmpty('caption')"><span
					id="captionSpan"></span></td>
			</tr>
			<tr>
				<td align="right">Type:</td>
				<td><select name="newsType" id="newsType">
						<c:forEach items="${applicationScope.newsTypes }" var="newsType">
							<c:choose>
								<c:when test="${newsType.name == requestScope.news.newsType}">
									<option value="${newsType.name}" selected>${newsType.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${newsType.name}">${newsType.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select></td>
			<tr>
				<td align="right">Author:</td>
				<td><input type="text" name="author" id="author"
					value=${requestScope.news.author
					}
					onBlur="valEmpty('author')"><span id="authorSpan"></span></td>
			</tr>
			<tr>
				<td align="right">News Time:</td>
				<td><input type="datetime-local" name="newsTime" id="newsTime"
					value="${requestScope.news.newsTime}" onBlur="valEmpty('newsTime')"><span
					id="newsTimeSpan"></span></td>
			</tr>
			<tr>
				<td colspan="2">
					<div>
						<script id="content" type="text/plain"
							style="width:700px;height:300px;"></script>
					</div>
				</td>
			</tr>
			<tr>
				<td align="center"><input type="submit" value="submit" /></td>
				<td align="center"><input type="button" onclick="cancel()"
					value="cancel" /></td>
			</tr>
		</table>
		<input type="hidden" name="newsId" id="newsId"
			value="${requestScope.news.newsId}">
	</form>
</body>
</html>

<script type="text/javascript">
	// 实例化编辑器
	// 建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	var ue = UE.getEditor('content');

	// 设置编辑器的内容
	ue.ready(function() {
		ue.setContent('${requestScope.news.content}');
	});

	function cancel() { // 取消修改 
		obj = document.getElementById("ids");
		obj.value = "";
		//提交
		document.getElementById('myform').action = "/NewsSystem/servlet/NewsServlet?condition=showANews&newsId="+${requestScope.news.newsId};
		document.getElementById('myform').submit();
	}
</script>