package mil.ln.ncos.rpt.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RptGenJob extends QuartzJobBean {

	public RptGenJob(RptGenerator rptGenerator) {
		this.rptGenerator = rptGenerator;
	}

	private final RptGenerator rptGenerator;

	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			this.rptGenerator.genReport();
		} catch (Exception e) {
			log.error("RptGenJob executeInternal error:{}", e);
		}
	}
}
