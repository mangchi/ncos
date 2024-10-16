package mil.ln.ncos.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

import mil.ln.ncos.annotation.Page;

import mil.ln.ncos.cmmn.util.StringUtil;
import mil.ln.ncos.user.service.UserService;
import mil.ln.ncos.user.vo.UserValidGroups.ModValid;
import mil.ln.ncos.user.vo.UserValidGroups.RegValid;
import mil.ln.ncos.user.vo.UserVo;


@Controller
@RequiredArgsConstructor
public class UserController {
	

	private final UserService userService;
	//private final CacheService cacheService;
	private final MessageSourceAccessor messageSource;


	
	@Value("${code.codeKey}")
    private String codeKey;
	
	@GetMapping("/userList")
	public ModelAndView userList(HttpServletRequest req) throws Exception{
		ModelAndView mv =  new ModelAndView("th/userList");
		UserVo vo  = new UserVo();
		mv.addObject("user", vo);
		return mv;
	}
	
	@PostMapping("/userAccountList")
	@Page
	public ResponseEntity<Map<String,Object>> getUserAccountList(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
		result.put("list", userService.getUserAccountList(param));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/saveUserAccount")
	public ResponseEntity<Map<String,Object>> saveUserAccount(@RequestBody @Validated(RegValid.class) UserVo param,Errors errors, HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
	    //try {
			if (errors.hasErrors()) {
				this.validateHandling(result,errors);
				result.put("fail_msg", this.validateHandling(result,errors));
				return ResponseEntity.ok().body(result);
			}
			if(!param.getPassword().equals(param.getConfirm())) {
				result.put("fail_msg", messageSource.getMessage("error.password.invalid8"));
				return ResponseEntity.ok().body(result);
			}
			if(userService.isExistedUser(param)) {
				result.put("fail_msg", messageSource.getMessage("error.userId.duplicated"));
				return ResponseEntity.ok().body(result);
			}
			String ip = req.getHeader("X-FORWARDED-FOR");
			if (ip == null) {
				ip = req.getRemoteAddr();
			}
			param.setTerminalIp(""+StringUtil.ipToLong(ip));
			int rtn = userService.saveUserAccount(param);
	
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
			
	    //}catch(Exception e) {
	    //	e.printStackTrace();
	    //	result.put("fail_msg", e.getMessage());
	    //}
	    return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/modUserAccount")
	public ResponseEntity<Map<String,Object>> modUserAccount(@RequestBody @Validated(ModValid.class) UserVo param,Errors errors, HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
	    //try {
			if (errors.hasErrors()) {
				this.validateHandling(result,errors);
				result.put("fail_msg", this.validateHandling(result,errors));
				return ResponseEntity.ok().body(result);
			}
		
			int rtn = userService.saveUserAccount(param);
	
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
			
	    //}catch(Exception e) {
	    //	e.printStackTrace();
	    //	result.put("fail_msg", e.getMessage());
	    //}
	    return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/delUserAccount")
	public ResponseEntity<Map<String,Object>> delUserAccount(@RequestBody Map<String,Object> param,HttpServletRequest req) throws Exception{
		Map<String,Object> result = new HashMap<>();
	    try {
			int rtn = userService.delUserAccount(param);
	
			if(rtn > 0) {
				result.put("success_msg", messageSource.getMessage("msg.success"));
			}
			else {
				result.put("fail_msg", messageSource.getMessage("msg.fail"));
			}
			
	    }catch(Exception e) {
	    	result.put("fail_msg", e.getMessage());
	    }
	    return ResponseEntity.ok().body(result);
	}
	
	
	
	
	private String validateHandling(Map<String, Object> validatorResult,Errors errors) {
		StringBuffer errMsg = new StringBuffer();
		for (FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			if(!validatorResult.containsKey(validKeyName)) {
				errMsg.append(error.getDefaultMessage()).append("\n");
				//validatorResult.put(validKeyName, error.getDefaultMessage());
			}
			
		}
		return errMsg.toString();
	}
	
	/*
	 * private void validateHandling(Map<String, Object> validatorResult,Errors
	 * errors) { for (FieldError error : errors.getFieldErrors()) { String
	 * validKeyName = String.format("valid_%s", error.getField());
	 * if(!validatorResult.containsKey(validKeyName)) {
	 * validatorResult.put(validKeyName, error.getDefaultMessage()); }
	 * 
	 * } }
	 */
}
	
