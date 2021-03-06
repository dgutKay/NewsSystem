package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import tools.Tool;

public class CharEncodingFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		if (Tool.isMaintain == false) {
			// 传给下个filter处理
			filterChain.doFilter(request, response);
		} else {
			HttpSession session = ((HttpServletRequest) request).getSession();
			User user = (User) session.getAttribute("user");

			if (user != null && "manager".equals(user.getType())) {
				// 管理员仍然可以正常使用
				filterChain.doFilter(request, response);// 传给下个filter处理
			} else {
				// 其他用户只能访问maintain.html
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendRedirect("/NewsSystem/maintain.jsp");
			}
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
