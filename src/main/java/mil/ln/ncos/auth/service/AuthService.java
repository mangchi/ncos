package mil.ln.ncos.auth.service;


import javax.servlet.http.HttpServletRequest;

import mil.ln.ncos.user.vo.UserVo;

public interface AuthService {

	UserVo  getUserAccount(UserVo vo,HttpServletRequest req) throws Exception;
	
	
	int setUdateLogout(UserVo vo) throws Exception;


}
