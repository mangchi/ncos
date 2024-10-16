package mil.ln.ncos.user.service;



import java.util.List;
import java.util.Map;

import mil.ln.ncos.user.vo.UserVo;

public interface UserService {
	List<Map<String, Object>> getUserAccountList(Map<String, Object> map) throws Exception;
	
	boolean isExistedUser(UserVo vo) throws Exception;
	
	int saveUserAccount(UserVo vo) throws Exception;
	
	int delUserAccount(Map<String, Object> map) throws Exception;


}
