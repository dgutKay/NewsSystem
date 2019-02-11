package job;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

public class DatabaseBackup {
	static public Scheduler scheduler;
	static public JobDetail jobDetail;
	static public CronTrigger cronTrigger;

	public static void createScheduler() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
	}

	public static void scheduleJob() throws SchedulerException {

		JobKey jobKey = JobKey.jobKey("databaseBackupJob", "databaseJobGroup");
		boolean isExists = scheduler.checkExists(jobKey);
		if (isExists) {
			System.out.println("isExists==true");
		} else {
			System.out.println("isExists==flase");

			jobDetail = JobBuilder.newJob(DatabaseBackupJob.class).withIdentity("databaseBackupJob", "databaseJobGroup")
					.build();

			// 可以通过JobDataMap与job相互传递数据
			// JobDataMap jobDataMap=jobDetail.getJobDataMap();
			// jobDataMap.put("jobName", "备份数据库");

			// cronTrigger 创建
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 35 18 * * ? *");
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity("databaseBackupTrigger", "databaseTriggerGroup").withSchedule(cronScheduleBuilder)
					.build();

			// 监听特定的job
			DatabaseBackupJobListener databaseBackupJobListener = new DatabaseBackupJobListener();
			Matcher<JobKey> matcher = KeyMatcher.keyEquals(new JobKey("databaseBackupJob", "databaseJobGroup"));
			scheduler.getListenerManager().addJobListener(databaseBackupJobListener, matcher);

			// 全局注册
			// scheduler.getListenerManager().addJobListener(new
			// DatabaseBackupJobListener());
			// 监听特定组的job
			// GroupMatcher<JobKey> matcher2 =
			// GroupMatcher.jobGroupEquals("databaseJobGroup");
			// scheduler.getListenerManager().addJobListener(new
			// DatabaseBackupJobListener(), matcher2);

			// 执行，绑定
			scheduler.scheduleJob(jobDetail, cronTrigger);
		}
	}

	public static void start() throws SchedulerException {
		scheduler.start();
	}
}
