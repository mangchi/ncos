package mil.ln.ncos.log;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.annotation.Page;
import mil.ln.ncos.log.service.LogService;

@RequiredArgsConstructor
@Controller
public class LogController {
	
	private final LogService logService;
	
	@GetMapping("/userAudit")
	public ModelAndView userAudit(Model model,HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/userAudit");
		return mv;
	}
	
	@Page
	@PostMapping("/getUserAuditList")
	public ResponseEntity<Map<String,Object>> getUserAuditList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list", logService.getUserAuditList(param));
		return ResponseEntity.ok().body(result);
	}

}
