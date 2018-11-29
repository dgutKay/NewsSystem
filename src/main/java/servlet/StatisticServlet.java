package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import service.NewsService;
import tools.Message;
import tools.Tool;

public class StatisticServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition = request.getParameter("condition");
		Message message = new Message();
		NewsService newsService = new NewsService();
		String result;
		Gson gson = new Gson();

		if ("articleNumberByMonthInAYear".equals(condition)) {
			result = newsService.articleNumberByMonthInAYear(request);

			if (result.startsWith("-")) {
				message.setResult(-1);
				message.setMessage("操作失败！");
			} else {
				message.setResult(1);
				message.setMessage("成功！请下载以下链接的excel文件。");
				message.setRedirectUrl(result);
			}

			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("articleNumberByMonthInAYearEveryYear".equals(condition)) {
			result = newsService.articleNumberByMonthInAYearEveryYear(request);

			if (result.startsWith("-")) {
				message.setResult(-1);
				message.setMessage("操作失败！");
			} else {
				message.setResult(1);
				message.setMessage("成功！请下载以下链接的excel文件。");
				message.setRedirectUrl(result);
			}

			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		}
	}
}
