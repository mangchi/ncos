package mil.ln.ncos.rpt;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.annotation.Page;
import mil.ln.ncos.annotation.ReportScan;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.rpt.service.RptService;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RptController {
	@Value("${spring.profiles.active}")
	private String activeProfile;
	private final RptService rptService;
	private final MessageSourceAccessor messageSource;

	@GetMapping("/clip")
	public ModelAndView clip(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.addObject("ClipID", req.getParameter("ClipID"));
		mv.setViewName("clipreport5/Clip");
		return mv;

	}

	@PostMapping("/reportServer")
	public ModelAndView reportServer(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("clipreport5/report_server");
		return mv;

	}

	@GetMapping("/rptSchlMng")
	public ModelAndView rptSchlMng(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/rptSchlMng");
		return mv;
	}

	@GetMapping("/rptFrmMng")
	public ModelAndView rptFrmMng(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/rptFrmMng");
		return mv;
	}

	@GetMapping("/rptMng")
	public ModelAndView rptMng(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/rptMng");
		if (null != req.getParameter("id")) {
			mv.addObject("manageId", req.getParameter("id"));
		}

		return mv;
	}

	@Page
	@PostMapping("/getRptSchlList")
	public ResponseEntity<Map<String, Object>> getRptSchlList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		log.debug("param:{}", param);
		Map<String, Object> result = new HashMap<>();
		result.put("list", rptService.getRptSchList(param));
		return ResponseEntity.ok().body(result);
	}

	@Page
	@PostMapping("/getRptFrmList")
	public ResponseEntity<Map<String, Object>> getRptFrmList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		log.debug("param:{}", param);
		Map<String, Object> result = new HashMap<>();
		result.put("list", rptService.getRptFrmList(param));
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("getRptFrms")
	public ResponseEntity<Map<String, Object>> getRptFrms(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		log.debug("param:{}", param);
		Map<String, Object> result = new HashMap<>();
		result.put("list", rptService.getRptFrms(param));
		return ResponseEntity.ok().body(result);
	}

	@Page
	@PostMapping("/getRptMngList")
	public ResponseEntity<Map<String, Object>> getRptMngList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", rptService.getRptMngList(param));
		return ResponseEntity.ok().body(result);
	}

	@ReportScan
	@PostMapping("/reportView")
	public ResponseEntity<Map<String, Object>> reportView(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("param", param);
		int threatLevel = (param.get("threatLevel") == null || param.get("threatLevel").equals("")) ? 0
				: Integer.parseInt(String.valueOf(param.get("threatLevel")));
		int assetLevel = (param.get("assetLevel") == null || param.get("assetLevel").equals("")) ? 0
				: Integer.parseInt(String.valueOf(param.get("assetLevel")));
		if (param.get("reportType").equals("1")) { // Detail{
			if (threatLevel > 0 && assetLevel > 0) {
				result.put("fileNm", "THREAT_ASSET_REPORT_DETAIL");
			} else if (threatLevel > 0 && assetLevel == 0) {
				result.put("fileNm", "THREAT_REPORT_DETAIL");
			} else if (assetLevel > 0 && threatLevel == 0) {
				result.put("fileNm", "ASSET_REPORT_DETAIL");
			} else {

			}

		} else if (param.get("reportType").equals("2")) {
			if (threatLevel > 0 && assetLevel > 0) {
				result.put("fileNm", "THREAT_ASSET_REPORT_SUMMARY");
			} else if (threatLevel > 0 && assetLevel == 0) {
				result.put("fileNm", "THREAT_REPORT_SUMMARY");
			} else if (assetLevel > 0 && threatLevel == 0) {
				result.put("fileNm", "ASSET_REPORT_SUMMARY");
			} else {

			}

		} else {

		}
		result.put("rtn", rptService.getReportView(param));
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveRptFrm")
	public ResponseEntity<Map<String, Object>> saveRptFrm(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
			param.put("assetYn", "Y");
		}
		param.put("accountId", SessionData.getUserVo().getAccountId());
		if (rptService.saveRptFrm(param) > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveRptSch")
	public ResponseEntity<Map<String, Object>> saveRptSch(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		param.put("accountId", SessionData.getUserVo().getAccountId());
		if (rptService.saveRptSch(param) > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/deleteRptMng")
	public ResponseEntity<Map<String, Object>> deleteRptMng(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();

		if (rptService.deleteRptMng(param) > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/deleteRptFrm")
	public ResponseEntity<Map<String, Object>> deleteRptFrm(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		if (rptService.deleteRptFrm(param) > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/deleteRptSch")
	public ResponseEntity<Map<String, Object>> deleteRptSch(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		if (rptService.deleteRptSch(param) > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/updateClickCount")
	public ResponseEntity<Map<String, Object>> updateClickCount(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();

		if (rptService.updateClickCount(param) > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/reportView")
	public ModelAndView reportView(HttpServletRequest req, HttpServletResponse res) throws Exception {

		ModelAndView mv = new ModelAndView();
		RedirectView redirectView = new RedirectView(); // redirect url 설정
		redirectView.setUrl("clipreport5/report");
		mv.addObject("reportKey", req.getParameter("reportKey"));
		mv.setView(redirectView);
		mv.setViewName("clipreport5/report");
		return mv;

	}

}
