package job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import service.DatabaseService;
import tools.Tool;

public class DatabaseBackupJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		Tool.isMaintain = true;

		// 通过context参数可以获取JobDetail的JobDataMap，互相传递数据
		// JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		// String jobName = dataMap.getString("jobName");
		// System.out.println(jobName);

		DatabaseService databaseService = new DatabaseService();
		databaseService.backup();

		Tool.isMaintain = false;
	}

}
