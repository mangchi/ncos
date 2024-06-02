package mil.ln.ncos.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.error.ErrorCode;
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

	@Value("${cryptoMode}")
	private String cryptoMode;
	@Value("${crypto.key1}")
	private String cryptoModeKey1;
	@Value("${crypto.key1}")
	private String cryptoModeKey2;

	@Override
	public UserVo getUserAccount(UserVo vo, HttpServletRequest req) throws Exception {
		if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
			vo.setSystemId("hmm");
		}
		if (cryptoMode.equals("Y")) {
			if (activeProfile.equals("navy")) {
				vo.setUserId(ScpDbUtil.scpEnc(vo.getUserId(), cryptoModeKey1));
			} else {
				vo.setUserId(ScpDbUtil.scpEnc(vo.getUserId(), cryptoModeKey2));
			}
		}
		UserVo userVo = (UserVo) dao.selectOptionalObject("Auth.selectUserAccount", vo).orElseGet(() -> null);
		log.debug("userVo::::{}", userVo);
		if (userVo != null) {
			if (userVo.getAccountActivation() == 0) {
				throw new BizException(ErrorCode.USER_NOT_ACTIVATED);
			}
			HttpSession session = req.getSession();
			if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
				/*
				 * String userId = SessionListener.getSessionidCheck("user",
				 * userVo.getUserId());
				 * if (userId != null && userId.length() > 0) {
				 * throw new BizException(ErrorCode.USER_DUPLOGIN);
				 * }
				 */
				userVo.setIsLogin(1);
				dao.update("Auth.updateLogin", userVo);
				if (userVo.getSessionControlStatus() == 0) {
					session.setMaxInactiveInterval(-1);
				} else {
					session.setMaxInactiveInterval(60 * 60);
				}
			} else {
				if (cryptoMode.equals("Y")) {
					if (activeProfile.equals("navy")) {
						userVo.setUserId(ScpDbUtil.scpDec(userVo.getUserId(), cryptoModeKey1));
						userVo.setUsername(ScpDbUtil.scpDec(userVo.getUsername(), cryptoModeKey1));
						userVo.setPhoneNo(ScpDbUtil.scpDec(userVo.getPhoneNo(), cryptoModeKey1));
						userVo.setEmail(ScpDbUtil.scpDec(userVo.getEmail(), cryptoModeKey1));
					} else {
						userVo.setUserId(ScpDbUtil.scpDec(userVo.getUserId(), cryptoModeKey2));
						userVo.setUsername(ScpDbUtil.scpDec(userVo.getUsername(), cryptoModeKey2));
						userVo.setPhoneNo(ScpDbUtil.scpDec(userVo.getPhoneNo(), cryptoModeKey2));
						userVo.setEmail(ScpDbUtil.scpDec(userVo.getEmail(), cryptoModeKey2));

					}
				}
				if (userVo.getSessionControlStatus() == 0) {
					session.setMaxInactiveInterval(-1);
				} else {
					session.setMaxInactiveInterval(5 * 60);
				}
				// session.setMaxInactiveInterval(1 * 60);

			}

			session.setAttribute("user", userVo);
		} else {
			throw new BizException(ErrorCode.USER_NOT_FOUND);
		}
		return userVo;

	}

	@Override
	public int setUdateLogout(UserVo vo) throws Exception {
		vo.setIsLogin(0);
		return dao.update("Auth.updateLogin", vo);
	}

}
