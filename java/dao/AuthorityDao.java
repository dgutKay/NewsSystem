package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Authority;

public class AuthorityDao {
	public List<Authority> getAll(DatabaseDao databaseDao) throws SQLException {
		List<Authority> authorities = new ArrayList<Authority>();
		databaseDao.query("select * from authority");
		while (databaseDao.next()) {
			Authority authority = new Authority();
			authority.setAuthorityId(databaseDao.getInt("authorityId"));
			authority.setUserType(databaseDao.getString("userType"));
			authority.setUrl(databaseDao.getString("url"));
			authority.setParam(databaseDao.getString("param"));
			authority.setRedirectUrl(databaseDao.getString("redirectUrl"));
			authorities.add(authority);
		}
		return authorities;
	}
}
