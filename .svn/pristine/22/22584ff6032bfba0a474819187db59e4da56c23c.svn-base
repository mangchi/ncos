package mil.ln.ncos;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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
	
	
	
	@Value("${spring.profiles.active}") 
	private String activeProfile;
	
	
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
	
	
	/*
	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
		if(activeProfile.equals("land") || activeProfile.equals("lnsystemLand")) {
			servletContext.getSessionCookieConfig().setName("LAND_JSESSIONID");
		}
		else if(activeProfile.equals("navy") || activeProfile.equals("lnsystemNavy")  || activeProfile.equals("local")) {
			servletContext.getSessionCookieConfig().setName("NAVY_JSESSIONID");
		}
		else if(activeProfile.equals("hmmNavy") || activeProfile.equals("lnsystemHmmNavy")) {
			servletContext.getSessionCookieConfig().setName("HMM_NAVY_JSESSIONID");
		}
		else {
        	servletContext.getSessionCookieConfig().setName("HMM_LAND_JSESSIONID");
		}
        
    }
    
	
	@Bean(initMethod = "initNcos")
    InitNcosApp initNcosApp() {
        return new InitNcosApp();
    }
	
	class InitNcosApp {
		public void initNcos() {
			log.debug("Init Ncos Application....................................");		
			cacheService.getCodeCacheData(codeKey);

		}
	}
	
	@Bean(destroyMethod = "destoryNcos")
    ShutdownNcosApp destoryNcosApp() {
        return new ShutdownNcosApp();
    }
	
	class ShutdownNcosApp {
		public void destoryNcos() {
			log.debug("Shutdown Ncos Application...................................");
		}
	}
    */
}
