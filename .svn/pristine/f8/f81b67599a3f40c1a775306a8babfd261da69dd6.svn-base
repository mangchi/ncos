package mil.ln.ncos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
            .ignoringAntMatchers("/sso/**", "/reportViewPopup", "/reportServer")  //csrf예외처리
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
		    .headers(headers -> headers.xssProtection())
		;

		return http.build();
	}

}