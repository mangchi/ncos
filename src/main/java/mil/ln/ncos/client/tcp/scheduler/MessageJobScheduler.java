package mil.ln.ncos.client.tcp.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.client.service.OutboudService;

@Slf4j
@ConditionalOnProperty(
	    value="tcpClient", 
	    havingValue = "true", 
	    matchIfMissing = false)
@Component
public class MessageJobScheduler {
	
	private OutboudService messageService;

    public MessageJobScheduler(OutboudService messageService) {
        this.messageService = messageService;
    }
    
    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void saveTransmitData() throws Exception {
    	
    	messageService.saveTransmitData();
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void sendHeartBeatJob() throws Exception {
    	log.debug("sendHeartBeatJob");
    	messageService.sendHeartBeat();
    }
    
    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void sendThreatInfo() throws Exception {
    	messageService.sendThreatInfo();
    }
    
    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void sendAssertInfo() throws Exception {
    	messageService.sendAssertInfo();
    }
   
}
