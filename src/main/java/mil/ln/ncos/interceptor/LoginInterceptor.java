package mil.ln.ncos.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

	private String activeProfile;

	private String ssoMode;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestURI = request.getRequestURI();
		log.debug("[interceptor] : " + requestURI);
		String refer = request.getHeader("Referer");
		log.debug("refer:{}", refer);
		HttpSession session = request.getSession();

		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
			log.debug("user:{}", session.getAttribute("user"));
			if (session == null || session.getAttribute("user") == null) {
				if (ssoMode.equals("Y")) {
					response.sendRedirect("/sso/business");
					return false;
				} else {
					log.debug("[미인증 사용자 요청]");
					// 로그인으로 redirect
					response.sendRedirect("/login?redirectURL=" + requestURI);
					return false;
				}
			}

		} else {
			if (session == null || session.getAttribute("user") == null) {
				// 로그인 되지 않음

				if (null == refer) {
					response.sendRedirect("/login");
					return false;
				}
				if (refer.indexOf("login") > -1) {
					return true;
				} else if (refer.indexOf("logout") > -1) {
					return true;
				} else {
					log.debug("[미인증 사용자 요청]");
					// 로그인으로 redirect
					response.sendRedirect("/login?redirectURL=" + requestURI);
					return false;
				}

			}
		}

		// 로그인 되어있을 떄
		return true;
	}
}
