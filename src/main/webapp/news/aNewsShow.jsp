<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>aNewsShow.jsp</title>
</head>

<body>
	<div class="center" style="width:800px;margin-top:30px;">
		<table width="800" border="0">
			<tbody>
				<tr>
					<td class="newsCaption">${requestScope.news.caption}</td>
				<tr>
					<td align="center" height="50">作者：${requestScope.news.author}&nbsp;
						&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; <myTag:LocalDateTimeTag
							dateTime="${requestScope.news.newsTime}" type="YMDHM" />
					</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
				<tr>
					<td>${requestScope.news.content}</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
			</tbody>
		</table>
	</div>
	<jsp:include page='/comment/addComment.jsp' flush="true">
		<jsp:param name="newsId" value="${requestScope.news.newsId}" />
		<jsp:param name="page" value="1" />
		<jsp:param name="pageSize" value="${param.pageSize}" />
	</jsp:include>
	<jsp:include
		page='<%="/servlet/CommentServlet?condition=showComment"%>'
		flush="true">
		<jsp:param name="newsId" value="${requestScope.news.newsId}" />
		<jsp:param name="page" value="${param.page}" />
		<jsp:param name="pageSize" value="${param.pageSize}" />
	</jsp:include>
</body>
</html>
<script language="JavaScript">
	if ('${param.addComment}' == 'addComment')
		location.hash = "#commentsStart";
</script>
