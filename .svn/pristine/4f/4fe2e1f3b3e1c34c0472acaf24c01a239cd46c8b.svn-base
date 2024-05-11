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
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.util.StringUtil;
import mil.ln.ncos.cmmn.vo.LogVo;
import mil.ln.ncos.dao.DAO;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogServiceImpl implements LogService{

	private final DAO dao;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${cryptoMode}")
	private String cryptoMode;
	@Value("${crypto.key1}")
	private String cryptoModeKey1;


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getUserAuditList(Map<String, Object> map) throws Exception {
		if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
			map.put("systemId", "hmm");
		}
		if(cryptoMode.equals("Y")) {
			map.put("workContent", ScpDbUtil.scpEnc(String.valueOf(map.get("workContent")),cryptoModeKey1));
		}
		return (List<Map<String,Object>>)dao.selectPage("Log.selectUserAuditCount","Log.selectUserAuditList",map);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int saveUserAction(LogVo vo) throws Exception {
		if(cryptoMode.equals("N")) {
			return dao.update("Log.insertUserAction", vo);
		}else {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Date date = new Date(ts.getTime());
			String collectionTime = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm.ss");
			vo.setCollectionTime(collectionTime);
			Map<String, Object> salt = dao.selectMapByJob("Cmmn.selectIntegritySalt", null).orElseGet(() -> new HashMap<String, Object>());
			final String saltKey="userAction";

			String integrityKey = vo.getWorkCodeId()
					+vo.getAccountId()
					+(vo.getResult()==1?"true":"false")
					+(vo.getWorkContent())
					+collectionTime.substring(0,10);
			log.debug("before saveUserAction integrityKey:{}",integrityKey);
			log.debug("before saveUserAction saltKey:{}",salt.get(saltKey));
			integrityKey = integrityKey + ScpDbUtil.scpDec((String)salt.get(saltKey),cryptoModeKey1);
			
			log.debug("saveUserAction integrityKey:{}",integrityKey);
			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
			vo.setIntegrity(integrity);
			vo.setTerminalIp(ScpDbUtil.scpEnc(String.valueOf(StringUtil.ipToLong(String.valueOf(vo.getTerminalIp()))),cryptoModeKey1));
			log.debug("saveUserAction integrity:{}",integrity);
			vo.setWorkEncContent(ScpDbUtil.scpEnc(vo.getWorkContent(),cryptoModeKey1));
			return dao.update("Log.insertUserActionNcos", vo);
		}

	}

}