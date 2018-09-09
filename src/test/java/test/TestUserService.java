package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import bean.User;
import bean.UserInformation;
import dao.DatabaseDao;
import dao.UserDao;
import service.UserService;
import tools.PageInformation;

public class TestUserService {
	static protected UserService userService;
	static protected User user;
	static protected PageInformation pageInformation;
	static protected Integer id;

	// 首先执行（在所有@test方法之前执行），并且只执行一次，多个@Test只执行一次
	@BeforeClass
	static public void beforeClass() throws Exception {
		// databaseDao = new DatabaseDao("");
		// databaseDao.drv = "com.mysql.jdbc.Driver";// 数据库类型
		// databaseDao.url = "jdbc:mysql://localhost:3306/NewsSystem";// 数据库网址
		// databaseDao.usr = "root";// 用户名
		// databaseDao.pwd = "659zxcvbnm";// 密码
		// databaseDao.init(databaseDao.drv, databaseDao.url, databaseDao.usr,
		// databaseDao.pwd);
		userService = new UserService();
		user = new User();
		pageInformation = new PageInformation();

		pageInformation.setPage(1);
		pageInformation.setPageSize(2);
		pageInformation.setAllRecordCount(7);
		pageInformation.setTableName("user");
	}

	@Test
	public void testRegister() throws Exception {
		user.setType("user");
		user.setName("test");
		user.setPassword("test");
		user.setUsability("use");
		int result = userService.register(user);
		assertEquals(result, 1);
	}

	@Test
	public void testLogin() throws Exception {
		user.setName("kay");
		user.setPassword("kay");
		int result = userService.login(user);
		assertEquals(result, 1);
	}

	@Test
	public void testGetOnePage() throws Exception {
		List<User> users = userService.getOnePage(pageInformation);
		assertEquals(new Integer(users.size()), pageInformation.getPageSize());
	}

	@Test
	public void testCheck() throws Exception {
		List<User> users = userService.check(pageInformation, "6");
		assertEquals(new Integer(users.size()), pageInformation.getPageSize());
	}

	@Test
	public void testDelete() throws Exception {
		pageInformation.setIds("7");
		List<User> users = userService.delete(pageInformation);
		assertEquals(new Integer(users.size()), pageInformation.getPageSize());
	}

	@Test
	public void testChangePassword() throws Exception {
		user.setUserId(5);
		int result = userService.changePassword(user, "testChangePassword");
		assertEquals(result, 1);
	}

	@Test
	public void testGetByUserId() throws Exception {
		UserInformation userInformation = userService.getByUserId(3);
		assertNotNull(userInformation);
	}

}
