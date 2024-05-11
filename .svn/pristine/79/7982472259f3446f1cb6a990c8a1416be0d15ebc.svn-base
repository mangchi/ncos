package mil.ln.ncos.func;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
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
import mil.ln.ncos.func.service.FuncService;

@RequiredArgsConstructor
@Controller
public class FuncController {
	
	@Value("${spring.profiles.active}") 
	private String activeProfile;
	
	private final FuncService funcService;
	
	@GetMapping("/funcDisp")
	public ModelAndView funcDisp(Model model,HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/funcDisp");
		if(activeProfile.equals("hmmLand")) {
			mv = new ModelAndView("th/hmmFuncDisp");
		}
		return mv;
	}
	
	@Page
	@PostMapping("/getFuncList")
	public ResponseEntity<Map<String,Object>> getFuncList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		if(activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
			param.put("systemId","hmm");
		}
		result.put("list", funcService.getFuncList(param));
		Map<String,Object> appendData = new HashMap<>();
		appendData.put("cscStatus", funcService.getCscStatus(param));
		result.put("appendData", appendData);
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/getShipStatusList")
	public ResponseEntity<Map<String,Object>> getShipStatusList(@RequestParam Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list", funcService.getShipStatusList(param));
		return ResponseEntity.ok().body(result);
	}



}
