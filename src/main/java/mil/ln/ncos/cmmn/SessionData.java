package mil.ln.ncos.cmmn;


import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import mil.ln.ncos.user.vo.UserVo;

@Component
public class SessionData {
	
	//private static UserVo userVo = null;
	

	public static UserVo getUserVo() {
		 ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		 HttpSession session = servletRequestAttribute.getRequest().getSession();
		 return( (UserVo)session.getAttribute("user"));

	}

	/*
	public static void setUserVo(UserVo vo) {
	    userVo = vo;
	}
	*/
	



}
