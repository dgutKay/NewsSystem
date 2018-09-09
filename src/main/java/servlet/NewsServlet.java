package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.News;
import bean.NewsType;
import service.NewsService;
import tools.Message;
import tools.PageInformation;
import tools.ServletTool;
import tools.Tool;
import tools.WebProperties;

public class NewsServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition = request.getParameter("condition");
		NewsService newsService = new NewsService();
		Message message = new Message();

		if ("add".equals(condition)) {
			News news = ServletTool.news(request);
			int result = newsService.add(news);
			if (result == 1) {
				message.setMessage("Added news successfully!");
			} else {
				message.setMessage("Fail to add news! Please add again or contact the manager!");
			}
			message.setRedirectUrl("/NewsSystem/servlet/NewsServlet?condition=showNews&page=1&pageSize=2");
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if ("showNews".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			List<News> newses = newsService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("newses", newses);
			getServletContext().getRequestDispatcher("/news/newsShow.jsp").forward(request, response);
		} else if ("showANews".equals(condition)) {
			Integer newsId = Integer.parseInt(request.getParameter("newsId"));
			News news = newsService.getNewsById(newsId);
			request.setAttribute("news", news);
			getServletContext().getRequestDispatcher("/news/aNewsShow.jsp").forward(request, response);
		} else if ("manage".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			List<News> newses = newsService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("newses", newses);
			getServletContext().getRequestDispatcher("/news/manage/manage.jsp").forward(request, response);
		} else if ("delete".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			List<News> newses = newsService.delete(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("newses", newses);
			getServletContext().getRequestDispatcher("/news/manage/manage.jsp").forward(request, response);
		} else if ("edit".equals(condition)) { // 显示编辑页面
			Integer newsId = Integer.parseInt(request.getParameter("newsId"));
			News news = newsService.getNewsById(newsId);
			request.setAttribute("news", news);
			getServletContext().getRequestDispatcher("/news/manage/edit.jsp").forward(request, response);
		} else if ("editANews".equals(condition)) { // 修改新闻
			News news = ServletTool.news(request);
			int result = newsService.edit(news);
			if (result == 1) {
				message.setMessage("Edited news successfully!");
			} else {
				message.setMessage("Fail to edit news! Please edit again or contact the manager!");
			}
			message.setRedirectUrl("/NewsSystem/servlet/NewsServlet?condition=showANews&newsId="
					+ news.getNewsId().toString() + "&page=1&pageSize=2");
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if ("homepage".equals(condition)) {// 主页多个分类新闻区
			String newsTypesString = new String(WebProperties.config.getString("newsTypes").getBytes("ISO-8859-1"),
					"UTF-8");
			String[] newsTypes = newsTypesString.split(",");
			Integer homePageNewsN = Integer.parseInt(WebProperties.config.getString("homePageNewsN"));
			List<List<String>> newsCaptionsList = new ArrayList<List<String>>();
			List<List<News>> newsesList = newsService.getByTypesTopN(newsTypes, homePageNewsN, newsCaptionsList);
			int newsTypesNumber = newsTypes.length;
			request.setAttribute("newsTypes", newsTypes);
			request.setAttribute("newsTypesNumber", newsTypesNumber);
			request.setAttribute("newsesList", newsesList);
			request.setAttribute("newsCaptionsList", newsCaptionsList);
			getServletContext().getRequestDispatcher("/homepage.jsp").include(request, response);
			return;
		} else if ("showNewsByNewsType".equals(condition)) {// 主页多个分类新闻区
			List<NewsType> newsTypes = (List<NewsType>) this.getServletContext().getAttribute("newsTypes");
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			String newsType = request.getParameter("newsType");

			if (!("all").equals(newsType))
				pageInformation.setSearchSql(" newsType='" + newsType + "' ");

			List<News> newss = newsService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("newses", newss);
			request.setAttribute("newsTypes", newsTypes);
			request.setAttribute("newsType", newsType);
			getServletContext().getRequestDispatcher("/news/newsShowByType.jsp").forward(request, response);
			return;
		}
	}

}
