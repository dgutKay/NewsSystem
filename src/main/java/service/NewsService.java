package service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jacob.com.ComThread;

import bean.ArticleNumberByMonthInAYear;
import bean.News;
import dao.DatabaseDao;
import dao.NewsDao;
import tools.JacobExcelTool;
import tools.JacobWordManager;
import tools.PageInformation;
import tools.Tool;
import tools.WebProperties;

public class NewsService {

	public Integer add(News news) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			return newsDao.add(news, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	public List<News> getOnePage(PageInformation pageInformation) {
		List<News> newses = new ArrayList<News>();

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			newses = newsDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newses;
	}

	public News getNewsById(Integer newsId) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			return newsDao.getById(newsId, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<News> delete(PageInformation pageInformation) {
		List<News> newses = new ArrayList<News>();

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			newsDao.delete(pageInformation.getTableName(), pageInformation.getIds(), databaseDao);
			pageInformation.setIds(null);
			newses = newsDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newses;
	}

	public Integer edit(News news) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			return newsDao.edit(news, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	public List<List<News>> getByTypesTopN(String[] newsTypes, Integer n, List<List<String>> newsCaptionsList) {
		List<List<News>> newsesList = new ArrayList<List<News>>();

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();

			for (String type : newsTypes) {
				List<News> newses = newsDao.getByTypesTopN(type, n, databaseDao);
				List<String> newsCaptions = new ArrayList<String>();
				for (News news : newses) {
					String newsCaption = Tool.getStringByMaxLength(news.getCaption(),
							Integer.parseInt(WebProperties.config.getString("homePageNewsCaptionMaxLength")));
					newsCaptions.add(newsCaption);
				}
				newsesList.add(newses);
				newsCaptionsList.add(newsCaptions);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return newsesList;
	}

	// 获取首页各类新闻的函数
	public List<List<News>> getByTypesTopN2(String[] newsTypes, Integer n) {
		// newsesList为数组元素为新闻数组的数组，newsesList的每个数组元素为一个数组（存放一类新闻），
		List<List<News>> newsesList = new ArrayList<List<News>>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			for (String type : newsTypes) {// 遍历newsTypes
				List<News> newses = newsDao.getByTypesTopN(type, n, databaseDao); // 获取相对应的类别的新闻
				newsesList.add(newses);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newsesList;
	}

	public String articleNumberByMonthInAYear(HttpServletRequest request) {
		List<Integer> articleNumberByMonthList = new ArrayList<Integer>();

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			articleNumberByMonthList = newsDao.articleNumberByMonthInAYear(request.getParameter("year"), databaseDao);
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

		Workbook workbook = null;
		FileInputStream fileInputStream = null;
		String excelFileFullPath = "";

		try {
			String fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("excelTemplate"));
			excelFileFullPath = fullPath + "\\articleNumberByMonthInAYear.xlsm";

			fileInputStream = new FileInputStream(excelFileFullPath);
			workbook = new XSSFWorkbook(fileInputStream);
			fileInputStream.close();

			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			for (int i = 2; i <= 13; i++) {
				row = sheet.getRow(i);
				row.getCell(1).setCellValue(articleNumberByMonthList.get(i - 2));
			}

			FileOutputStream fileOutputStream = new FileOutputStream(excelFileFullPath);
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-2";
		}

		try {
			ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住
			JacobExcelTool tool = new JacobExcelTool();
			// 打开
			tool.OpenExcel(excelFileFullPath, false, false);
			// 调用Excel宏
			tool.callMacro("articleNumberByMonthInAYear");
			// 关闭并保存，释放对象
			tool.CloseExcel(true, true);

			String excelFile = "\\" + WebProperties.config.getString("projectName")
					+ WebProperties.config.getString("excelTemplate") + "\\articleNumberByMonthInAYear.xlsm";
			return excelFile.replace("\\", "/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ComThread.Release();// 结束
		}

		return "-3";
	}

	public String articleNumberByMonthInAYearEveryYear(HttpServletRequest request) {
		List<ArticleNumberByMonthInAYear> articleNumberByMonthInAYearEveryYearList = new ArrayList<ArticleNumberByMonthInAYear>();

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			articleNumberByMonthInAYearEveryYearList = newsDao
					.articleNumberByMonthInAYearEveryYear(request.getParameter("year"), databaseDao);
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

		Workbook workbook = null;
		FileInputStream fileInputStream = null;
		String wordFile = "";

		// 打开word
		ComThread.InitMTA(true);
		JacobWordManager jacobWordManager = null;

		try {
			int first = 1;

			for (ArticleNumberByMonthInAYear ArticleNumberByMonthInAYear : articleNumberByMonthInAYearEveryYearList) {
				String fullPath = request.getServletContext()
						.getRealPath(WebProperties.config.getString("excelTemplate"));
				String excelFileFullPath = fullPath + "\\articleNumberByMonthInAYearEveryYear.xlsm";

				fileInputStream = new FileInputStream(excelFileFullPath);
				workbook = new XSSFWorkbook(fileInputStream);
				fileInputStream.close();

				Sheet sheet = workbook.getSheetAt(0);
				Row row;
				// 修改年度
				row = sheet.getRow(1);
				row.getCell(1).setCellValue(ArticleNumberByMonthInAYear.getYear() + "年各月份发表的文章数");
				for (int i = 2; i <= 13; i++) {
					row = sheet.getRow(i);
					row.getCell(1).setCellValue(ArticleNumberByMonthInAYear.getArticleNumberByMonthList().get(i - 2));
				}

				FileOutputStream fileOutputStream = new FileOutputStream(excelFileFullPath);
				workbook.write(fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				workbook.close();

				JacobExcelTool tool = new JacobExcelTool();
				// 打开
				tool.OpenExcel(excelFileFullPath, false, false);
				// 调用Excel宏
				tool.callMacro("articleNumberByMonthInAYearEveryYear");
				// 关闭并保存，释放对象
				tool.CloseExcel(true, true);

				jacobWordManager = new JacobWordManager(false);

				fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("wordTemplate"));
				String copySentence = fullPath + "\\copySentence.docm";
				String oneYear = fullPath + "\\oneYear.docm";

				// 先粘贴图表，再在图表前粘贴句子 // 因为先前处理Excel已复制了图表，所以应先粘贴图表
				jacobWordManager.openDocument(oneYear);
				jacobWordManager.callMacro("deleteAll");
				jacobWordManager.callMacro("pasteChart");
				jacobWordManager.copyContentFromAnotherDocInsertBefore(copySentence);
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#year", ArticleNumberByMonthInAYear.getYear().toString());
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#total", ArticleNumberByMonthInAYear.getTotalNewsNumber().toString());
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#averageByMonth",
						Tool.formatDouble(ArticleNumberByMonthInAYear.getTotalNewsNumber() * 1.0 / 12).toString());
				jacobWordManager.callMacro("println");
				jacobWordManager.closeDocumentWithSave();

				String wordFileFullPath = fullPath + "\\articleNumberByMonthInAYearEveryYearAll.docm";
				jacobWordManager.openDocument(wordFileFullPath);

				if (first == 1) {
					jacobWordManager.callMacro("deleteAll");
					first++;
				}

				jacobWordManager.goToEnd();
				jacobWordManager.copyContentFromAnotherDocInsertAfter(oneYear);
				jacobWordManager.closeDocumentWithSave();
				jacobWordManager.close();
			}

			wordFile = "\\" + WebProperties.config.getString("projectName")
					+ WebProperties.config.getString("wordTemplate")
					+ "\\articleNumberByMonthInAYearEveryYearAll.docm";
			wordFile.replace("\\", "/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-2";
		} finally {
			jacobWordManager.close();// 关闭word程序
			ComThread.Release();
		}

		return wordFile;
	}
}
