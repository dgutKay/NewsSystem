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
import org.quartz.SchedulerException;

import bean.Authority;
import bean.NewsType;
import tools.AuthorityTool;
import tools.EMailTool;
import tools.FileTool;
import tools.WebProperties;
import dao.DatabaseDao;
import job.DatabaseBackup;
import service.AuthorityService;
import service.NewsTypeService;

public class InitServlet extends HttpServlet {
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);

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

		// 初始化email参数
		EMailTool.domain = WebProperties.config.getString("domain");
		EMailTool.emailHost = WebProperties.config.getString("emailHost");
		EMailTool.emailUserEmail = WebProperties.config.getString("emailUserEmail");
		EMailTool.emailUserName = WebProperties.config.getString("emailUserName");
		EMailTool.emailPassword = WebProperties.config.getString("emailPassword");

		String d = this.getServletContext().getRealPath(WebProperties.config.getString("databaseBackupDir"));
		WebProperties.config.setProperty("databaseBackupDir", d);

		/*// quartz实现周期性备份数据库任务
		try {
			DatabaseBackup.createScheduler();
			DatabaseBackup.scheduleJob();
			DatabaseBackup.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}*/
	}

}
