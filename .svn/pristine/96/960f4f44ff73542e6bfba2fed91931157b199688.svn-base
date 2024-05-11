package mil.ln.ncos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.func.service.FuncService;
import mil.ln.ncos.server.udp.service.UdpInboundService;


@RequiredArgsConstructor
@ConditionalOnProperty(
	    value="udpServer", 
	    havingValue = "true", 
	    matchIfMissing = false)
@Configuration
public class UdpServerConfig {
	
	@Value("${udp.server.port}") 
	private int port;
	
	private final DAO dao;
	
	private final FuncService funcService;

    @Bean
    UdpInboundService udpService() {
        return new UdpInboundService(dao,funcService);
    }

    @Bean
    IntegrationFlow processUdpMessage() {
    	return IntegrationFlows
    			.from(new UnicastReceivingChannelAdapter(port))
                .handle("udpService", "receive")
                .get();
    }
}
