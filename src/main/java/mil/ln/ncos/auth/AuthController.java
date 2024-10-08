package mil.ln.ncos.auth;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.auth.service.AuthService;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.error.ErrorCode;
import mil.ln.ncos.cmmn.util.CryptoUtil;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.vo.LogVo;
import mil.ln.ncos.exception.BizException;
import mil.ln.ncos.log.service.LogService;
import mil.ln.ncos.user.vo.UserVo;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${ssoMode}")
	private String ssoMode;

	@Value("${cryptoMode}")
	private String cryptoMode;

	@Value("${crypto.key}")
	private String cryptoModeKey;

	private final MessageSourceAccessor messageSource;
	private final LogService logService;
	private final AuthService authService;

	@GetMapping("/login")
	public ModelAndView login(Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {

		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1 && ssoMode.equals("Y")) {
			res.sendRedirect("/sso/business");
		}

		ModelAndView mv = new ModelAndView("th/auth/login");
		UserVo vo = new UserVo();
		mv.addObject("user", vo);
		return mv;
	}

	@PostMapping("/login")
	public void login(HttpServletRequest req, HttpServletResponse res) throws Exception {
		final FlashMap flashMap = new FlashMap();
		LogVo logVo = new LogVo();
		JsonObject json = new JsonObject();
		final FlashMapManager flashMapManager = new SessionFlashMapManager();
		UserVo param = new UserVo();
		try {
			logVo.setWorkCodeId("1");
			logVo.setWorkUiId("7");
			Field[] fields = UserVo.class.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (null != req.getParameter(f.getName())) {
					f.set(param, req.getParameter(f.getName()));
				}

			}
			String loginId = param.getLoginUserId();
			// log.debug("enc 전 id:{}",loginId);
			if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1 && cryptoMode.equals("Y")) {
				// log.debug("ENCODE start.........");
				loginId = ScpDbUtil.scpEnc(loginId, cryptoModeKey);

				// log.debug("ENCODE finish.........");
				// log.debug("enc후 id:{}",loginId);
			}
			/*
			 * String testId = "azv6Rn4ISzGmmA==";
			 * log.debug("dec전 id:{}",testId);
			 * log.debug("dec후 id:{}",ScpDbUtil.scpDec(testId));
			 * log.debug("loginId:{}",loginId);
			 */
			param.setUserId(loginId);
			param.setPassword(CryptoUtil.encode(param.getCurPassword()));
			log.debug("param:{}", param);
			/*
			 * if (errors.hasErrors()) { // 유효성 통과 못한 필드와 메시지를 핸들링 Map<String,String> errMsg
			 * = new HashMap(); this.validateHandling(errMsg,errors); Set<String> keys =
			 * errMsg.keySet(); Iterator iter = keys.iterator(); while(iter.hasNext()) {
			 * String key = (String)iter.next(); json.addProperty(key, errMsg.get(key)); }
			 * flashMap.put("errMsg", json.toString());
			 * flashMapManager.saveOutputFlashMap(flashMap, req, res);
			 * req.getRequestDispatcher("/login").forward(req, res);
			 * 
			 * }
			 */
			UserVo user = authService.getUserAccount(param, req);
			logVo.setAccountId(user.getAccountId());
			log.debug("user:{}", user);
			json.addProperty("accountId", user.getAccountId());
			json.addProperty("userId", user.getUserId());
			json.addProperty("username", user.getUsername());
			json.addProperty("auth", user.getAuthorization());
			json.addProperty("alarmStatus", user.getAlarmStatus());
			json.addProperty("alarmLevel", user.getAlarmLevel());
			json.addProperty("warningStatus", user.getWarningStatus());
			json.addProperty("warningLevel", user.getWarningLevel());
			if (user.getSessionControlStatus() == 0) {
				json.addProperty("sessionTm", -1);
			} else {
				json.addProperty("sessionTm", 60 * 60);
			}
			if (null != user.getLastSuccessAccessDate()) {
				json.addProperty("connectTime", DateUtil.formatDate(user.getLastSuccessAccessDate(), "HH:mm:ss"));
			} else {
				json.addProperty("connectTime", DateUtil.getFrmtDate(null, "HH:mm:ss"));
			}

			flashMap.put("sessionData", json.toString());

			flashMapManager.saveOutputFlashMap(flashMap, req, res);
			if (user.getAuthorization().equals("3")) {
				logVo.setWorkContent("관리자 로그인");
			} else if (user.getAuthorization().equals("2")) {
				logVo.setWorkContent("운용자 로그인");
			} else {
				logVo.setWorkContent("사용자 로그인");
			}
			logVo.setWorkCodeId("1");
			logVo.setWorkUiId("7");
			logVo.setResult(1);
			logService.saveUserAction(logVo);

			res.sendRedirect("/");

		} catch (BizException e) {
			e.printStackTrace();

			if (e.getErrorCode().equals(ErrorCode.USER_DUPLOGIN)) {
				flashMap.put("errMsg", messageSource.getMessage("error.duplicatedLogin"));
			} else if (e.getErrorCode().equals(ErrorCode.USER_NOT_ACTIVATED)) {
				flashMap.put("errMsg", messageSource.getMessage("error.userDisable"));
			} else if (e.getErrorCode().equals(ErrorCode.USER_NOT_FOUND)) {
				flashMap.put("errMsg", messageSource.getMessage("error.userNotMatch"));
			}
			// json.addProperty("exceptionMsg", e.getMessage());
			logVo.setWorkContent("사용자 아이디:" + param.getUserId() + ", " + String.valueOf(flashMap.get("errMsg")));
			logVo.setResult(0);
			logService.saveUserAction(logVo);
			flashMapManager.saveOutputFlashMap(flashMap, req, res);
			res.sendRedirect("/login");
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			logVo.setWorkContent("사용자 아이디:" + param.getUserId() + ", " + e.getMessage());
			logVo.setResult(0);
			logService.saveUserAction(logVo);
			json.addProperty("exceptionMsg", e.getMessage());
			flashMap.put("errMsg", json.toString());
			flashMapManager.saveOutputFlashMap(flashMap, req, res);
			res.sendRedirect("/login");
			throw e;
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest req, HttpServletResponse res) throws Exception {
		LogVo logVo = new LogVo();
		logVo.setWorkCodeId("2");

		logVo.setAccountId(SessionData.getUserVo().getAccountId());
		authService.setUdateLogout(SessionData.getUserVo());
		String refer = req.getHeader("Referer");
		log.debug("logout refer:{}", refer);
		// logVo.setWorkUiId("7");
		logVo.setWorkContent("페이지:" + refer + "에서 로그 아웃");
		logService.saveUserAction(logVo);
		HttpSession session = req.getSession();
		session.invalidate();
		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1 && ssoMode.equals("Y")) {
			return "redirect:/sso/logout?activeProfile=" + activeProfile;
		} else {
			return "redirect:/login";
		}
		//

	}

}
