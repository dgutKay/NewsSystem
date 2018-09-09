package tools;

import javax.servlet.http.HttpServletRequest;

public class SearchTool {
	// 增加一个字符型与查询条件:模糊查询
	private static String addStringFuzzyAnd(String fieldName, String searchSql, HttpServletRequest request) {
		String fieldValue = request.getParameter(fieldName);
		if (fieldValue != null && !fieldValue.isEmpty() && !fieldValue.equals("all")) {
			if (searchSql.length() > 1) { // 已经有一个条件，需要用and
				searchSql += " and " + fieldName + " like '%" + fieldValue + "%'";
			} else {
				searchSql += " " + fieldName + " like '%" + fieldValue + "%'";
			}
		}
		return searchSql;
	}

	// 增加一个字符型与查询条件:精确查询
	private static String addStringAnd(String fieldName, String searchSql, HttpServletRequest request) {
		String fieldValue = request.getParameter(fieldName);
		if (fieldValue != null && !fieldValue.isEmpty() && !fieldValue.equals("all")) {
			if (searchSql.length() > 1) {
				searchSql += " and " + fieldName + "='" + fieldValue + "'";
			} else {
				searchSql += " " + fieldName + "='" + fieldValue + "'";
			}
		}
		return searchSql;
	}

	// 增加一个日期与查询条件:>=lowDate and <upDate
	private static String addDateAnd(String fieldName, String searchSql, HttpServletRequest request) {
		String lowDate = request.getParameter("lowDate");
		String upDate = request.getParameter("upDate");
		if (lowDate != null && !lowDate.isEmpty()) {
			if (searchSql.length() > 1) {
				searchSql += " and " + fieldName + ">='" + lowDate + "' ";
			} else {
				searchSql += " " + fieldName + ">='" + lowDate + "' ";
			}
		}
		if (upDate != null && !upDate.isEmpty()) {
			if (searchSql.length() > 1) {
				searchSql += " and " + fieldName + "<'" + upDate + "' ";
			} else {
				searchSql += " " + fieldName + "<'" + upDate + "' ";
			}
		}
		return searchSql;
	}

	// 用户表的查询条件
	public static String searchUser(HttpServletRequest request) {
		String searchSql = "";
		searchSql = addStringAnd("type", searchSql, request);
		searchSql = addStringFuzzyAnd("name", searchSql, request);
		searchSql = addStringAnd("usability", searchSql, request);
		searchSql = addDateAnd("registerDate", searchSql, request);
		return searchSql;
	}

}
