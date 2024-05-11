package mil.ln.ncos.auth.service;

import java.util.Date;
import java.util.Objects;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.error.ErrorCode;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.HttpIpUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.exception.BizException;
import mil.ln.ncos.user.vo.UserVo;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
	private final DAO dao;
	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${ssoMode}")
	private String ssoMode;

	@Value("${cryptoMode}")
	private String cryptoMode;
	@Value("${crypto.key1}")
	private String cryptoModeKey1;

	@Override
	public UserVo getUserAccount(UserVo vo, HttpServletRequest req) throws Exception {
		if (cryptoMode.equals("Y")) {
			vo.setUserId(ScpDbUtil.scpEnc(vo.getUserId(), cryptoModeKey1));
			if (ssoMode.equals("N")) {
				vo.setUserId("exR8dNo=");
				vo.setPassword("pgre1KVBSzIO/tD0GZTIcQxMMQQcHhSotzIhvm8u4no=");
				// exR8dNo=
				// /uIZMBU/0zmoVEU0FGTKkNcyQcKIk9LayOpD+CRD5m8=
			}

		} else {
			if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
				vo.setSystemId("hmm");
			}

		}
		
		String ip = HttpIpUtil.getClientIpAddress(req);
		//log.debug("ip::::::::::::::::::::::::::{}",ip);

		log.debug("vo:{}", vo);
		UserVo userVo = (UserVo) dao.selectOptionalObject("Auth.selectUserAccount", vo).orElseGet(() -> null);
		if (userVo != null) {
			
			if (userVo.getAccountActivation() == 0) {
				throw new BizException(ErrorCode.USER_NOT_ACTIVATED);
			}
			HttpSession session = req.getSession();
			if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
				userVo.setIsLogin(1);
				dao.update("Auth.updateLogin", userVo);
				SimpleDateFormat dtFormat = new SimpleDateFormat("HH:mm:ss");
				Date ds = new Date();
				userVo.setLastSuccessAccessDate(ds);
				if (userVo.getSessionControlStatus() == 0) {
					session.setMaxInactiveInterval(-1);
				} else {
					session.setMaxInactiveInterval(60 * 60);
				}
			} else {
				if (cryptoMode.equals("Y")) {
					if(ssoMode.equals("N")) {
						userVo.setIsLogin(1);
						dao.update("Auth.updateLogin", userVo);
					}
					userVo.setUserId(ScpDbUtil.scpDec(userVo.getUserId(), cryptoModeKey1));
					userVo.setUsername(ScpDbUtil.scpDec(userVo.getUsername(), cryptoModeKey1));
					userVo.setPhoneNo(ScpDbUtil.scpDec(userVo.getPhoneNo(), cryptoModeKey1));
					userVo.setEmail(ScpDbUtil.scpDec(userVo.getEmail(), cryptoModeKey1));
					
				} else {
					userVo.setIsLogin(1);
					dao.update("Auth.updateLogin", userVo);
				}
				if (userVo.getSessionControlStatus() == 0) {
					session.setMaxInactiveInterval(-1);
				} else {
					session.setMaxInactiveInterval(5 * 60);
				}
			}
			userVo.setTerminalIp(ip);
			session.setAttribute("user", userVo);
		} else {
			throw new BizException(ErrorCode.USER_NOT_FOUND);
		}
		return userVo;

	}

	@Override
	public UserVo getCurUserAccount(UserVo vo, HttpServletRequest req) throws Exception {
		UserVo paramUser = new UserVo();
		if (cryptoMode.equals("Y")) {
			paramUser.setUserId(ScpDbUtil.scpEnc(vo.getUserId(), cryptoModeKey1));

		} else {
			paramUser.setUserId(vo.getUserId());
			if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
				paramUser.setPassword(vo.getPassword());
				paramUser.setSystemId("hmm");
			}

		}
		UserVo userVo = (UserVo) dao.selectOptionalObject("Auth.selectUserAccount", paramUser).orElseGet(() -> null);
		if (userVo != null) {
			if (cryptoMode.equals("Y")) {
				userVo.setUserId(ScpDbUtil.scpDec(userVo.getUserId(), cryptoModeKey1));
				userVo.setUsername(ScpDbUtil.scpDec(userVo.getUsername(), cryptoModeKey1));
				userVo.setPhoneNo(ScpDbUtil.scpDec(userVo.getPhoneNo(), cryptoModeKey1));
				userVo.setEmail(ScpDbUtil.scpDec(userVo.getEmail(), cryptoModeKey1));
			}
			HttpSession session = req.getSession();
			if (userVo.getSessionControlStatus() == 0) {
				session.setMaxInactiveInterval(-1);
			} else {
				if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
					session.setMaxInactiveInterval(60 * 60);
				}
				else {
					session.setMaxInactiveInterval(5 * 60);
				}
				
			}
		} else {
			log.error("Session Error");
		}

		return userVo;
	}

	@Override
	public int setUdateLogout(UserVo vo) throws Exception {
		vo.setIsLogin(0);
		return dao.update("Auth.updateLogin", vo);
	}

	@Override
	public String getUserAccountSalt(String userId) throws Exception {
		return String.valueOf(dao.selectOne("Auth.selectAccountSalt", userId));
	}

}
