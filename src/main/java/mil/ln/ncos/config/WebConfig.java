package mil.ln.ncos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.interceptor.LoginInterceptor;

@RequiredArgsConstructor
@Component
public class WebConfig implements WebMvcConfigurer{
	
	@Value("${spring.profiles.active}") 
	private String activeProfile;
	
	
	@Value("${ssoMode}") 
	private String ssoMode;

	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*
		 * registry.addInterceptor(new LogInterceptor()) // LogInterceptor 등록 .order(1)
		 * // 적용할 필터 순서 설정 .addPathPatterns("/**") .excludePathPatterns("/error"); //
		 * 인터셉터에서 제외할 패턴
		 */
		
		if(activeProfile.startsWith("hmm")){
			registry.addInterceptor(new LoginInterceptor(activeProfile,ssoMode)) //LoginCheckInterceptor 등록
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/js/**","/css/**","/font/**","/img/**","/report/**","/sso/**","/sysInfo","/makeJsonStr","/login","/codes","/sessionChk");
		}
		else {
			
			registry.addInterceptor(new LoginInterceptor(activeProfile,ssoMode)) //LoginCheckInterceptor 등록
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/js/**","/css/**","/font/**","/img/**","/report/**","/sso/**","/sysInfo","/makeJsonStr","/login","/codes","/sessionChk");
			
		}
		
	}
	
	@Override 
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
	            .allowedOrigins("*") 
	            .allowedMethods("GET","POST"); 
	}

}
