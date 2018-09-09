package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.News;
import tools.PageInformation;
import tools.Tool;

public class NewsDao {

	public Integer add(News news, DatabaseDao databaseDao) throws SQLException {
		String sql = "insert into news(caption,author,newsType,content,newsTime) values('" + news.getCaption() + "','"
				+ news.getAuthor() + "','" + news.getNewsType() + "','" + news.getContent() + "','" + news.getNewsTime()
				+ "')";
		return databaseDao.update(sql);
	}

	public List<News> getOnePage(PageInformation pageInformation, DatabaseDao databaseDao) throws SQLException {
		List<News> newses = new ArrayList<News>();
		String sql = Tool.getSql(pageInformation, "count");
		Integer allRecordCount = databaseDao.getCount(sql);
		Tool.setPageInformation(allRecordCount, pageInformation);

		sql = Tool.getSql(pageInformation, "select");
		databaseDao.query(sql);
		while (databaseDao.next()) {
			News news = new News();
			news.setNewsId(databaseDao.getInt("newsId"));
			news.setCaption(databaseDao.getString("caption"));
			news.setAuthor(databaseDao.getString("author"));
			news.setNewsType(databaseDao.getString("newsType"));
			news.setNewsTime(databaseDao.getLocalDateTime("newsTime"));
			news.setPublishTime(databaseDao.getTimestamp("publishTime"));
			newses.add(news);
		}

		return newses;
	}

	public News getById(Integer newsId, DatabaseDao databaseDao) throws SQLException {
		News news = new News();
		databaseDao.getById("news", newsId);
		while (databaseDao.next()) {
			news.setNewsId(databaseDao.getInt("newsId"));
			news.setCaption(databaseDao.getString("caption"));
			news.setAuthor(databaseDao.getString("author"));
			news.setNewsType(databaseDao.getString("newsType"));
			news.setContent(databaseDao.getString("content"));
			news.setNewsTime(databaseDao.getLocalDateTime("newsTime"));
			news.setPublishTime(databaseDao.getTimestamp("publishTime"));
		}

		return news;
	}

	public Integer delete(String tableName, String ids, DatabaseDao databaseDao) throws SQLException {
		return databaseDao.delete(tableName, ids);
	}

	public Integer edit(News news, DatabaseDao databaseDao) throws SQLException {
		String sql = "update news set caption='" + news.getCaption() + "',author='" + news.getAuthor() + "',newsType='"
				+ news.getNewsType() + "',content='" + news.getContent() + "',newsTime='" + news.getNewsTime()
				+ "' where newsId=" + news.getNewsId().toString();
		return databaseDao.update(sql);
	}

	public List<News> getByTypesTopN(String type, Integer n, DatabaseDao databaseDao) throws SQLException {
		List<News> newses = new ArrayList<News>();
		String sql;
		if ("all".equals(type))
			sql = "select newsId,caption,author,newsType,newsTime,publishTime from news order by newsTime desc limit 0,"
					+ n.toString();
		else
			sql = "select newsId,caption,author,newsType,newsTime,publishTime from news  where newsType ='" + type
					+ "' order by newsTime desc limit 0," + n.toString();

		databaseDao.query(sql);
		while (databaseDao.next()) {
			News news = new News();
			news.setNewsId(databaseDao.getInt("newsId"));
			news.setCaption(databaseDao.getString("caption"));
			news.setAuthor(databaseDao.getString("author"));
			news.setNewsType(databaseDao.getString("newsType"));
			news.setNewsTime(databaseDao.getLocalDateTime("newsTime"));
			news.setPublishTime(databaseDao.getTimestamp("publishTime"));
			newses.add(news);
		}
		return newses;
	}

}
