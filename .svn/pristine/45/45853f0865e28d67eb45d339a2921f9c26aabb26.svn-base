package mil.ln.ncos.threat;


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
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.annotation.Page;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.threat.service.ThreatService;

@RequiredArgsConstructor
@Controller
public class ThreatController {
	
	private final ThreatService threatService;
	private final MessageSourceAccessor messageSource;
	
	@GetMapping("/threatDisp")
	public ModelAndView threatDisp(Model model,HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/threatDisp");

		return mv;
	}
	
	@Page
	@PostMapping("/getThreatList")
	public ResponseEntity<Map<String,Object>> getThreatList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		Map<String,Object> appendData = new HashMap<>();
		appendData.put("detectPriorList", threatService.getDetectPriorStatus(param));
		appendData.put("eventStatusInfo", threatService.getEventStatus(param));
		result.put("appendData", appendData);
		result.put("list", threatService.getThreatList(param));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/getThreatInfo")
	public ResponseEntity<Map<String,Object>> getThreatInfo(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("info", threatService.getThreatInfo(param));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/saveThreatAnalysis")
	public ResponseEntity<Map<String,Object>> saveThreatAnalysis(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			param.put("analyst",SessionData.getUserVo().getAccountId());
			int cnt = threatService.saveThreatAnalysis(param);
			if(cnt > 0) {
	    		result.put("success_msg", messageSource.getMessage("msg.success"));
	    	}
	    	else {
	    		result.put("fail_msg", messageSource.getMessage("msg.fail"));
	    	}
		}catch(Exception e) {
			//e.printStackTrace();
		    result.put("fail_msg", e.getMessage());
		}
		return ResponseEntity.ok().body(result);
	}
	/*
	@PostMapping("/saveWatchThreat")
	public ResponseEntity<Map<String,Object>> saveWatchThreat(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			param.put("analyst",SessionData.getUserVo().getAccountId());
			int cnt = threatService.saveThreatAnalysis(param);
			if(cnt > 0) {
	    		result.put("success_msg", messageSource.getMessage("msg.success"));
	    	}
	    	else {
	    		result.put("fail_msg", messageSource.getMessage("msg.fail"));
	    	}
		}catch(NoSuchMessageException e) {
			//e.printStackTrace();
		    result.put("fail_msg", e.getMessage());
		}
		return ResponseEntity.ok().body(result);
	}
	*/
	

}
