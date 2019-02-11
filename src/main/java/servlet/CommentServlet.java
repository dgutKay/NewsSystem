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
import tools.Message;
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
		Message message=new Message();

		if ("showComment".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("commentUserView", request, pageInformation);
			pageInformation.setSearchSql(" newsId=" + newsId);
			pageInformation.setOrder("desc");
			pageInformation.setOrderField("time");
			List<CommentUserView> commentUserViews = commentService.getOnePage(pageInformation);
			
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("commentUserViews", commentUserViews);
			request.getServletContext().getRequestDispatcher("/comment/showComment.jsp").include(request,response);
			return;
		} else if ("praise".equals(condition)) {
			String commentId = request.getParameter("commentId");
			commentService.praise(commentId);
			Integer praise = commentService.getPraise(commentId);
			message.setResult(praise);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("addComment".equals(condition)) {
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
			
			getServletContext().getRequestDispatcher("/servlet/CommentServlet?condition=showComment").forward(request, response);
		}
	}
}
