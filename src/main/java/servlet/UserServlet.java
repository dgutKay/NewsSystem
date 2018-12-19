package servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.User;
import bean.UserInformation;
import service.UserService;
import tools.Message;
import tools.PageInformation;
import tools.SearchTool;
import tools.Tool;

public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition = request.getParameter("condition");
		HttpSession session = request.getSession();
		UserService userService = new UserService();
		Message message = new Message();
		Integer result;

		User user = new User();
		user.setName(request.getParameter("name"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));

		if ("register".equals(condition)) {
			user.setType(request.getParameter("type"));
			if ("user".equals(user.getType()))
				user.setUsability("use");
			else
				user.setUsability("stop");

			String checkCode = request.getParameter("checkCode");
			String severCheckCode = (String) session.getAttribute("checkCode");// 获取session中的验证码
			if (severCheckCode == null) {// 服务器端验证图片验证码不存在
				result = -3;
				message.setMessage("服务器端验证图片验证码不存在!");
			} else if (!severCheckCode.equals(checkCode)) {// 服务器端验证图片验证码验证失败
				result = -4;
				message.setMessage("The check code is wrong!");
			} else {// 验证码验证正确
				result = userService.register(user);// 注册用户
				if (result == 1) {
					message.setMessage("Registered successfully!");
					message.setRedirectUrl("/NewsSystem/user/free/login.jsp");
				} else if (result == -1) {
					message.setMessage("User already exist! Please register again!");
				} else if (result == 0) {
					message.setMessage("Fail to register! Please register again!");
				} else if (result == -10) {
					message.setMessage("The email was registered! Please change another email!");
				} else if (result == -11) {
					message.setMessage(
							"The user is existed! And the email is registered! Please change another email!");
				}
			}
			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("login".equals(condition)) {
			String checkCode = request.getParameter("checkCode");
			String severCheckCode = (String) session.getAttribute("checkCode");// 获取session中的验证码

			if (severCheckCode == null) {// 服务器端验证图片验证码不存在
				result = -4;
				message.setMessage("服务器端验证图片验证码不存在!");
			} else if (!severCheckCode.equals(checkCode)) {// 服务器端验证图片验证码验证失败
				result = -5;
				message.setMessage("The check code is wrong!");
			} else {// 验证码验证正确
				result = userService.login(user);
				if (result == 1) {
					user.setPassword(null);// 防止密码泄露
					session.setAttribute("user", user);

					String originalUrl = (String) session.getAttribute("originalUrl");
					if (originalUrl == null)
						message.setRedirectUrl("/NewsSystem/index.jsp");
					else
						message.setRedirectUrl(originalUrl);
				} else if (result == 0) {
					message.setMessage("The password is incorrect. Please login again!");
				} else if (result == -1) {
					message.setMessage("The account is not existed. Please login again!");
				} else if (result == -2) {
					message.setMessage("The account exists, but has been stopped. Please contact the manager!");
				} else if (result == -3) {
					message.setMessage("Fail to login! Please login again!");
				}
			}
			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("showPage".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			List<User> users = userService.getOnePage(pageInformation);
			request.setAttribute("users", users);
			request.setAttribute("pageInformation", pageInformation);
			getServletContext().getRequestDispatcher("/manager/showUser.jsp").forward(request, response);
		} else if ("check".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			String id = pageInformation.getIds();
			pageInformation.setIds(null);
			List<User> users = userService.check(pageInformation, id);
			if (users == null) {
				message.setMessage("Fail to change! Please change again or contact the manager!");
				message.setRedirectUrl("/NewsSystem/servlet/UserServlet?condition=check&page=1&pageSize=2");
			} else {
				request.setAttribute("users", users);
				request.setAttribute("pageInformation", pageInformation);
				getServletContext().getRequestDispatcher("/manager/checkUser.jsp").forward(request, response);
			}
		} else if ("search".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			pageInformation.setSearchSql(SearchTool.searchUser(request));
			List<User> users = userService.getOnePage(pageInformation);
			request.setAttribute("users", users);
			request.setAttribute("pageInformation", pageInformation);
			getServletContext().getRequestDispatcher("/manager/showUser.jsp").forward(request, response);
		} else if ("delete".equals(condition)) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			pageInformation.setSearchSql(" type='user' or type='newsAuthor'");
			List<User> users = userService.delete(pageInformation);
			request.setAttribute("users", users);
			request.setAttribute("pageInformation", pageInformation);
			getServletContext().getRequestDispatcher("/manager/deleteUser.jsp").forward(request, response);
		} else if ("changePassword".equals(condition)) {
			String newPassword = request.getParameter("newPassword");
			user = (User) request.getSession().getAttribute("user");
			user.setPassword(request.getParameter("curPassword"));
			result = userService.changePassword(user, newPassword);
			if (result == 1) {
				message.setMessage("Changed successfully!");
			} else if (result == -1) {
				message.setMessage("The old password is wrong! Please try again!");
			} else {
				message.setMessage("Fail to change! Please contact the manager!");
			}

			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("showUserInformation".equals(condition)) { // 显示普通用户个人信息
			user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				UserInformation userInformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userInformation", userInformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/showUserInformation.jsp").forward(request, response);
		} else if ("changeUserInformation".equals(condition)) { // 修改普通用户个人信息的第一步：显示可修改信息
			user = (User) session.getAttribute("user");
			if ("user".equals(user.getType())) {
				UserInformation userInformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userInformation", userInformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/changeUserInformation.jsp").forward(request,
					response);
		} else if ("changeInformation".equals(condition)) { // 修改普通用户个人信息的第二步：修改信息
			user = (User) session.getAttribute("user");
			result = userService.updateInformation(user, request);
			if (result >= 3) {
				message.setMessage("Changed successfully!");
			} else {
				message.setMessage("Fail to change! Please contact the manager!");
			}
			message.setRedirectUrl("/NewsSystem/servlet/UserServlet?condition=showUserInformation");
			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("exit".equals(condition)) {
			session.removeAttribute("user");
			response.sendRedirect("/NewsSystem/index.jsp");
		} else if ("newPassword".equals(condition)) {
			String rand = (String) request.getParameter("rand");
			Integer trueRand = (Integer) session.getAttribute("rand");
			Date old = (Date) session.getAttribute("time");
			message.setRedirectUrl("/NewsSystem/user/free/findPassword.jsp");
			if (!rand.equals(trueRand.toString())) {
				// rand值不对，无权限修改密码
				result = -3;
				message.setMessage("无权限修改密码！");
			} else if (old == null || Tool.getSecondFromNow(old) > 300) {
				// 修改密码超时
				result = -2;
				message.setMessage("修改密码超时！");
			} else {
				result = userService.updatePassword(user);
				if (result == 1) {
					message.setMessage("修改密码成功！");
					message.setRedirectUrl("/NewsSystem/user/free/login.jsp");
				} else if (result == -1) {
					message.setMessage("修改密码失败！");
				}
			}
			session.removeAttribute("email");// 删除session数据
			session.removeAttribute("rand");
			session.removeAttribute("time");
			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if (condition.equals("findPassword")) {// 找回密码
			if ("email".equals(request.getParameter("type"))) {
				user.setEmail(request.getParameter("wayInput"));
				Integer rand = Tool.getRandomInRangeInteger(10, 100000);// 随机数作为验证修改密码用
				result = userService.findPasswordByEmail(user, rand);
				if (result == 1) {// 发送邮件成功
					session.setAttribute("email", user.getEmail());
					session.setAttribute("rand", rand);
					session.setAttribute("time", new Date());
					message.setMessage("请登录邮箱查看邮件，按邮件提示操作找回密码！");
				} else if (result == -1) {
					message.setMessage("发送邮件失败！");
				} else if (result == -2) {
					message.setMessage("邮箱未注册过！");
				}
				message.setResult(result);
				Gson gson = new Gson();
				String jsonString = gson.toJson(message);
				Tool.returnJsonString(response, jsonString);
			} else if ("telephone".equals(request.getParameter("type"))) {

			}
		} else if (condition.equals("batchAdd")) {// 批量添加用户
			String url = userService.batchAdd(request);

			if (url.startsWith("-")) {
				message.setResult(-1);
				message.setMessage("操作失败！可能的失败原因：用户名与已有用户重名。");
			} else {
				message.setResult(1);
				message.setMessage("success!请下载以下链接的excel文件，其中有每个用户的初始密码。<br><br><br>");
				message.setRedirectUrl(url);
			}

			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("qqLogin".equals(condition)) {
			user.setName(request.getParameter("nickname"));
			user.setOpenId(request.getParameter("openId"));
			user.setAccessToken(request.getParameter("accessToken"));

			if (userService.qqLogin(user) == 1) {
				user.setPassword(null); // 防止密码泄露
				session.setAttribute("user", user);
				getServletContext().getRequestDispatcher("index.jsp").forward(request, response);
			} else {
				// 如果已有用户登录，可以强行将原有用户注销
				session.removeAttribute("user");
				// 保存qq用户信息
				session.setAttribute("qqUser", user);
				message.setResult(-1);
				// 绑定用户的页面
				getServletContext().getRequestDispatcher("/user/free/qqBindUser.jsp").forward(request, response);
				return;
			}
		} else if ("qqBindUser".equals(condition)) {// qq登录 回调后
			user = (User) session.getAttribute("qqUser");
			String qqType = request.getParameter("qqType");

			if ("bindNewUser".equals(qqType)) {
				result = userService.qqBindNewUser(user);
				if (result == 1) {// 登录成功
					session.setAttribute("user", user);
					response.sendRedirect("/NewsSystem/index.jsp");
					return;
				} else {// 绑定失败，需要重新登录
					response.sendRedirect("/user/free/login.jsp");
					return;
				}
			} else if ("bindOldUser".equals(qqType)) {
				userService.qqBindOldUser(user);
				return;
			}
		}
	}

}
