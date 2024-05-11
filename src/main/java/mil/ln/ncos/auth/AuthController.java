package mil.ln.ncos.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.springframework.web.servlet.view.RedirectView;

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
	
	@Value("${crypto.key1}")
	private String cryptoModeKey1;

	private final MessageSourceAccessor messageSource;
	private final LogService logService;
	private final AuthService authService;

	@GetMapping("/login")
	public ModelAndView login(Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {

		if (ssoMode.equals("Y")) {
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
		//LogVo logVo = new LogVo();
		JsonObject json = new JsonObject();
		final FlashMapManager flashMapManager = new SessionFlashMapManager();
		UserVo param = new UserVo();
		try {
			//logVo.setWorkCodeId("1");
			//logVo.setWorkUiId("7");
			Field[] fields = UserVo.class.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (null != req.getParameter(f.getName())) {
					f.set(param, req.getParameter(f.getName()));
				}

			}
			String loginId = param.getLoginUserId();
			if(cryptoMode.equals("Y")) {
				loginId = ScpDbUtil.scpEnc(loginId,cryptoModeKey1);
				
			}

			param.setUserId(loginId);
			if(activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
				String salt = authService.getUserAccountSalt(param.getUserId());
				if(null == salt || "null".equals(salt)) {
					throw new BizException(ErrorCode.USER_NOT_FOUND);
				}
				param.setPassword(CryptoUtil.encode(param.getCurPassword(),salt.getBytes()));
			}
			

			UserVo user = authService.getUserAccount(param, req);
			//logVo.setAccountId(user.getAccountId());
			json.addProperty("accountId", user.getAccountId());
			json.addProperty("userId", user.getUserId());
			json.addProperty("username", user.getUsername());
			json.addProperty("auth", user.getAuthorization());
			json.addProperty("affiliationId", user.getAffiliationId());
			json.addProperty("phoneNo", user.getPhoneNo());
			json.addProperty("classId", user.getClassId());
			json.addProperty("alarmStatus", user.getAlarmStatus());
			json.addProperty("alarmLevel", user.getAlarmLevel());
			json.addProperty("warningStatus", user.getWarningStatus());
			json.addProperty("warningLevel", user.getWarningLevel());
			if (user.getSessionControlStatus() == 0) {
				json.addProperty("sessionTm", -1);
			} else {
				if(activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1 ) {
					json.addProperty("sessionTm", 5 * 60);
				}
				else {
					json.addProperty("sessionTm", 60 * 60);
				}
				
			}
			if(null != user.getLastSuccessAccessDate()) {
				json.addProperty("connectTime", DateUtil.formatDate(user.getLastSuccessAccessDate(), "HH:mm:ss"));
			}
			else {
				json.addProperty("connectTime", DateUtil.getFrmtDate(null, "HH:mm:ss"));
			}

			
			flashMap.put("sessionData", json.toString());

			flashMapManager.saveOutputFlashMap(flashMap, req, res);
			/*
			if (user.getAuthorization().equals("3")) {
				logVo.setWorkContent("관리자 로그인");
			} else if (user.getAuthorization().equals("2")) {
				logVo.setWorkContent("운용자 로그인");
			} else {
				logVo.setWorkContent("사용자 로그인");
			}
			logVo.setAccountId(user.getAccountId());
			logVo.setWorkCodeId("1");
			logVo.setWorkUiId("7");
			logVo.setResult(1);
			logService.saveUserAction(logVo);
            */
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

			flashMapManager.saveOutputFlashMap(flashMap, req, res);
			res.sendRedirect("/login");
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("exceptionMsg", e.getMessage());
			flashMap.put("errMsg", json.toString());
			flashMapManager.saveOutputFlashMap(flashMap, req, res);
			res.sendRedirect("/login");
			throw e;
		}
	}
  

	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView();
		if (ssoMode.equals("Y")) {
			mv.setViewName("sso/logout");
			return mv;
		}
		else {
			/*
			LogVo logVo = new LogVo();
			logVo.setWorkCodeId("2");

			logVo.setAccountId(SessionData.getUserVo().getAccountId());
			authService.setUdateLogout(SessionData.getUserVo());
			String refer = req.getHeader("Referer");
			logVo.setWorkContent("페이지:" + refer + "에서 로그 아웃");
			logVo.setAccountId(SessionData.getUserVo().getAccountId());
			logService.saveUserAction(logVo);
			*/
			HttpSession session = req.getSession();
			session.invalidate();
			RedirectView redirectView = new RedirectView(); // redirect url 설정
			redirectView.setUrl("/login");
			mv.setView(redirectView);
			return mv;
		}
		// 
		
	}

}
