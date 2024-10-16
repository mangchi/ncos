package mil.ln.ncos.func.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.func.service.FuncService;

@Slf4j
@Component
public class FuncJobScheduler {
	
	private FuncService funcService;

    public FuncJobScheduler(FuncService funcService) {
        this.funcService = funcService;
    }

    @Scheduled(fixedDelay = 300000, initialDelay = 300000)
    public void sendHeartBeatJob() throws Exception {
    	log.debug("saveFuncOperationJob");
    	
    	funcService.saveFuncOperationJob();
    }
    

}  
