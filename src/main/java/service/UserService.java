package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bean.*;
import dao.*;
import tools.EMailTool;
import tools.Encryption;
import tools.FileTool;
import tools.PageInformation;
import tools.Tool;
import tools.WebProperties;

public class UserService {
	public Integer register(User user) {
		int result = 0;
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			// 有同名用户
			if (databaseDao.hasStringValue("user", "name", user.getName()) == 1)
				result = -1;
			// email已被注册过
			if (databaseDao.hasStringValue("user", "email", user.getEmail()) == 1) {
				result += -10;
			}
			// 有同名用户或email被注册过
			if (result < 0)
				return result;

			// 根据密码生成盐和加密密码
			Encryption.encryptPasswd(user);

			if (userDao.register(user, databaseDao) != 0)
				return 1;// 成功创建用户
			else
				return 0;// 数据库操作失败
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	public Integer login(User user) {
		int result = -3; // 出现异常，数据库操作失败

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			return userDao.login(user, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<User> getOnePage(PageInformation pageInformation) {
		List<User> users = new ArrayList<User>();

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			users = userDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return users;
	}

	public List<User> check(PageInformation pageInformation, String id) {
		List<User> users = null;

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();

			if (id != null && !id.isEmpty()) {
				userDao.changeUsability(id, databaseDao);
			}
			users = userDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return users;
	}

	public List<User> delete(PageInformation pageInformation) {
		List<User> users = null;
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			userDao.delete(pageInformation.getIds(), databaseDao);
			pageInformation.setIds(null);
			users = userDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	public Integer changePassword(User user, String newPassword) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			if (userDao.login(user, databaseDao) == 1) {
				user.setPassword(newPassword);
				// 根据密码生成盐和加密密码
				Encryption.encryptPasswd(user);

				if (userDao.updatePassword(user, databaseDao) > 0)
					return 1; // 修改成功
				else
					return 0; // 用户存在，但修改失败
			} else
				return -1; // 用户不存在或旧密码错误
		} catch (SQLException e) {
			e.printStackTrace();
			return -2; // 数据库问题
		} catch (Exception e) {
			e.printStackTrace();
			return -3; // 其它异常
		}
	}

	public UserInformation getByUserId(Integer userId) {
		UserInformation userInformation = null;

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserInformationDao userInformationDao = new UserInformationDao();
			userInformation = userInformationDao.getByUserId(userId, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userInformation;
	}

	public Integer updateInformation(User user, HttpServletRequest request) {
		Integer result;
		UserInformation userInformation = new UserInformation();
		String oldHeadIconUrl = user.getHeadIconUrl();

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Configure a repository (to ensure a secure temp location is used)
		String fullPath = request.getServletContext().getRealPath(WebProperties.propertiesMap.get("tempDir"));// 获取相对路径的绝对路径
		File repository = new File(fullPath);
		factory.setRepository(repository);// 设置临时文件存放的文件夹
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 解析request，将其中各表单元素和上传文件提取出来

		try {
			List<FileItem> items = upload.parseRequest(request); // items存放各表单元素
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (item.isFormField()) { // 非上传文件表单元素
					if ("sex".equals(item.getFieldName())) // 获取表单元素的name属性
						userInformation.setSex(item.getString("UTF-8"));// 获取表单元素的值（一般是value属性）
					if ("hobby".equals(item.getFieldName()))
						userInformation.setHobby(item.getString("UTF-8"));
				} else { // 上传文件
					if (item.getName() != null && !item.getName().isEmpty()) {
						File uploadedFile;
						String randomFileName;
						do {
							randomFileName = FileTool.getRandomFileNameByCurrentTime(item.getName());
							fullPath = request.getServletContext().getRealPath(
									WebProperties.propertiesMap.get("headIconDirDefault")) + "\\" + randomFileName;
							uploadedFile = new File(fullPath);
						} while (uploadedFile.exists()); // 确保文件未存在

						item.write(uploadedFile); // 将临时文件转存为新文件保存
						result = 1; // 表示上传文件成功
						item.delete(); // 删除临时文件
						result = 2; // 表示上传文件成功，且临时文件删除
						user.setHeadIconUrl("\\" + WebProperties.propertiesMap.get("projectName")
								+ WebProperties.propertiesMap.get("headIconDirDefault") + "\\" + randomFileName);
					}
				}
			}

			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			UserInformationDao userInformationDao = new UserInformationDao();

			// 开始事务处理
			databaseDao.setAutoCommit(false);
			userDao.updateHeadIcon(user, databaseDao); // 更新用户表的头像
			if ("user".equals(user.getType())) {
				userInformation.setUserId(user.getUserId());
				if (userInformationDao.hasUserId(user.getUserId(), databaseDao))
					userInformationDao.update(userInformation, databaseDao);
				else
					userInformationDao.insert(userInformation, databaseDao);
			}
			databaseDao.commit();
			databaseDao.setAutoCommit(true);
			result = 3; // 表示上传文件成功，临时文件删除，且路径保存到数据库成功

			if (oldHeadIconUrl.contains(FileTool.getFileName(WebProperties.propertiesMap.get("headIconFileDefault"))))
				result = 5; // 表示上传文件成功，临时文件删除，且路径保存到数据库成功，老的图片是系统默认图片，不需要删除
			else { // 老的图片不是系统默认图片，需要删除
				if (FileTool.deleteFile(
						new File(FileTool.root.replace("\\" + WebProperties.propertiesMap.get("projectName"), "")
								+ oldHeadIconUrl)))
					result = 5; // 表示上传文件成功，临时文件删除，且路径保存到数据库成功，老的图片被删除
				else
					result = 4; // 表示上传文件成功，临时文件删除，且路径保存到数据库成功，老的图片无法被删除
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		return result;
	}

	// 返回值：1成功发送邮件，-1发送邮件失败，-2邮箱未注册过
	public Integer findPasswordByEmail(User user, Integer rand) {
		Integer result = 0;

		try {
			DatabaseDao databaseDao = new DatabaseDao();
			if (databaseDao.hasStringValue("user", "email", user.getEmail()) == 1)
				// 该email存在
				result = EMailTool.sendReturnPassword(user.getEmail(), rand);
			else
				result = -2;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public Integer updatePassword(User user) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();

			// 根据密码生成盐和加密密码
			Encryption.encryptPasswd(user);

			if (userDao.updatePassword(user, databaseDao) > 0)// 修改密码成功
				return 1;
			else// 修改密码失败！
				return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public String batchAdd(HttpServletRequest request) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String fullPath = request.getServletContext().getRealPath(WebProperties.propertiesMap.get("tempDir"));
		File repository = new File(fullPath);
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (!item.isFormField()) { // 上传文件
					String excelFileFullPath = fullPath + "\\" + item.getName();
					item.write(new File(excelFileFullPath));

					// excel文件读写
					FileInputStream fileInputStream = new FileInputStream(excelFileFullPath);
					String extend = FileTool.getExtendedFileName(item.getName());

					Workbook workbook = null;
					if (extend.equalsIgnoreCase("xls"))
						workbook = new HSSFWorkbook(fileInputStream);
					else if (extend.equalsIgnoreCase("xlsx"))
						workbook = new XSSFWorkbook(fileInputStream);
					else {// 文件后缀名不正确
						fileInputStream.close();
						return "-1";
					}

					fileInputStream.close();

					Sheet sheet = workbook.getSheetAt(0);
					Row row;

					// 遍历excel行,插入数据库
					List<User> users = new ArrayList<User>();
					for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
						row = sheet.getRow(i);
						String name = row.getCell(0).getRichStringCellValue().getString().trim();

						if (name != null && !name.isEmpty()) {
							User user = new User();
							user.setName(name);
							user.setUsability("use");
							user.setPassword(Tool.getRandomPassword());

							Cell cell = row.createCell(1);
							cell.setCellValue(user.getPassword());

							Encryption.encryptPasswd(user);
							users.add(user);
						}
					}

					DatabaseDao databaseDao = new DatabaseDao();
					UserDao userDao = new UserDao();
					if (userDao.batchAdd(users, databaseDao) != 1) {
						workbook.close();
						return "-2";
					}

					FileOutputStream fileOutputStream = new FileOutputStream(excelFileFullPath);
					workbook.write(fileOutputStream);

					fileOutputStream.flush();
					fileOutputStream.close();
					workbook.close();

					String excelFile = "\\" + WebProperties.propertiesMap.get("projectName")
							+ WebProperties.propertiesMap.get("tempDir") + "\\" + item.getName();
					excelFile = excelFile.replace("\\", "/");
					return excelFile;
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-3";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-4";
		}

		return "-5";
	}

}
