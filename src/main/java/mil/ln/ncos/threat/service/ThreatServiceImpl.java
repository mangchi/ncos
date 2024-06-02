package mil.ln.ncos.threat.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.dao.DAO;

@RequiredArgsConstructor
@Service
public class ThreatServiceImpl implements ThreatService {

	private final DAO dao;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${cryptoMode}")
	private String cryptoMode;
	@Value("${crypto.key1}")
	private String cryptoModeKey1;
	@Value("${crypto.key2}")
	private String cryptoModeKey2;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getThreatList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectPage("Threat.selectThreatCount", "Threat.selectThreatList", map);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getThreatCurrentList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Threat.selectThreatCurrentList", map);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDetectPriorStatus(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Threat.selectDetectPriorStatus", map);
	}

	@Override
	public Map<String, Object> getEventStatus(Map<String, Object> map) throws Exception {
		return dao.selectMap("Threat.selectEventStatus", map).orElseGet(() -> new HashMap<String, Object>());
	}

	@Override
	public Map<String, Object> getThreatInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> m = null;
		BufferedReader reader = null;
		try {
			m = dao.selectMap("Threat.selectThreatInfo", map).orElseGet(() -> new HashMap<String, Object>());
			if (m.containsKey("payload") && m.get("payload") != null && !m.get("payload").equals("")) {
				StringBuffer sb = new StringBuffer();
				String payloadPath = String.valueOf(m.get("payload"));
				if (cryptoMode.equals("Y")) {
					if (activeProfile.equals("navy")) {
						payloadPath = ScpDbUtil.scpDec(payloadPath, cryptoModeKey1);
					} else {
						payloadPath = ScpDbUtil.scpDec(payloadPath, cryptoModeKey2);
					}
					m.put("payload", payloadPath);
				}
				reader = new BufferedReader(new FileReader(payloadPath));
				String str;
				while ((str = reader.readLine()) != null) {

					sb.append(str + "\n");
				}
				m.put("payloadContent", sb.toString());
				reader.close();
			}
		} catch (IOException ie) {
			m.put("payloadContent", ie.getMessage());
		} finally {
			if (reader != null)
				reader.close();
		}
		return m;
	}

	@Override
	public int saveThreatAnalysis(Map<String, Object> map) throws Exception {
		if (cryptoMode.equals("N")) {
			if (map.containsKey("analysisId") && map.get("analysisId") != null
					&& !map.get("analysisId").toString().trim().equals("")) {
				return dao.update("Threat.updateThreatAnalysis", map);
			} else {
				return dao.update("Threat.insertThreatAnalysis", map);
			}
		} else {// ncos 무결성검증.
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Date date = new Date(ts.getTime());
			String analysisTime = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm.ss");
			Map<String, Object> salt = dao.selectMap("Cmmn.selectIntegritySalt", null)
					.orElseGet(() -> new HashMap<String, Object>());
			final String saltKey = "threatAnalysis";
			String integrityKey = (String) map.get("analysisId")
					+ (String) map.get("threatId")
					+ (String) map.get("analyst")
					+ analysisTime.substring(0, 10)// yyyy-mm-dd
					+ (String) map.get("analysisResult")
					+ (String) map.get("actionResult");
			if (activeProfile.equals("navy")) {
				integrityKey = integrityKey + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey1);
			} else {
				integrityKey = integrityKey + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey2);
			}

			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
			map.put("analysisTime", analysisTime);
			map.put("integrity", integrity);
			if (map.containsKey("analysisId") && map.get("analysisId") != null
					&& !map.get("analysisId").toString().trim().equals("")) {
				return dao.update("Threat.updateThreatAnalysisNcos", map);
			} else {
				return dao.update("Threat.insertThreatAnalysisNcos", map);
			}
		}
	}

}
