package service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.Databasebackup;
import dao.DatabaseDao;
import dao.DatabasebackupDao;
import tools.Message;
import tools.WebProperties;

public class DatabaseService {
	public Message backup() {
		Message message = new Message();

		DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		String backupFileName = dateFormat.format(new Date()) + ".sql";
		String backupFullPath = WebProperties.config.getString("databaseBackupDir") + "\\" + backupFileName;

		String mySqlCommand = "mysqldump -u" + WebProperties.config.getString("databaseUser") + " -p"
				+ WebProperties.config.getString("databasePassword") + " "
				+ WebProperties.config.getString("databaseName") + " > \"" + backupFullPath + "\"";
		StringBuffer dosCommand = new StringBuffer();
		dosCommand.append("cmd.exe /c \"").append(mySqlCommand).append("\"");
		System.out.println(dosCommand);// 打印执行的命令

		Process ls_Process;// java的进程类
		BufferedReader in = null;
		DatabaseDao databaseDao = null;
		try {
			// 执行进程
			ls_Process = Runtime.getRuntime().exec(dosCommand.toString());
			in = new BufferedReader(new InputStreamReader(ls_Process.getErrorStream()));

			StringBuffer result = new StringBuffer("");// 记录dos命令在控制台的输出结果
			String aLine = "";
			while ((aLine = in.readLine()) != null) {
				result.append(aLine).append("<br>");
			}

			in.close();
			ls_Process.destroy();
			message.setMessage(result.toString());
			System.out.println(result.toString());

			File backupFile = new File(backupFullPath);
			// 生成的备份文件大小至少1k，防止生成0k大小的文件
			if (backupFile.length() > 1024) {
				Databasebackup databasebackup = new Databasebackup();
				databasebackup.setName(backupFileName);
				databasebackup.setDirectory(backupFullPath);
				// 保存数据库备份信息存到数据库
				databaseDao = new DatabaseDao();
				DatabasebackupDao databasebackupDao = new DatabasebackupDao();
				if (databasebackupDao.add(databasebackup, databaseDao) > 0) {
					message.setMessage(message.getMessage() + "<br>备份信息已添加至数据库。");
				} else {
					message.setMessage(message.getMessage() + "<br>备份信息添加至数据库失败！");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			message.setMessage(message.getMessage() + "<br>IOException！");
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage(message.getMessage() + "<br>Exception！");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				databaseDao.close();
			} catch (IOException e) {
				e.printStackTrace();
				message.setMessage(message.getMessage() + "<br>IOException2！");
			}
		}

		return message;
	}

	public List<Databasebackup> getAll() {
		DatabaseDao databaseDao = null;
		DatabasebackupDao databasebackupDao = new DatabasebackupDao();
		try {
			databaseDao = new DatabaseDao();
			return databasebackupDao.getAll(databaseDao);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			databaseDao.close();
		}

		return null;
	}

	public Message restore(Integer databasebackupId) {
		Message message = new Message();
		DatabaseDao databaseDao = null;
		DatabasebackupDao databasebackupDao = new DatabasebackupDao();
		BufferedReader in = null;

		try {
			databaseDao = new DatabaseDao();
			Databasebackup databasebackup = databasebackupDao.getById(databaseDao, databasebackupId);
			if (databasebackup != null) {
				File backupfile = new File(databasebackup.getDirectory());
				if (backupfile.exists()) {
					String mysqlCommand = "mysql -u" + WebProperties.config.getString("databaseUser") + " -p"
							+ WebProperties.config.getString("databasePassword") + " "
							+ WebProperties.config.getString("databaseName") + " < \"" + databasebackup.getDirectory()
							+ "\"";
					StringBuffer dosCommand = new StringBuffer();
					dosCommand.append("cmd.exe /c \"").append(mysqlCommand).append("\"");
					System.out.println(dosCommand);

					Process ls_Process;// java的进程类
					// 执行进程
					ls_Process = Runtime.getRuntime().exec(dosCommand.toString());
					in = new BufferedReader(new InputStreamReader(ls_Process.getErrorStream()));

					StringBuffer result = new StringBuffer("");// 记录dos命令在控制台的输出结果
					String aLine = "";
					while ((aLine = in.readLine()) != null) {
						result.append(aLine).append("<br>");
					}

					in.close();
					ls_Process.destroy();
					message.setMessage(result.toString());
					System.out.println(result.toString());
					message.setMessage(message.getMessage() + "<br>已成功还原数据库。");
				} else {
					message.setMessage("备份文件在目录中不存在！");
				}
			} else {
				message.setMessage("备份文件在数据库中不存在！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			message.setMessage(message.getMessage() + "<br>SQLException！");
		} catch (IOException e) {
			e.printStackTrace();
			message.setMessage(message.getMessage() + "<br>IOException！");
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage(message.getMessage() + "<br>Exception！");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				databaseDao.close();
			} catch (IOException e) {
				e.printStackTrace();
				message.setMessage(message.getMessage() + "<br>IOException2！");
			}
		}

		return message;
	}
}
