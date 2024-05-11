package mil.ln.ncos.env;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.NoSuchMessageException;
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
import mil.ln.ncos.env.service.EnvService;

@RequiredArgsConstructor
@Controller
public class EnvController {
	
    private final EnvService envService;
    private final MessageSourceAccessor messageSource;
	
	@GetMapping("/envConf")
	public ModelAndView envConf(Model model,HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/envConf");
		return mv;
	}
	
	@Page
	@PostMapping("/getConfList")
	public ResponseEntity<Map<String,Object>> getConfList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list", envService.getConfList(param));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/saveEnvConf")
	public ResponseEntity<Map<String,Object>> saveEnvConf(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		try {
			int cnt = envService.saveEnvConf(param);
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
	

}
