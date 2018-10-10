package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.User;
import tools.Encryption;
import tools.PageInformation;
import tools.Tool;
import tools.WebProperties;

public class UserDao {
	public Integer hasUser(User user, DatabaseDao databaseDao) throws SQLException {
		String sql = "select * from user where name='" + user.getName() + "'";
		databaseDao.query(sql);
		while (databaseDao.next())
			return 1;
		return 0;
	}

	public Integer register(User user, DatabaseDao databaseDao) throws SQLException {
		user.setHeadIconUrl("\\" + WebProperties.propertiesMap.get("projectName")
				+ WebProperties.propertiesMap.get("headIconFileDefault"));// 默认头像
		String sql = "insert into user(type,name,email,password,salt,usability,headIconUrl) values('" + user.getType()
				+ "','" + user.getName() + "','" + user.getEmail() + "','" + user.getPassword() + "','" + user.getSalt()
				+ "','" + user.getUsability() + "','" + user.getHeadIconUrl().replace("\\", "\\\\") + "')";
		return databaseDao.update(sql);
	}

	public Integer login(User user, DatabaseDao databaseDao) throws SQLException {
		String sql = "select * from user where name='" + user.getName() + "';";
		databaseDao.query(sql);
		while (databaseDao.next()) {
			user.setSalt(databaseDao.getString("salt"));
			if (Encryption.checkPassword(user, databaseDao.getString("password"))) {

				if ("use".equals(databaseDao.getString("usability"))) {
					user.setUserId(databaseDao.getInt("userId"));
					user.setType(databaseDao.getString("type"));
					user.setHeadIconUrl(databaseDao.getString("headIconUrl"));
					user.setRegisterDate(databaseDao.getTimestamp("registerDate"));
					return 1; // 可以登录
				}
				return -2; // 用户存在，但被停用
			}
			return 0; // 密码错误
		}
		return -1; // 该用户不存在
	}

	public List<User> getOnePage(PageInformation pageInformation, DatabaseDao databaseDao) throws SQLException {
		List<User> users = new ArrayList<User>();
		String sql = Tool.getSql(pageInformation, "count");
		Integer allRecordCount = databaseDao.getCount(sql);
		Tool.setPageInformation(allRecordCount, pageInformation);

		sql = Tool.getSql(pageInformation, "select");
		databaseDao.query(sql);
		while (databaseDao.next()) {
			User user = new User();
			user.setUserId(databaseDao.getInt("userId"));
			user.setName(databaseDao.getString("name"));
			user.setRegisterDate(databaseDao.getTimestamp("registerDate"));
			user.setType(databaseDao.getString("type"));
			user.setUsability(databaseDao.getString("usability"));
			users.add(user);
		}
		return users;
	}

	// 切换用户的可用性
	public Integer changeUsability(String id, DatabaseDao databaseDao) throws SQLException {// 切换用户的可用性
		String sql = "select * from user where userId='" + id + "';";
		databaseDao.query(sql);
		while (databaseDao.next()) {
			if (databaseDao.getString("usability").equals("use"))
				sql = "update user set usability='stop' where userId='" + id + "';";
			else
				sql = "update user set usability='use' where userId='" + id + "';";
			databaseDao.update(sql);
			return 1;
		}
		return 0;
	}

	// 删除多个用户
	public Integer delete(String ids, DatabaseDao databaseDao) throws SQLException {
		if (ids != null && ids.length() > 0) {
			String sql = "delete from user where userId in (" + ids + ") ";
			return databaseDao.update(sql);
		}
		return -1;
	}

	public Integer updateHeadIcon(User user, DatabaseDao databaseDao) throws SQLException {
		String sql = "update user set headIconUrl='" + user.getHeadIconUrl() + "' where userId="
				+ user.getUserId().toString();
		return databaseDao.update(sql.replace("\\", "\\\\"));

	}

	public Integer updatePassword(User user, DatabaseDao databaseDao) throws SQLException {
		String sql = "update user set password='" + user.getPassword() + "', salt='" + user.getSalt()
				+ "' where email='" + user.getEmail() + "'";
		return databaseDao.update(sql);
	}
}
