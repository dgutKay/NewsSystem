<%@ page language="java" import="java.util.*,tools.WebProperties"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>addNews.jsp</title>
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script src='<%=WebProperties.config.getString("ueditConfigJs")%>'
	type="text/javascript"></script>
<script src='<%=WebProperties.config.getString("ueditJs")%>'
	type="text/javascript"></script>
<script src='<%=WebProperties.config.getString("ueditLang")%>'
	type="text/javascript"></script>
<script type="text/javascript">
	function valEmpty(input) {
		var inputValue = $("#" + input).val();
		if (inputValue == null || inputValue == "") {
			$("#" + input + "Span").html("*Can not be empty");
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

	$(document).ready(function() {
		//实例化编辑器
		//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
		var ue = UE.getEditor('content');
	});
</script>
</head>

<body>
<div align="center">
	<form action="/NewsSystem/servlet/NewsServlet?condition=add"
		method="post" name="myform" onsubmit="return check()">
		<table border="0" align="center" cellpadding="5" cellspacing="0">
			<tr>
				<td colspan="2" align="center">添加新闻</td>
			</tr>
			<tr>
				<td align="right">标题：</td>
				<td><input type="text" name="caption" id="caption"
					onBlur="valEmpty('caption')"><span id="captionSpan"></span></td>
			</tr>
			<tr>
				<td align="right">类型：</td>
				<td><select name="newsType" id="newsType">
						<c:forEach items="${applicationScope.newsTypes }" var="newsType">
							<option value="${newsType.name }">${newsType.name}</option>
						</c:forEach>
				</select></td>
			<tr>
				<td align="right">作者：</td>
				<td><input type="text" name="author" id="author"
					onBlur="valEmpty('author')"><span id="authorSpan"></span></td>
			</tr>
			<tr>
				<td align="right">时间：</td>
				<td><input type="datetime-local" name="newsTime" id="newsTime"
					onBlur="valEmpty('newsTime')"><span id="newsTimeSpan"></span></td>
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
				<td colspan="2" align="center"><input type="submit"
					value="确认" /></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>