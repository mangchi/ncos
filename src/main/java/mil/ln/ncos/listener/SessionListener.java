package mil.ln.ncos.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.user.vo.UserVo;

@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener {

	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

	// 중복로그인 지우기
	public synchronized static String getSessionidCheck(String type, String compareId) {
		String result = "";
		for (String key : sessions.keySet()) {
			HttpSession hs = sessions.get(key);
			UserVo user = new UserVo();
			if (hs != null) {
				user = (UserVo) hs.getAttribute(type);
				if (user != null && user.getUserId().toString().equals(compareId)) {
					result = key.toString();
				}
			}
		}
		//removeSessionForDoubleLogin(result);
		return result;
	}
//	private static void removeSessionForDoubleLogin(String userId) {
//		log.debug("removeSessionForDoubleLogin :{} ",userId);
//		if (userId != null && userId.length() > 0) {
//			sessions.get(userId).invalidate();
//			sessions.remove(userId);
//		}
//	}

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
		log.debug("sessoionCreated::{}",hse);
		sessions.put(hse.getSession().getId(), hse.getSession());
	}

	@Override
    public void sessionDestroyed(HttpSessionEvent hse) {
		log.debug("sessionDestroyed::{}",hse);
        if(sessions.get(hse.getSession().getId()) != null){
            sessions.get(hse.getSession().getId()).invalidate();
            sessions.remove(hse.getSession().getId());	
        }
    }
}
