package mil.ln.ncos.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.vo.LogVo;
import mil.ln.ncos.log.service.LogService;
import mil.ln.ncos.user.vo.UserVo;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/sso", method = { RequestMethod.POST, RequestMethod.GET })
public class SsoController {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${cryptoMode}")
	private String cryptoMode;

	private final AuthService authService;
	private final LogService logService;

	@PostMapping("business")
	public ModelAndView ssoBusiness(HttpServletRequest req, HttpServletResponse res) throws Exception {
		log.debug("ssoBusiness..post.............");
		ModelAndView mv = new ModelAndView();
		req.setAttribute("activeProfile", activeProfile);
		mv.setViewName("sso/business");
		return mv;
	}

	@PostMapping("checkAuth")
	public ModelAndView checkAuth(HttpServletRequest req, HttpServletResponse res) throws Exception {
		log.debug("checkAuth...............");
		ModelAndView mv = new ModelAndView();
		// req.setAttribute("activeProfile", activeProfile);
		mv.setViewName("sso/checkauth");
		return mv;
	}

	@PostMapping("agentProc")
	public ModelAndView agentProc(HttpServletRequest req, HttpServletResponse res) throws Exception {
		final FlashMap flashMap = new FlashMap();
		final FlashMapManager flashMapManager = new SessionFlashMapManager();
		JsonObject json = new JsonObject();
		LogVo logVo = new LogVo();
		log.debug("agentProc...............");
		ModelAndView mv = new ModelAndView();
		// req.setAttribute("activeProfile", activeProfile);
		HttpSession session = req.getSession();
		String resultCode = session.getAttribute("resultCode") == null ? ""
				: session.getAttribute("resultCode").toString();
		// String resultMessage = session.getAttribute("resultMessage") == null ? ""
		// : session.getAttribute("resultMessage").toString();

		String id = null;
		if ("000000".equals(resultCode)) {

			if (session.getAttribute("id") != null) {
				id = (String) session.getAttribute("id");
				log.debug("sso return id :{}", id);
				UserVo param = new UserVo();

				param.setUserId(id);
				UserVo user = authService.getUserAccount(param, req);
				if (user != null) {
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
						json.addProperty("sessionTm", 5 * 60);
					}
					if (null != user.getLastSuccessAccessDate()) {
						json.addProperty("connectTime",
								DateUtil.formatDate(user.getLastSuccessAccessDate(), "HH:mm:ss"));
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
					RedirectView redirectView = new RedirectView(); // redirect url 설정
					redirectView.setUrl("/");
					mv.setView(redirectView);
					log.debug(
							"################################## redirect agentProc to / ##################################");
					return mv;
				}

			}
		}
		mv.setViewName("/sso/agentProc");
		return mv;
	}

	@GetMapping("logout")
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) throws Exception {

		ModelAndView mv = new ModelAndView();
		// req.setAttribute("activeProfile", activeProfile);
		mv.setViewName("sso/logout");
		return mv;
	}

	@GetMapping("notice")
	public ModelAndView notice(HttpServletRequest req, HttpServletResponse res) throws Exception {

		ModelAndView mv = new ModelAndView();
		// req.setAttribute("activeProfile", activeProfile);
		mv.setViewName("sso/notice");
		return mv;
	}

	@GetMapping("error")
	public ModelAndView error(HttpServletRequest req, HttpServletResponse res) throws Exception {

		ModelAndView mv = new ModelAndView();
		// req.setAttribute("activeProfile", activeProfile);
		mv.setViewName("sso/error");
		return mv;
	}

	@GetMapping("exception")
	public ModelAndView exception(HttpServletRequest req, HttpServletResponse res) throws Exception {

		ModelAndView mv = new ModelAndView();
		// req.setAttribute("activeProfile", activeProfile);
		RedirectView redirectView = new RedirectView(); // redirect url 설정
		redirectView.setUrl("/error");
		return mv;
	}

}
