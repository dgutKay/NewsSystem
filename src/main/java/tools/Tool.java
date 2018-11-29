package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tool {
	// 从客户端获取分页、排序、删除等的参数
	public static void getPageInformation(String tableName, HttpServletRequest request,
			PageInformation pageInformation) {
		pageInformation.setTableName(tableName);

		String param = request.getParameter("page");
		if (param == null)
			pageInformation.setPage(null);
		else
			pageInformation.setPage(Integer.parseInt(param));
		param = request.getParameter("pageSize");
		if (param == null)
			pageInformation.setPageSize(null);
		else
			pageInformation.setPageSize(Integer.parseInt(param));
		param = request.getParameter("totalPageCount");
		if (param == null)
			pageInformation.setTotalPageCount(null);
		else
			pageInformation.setTotalPageCount(Integer.parseInt(param));

		param = request.getParameter("allRecordCount");
		if (param == null)
			pageInformation.setAllRecordCount(null);
		else
			pageInformation.setAllRecordCount(Integer.parseInt(param));

		pageInformation.setOrderField(request.getParameter("orderField"));
		pageInformation.setOrder(request.getParameter("order"));
		pageInformation.setIds(request.getParameter("ids"));
		pageInformation.setSearchSql(request.getParameter("searchSql"));
	}

	// 生成表的查询语句
	public static String getSql(PageInformation pageInformation, String type) {
		String sql = "";

		if ("count".equals(type)) { // 查询：只查符合条件的记录数目
			sql += "select count(*) as resultCnt from " + pageInformation.getTableName().toLowerCase();
			// 查询条件
			if (pageInformation.getSearchSql() != null && !pageInformation.getSearchSql().isEmpty()) {
				sql += " where " + pageInformation.getSearchSql();
			}
		} else if ("select".equals(type)) { // 一般查询
			sql += "select * from " + pageInformation.getTableName().toLowerCase();
			// 查询条件
			if (pageInformation.getSearchSql() != null && !pageInformation.getSearchSql().isEmpty())
				sql += " where " + pageInformation.getSearchSql();
			// 排序,默认按主键的降序排列
			if (pageInformation.getOrderField() == null || pageInformation.getOrderField().isEmpty())
				sql += " order by " + pageInformation.getTableName().toLowerCase() + "Id desc";
			else
				sql += " order by " + pageInformation.getOrderField() + " " + pageInformation.getOrder();
			// 分页
			if (pageInformation.getPage() != null && pageInformation.getPage() >= 1)
				sql += " limit "
						+ ((Integer) ((pageInformation.getPage() - 1) * pageInformation.getPageSize())).toString() + ","
						+ pageInformation.getPageSize();
		} else if (pageInformation.getIds() != null && !pageInformation.getIds().isEmpty()) { // 删除
			sql += "delete * from" + pageInformation.getTableName().toLowerCase() + " where "
					+ pageInformation.getTableName().toLowerCase() + "Id in (" + " " + pageInformation.getIds() + ") ";
		}

		return sql;
	}

	// 更新pageInformation的总页数等
	public static void setPageInformation(Integer allRecordCount, PageInformation pageInformation) {
		pageInformation.setAllRecordCount(allRecordCount);
		// 总页数
		Integer totalPageCount = (int) Math.ceil(1.0 * allRecordCount / pageInformation.getPageSize());
		pageInformation.setTotalPageCount(totalPageCount);
		// 防止页码越界 删除时有可能页码越界
		if (pageInformation.getPage() < 1)
			pageInformation.setPage(1);
		if (pageInformation.getPage() > totalPageCount)
			pageInformation.setPage(totalPageCount);
	}

	public static String getStringByMaxLength(String text, Integer maxLength) {
		int number = 0;

		String resultString = "";

		for (int i = 0; i < text.length(); i++) {
			if (number < maxLength) {
				String a = text.substring(i, i + 1);
				if (a.getBytes().length == a.length()) {// 中文，全角
					if (number == maxLength - 1) {// 只差一个字符,在最后加一个空格，保证长度为maxLength
						resultString += " ";
						break;
					}
					number++;
				} else// 半角，英文
					number++;

				resultString += a;
			} else
				break;
		}

		if (resultString.length() < text.length()) {// 要加..
			String a = resultString.substring(resultString.length() - 1);
			if (a.getBytes().length == a.length()) {// 最后一个字符为中文，全角
				resultString = resultString.substring(0, resultString.length() - 1) + "..";
			} else {// 最后一个字符为半角字符
				a = resultString.substring(resultString.length() - 2, resultString.length() - 1);
				if (a.getBytes().length == a.length())// 倒数第二个字符为中文，全角
					resultString = resultString.substring(0, resultString.length() - 2) + "...";
				else// 倒数第二个字符为半角字符
					resultString = resultString.substring(0, resultString.length() - 2) + "..";
			}
		}

		return resultString;
	}

	// 给ajax请求返回json格式的数据
	static public void returnJsonString(HttpServletResponse response, String jsonString)
			throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(jsonString);
		out.flush();
	}

	static public Integer getRandomInRangeInteger(Integer min, Integer max) {
		int rand = min + (int) (Math.random() * ((max - min) + 1));
		return rand;
	}

	static public Long getSecondFromNow(Date old) {// 当前时间领先old多少秒
		Date now = new Date();
		// 得到间隔秒数
		Long between = (now.getTime() - old.getTime()) / 1000;
		return between;
	}

	static public Integer getRandomInRangInteger(Integer min, Integer max) {
		return (min + (int) (Math.random() * ((max - min) + 1)));
	}

	// 生成符合条件的随机字符串
	static public String getRandomString(Integer length, String firstChar, String remainString) {
		int randomNum = getRandomInRangInteger(0, firstChar.length() - 1);
		String result = firstChar.substring(randomNum, randomNum + 1);
		length--;

		for (int i = 0; i < length; i++) {
			randomNum = getRandomInRangInteger(0, remainString.length() - 1);
			result += remainString.substring(randomNum, randomNum + 1);
		}

		return result;
	}

	// 至少需要8个字符，以字母开头，以字母或数字结尾
	static public String getRandomPassword() {
		Integer length = 8;
		String firstChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String remainString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return getRandomString(length, firstChar, remainString);
	}

	// 初始化数组 参数为数组长度和数组初始值
	public static List<Integer> getListWithLengthInitIntValue(Integer length, Integer value) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			list.add(value);
		}
		return list;
	}

	// 保留两位小数，四舍五入
	public static Double formatDouble(Double d) {
		return new Double(Math.round(d * 100) * 1.0 / 100);
	}

}
