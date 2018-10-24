package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Comment;
import bean.CommentUserView;
import bean.User;
import service.CommentService;
import tools.PageInformation;
import tools.Tool;

public class CommentServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition = request.getParameter("condition");
		String newsId = request.getParameter("newsId");
		CommentService commentService = new CommentService();

		if ("showComment".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("commentUserView", request, pageInformation);
			pageInformation.setSearchSql(" newsId=" + newsId);
			pageInformation.setOrder("desc");
			pageInformation.setOrderField("time");
			List<CommentUserView> commentUserViews = commentService.getOnePage(pageInformation);

			List<Object> list = new ArrayList<Object>();
			list.add(pageInformation);
			for (int i = 0; i < commentUserViews.size(); i++) {
				list.add(commentUserViews.get(i));
			}
			Gson gson = new Gson();
			String jsonString = gson.toJson(list);
			Tool.returnJsonString(response, jsonString);
		} else if ("praise".equals(condition)) {
			String commentId = request.getParameter("commentId");
			commentService.praise(commentId);
			Integer praise = commentService.getPraise(Integer.parseInt(commentId));
			Gson gson = new Gson();
			String jsonString = gson.toJson(praise);
			Tool.returnJsonString(response, jsonString);
		} else if ("addComment".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			pageInformation.setPage(Integer.parseInt(request.getParameter("page")));
			pageInformation.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
			Integer allRecordCount = Integer.parseInt(request.getParameter("allRecordCount"));

			Comment comment = new Comment();
			comment.setContent(request.getParameter("content"));
			comment.setNewsId(Integer.parseInt(newsId));
			User user = (User) request.getSession().getAttribute("user");
			comment.setUserId(user.getUserId());

			String commentId = request.getParameter("commentId");
			if (commentId == null || commentId.isEmpty()) {
				commentService.addComment(comment);// 对新闻的回复
				
			} else {
				comment.setCommentId(Integer.parseInt(commentId));
				commentService.addCommentToComment(comment);// 对回复的回复
			}
			
			comment = commentService.getComment();
			allRecordCount++;
			Tool.setPageInformation(allRecordCount, pageInformation);

			List<Object> list = new ArrayList<Object>();
			list.add(user);
			list.add(comment);
			list.add(pageInformation);
			Gson gson = new Gson();
			String jsonString = gson.toJson(list);
			Tool.returnJsonString(response, jsonString);
		}
	}
}
