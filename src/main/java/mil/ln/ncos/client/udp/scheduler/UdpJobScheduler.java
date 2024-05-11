package mil.ln.ncos.client.udp.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mil.ln.ncos.client.service.OutboudService;


@ConditionalOnProperty(
	    value="udpClient", 
	    havingValue = "true", 
	    matchIfMissing = false)
@Component
public class UdpJobScheduler {
	
	private OutboudService messageService;

    public UdpJobScheduler(OutboudService messageService) {
        this.messageService = messageService;
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void sendHeartBeatJob() throws Exception {
    	messageService.sendHeartBeat();
    }
    
    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void sendAssertInfo() throws Exception {
    	messageService.sendAssertInfo();
    }
    
    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void sendThreatInfo() throws Exception {
    	messageService.sendThreatInfo();
    }

}  
