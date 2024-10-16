package mil.ln.ncos.asset;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.annotation.Page;
import mil.ln.ncos.asset.service.AssetService;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.threat.service.ThreatService;

@RequiredArgsConstructor
@Controller
public class AssetController {

	private final AssetService assetService;
	private final ThreatService threatService;
	private final MessageSourceAccessor messageSource;

	@GetMapping("/assetMng")
	public ModelAndView assetMng(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/assetMng");
		return mv;
	}

	@GetMapping("/assetDisp")
	public ModelAndView assetDisp(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/assetDisp");
		return mv;
	}

	@GetMapping("/assetDispo")
	public ModelAndView assetDispo(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/assetDispo");
		return mv;
	}

	@Page
	@PostMapping("/getAssetList")
	public ResponseEntity<Map<String, Object>> getAssetList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", assetService.getAssetList(param));
		return ResponseEntity.ok().body(result);
	}

	@Page
	@PostMapping("/getAssetDispList")
	public ResponseEntity<Map<String, Object>> getAssetDispList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", assetService.getAssetList(param));
		Map<String, Object> appendData = new HashMap<>();
		appendData.put("detectPriorList", threatService.getDetectPriorStatus(param));
		appendData.put("assetStatus", assetService.getAssetStatus(param));
		appendData.put("assetAffair", assetService.getAssetAffair(param));
		result.put("appendData", appendData);
		return ResponseEntity.ok().body(result);
	}

	@Page
	@PostMapping("/getAssetDispoList")
	public ResponseEntity<Map<String, Object>> getAssetDispoList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", assetService.getAssetDispoList(param));
		Map<String, Object> appendData = new HashMap<>();
		appendData.put("assetZoneList", assetService.getAssetDispoZoneList(param));
		appendData.put("assetZoneGroup", assetService.getAssetDispoZoneGroup(param));
		result.put("appendData", appendData);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/getZoneLocationList")
	public ResponseEntity<Map<String, Object>> getZoneLocationList(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", assetService.getZoneLocationList(param));
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveAsset")
	public ResponseEntity<Map<String, Object>> saveAsset(@RequestBody Map<String, Object> param, HttpServletRequest req)
			throws Exception {
		param.put("lastUpdater", SessionData.getUserVo().getAccountId());
		Map<String, Object> result = new HashMap<>();
		// try {
		if (assetService.getSystemIdCount(param) > 0) {
			result.put("fail_msg", messageSource.getMessage("error.duplicatedSystemId"));
			return ResponseEntity.ok().body(result);
		}
		param.put("lastUpdater", SessionData.getUserVo().getAccountId());
		int cnt = assetService.saveAsset(param);
		if (cnt > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		// }catch(Exception e) {
		// e.printStackTrace();
		// result.put("fail_msg", e.getMessage());
		// }
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveZoneLocation")
	public ResponseEntity<Map<String, Object>> saveZoneLocation(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		param.put("lastUpdater", SessionData.getUserVo().getAccountId());
		Map<String, Object> result = new HashMap<>();
		// try {

		param.put("lastUpdater", SessionData.getUserVo().getAccountId());
		int cnt = assetService.saveZoneLocation(param);
		if (cnt > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		// }catch(Exception e) {
		// e.printStackTrace();
		// result.put("fail_msg", e.getMessage());
		// }
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/deleteAsset")
	public ResponseEntity<Map<String, Object>> deleteAsset(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		// try {
		int cnt = assetService.deleteAsset(param);
		if (cnt > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		// }catch(Exception e) {
		// e.printStackTrace();
		// result.put("fail_msg", e.getMessage());
		// }
		return ResponseEntity.ok().body(result);
	}

}
