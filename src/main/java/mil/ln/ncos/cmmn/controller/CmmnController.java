package mil.ln.ncos.cmmn.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.service.CacheService;
import mil.ln.ncos.cmmn.service.CmmnService;
import mil.ln.ncos.cmmn.util.StringUtil;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CmmnController  {

    private final CmmnService cmmnService;
    private final MessageSourceAccessor messageSource;
	
	@GetMapping("/codes")
	public  @ResponseBody Map<String,Object> getList(HttpServletRequest req) throws Exception {
        Map<String,Object> codeMap = new HashMap<>();
        //codeMap.put("codes", cacheService.getCodeCacheData(codeKey));
        codeMap.put("codes", cmmnService.getSelectCodeList());
		return codeMap;
	}
	
	@GetMapping("/zoneList")
	public @ResponseBody Map<String,Object> getZoneList(HttpServletRequest req) throws Exception{
		Map<String,Object> zoneInfo = new HashMap<>();
		zoneInfo.put("zoneInfo", cmmnService.getZoneList(null));
		return zoneInfo;
	}
	
	@GetMapping("/managerList")
	public @ResponseBody Map<String,Object> getManagerList(HttpServletRequest req) throws Exception{
		Map<String,Object> map = new HashMap<>();
		map.put("info", cmmnService.getManagerList(null));
		return map;
	}
	
	@GetMapping("/userCodeList")
	public @ResponseBody Map<String,Object> getUserCodeList(HttpServletRequest req) throws Exception{
		Map<String,Object> map = new HashMap<>();
		map.put("info", cmmnService.getUserCodeList(null));
		return map;
	}
	
	@GetMapping("/korMap")
	public String gerKorMap(Model model) throws Exception {
		return "popup/korMap";
	}
	
	@GetMapping("/sessionChk")
	public ResponseEntity<Map<String,Object>> sessionChk(HttpServletRequest req) throws Exception {
		Map<String,Object> result = new HashMap<>();
		HttpSession session = req.getSession();
		if(session == null || session.getAttribute("user") == null) {
			result.put("session", "off");
			return ResponseEntity.ok().body(result);
		}
		result.put("session", "on");
		return ResponseEntity.ok().body(result);
	}

	
	@PostMapping("/whiteList")
	public ResponseEntity<Map<String,Object>> getWhiteList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list", cmmnService.getWhiteList(param));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/watchList")
	public ResponseEntity<Map<String,Object>> getWatchList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		param.put("accountId", SessionData.getUserVo().getAccountId());
		return ResponseEntity.ok().body(cmmnService.getWatchList(param));
	}
	
	@GetMapping("/watchEquipStatus")
	public ResponseEntity<Map<String,Object>> getEquipStatus(@RequestParam Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list", cmmnService.getNetworkEquipementStatus(param));
		return ResponseEntity.ok().body(result);
	}
	

	@PostMapping("/saveWhiteList")
	public ResponseEntity<Map<String,Object>> saveWhiteList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		int rtn = cmmnService.saveWhiteList(param);
		if(rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		}
		else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/saveLinkInfo")
	public ResponseEntity<Map<String,Object>> saveLinkInfo(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		int rtn = cmmnService.saveLinkInfo(param);
		if(rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		}
		else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/getSatelliteTrans")
	public ResponseEntity<Map<String,Object>> getSatelliteTrans(@RequestParam Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("info",cmmnService.getSatelliteTrans(param));
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/getAggregationStandard")
	public ResponseEntity<Map<String,Object>> getAggregationStandard(@RequestParam Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("day",cmmnService.selectAggregationStandard());
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/getUnitInfos")
	public ResponseEntity<Map<String,Object>> getUnitInfos(@RequestParam Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list",cmmnService.getUnitInfos(param)); 
		return ResponseEntity.ok().body(result);
	}
	@PostMapping("/getShipInfos")
	public ResponseEntity<Map<String,Object>> getShipInfos(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list",cmmnService.getShipInfos(param));
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/deleteWhiteList")
	public ResponseEntity<Map<String,Object>> deleteWhiteList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			int rtn = cmmnService.deleteWhiteList(param);
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			result.put("fail_msg", e.getMessage());
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/updateWhitePolicyList")
	public ResponseEntity<Map<String,Object>> updateWhitePolicyList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			int rtn = cmmnService.updateWhitePolicyList(param);
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			result.put("fail_msg", e.getMessage());
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/saveSatelliteTrans")
	public ResponseEntity<Map<String,Object>> saveSatelliteTrans(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			param.put("setter",SessionData.getUserVo().getAccountId());
			int rtn = cmmnService.saveSatelliteTrans(param);
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			result.put("fail_msg", e.getMessage());
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/saveAlarmCheck")
	public ResponseEntity<Map<String,Object>> saveAlarmCheck(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			param.put("accountId",SessionData.getUserVo().getAccountId());
			int rtn = cmmnService.saveAlarmCheck(param);
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			result.put("fail_msg", e.getMessage());
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/saveAggregationStandard")
	public ResponseEntity<Map<String,Object>> saveAggregationStandard(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			int rtn = cmmnService.saveAggregationStandard(param);
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			result.put("fail_msg", e.getMessage());
		}
		return ResponseEntity.ok().body(result);
	}

}