package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tools.PageInformation;
import tools.Tool;
import bean.Comment;
import bean.CommentUserView;

public class CommentDao {

	public List<CommentUserView> getOnePage(PageInformation pageInformation, DatabaseDao databaseDao)
			throws SQLException {
		List<CommentUserView> commentUserViews = new ArrayList<CommentUserView>();
		String sqlCount = Tool.getSql(pageInformation, "count");
		Integer allRecordCount = databaseDao.getCount(sqlCount);// 符合条件的总记录数
		Tool.setPageInformation(allRecordCount, pageInformation);// 更新pageInformation的总页数等

		String sqlSelect = Tool.getSql(pageInformation, "select");
		databaseDao.query(sqlSelect);
		while (databaseDao.next()) {
			CommentUserView commentUserView = new CommentUserView();
			commentUserView.setCommentId(databaseDao.getInt("commentId"));
			commentUserView.setContent(databaseDao.getString("content"));
			commentUserView.setNewsId(databaseDao.getInt("newsId"));
			commentUserView.setPraise(databaseDao.getInt("praise"));
			commentUserView.setStair(databaseDao.getInt("stair"));
			commentUserView.setTime(databaseDao.getTimestamp("time"));
			commentUserView.setUserName(databaseDao.getString("name"));
			commentUserView.setHeadIconUrl(databaseDao.getString("headIconUrl"));
			commentUserView.setUserId(databaseDao.getInt("userId"));
			commentUserViews.add(commentUserView);
		}
		return commentUserViews;
	}

	public Integer praise(String commentId, DatabaseDao databaseDao) throws SQLException {
		String sql = "update comment set praise=praise+1 where commentId=" + commentId;
		return databaseDao.update(sql);
	}

	public Integer getStairByNewsId(Integer newsId, DatabaseDao databaseDao) throws SQLException {
		String sql = "select max(stair) from comment where newsId=" + newsId.toString();
		databaseDao.query(sql);
		while (databaseDao.next()) {
			return databaseDao.getInt("max(stair)");
		}
		return 1;
	}

	public Integer addComment(Comment comment, DatabaseDao databaseDao) throws SQLException {
		String sql = "insert into comment(newsId,userId,content,stair) values(" + comment.getNewsId() + ","
				+ comment.getUserId() + ",'" + comment.getContent() + "', " + comment.getStair() + ")";
		return databaseDao.update(sql);
	}

	public Comment getById(Integer commentId, DatabaseDao databaseDao) throws SQLException {
		databaseDao.getById("comment", commentId);
		while (databaseDao.next()) {
			Comment comment = new Comment();
			comment.setCommentId(databaseDao.getInt("commentId"));
			comment.setContent(databaseDao.getString("content"));
			comment.setNewsId(databaseDao.getInt("newsId"));
			comment.setPraise(databaseDao.getInt("praise"));
			comment.setStair(databaseDao.getInt("stair"));
			comment.setTime(databaseDao.getTimestamp("time"));
			comment.setUserId(databaseDao.getInt("userId"));
			return comment;
		}
		return null;
	}

	public CommentUserView getByIdFromView(Integer commentId, DatabaseDao databaseDao) throws SQLException {
		String sql = "select * from commentUserView  where commentId=" + commentId.toString();
		databaseDao.query(sql);
		while (databaseDao.next()) {
			CommentUserView commentUserView = new CommentUserView();
			commentUserView.setCommentId(databaseDao.getInt("commentId"));
			commentUserView.setContent(databaseDao.getString("content"));
			commentUserView.setNewsId(databaseDao.getInt("newsId"));
			commentUserView.setPraise(databaseDao.getInt("praise"));
			commentUserView.setStair(databaseDao.getInt("stair"));
			commentUserView.setTime(databaseDao.getTimestamp("time"));
			commentUserView.setUserName(databaseDao.getString("name"));
			commentUserView.setHeadIconUrl(databaseDao.getString("headIconUrl"));
			commentUserView.setUserId(databaseDao.getInt("userId"));
			return commentUserView;
		}
		return null;
	}

	public Integer getPraise(Integer commentId, DatabaseDao databaseDao) throws SQLException {
		String sql = "select * from comment where commentId=" + commentId;
		databaseDao.query(sql);
		if (databaseDao.next()) {
			return databaseDao.getInt("praise");
		}
		return null;
	}

	public Comment getComment(DatabaseDao databaseDao) throws SQLException {
		String sql = "select * from comment order by time desc";
		databaseDao.query(sql);
		if (databaseDao.next()) {
			Comment comment = new Comment();
			comment.setCommentId(databaseDao.getInt("commentId"));
			comment.setContent(databaseDao.getString("content"));
			comment.setNewsId(databaseDao.getInt("newsId"));
			comment.setPraise(databaseDao.getInt("praise"));
			comment.setStair(databaseDao.getInt("stair"));
			comment.setTime(databaseDao.getTimestamp("time"));
			comment.setUserId(databaseDao.getInt("userId"));
			return comment;
		}
		return null;
	}
}
