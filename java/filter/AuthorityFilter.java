package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import bean.Authority;
import tools.AuthorityTool;
import tools.Message;

public class AuthorityFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		String key = AuthorityTool.getKey(req);
		Authority authority = AuthorityTool.authorityMap.get(key);

		if (authority == null) {// 无权限
			Message message = new Message();
			message.setMessage("Limited authority!");
			message.setRedirectTime(1000);
			request.setAttribute("message", message);
			request.getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
			return;
		} else
			chain.doFilter(request, response);// 有权限，可以继续访问
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}
}
