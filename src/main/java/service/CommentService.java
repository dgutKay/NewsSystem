package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Comment;
import bean.CommentUserView;
import dao.CommentDao;
import dao.DatabaseDao;
import tools.PageInformation;

public class CommentService {

	public List<CommentUserView> getOnePage(PageInformation pageInformation) {
		List<CommentUserView> commentUserViews = new ArrayList<CommentUserView>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			commentUserViews = commentDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentUserViews;
	}

	public Integer praise(String commentId) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			if (commentDao.praise(commentId, databaseDao) > 0)
				return 1;
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	public Integer addComment(Comment comment) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			comment.setStair(commentDao.getStairByNewsId(comment.getNewsId(), databaseDao) + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	public Integer addCommentToComment(Comment comment) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			CommentUserView oldCommentUserView = commentDao.getByIdFromView(comment.getCommentId(), databaseDao);
			String content = oldCommentUserView.getContent();
			if (content.contains("<br><br>")) {// 消除之前的留言
				content = content.substring(content.indexOf("<br><br>") + 8);
			}
			content = "reply to No " + oldCommentUserView.getStair() + " floor: " + oldCommentUserView.getUserName()
					+ ":<br>" + content + "<br><br>";
			comment.setContent(content + comment.getContent());
			comment.setStair(commentDao.getStairByNewsId(comment.getNewsId(), databaseDao) + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	public Integer getPraise(Integer commentId) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			return commentDao.getPraise(commentId, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	public Comment getComment() {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			return commentDao.getComment(databaseDao);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
