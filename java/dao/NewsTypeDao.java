package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.NewsType;

public class NewsTypeDao {
	public List<NewsType> getAll(DatabaseDao databaseDao) throws SQLException {
		List<NewsType> newsTypes = new ArrayList<NewsType>();
		String sql = "select * from newstype";
		databaseDao.query(sql);
		while (databaseDao.next()) {
			NewsType newsType = new NewsType();
			newsType.setNewsTypeId(databaseDao.getInt("newsTypeId"));
			newsType.setName(databaseDao.getString("name"));
			newsTypes.add(newsType);
		}
		return newsTypes;
	}
}
