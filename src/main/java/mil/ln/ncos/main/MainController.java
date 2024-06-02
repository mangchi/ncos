package mil.ln.ncos.main;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.annotation.Page;
import mil.ln.ncos.cmmn.service.CmmnService;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.main.service.MainService;
import mil.ln.ncos.threat.service.ThreatService;
import mil.ln.ncos.user.vo.UserVo;

@RequiredArgsConstructor
@Controller
public class MainController {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	private final CmmnService cmmnService;
	private final MainService mainService;
	private final ThreatService threatService;
	private final MessageSourceAccessor messageSource;

	@GetMapping("/")
	public ModelAndView home(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = null;
		mv = new ModelAndView("th/navyMain");
		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
			HttpSession session = req.getSession(false);
			if (session == null || session.getAttribute("user") == null) {
				mv = new ModelAndView("th/auth/login");
				UserVo vo = new UserVo();
				mv.addObject("user", vo);
				return mv;
			} else {
				if (activeProfile.indexOf("navy") == -1 && activeProfile.indexOf("Navy") == -1) {
					mv = new ModelAndView("th/landMain");
				}
				return mv;
			}
			/*
			 * if(activeProfile.indexOf("navy") == -1 && activeProfile.indexOf("Navy") ==
			 * -1) {
			 * mv = new ModelAndView("th/landMain");
			 * }
			 */
			/*
			 * if(activeProfile.indexOf("local") > -1) { if(SessionData.getUserVo() == null)
			 * { UserVo user = new UserVo(); user.setUserId("admin");
			 * user.setPassword("ncos!1245");
			 * //user.setPassword(CryptoUtil.encode("ncos!1245")); UserVo rtnUser =
			 * authService.getSsoUserAccount(user, req); JsonObject json = new JsonObject();
			 * json.addProperty("userId", rtnUser.getUserId()); json.addProperty("username",
			 * rtnUser.getUsername()); json.addProperty("auth", rtnUser.getAuthorization());
			 * json.addProperty("connectTime", DateUtil.getToday("HH:MM:SS"));
			 * mv.addObject("sessionData", json.toString()); }
			 * 
			 * }
			 */

		} else {
			HttpSession session = req.getSession(false);
			if (session == null || session.getAttribute("user") == null) {
				mv = new ModelAndView("th/auth/login");
				UserVo vo = new UserVo();
				mv.addObject("user", vo);
				return mv;
			} else {
				mv = new ModelAndView("th/hmmNavyMain");
				return mv;
			}
		}
	}

	@GetMapping("/landMain")
	public ModelAndView landMain(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/landMain");
		return mv;
	}

	@GetMapping("/index")
	public ModelAndView index(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/index");
		return mv;
	}

	// @GetMapping("/navyIntro")
	// @Page
	@GetMapping("/navyIntro")
	public ResponseEntity<Map<String, Object>> getNavyIntro(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> appendData = new HashMap<>();
		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
			param.put("cyberDefense", "Y");
		}
		appendData.put("threatStatusList", mainService.getThreatStatusList(param));
		appendData.put("assetStatusList", mainService.getAssetStatusList(param));
		appendData.put("statusInfo", mainService.getStatus(param));
		result.put("appendData", appendData);
		result.put("list", threatService.getThreatCurrentList(param));
		return ResponseEntity.ok().body(result);
	}

	@Page
	@PostMapping("/navyHmmIntro")
	public ResponseEntity<Map<String, Object>> getNavyHmmIntro(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> appendData = new HashMap<>();
		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
			param.put("cyberDefense", "Y");
		}
		appendData.put("threatStatusList", mainService.getThreatStatusList(param));
		// appendData.put("assetStatusList", mainService.getAssetStatusList(param));
		// appendData.put("dataTransList", mainService.getDataTransList(param));
		appendData.put("statusInfo", mainService.getStatus(param));
		result.put("appendData", appendData);
		result.put("list", threatService.getThreatList(param));
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/dataTrans")
	public ResponseEntity<Map<String, Object>> getDataTrans(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		param.put("curTime", DateUtil.getFrmtDate(Timestamp.valueOf(LocalDateTime.now()), "yyyy-MM-dd HH:mm:ss"));
		result.put("curTime", param.get("curTime"));
		result.put("list", mainService.getDataTransList(param));
		result.put("totSrcBytes", mainService.getTotDataTrans(param));
		return ResponseEntity.ok().body(result);
	}

	@SuppressWarnings("unchecked")
	@Page
	@PostMapping("/landIntro")
	public ResponseEntity<Map<String, Object>> getLandIntro(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> appendData = new HashMap<>();
		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
			param.put("cyberDefense", "Y");
		}

		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
			param.put("cyberDefense", "Y");
		}

		// 지도에 표시되는 부대 정보를 yml 파일에 서 읽어옴 @kw.ryu
		Yaml unitList = new Yaml();
		InputStream inputStream = MainController.class.getClassLoader().getResourceAsStream("ncos.yml");
		Map<String, Object> data = unitList.load(inputStream);

		Map<String, Object> affiliation = (Map<String, Object>) data.get("affiliation");
		int unitCount = (int) affiliation.get("unitcount");

		ArrayList<Map<String, Object>> unitResult = new ArrayList<>();

		for (int i = 0; i < unitCount; i++) {
			String unitKey = "unit" + i;
			Map<String, Object> unit = (Map<String, Object>) affiliation.get(unitKey);

			/*
			 * String id = (String) unit.get("id");
			 * double lat = (double) unit.get("lat");
			 * double lon = (double) unit.get("lon");
			 * String img = (String) unit.get("img");
			 * String label = (String) unit.get("label");
			 * String color = (String) unit.get("color");
			 * 
			 * String unitItem = "id : '" + id + "', lat : '" + lat + "', lon : '" + lon +
			 * "', img : '" + img + "', label : '" + label + "', color : '" + color + "'";
			 */
			unitResult.add(unit);
		}

		result.put("unitResult", unitResult);

		appendData.put("vesselList", cmmnService.getNetEquipStatusList(param));
		appendData.put("shipList", cmmnService.getShipStatusList(param));
		appendData.put("unitResult", unitResult);
		result.put("appendData", appendData);
		result.put("list", threatService.getThreatList(param));

		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveCyberDefense")
	public ResponseEntity<Map<String, Object>> saveCyberDefense(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int rtn = cmmnService.saveCyberDefense(param);
		if (rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/sysInfo")
	public ModelAndView sysInfo(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("sysInfo");
		return mv;
	}

	@GetMapping("/map")
	public ModelAndView map(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("map");
		return mv;
	}

	@GetMapping("/kormap")
	public ModelAndView kormap(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("kormap");
		return mv;
	}

	@GetMapping("/test")
	public ModelAndView tst(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("test");
		return mv;
	}
}
