package mil.ln.ncos.train;

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
import mil.ln.ncos.train.service.TrainService;

@RequiredArgsConstructor
@Controller
public class TrainController {

	
	private final TrainService trainService;
	
	@GetMapping("/trainResult")
	public ModelAndView funcDisp(Model model,HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("th/trainResult");
		return mv;
	}
	
	@Page
	@PostMapping("/getTrainList")
	public ResponseEntity<Map<String,Object>> getFuncList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list", trainService.getTrainList(param));
		Map<String,Object> appendData = new HashMap<>();
		appendData.put("info", trainService.getTrainInfo());
		result.put("appendData", appendData);
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/execTrain")
	public ResponseEntity<Map<String,Object>> getShipStatusList(@RequestParam Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("callResult",trainService.execTrain(param));
		return ResponseEntity.ok().body(result);
	}



}
