package mil.ln.ncos.auth.service;


import javax.servlet.http.HttpServletRequest;

import mil.ln.ncos.user.vo.UserVo;

public interface AuthService {

	UserVo  getUserAccount(UserVo vo,HttpServletRequest req) throws Exception;
	
	String getUserAccountSalt(String userId) throws Exception;
	
	UserVo  getCurUserAccount(UserVo vo,HttpServletRequest req) throws Exception;
	
	int setUdateLogout(UserVo vo) throws Exception;


}
