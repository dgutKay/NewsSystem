package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Databasebackup;
import dao.DatabaseDao;
import dao.DatabasebackupDao;
import service.DatabaseService;
import tools.Message;
import tools.Tool;

public class DatabaseServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String condition = request.getParameter("condition");
		DatabaseService databaseService = new DatabaseService();
		Message message = new Message();
		Gson gson = new Gson();

		if ("backup".equals(condition)) {
			Tool.isMaintain = true;
			message = databaseService.backup();
			Tool.isMaintain = false;

			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if ("getAll".equals(condition)) {
			List<Databasebackup> databasebackups = databaseService.getAll();
			request.setAttribute("databasebackups", databasebackups);
			getServletContext().getRequestDispatcher("/manager/databaseRestore.jsp").forward(request, response);
		} else if ("restore".equals(condition)) {
			Tool.isMaintain = true;
			Integer databasebackupId=Integer.parseInt(request.getParameter("databasebackupId"));
			message = databaseService.restore(databasebackupId);
			Tool.isMaintain = false;

			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		}
	}

}
