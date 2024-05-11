package mil.ln.ncos.client.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "outboundChannel")
public interface TcpGateway {
	
	 byte[] send(byte[] message);

}
