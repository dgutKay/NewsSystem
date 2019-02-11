<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>

<!doctype html>
<html>
<head>
<link href="/NewsSystem/css/1.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//设置下标为奇数的div的类样式为：newsRight
		$(".newsleft:odd").each(function() {
			$(this).attr("class", "newsRight");
		});
	});
</script>
</head>
<body>
	<div class="news">
		<c:forEach items="${requestScope.newsTypes}" var="newsType"
			varStatus="newsTypeStatus">
			<div class="newsleft">
				<table class="invisibleTable">
					<tbody>
						<tr class="newsColumn">
							<td><c:choose>
									<c:when test="${newsType == 'all'}">
										New
									</c:when>
									<c:otherwise>
					        			${newsType}
					    			</c:otherwise>
								</c:choose></td>
							<td align="right"><a
								href="/NewsSystem/servlet/NewsServlet?condition=showNewsByNewsType&newsType=${newsType}&page=1&pageSize=5">More</a>
							</td>
						</tr>
						<c:forEach
							items="${requestScope.newsesList[newsTypeStatus.index]}"
							var="news" varStatus="status">
							<tr>
								<td class="mainPageUl"><a href="${news.url}"
									title="${news.caption}">
										${requestScope.newsCaptionsList[newsTypeStatus.index].get(status.index)}
								</a></td>
								<td align="right" width="130"><myTag:LocalDateTimeTag
										dateTime="${news.newsTime}" type="YMD" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:forEach>
	</div>
	<form>
		<input type="hidden" id="newsTypeNumber"
			value="${requestScope.newsTypesNumber}">
	</form>
</body>
</html>
