package servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import bean.Authority;
import bean.NewsType;
import tools.AuthorityTool;
import tools.FileTool;
import tools.WebProperties;
import dao.DatabaseDao;
import service.AuthorityService;
import service.NewsTypeService;

public class InitServlet extends HttpServlet {
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		// 初始化数据库参数
		DatabaseDao.drv = this.getServletContext().getInitParameter("drv");
		DatabaseDao.url = this.getServletContext().getInitParameter("url");
		DatabaseDao.usr = this.getServletContext().getInitParameter("usr");
		DatabaseDao.pwd = this.getServletContext().getInitParameter("pwd");

		ServletContext servletContext = conf.getServletContext();
		FileTool.root = servletContext.getRealPath("\\");

		// 读取属性文件
		String fileDir = servletContext.getRealPath("\\WEB-INF\\web.properties");
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class).configure(params.properties().setFileName(fileDir));

		try {
			WebProperties.config = builder.getConfiguration();
			WebProperties.config.addProperty("projectRoot", 
					servletContext.getRealPath(WebProperties.config.getString("projectName")));
			
			
			Configuration config = builder.getConfiguration();
			WebProperties.propertiesMap.put("projectRoot", servletContext.getRealPath(config.getString("projectName")));
			WebProperties.propertiesMap.put("projectName", config.getString("projectName"));
			WebProperties.propertiesMap.put("tempDir", config.getString("tempDir"));
			WebProperties.propertiesMap.put("headIconFileDefault", config.getString("headIconFileDefault"));
			WebProperties.propertiesMap.put("headIconDirDefault", config.getString("headIconDirDefault"));
			WebProperties.propertiesMap.put("redirectTime", config.getString("redirectTime"));
			WebProperties.propertiesMap.put("ueditConfigJs", config.getString("ueditConfigJs"));
			WebProperties.propertiesMap.put("ueditJs", config.getString("ueditJs"));
			WebProperties.propertiesMap.put("ueditLang", config.getString("ueditLang"));

			// 加载新闻类型
			NewsTypeService newsTypeService = new NewsTypeService();
			List<NewsType> newsTypes = new ArrayList<NewsType>();
			newsTypes = newsTypeService.getAll();
			this.getServletContext().setAttribute("newsTypes", newsTypes);

			// 加载权限信息:利用哈希map存储权限，目的是方便查找权限
			List<Authority> authorities = new AuthorityService().getAll();
			for (Authority authority : authorities) {
				String key;
				if (authority.getParam() == null || authority.getParam().isEmpty())
					key = authority.getUrl() + authority.getUserType();
				else
					key = authority.getUrl() + authority.getParam() + authority.getUserType();
				AuthorityTool.authorityMap.put(key, authority);
			}

		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

}
