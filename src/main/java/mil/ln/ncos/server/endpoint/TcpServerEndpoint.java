package mil.ln.ncos.server.endpoint;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import mil.ln.ncos.server.tcp.service.MessageService;

@MessageEndpoint
public class TcpServerEndpoint {
	
	private MessageService messageService;

    public TcpServerEndpoint(MessageService messageService) {
        this.messageService = messageService;
    }

    @ServiceActivator(inputChannel = "inboundChannel")
    public byte[] process(byte[] message) {
        return messageService.processMessage(message);
    }

}
