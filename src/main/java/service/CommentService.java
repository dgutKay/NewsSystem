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

import bean.Comment;
import bean.CommentUserView;
import bean.FirstTenCommentNumberAYear;
import dao.CommentDao;
import dao.DatabaseDao;
import tools.JacobExcelTool;
import tools.JacobWordManager;
import tools.PageInformation;
import tools.Tool;
import tools.WebProperties;

public class CommentService {

	public List<CommentUserView> getOnePage(PageInformation pageInformation) {
		List<CommentUserView> commentUserViews = new ArrayList<CommentUserView>();
		DatabaseDao databaseDao = null;
		try {
			databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			commentUserViews = commentDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (databaseDao.close() < 0)
				commentUserViews = null;
		}
		return commentUserViews;
	}

	public Integer praise(String commentId) {
		DatabaseDao databaseDao = null;
		try {
			databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			if (commentDao.praise(commentId, databaseDao) > 0)
				return 1;
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		} finally {
			if (databaseDao.close() < 0)
				return -4;
		}
	}

	public Integer addComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		DatabaseDao databaseDao = null;
		try {
			databaseDao = new DatabaseDao();
			comment.setStair(commentDao.getStairByNewsId(comment.getNewsId(), databaseDao) + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		} finally {
			if (databaseDao.close() < 0)
				return -4;
		}
	}

	public Integer addCommentToComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		DatabaseDao databaseDao = null;
		try {
			databaseDao = new DatabaseDao();
			CommentUserView oldCommentUserView = commentDao.getByIdFromView(comment.getCommentId(), databaseDao);
			String content = oldCommentUserView.getContent();
			if (content.contains("<br><br>")) {// 消除之前的留言
				content = content.substring(content.indexOf("<br><br>") + 8);
			}
			content = "reply to No " + oldCommentUserView.getStair() + " floor: " + oldCommentUserView.getUserName()
					+ ":<br>" + content + "<br><br>";
			comment.setContent(content + comment.getContent());
			comment.setStair(commentDao.getStairByNewsId(comment.getNewsId(), databaseDao) + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		} finally {
			if (databaseDao.close() < 0)
				return -4;
		}
	}

	public Integer getPraise(String commentId) {
		DatabaseDao databaseDao = null;
		try {
			databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			return commentDao.getPraise(commentId, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		} finally {
			if (databaseDao.close() < 0)
				return -4;
		}
	}

	public Comment getComment() {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			return commentDao.getComment(databaseDao);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String firstTenCommentNumberAYearEveryYear(HttpServletRequest request) {
		CommentDao commentDao = new CommentDao();
		List<FirstTenCommentNumberAYear> firstTenCommentNumberAYearEveryYearList = null;

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			firstTenCommentNumberAYearEveryYearList = commentDao.firstTenCommentNumberAYearEveryYear(databaseDao);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-1";
		}

		Workbook workbook = null;
		FileInputStream fileInputStream = null;
		String wordFile = "-";

		// 打开word
		ComThread.InitMTA(true);
		JacobWordManager jacobWordManager = null;

		try {
			int first = 1;

			for (FirstTenCommentNumberAYear firstTenCommentNumberAYear : firstTenCommentNumberAYearEveryYearList) {
				String fullPath = request.getServletContext()
						.getRealPath(WebProperties.config.getString("excelTemplate"));
				String excelFileFullPath = fullPath + "\\firstTenCommentNumberAYear.xlsm";

				fileInputStream = new FileInputStream(excelFileFullPath);
				workbook = new XSSFWorkbook(fileInputStream);
				fileInputStream.close();

				Sheet sheet = workbook.getSheetAt(0);
				Row row;
				// 修改年度
				row = sheet.getRow(1);
				row.getCell(1).setCellValue(firstTenCommentNumberAYear.getYear() + "年前十名评论数");
				for (int i = 2; i <= 11; i++) {
					row = sheet.getRow(i);
					row.getCell(0).setCellValue(
							firstTenCommentNumberAYear.getFirstTenCommentNumberList().get(i - 2).getUserName());
					row.getCell(1).setCellValue(
							firstTenCommentNumberAYear.getFirstTenCommentNumberList().get(i - 2).getCommentNumber());
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
				tool.callMacro("firstTenCommentNumber");
				// 关闭并保存，释放对象
				tool.CloseExcel(true, true);

				jacobWordManager = new JacobWordManager(false);

				fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("wordTemplate"));
				String copySentence = fullPath + "\\firstTenCommentNumberCopySentence.docx";
				String oneYear = fullPath + "\\oneYear.docm";

				// 先粘贴图表，再在图表前粘贴句子 // 因为先前处理Excel已复制了图表，所以应先粘贴图表
				jacobWordManager.openDocument(oneYear);
				jacobWordManager.callMacro("deleteAll");
				jacobWordManager.callMacro("pasteChart");
				jacobWordManager.copyContentFromAnotherDocInsertBefore(copySentence);
				jacobWordManager.goToBegin();
				jacobWordManager.replaceAllText("#year", firstTenCommentNumberAYear.getYear().toString());
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#total", firstTenCommentNumberAYear.getTotalCommentNumber().toString());
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#averageByMonth",
						Tool.formatDouble(firstTenCommentNumberAYear.getTotalCommentNumber() * 1.0 / 12).toString());
				jacobWordManager.callMacro("println");
				jacobWordManager.closeDocumentWithSave();

				String wordFileFullPath = fullPath + "\\firstTenCommentNumberInAYearEveryYearAll.docm";
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
					+ "\\firstTenCommentNumberInAYearEveryYearAll.docm";
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
