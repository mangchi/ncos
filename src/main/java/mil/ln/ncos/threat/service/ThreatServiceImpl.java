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
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.dao.DAO;

@Slf4j
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

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getThreatList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>)dao.selectPage("Threat.selectThreatCount","Threat.selectThreatList", map);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getThreatAllList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>)dao.selectList("Threat.selectThreatAllList", map);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getThreatCurrentList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>)dao.selectList("Threat.selectThreatCurrentList", map);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDetectPriorStatus(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>)dao.selectList("Threat.selectDetectPriorStatus", map);
	}
	
	@Override
	public Map<String, Object> getEventStatus(Map<String, Object> map) throws Exception {
		return dao.selectMap("Threat.selectEventStatus", map).orElseGet(() -> new HashMap<String, Object>());
	}
	
	@Override
	public Map<String, Object> getListEventStatus(Map<String, Object> map) throws Exception {
		return dao.selectMap("Threat.selectListEventStatus", map).orElseGet(() -> new HashMap<String, Object>());
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getThreatInfo(Map<String, Object> map) throws Exception {
		Map<String,Object> m = new HashMap();
		BufferedReader reader = null;
	    try {
	    	if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
	    		map.put("systemId", "hmm");
			}
			m = dao.selectMap("Threat.selectThreatInfo", map).orElseGet(() -> new HashMap<String, Object>());
			if(m.containsKey("payload") && m.get("payload") != null && !m.get("payload").equals("")) {
				StringBuffer sb = new StringBuffer();
				String payloadPath = String.valueOf(m.get("payload"));
				log.debug("before payloadPath:::::::::{}",payloadPath);
				if(cryptoMode.equals("Y")) {
					m.put("fragmentId", ScpDbUtil.scpDec(String.valueOf(m.get("fragmentId")), cryptoModeKey1));
					payloadPath = ScpDbUtil.scpDec(payloadPath, cryptoModeKey1);
					 log.debug("after payloadPath:::::::::{}",payloadPath);
	        		
	        	}
				m.put("payload", payloadPath);
				reader = new BufferedReader(new FileReader(payloadPath));
			    String str;
		        while ((str = reader.readLine()) != null) {
		        	
		        	sb.append(str+"\n");
		        }
		        String payloadContent = sb.toString();
		        m.put("payloadContent", payloadContent);
		        reader.close();
			}
	    }catch(IOException ie) {
	    	 m.put("payloadContent", ie.getMessage());
	    }
	    finally {
	    	if(reader != null) reader.close();	    	
	    }
	    return m;
	}
	
	@Override
	public int saveThreatAnalysis(Map<String, Object> map) throws Exception {
		if(cryptoMode.equals("N")) {
			if(map.containsKey("analysisId") && map.get("analysisId") != null && !map.get("analysisId").toString().trim().equals("")) {
				
				return dao.update("Threat.updateThreatAnalysis", map);
			}else {
				log.debug("before map:{}",map);
				int cnt = dao.update("Threat.insertThreatAnalysis", map);
				log.debug("after map:{}",map);
				return cnt;
			}
		}else{//ncos 무결성검증.
			int rtn = 0;
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Date date = new Date(ts.getTime());
			String analysisTime = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm.ss");
			Map<String, Object> salt = dao.selectMapByJob("Cmmn.selectIntegritySalt", null).orElseGet(() -> new HashMap<String, Object>());
			final String saltKey="threatAnalysis";
			/*
			String integrityKey = String.valueOf(map.get("analysisId"))
					+String.valueOf(map.get("threatId"))
					+String.valueOf(map.get("analyst"))
					+analysisTime.substring(0,10)//yyyy-mm-dd
					+String.valueOf(map.get("analysisResult"))
					+String.valueOf(map.get("actionResult"));
            */
			
			map.put("analysisTime", analysisTime);
			
			if(map.containsKey("analysisId") && map.get("analysisId") != null && !map.get("analysisId").toString().trim().equals("")) {
				rtn = dao.update("Threat.updateThreatAnalysisNcos", map);
			}else {
				map.put("integrity", "temp");
				rtn = dao.update("Threat.insertThreatAnalysisNcos", map);
			}
			String integrityKey = map.get("analysisId").toString()
					+map.get("threatId").toString()
					+map.get("analyst").toString()
					+analysisTime.substring(0,10)//yyyy-mm-dd
					+map.get("analysisResult").toString()
					+map.get("actionResult").toString();
			integrityKey = integrityKey + ScpDbUtil.scpDec((String)salt.get(saltKey),cryptoModeKey1);
			log.debug("n integrityKey:{}",integrityKey);		
					
			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
			map.put("integrity", integrity);
			log.debug("saveThreatAnalysis integrity:{}",integrity);
			rtn = dao.update("Threat.updateThreatAnalysisIntegrity", map);
			return rtn;
		} 
	}
	
	@Override
	public int saveMultiThreatAnalysis(Map<String, Object> map) throws Exception {
		int rtn = 0;
		List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("list");
		for(Map<String,Object> item: list) {
			item.put("threatId",item.get("id"));
			item.put("analyst",map.get("analyst"));
			item.put("analysisResult", map.get("analysisResults"));
			item.put("actionResult", map.get("actionResults"));
			
			rtn += saveThreatAnalysis(item);
		}
		return rtn;
	}



}
