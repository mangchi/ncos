package mil.ln.ncos;


import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServletComponentScan
@EnableIntegration
@EnableScheduling
@RequiredArgsConstructor
@SpringBootApplication
public class NcosApplication extends SpringBootServletInitializer{ //implements ServletContextInitializer{
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String,Object> transStatus = new HashMap();

	public static void main(String[] args) {	
		log.debug("transStatus setting start..");
		transStatus.put("heartbeatStatus", "Y");
	    transStatus.put("threatStatus", "Y");
	    transStatus.put("assetStatus", "Y");
		transStatus.put("heartbeatDataId", "");
		transStatus.put("threatDataId", "");
		transStatus.put("assetDataId", "");
	    log.debug("transStatus setting finish..");
		SpringApplication.run(NcosApplication.class, args);
	
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(NcosApplication.class);
	}
	
}
