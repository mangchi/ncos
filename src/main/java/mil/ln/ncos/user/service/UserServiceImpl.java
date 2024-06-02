package mil.ln.ncos.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.util.CryptoUtil;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.user.vo.UserVo;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final DAO dao;
	//private final CacheService cacheService;
	//private final AuthService authService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getUserAccountList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectPage("User.selectUserAccountCount","User.selectUserAccountList",map);
	}
	
	@Override
	public int saveUserAccount(UserVo vo) throws Exception {
		int rtn = 0;
		vo.setPassword(CryptoUtil.encode(vo.getPassword()));
		//log.debug("vo:{}",vo);
	    if(vo.getAccountId() == null || vo.getAccountId().trim().equals("")) {
	    	rtn += dao.update("User.insertUserAccount", vo);
	    	rtn += dao.update("User.insertAccountStatus", vo);
	    	return rtn;
	    }

	    rtn += dao.update("User.updateUserAccount", vo);
	    rtn += dao.update("User.updateAccountStatus", vo);
	    return rtn;
	}

	@Override
	public boolean isExistedUser(UserVo vo) throws Exception {
		if(dao.selectCount("User.selectAccountCount", vo) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int delUserAccount(Map<String, Object> map) throws Exception {
		int rtn = 0;
		rtn += dao.delete("User.deleteAccountSetting", map);
		rtn += dao.delete("User.deleteAccountStatus", map);
		rtn += dao.delete("User.deleteUserAccount", map);
		return rtn;
	}
	

	

}
