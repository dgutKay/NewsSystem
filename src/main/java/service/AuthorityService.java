package service;

import java.sql.SQLException;
import java.util.List;

import bean.Authority;
import dao.AuthorityDao;
import dao.DatabaseDao;

public class AuthorityService {

	public List<Authority> getAll() {
		DatabaseDao databaseDao=null;
		try {
			databaseDao=new DatabaseDao();
			return new AuthorityDao().getAll(databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(databaseDao.close()<0)
				return null;
		}	
		return null;
	}

}
