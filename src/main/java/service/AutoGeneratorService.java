package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.print.attribute.SetOfIntegerSyntax;
import javax.servlet.http.HttpServletRequest;

import bean.CommentUserView;
import bean.News;
import dao.CommentDao;
import dao.DatabaseDao;
import dao.NewsDao;
import freemarker.template.Configuration;
import freemarker.template.Template;
import tools.PageInformation;
import tools.Tool;
import tools.WebProperties;

public class AutoGeneratorService {
	public Integer generateNewsHtml(HttpServletRequest request) {
		// 创建一个合适的Configration对象
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
		configuration.setDefaultEncoding("UTF-8");// 这个一定要设置，不然在生成的页面中 会乱码
		configuration.setLocale(Locale.SIMPLIFIED_CHINESE);// 设置地区，影响时间，数字等格式
		// 设置模板所在的根文件夹
		configuration.setServletContextForTemplateLoading(request.getServletContext(), "/template/html");

		// 建立数据模型
		Map<String, Object> rootMap = new HashMap<String, Object>();
		DatabaseDao databaseDao = null;

		try {
			databaseDao = new DatabaseDao();

			// 一开始所有新闻均可以生成静态html
			NewsDao newsDao = new NewsDao();
			newsDao.resetStaticHtml(databaseDao);
			List<News> newsList = newsDao.getAll(databaseDao);

			// 评论分页信息
			CommentDao commentDao = new CommentDao();
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("commentUserView", request, pageInformation);
			pageInformation.setPage(1);
			pageInformation.setPageSize(2);
			pageInformation.setOrder("desc");
			pageInformation.setOrderField("time");

			// 遍历每个新闻
			for (News news : newsList) {
				pageInformation.setSearchSql(" (newsId=" + news.getNewsId() + ") ");
				List<CommentUserView> commentUserViews = commentDao.getOnePage(pageInformation, databaseDao);

				rootMap.clear();// 清空map中的数据
				rootMap.put("news", news);
				rootMap.put("pageInformation", pageInformation);
				rootMap.put("commentUserViews", commentUserViews);

				// 按时间创建文件夹保存静态网页
				String directory = request.getServletContext().getRealPath("/newsHtml");
				String year = String.valueOf(news.getNewsTime().getYear());
				String month = String.valueOf(news.getNewsTime().getMonth());
				directory += "\\" + year + "\\" + month;
				File nowDir = new File(directory);
				if (!nowDir.exists()) {
					nowDir.mkdirs();
				}
				// 文件最终路径命名
				String filePath = directory + "\\" + news.getNewsId().toString() + ".html";

				// 获取模板文件
				Template template = configuration.getTemplate("aNewsShowTemplate.ftl");
				BufferedWriter out = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
				// 根据模板文件和数据，生成html文件
				template.process(rootMap, out);

				// 设置URL
				if (news.getUrl().isEmpty()) {
					String url = WebProperties.config.getString("newsHtmlRoot") + "/" + year + "/" + month + "/"
							+ news.getNewsId().toString() + ".html";
					news.setUrl(url);
				}
			}

			// 开始事务处理
			databaseDao.setAutoCommit(false);
			newsDao.setStaticHtml(databaseDao);
			newsDao.batchUpdateUrl(newsList, databaseDao);
			databaseDao.commit();
			databaseDao.setAutoCommit(true);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				databaseDao.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return -1;
	}
}
