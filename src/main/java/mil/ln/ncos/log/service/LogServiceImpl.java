package mil.ln.ncos.log.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.vo.LogVo;
import mil.ln.ncos.dao.DAO;

@RequiredArgsConstructor
@Service
public class LogServiceImpl implements LogService {

	private final DAO dao;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${cryptoMode}")
	private String cryptoMode;
	@Value("${crypto.key}")
	private String cryptoModeKey;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getUserAuditList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectPage("Log.selectUserAuditCount", "Log.selectUserAuditList", map);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int saveUserAction(LogVo vo) throws Exception {
		if (cryptoMode.equals("N")) {
			return dao.update("Log.insertUserAction", vo);
		} else {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Date date = new Date(ts.getTime());
			String collectionTime = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm.ss");
			vo.setCollectionTime(collectionTime);
			Map<String, Object> salt = dao.selectMap("Cmmn.selectIntegritySalt", null)
					.orElseGet(() -> new HashMap<String, Object>());
			final String saltKey = "userAction";
			String integrityKey = vo.getWorkCodeId()
					+ vo.getAccountId()
					+ (vo.getResult() == 1 ? "true" : "false")
					+ (vo.getWorkContent())
					+ collectionTime.substring(0, 10);// yyyy-mm-dd

			integrityKey = integrityKey + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey);

			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
			vo.setIntegrity(integrity);
			return dao.update("Log.insertUserActionNcos", vo);
			// return dao.update("Log.updateUserActionNcos", vo); action_id 가 포함 안되면 필요 없을듯.
		}

	}

}