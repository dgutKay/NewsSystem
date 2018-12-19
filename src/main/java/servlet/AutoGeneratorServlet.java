package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import service.AutoGeneratorService;
import tools.Message;
import tools.Tool;

public class AutoGeneratorServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition = request.getParameter("condition");
		Message message = new Message();

		if ("newsHtml".equals(condition)) {
			AutoGeneratorService autoGeneratorService = new AutoGeneratorService();
			message.setResult(autoGeneratorService.generateNewsHtml(request));

			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		}
	}

}
