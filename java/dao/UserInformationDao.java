package dao;

import java.sql.SQLException;

import bean.User;
import bean.UserInformation;

public class UserInformationDao {
	public UserInformation getByUserId(Integer userId, DatabaseDao databaseDao) throws SQLException {
		UserInformation userInformation = null;
		String sql = "select * from userinformation where userId=" + userId;
		databaseDao.query(sql);
		while (databaseDao.next()) {
			userInformation = new UserInformation();
			userInformation.setUserInformationId(databaseDao.getInt("userInformationId"));
			userInformation.setUserId(databaseDao.getInt("userId"));
			userInformation.setSex(databaseDao.getString("sex"));
			userInformation.setHobby(databaseDao.getString("hobby"));
		}
		return userInformation;
	}

	public Integer update(UserInformation userInformation, DatabaseDao databaseDao) throws SQLException {
		String sql = "update userinformation set sex='" + userInformation.getSex() + "',hobby='"
				+ userInformation.getHobby() + "' where userId=" + userInformation.getUserId();
		return databaseDao.update(sql);
	}

	public Integer insert(UserInformation userInformation, DatabaseDao databaseDao) throws SQLException {
		String sql = "insert into userinformation(userId,sex,hobby) values(" + userInformation.getUserId() + ",'"
				+ userInformation.getSex() + "','" + userInformation.getHobby() + "')";
		return databaseDao.update(sql);
	}

	public boolean hasUserId(Integer userId, DatabaseDao databaseDao) throws SQLException {
		String sql = "select * from userinformation where userId=" + userId.toString();
		databaseDao.query(sql);
		while (databaseDao.next()) {
			return true;
		}
		return false;
	}
}
