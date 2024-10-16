package mil.ln.ncos.net;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import mil.ln.ncos.cmmn.service.CmmnService;
import mil.ln.ncos.net.service.NetService;

@RequiredArgsConstructor
@Controller
public class NetController {

	private final NetService netService;
	private final CmmnService cmmnService;
	private final AssetService assetService;

	@GetMapping("/netTopo")
	public ModelAndView index(Model model, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/netTopo");

		return mv;
	}

	@Page
	@PostMapping("/getEventList")
	public ResponseEntity<Map<String, Object>> getEventList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		// result.put("list", netService.getEventList(param));
		result.put("list", assetService.getAssetDispoList(param));
		Map<String, Object> appendData = new HashMap<>();
		appendData.put("linkInfoList", cmmnService.getLinkInfoList(param));
		appendData.put("errorList", netService.getErrorAssetList(param));
		appendData.put("topoList", netService.getTopoList(param));
		// appendData.put("netEquipList", cmmnService.getNetEquipList(param));
		// appendData.put("topoList", cmmnService.getTopoAssetList(param));
		result.put("appendData", appendData);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/popNetTopology")
	public ResponseEntity<Map<String, Object>> getNetEquipList(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("netEquipList", netService.getNetEquipList(param));
		result.put("linkInfoList", cmmnService.getLinkInfoList(param));
		return ResponseEntity.ok().body(result);
	}

}
