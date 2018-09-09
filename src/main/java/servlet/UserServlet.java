package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import bean.UserInformation;
import service.UserService;
import tools.Message;
import tools.PageInformation;
import tools.SearchTool;
import tools.Tool;

public class UserServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition = request.getParameter("condition");
		UserService userService = new UserService();
		Message message = new Message();

		if ("register".equals(condition)) {
			User user = new User();
			user.setType(request.getParameter("type"));
			user.setName(request.getParameter("name"));
			user.setPassword(request.getParameter("password"));
			if ("user".equals(user.getType()))
				user.setUsability("use");
			else
				user.setUsability("stop");

			int result = userService.register(user);
			if (result == 1) {
				message.setMessage("Registered successfully!");
				message.setRedirectUrl("/NewsSystem/user/free/login.jsp");
			} else if (result == 0) {
				message.setMessage("User already exist! Please register again!");
				message.setRedirectUrl("/NewsSystem/user/free/register.jsp");
			} else {
				message.setMessage("Fail to register! Please register again!");
				message.setRedirectUrl("/NewsSystem/user/free/register.jsp");
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if ("login".equals(condition)) {
			User user = new User();
			user.setName(request.getParameter("name"));
			user.setPassword(request.getParameter("password"));

			int result = userService.login(user);
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
			User user = (User) request.getSession().getAttribute("user");
			int result = userService.changePassword(user, newPassword);
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
			User user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				UserInformation userInformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userInformation", userInformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/showUserInformation.jsp").forward(request, response);
		} else if ("changeUserInformation".equals(condition)) { // 修改普通用户个人信息的第一步：显示可修改信息
			User user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				UserInformation userInformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userInformation", userInformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/changeUserInformation.jsp").forward(request,
					response);
		} else if ("changeInformation".equals(condition)) { // 修改普通用户个人信息的第二步：修改信息
			User user = (User) request.getSession().getAttribute("user");
			int result = userService.updateInformation(user, request);
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
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
