package mil.ln.ncos.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.rpt.job.RptGenJob;


@RequiredArgsConstructor
@Configuration
@Slf4j
public class RptJobConfig {
	
	private final Scheduler scheduler;
	
	@Value("${cronFrmt}") 
	private String cronFrmt;

	@PostConstruct
	public void start() {
		JobDetail rptJobDetail = buildJobDetail(RptGenJob.class, new HashMap<>());
		try {
			this.scheduler.scheduleJob(rptJobDetail, buildJobTrigger(cronFrmt));
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
	}


	public Trigger buildJobTrigger(String scheduleExp) {
		return TriggerBuilder.newTrigger().withSchedule((ScheduleBuilder<?>) CronScheduleBuilder.cronSchedule(scheduleExp))
				.build();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JobDetail buildJobDetail(Class job, Map params) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(params);

		return JobBuilder.newJob(job).usingJobData(jobDataMap).build();
	}
    
}
