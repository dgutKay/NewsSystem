<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<title>articleNumberByMonthInAYear.jsp</title>
<meta charset="utf-8">
<script type="text/javascript"
	src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#myform").on("submit", function(ev) {
			$.ajax({
				type : "post",
				dataType : "json",
				url : '/NewsSystem/servlet/StatisticServlet?condition=articleNumberByMonthInAYear',
				data : $("#myform").serialize(),
				success : function(data) {
					if (data != null) {
						alert(data.message);
						if (data.result == 1)
							$("#myChart").html("<br><br><a href='" + data.redirectUrl + "'>excel文件</a>");
					} else
						alert("111异常！");
				},
				error : function() {
					alert("ccc异常！");
				}
			});
			ev.preventDefault();
		});
	});
</script>

</head>
<body>
	<div>
		年度各月新闻发布数据折线图<br>
		<br>
		<br>
		<form id="myform">
			请输入年份： <input type="number" step="1" min="2017" max="2030"
				name="year" title="年份应在2017-2030之间" /> <input type="submit"
				value="    提交    " />
		</form>
	</div>

	<div id="myChart"></div>

</body>
</html>
