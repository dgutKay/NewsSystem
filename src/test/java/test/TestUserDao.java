package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import bean.User;
import dao.DatabaseDao;
import dao.UserDao;
import tools.PageInformation;

public class TestUserDao {
	static protected UserDao userDao;
	static protected User user;
	static protected DatabaseDao databaseDao;
	static protected PageInformation pageInformation;
	static protected Integer id;

	// 首先执行（在所有@test方法之前执行），并且只执行一次，多个@Test只执行一次
	@BeforeClass
	static public void beforeClass() throws Exception {
		databaseDao = new DatabaseDao();
		databaseDao.drv = "com.mysql.jdbc.Driver";// 数据库类型
		databaseDao.url = "jdbc:mysql://localhost:3306/NewsSystem";// 数据库网址
		databaseDao.usr = "root";// 用户名
		databaseDao.pwd = "659zxcvbnm";// 密码
		userDao = new UserDao();
		user = new User();
		pageInformation = new PageInformation();
	}

	@Test
	public void testHasUser() throws Exception {
		user.setName("kay");
		int result = userDao.hasUser(user, databaseDao);
		assertEquals(result, 1);
	}

	@Test
	public void testRegister() throws Exception {
		user.setType("user");
		user.setName("test");
		user.setPassword("test");
		user.setUsability("use");

		int result = userDao.register(user, databaseDao);
		assertFalse(result == 0);
	}

	@Test
	public void testLogin() throws Exception {
		user.setName("kay");
		user.setPassword("kay");
		int result = userDao.login(user, databaseDao);
		assertEquals(result, 1);
	}

	@Test
	public void testGetOnePage() throws Exception {
		pageInformation.setPage(1);
		pageInformation.setPageSize(2);
		pageInformation.setAllRecordCount(7);
		pageInformation.setTableName("user");
		List<User> users = userDao.getOnePage(pageInformation, databaseDao);
		assertEquals(new Integer(users.size()), pageInformation.getPageSize());
	}

	@Test
	public void testChangeUsability() throws Exception {
		int result = userDao.changeUsability("6", databaseDao);
		assertEquals(result, 1);
	}

	@Test
	public void testDelete() throws Exception {
		int result = userDao.delete("7", databaseDao);
		assertFalse(result == 0);
	}

	@Test
	public void testUpdateHeadIcon() throws Exception {
		user.setUserId(5);
		int result = userDao.updateHeadIcon(user, databaseDao);
		assertFalse(result == 0);
	}

	@AfterClass
	static public void AfterClass() throws Exception {
		databaseDao.close();
	}
}
