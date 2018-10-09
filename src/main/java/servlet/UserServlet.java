package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			} else if (!severCheckCode.equals(checkCode)) {// 服务器端验证图片验证码验证失败
				result = -4;
			} else {// 验证码验证正确
				result = userService.register(user);// 注册用户
			}

			// 将result返回客户端的 ajax 请求
			Tool.returnIntResult(response, result);// 使用工具类的方法给ajax请求返回json格式的数据

			/*
			 * if (result == 1) {
			 * message.setMessage("Registered successfully!");
			 * message.setRedirectUrl("/NewsSystem/user/free/login.jsp"); } else
			 * if (result == 0) {
			 * message.setMessage("User already exist! Please register again!");
			 * message.setRedirectUrl("/NewsSystem/user/free/register.jsp"); }
			 * else {
			 * message.setMessage("Fail to register! Please register again!");
			 * message.setRedirectUrl("/NewsSystem/user/free/register.jsp"); }
			 * request.setAttribute("message", message);
			 * getServletContext().getRequestDispatcher("/message.jsp").forward(
			 * request, response);
			 */
		} else if ("login".equals(condition)) {
			result = userService.login(user);
			if (result == 1) {
				request.getSession().setAttribute("user", user);
				getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
				return;
			} else if (result == 0) {
				message.setMessage("The account exists, but has been stopped. Please contact the manager!");
				message.setRedirectUrl("/NewsSystem/user/free/login.jsp");
			} else if (result == -1) {
				message.setMessage("Your account or password is incorrect. Please login again!");
				message.setRedirectUrl("/NewsSystem/user/free/login.jsp");
			} else {
				message.setMessage("Fail to login! Please login again!");
				message.setRedirectUrl("/NewsSystem/user/free/login.jsp");
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
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
			result = userService.changePassword(user, newPassword);
			if (result == 1) {
				message.setMessage("Changed successfully!");
			} else if (result == 0) {
				message.setMessage("Fail to change! Please contact the manager!");
			} else {
				message.setMessage("Fail to change! Please try again!");
			}
			message.setRedirectUrl("/NewsSystem/user/manage/showUserInformation.jsp");
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if ("showUserInformation".equals(condition)) { // 显示普通用户个人信息
			user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				UserInformation userInformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userInformation", userInformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/showUserInformation.jsp").forward(request, response);
		} else if ("changeUserInformation".equals(condition)) { // 修改普通用户个人信息的第一步：显示可修改信息
			user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				UserInformation userInformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userInformation", userInformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/changeUserInformation.jsp").forward(request,
					response);
		} else if ("changeInformation".equals(condition)) { // 修改普通用户个人信息的第二步：修改信息
			user = (User) request.getSession().getAttribute("user");
			result = userService.updateInformation(user, request);
			if (result >= 3) {
				message.setMessage("Changed successfully!");
			} else {
				message.setMessage("Fail to change! Please contact the manager!");
			}
			message.setRedirectUrl("/NewsSystem/servlet/UserServlet?condition=showUserInformation");
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if ("exit".equals(condition)) {
			request.getSession().removeAttribute("user");
			response.sendRedirect("/NewsSystem/index.jsp");
			// } else if ("findPassword".equals(condition)) {
			// // if ("email".equals(request.getParameter("type"))) {
			// Integer rand = Tool.getRandomInRangeInteger(10, 100000);//
			// 随机数作为验证修改密码用
			// result = userService.findPasswordByEmail(user, rand);
			// if (result == 1) {// 发送邮件成功
			// session.setAttribute("email", user.getEmail());
			// session.setAttribute("rand", rand);
			// session.setAttribute("time", new Date());
			// }
			// Tool.returnIntResult(response, result);
			// // } else if ("telephone".equals(request.getParameter("type"))) {
			//
			// // }
		} else if ("newPassword".equals(condition)) {
			String rand = (String) request.getParameter("rand");
			Integer trueRand = (Integer) session.getAttribute("rand");
			Date old = (Date) session.getAttribute("time");

			if (!rand.equals(trueRand.toString()))
				// rand值不对，无权限修改密码
				result = -3;
			else if (old == null || Tool.getSecondFromNow(old) > 300)
				// 修改密码超时
				result = -2;
			else
				result = userService.updatePassword(user);
			session.removeAttribute("email");// 删除session数据
			session.removeAttribute("rand");
			session.removeAttribute("time");
			Tool.returnIntResult(response, result);
		} else if (condition.equals("findPassword")) {// 找回密码
			if ("email".equals(request.getParameter("type"))) {
				user.setEmail(request.getParameter("wayInput"));
				Integer rand = Tool.getRandomInRangeInteger(10, 100000);// 随机数作为验证修改密码用
				result = userService.findPasswordByEmail(user, rand);
				if (result == 1) {// 发送邮件成功
					session.setAttribute("email", user.getEmail());
					session.setAttribute("rand", rand);
					session.setAttribute("time", new Date());
				}

				Tool.returnIntResult(response, result);
			} else if ("telephone".equals(request.getParameter("type"))) {

			}
		}
	}

}
