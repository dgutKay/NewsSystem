package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.CommentUserView;
import bean.News;
import bean.NewsType;
import service.CommentService;
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
			CommentService commentService = new CommentService();
			PageInformation pageInformation = new PageInformation();
			Integer newsId = Integer.parseInt(request.getParameter("newsId"));
			News news = newsService.getNewsById(newsId);
			Tool.getPageInformation("commentUserView", request, pageInformation);
			pageInformation.setSearchSql(" (newsId=" + newsId + ") ");
			pageInformation.setOrder("desc");
			pageInformation.setOrderField("time");
			List<CommentUserView> commentUserViews = commentService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("commentUserViews", commentUserViews);
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
			Integer newsId = Integer.parseInt(request.getParameter("ids"));
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
		} else if ("showNewsByNewsTypeAjax".equals(condition)) {// 主页多个分类新闻区
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			String newsType = request.getParameter("newsType");

			if (!("all").equals(newsType))
				pageInformation.setSearchSql(" newsType='" + newsType + "' ");

			List<News> newses = newsService.getOnePage(pageInformation);
			List<Object> list = new ArrayList<Object>();
			list.add(pageInformation);// 第一个对象保存分页信息
			list.add(newsType);// 第二个对象保存新闻类别信息
			for (int i = 0; i < newses.size(); i++)// 从第三个对象开始，保存新闻信息
				list.add(newses.get(i));

			Gson gson = new Gson();
			String jsonString = gson.toJson(list);
			Tool.returnJsonString(response, jsonString);
		}
	}

}
