package mil.ln.ncos.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.util.CryptoUtil;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.user.vo.UserVo;

import mil.ln.ncos.user.vo.CustomUserVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService,UserDetailsService  {
	
	private final DAO dao;
	//private final CacheService cacheService;
	//private final AuthService authService;


	/**
	 * Spring Security 필수 메소드 구현
     * @param username 
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
	 */
	//@SuppressWarnings("unchecked")
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException,AuthenticationServiceException{
		CustomUserVo user = (UserVo)dao.selectOne("User.selectUser", userId);
		if(user == null) {
			throw new UsernameNotFoundException(messageSource.getMessage("error.userNotFound"));
		}
		
		Map<String, PwFailCacheData> pwdFailCacheData = cacheService.getPwdFailCacheData(pwFailKey);
		int failCnt = pwdFailCacheData.containsKey(user.getUserId()) ? pwdFailCacheData.get(user.getUserId()).getPwFailCnt() : 0;
        if (failCnt >= pwFailCnt) {
			if(!LocalDateTime.now().isAfter(pwdFailCacheData.get(user.getUserId()).getPwFailDateTime().plusMinutes(pwFaileLockMin))) {
				throw new AuthenticationServiceException(messageSource.getMessage("error.userLockedByError",new String[] { String.valueOf(pwFaileLockMin) }));
			}
		}

		user.setUserPw(user.getUserPw().replace("&#36;", "$"));
		//List<String> roles = (List<String>)dao.selectList("User.selectUserRole", userId);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		authorities.add(new SimpleGrantedAuthority(user.getAuthority().name())); 
		/*
		for (String authority : roles) 
		{ 
			authorities.add(new SimpleGrantedAuthority(authority)); 
			user.setAuthority(Authority.valueOf(authority));
		} 
		*/
		user.setAuthorities(authorities);
		return user;
	}
	
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
