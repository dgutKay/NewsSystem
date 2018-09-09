package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.NewsType;
import dao.DatabaseDao;
import dao.NewsTypeDao;

public class NewsTypeService {
	public List<NewsType> getAll() {
		List<NewsType> newsTypes = new ArrayList<NewsType>();

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsTypeDao newsTypeDao = new NewsTypeDao();
			newsTypes = newsTypeDao.getAll(databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newsTypes;
	}
}
