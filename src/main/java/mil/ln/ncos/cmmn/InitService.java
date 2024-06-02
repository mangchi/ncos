package mil.ln.ncos.cmmn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.service.CacheService;

@RequiredArgsConstructor
@Component
public class InitService implements ApplicationRunner {
	
	private final CacheService cacheService;
	
	@Value("${code.codeKey}")
    private String codeKey;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		cacheService.getCodeCacheData(codeKey);
		
	}

}
