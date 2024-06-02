package mil.ln.ncos.threat.service;

import java.util.List;
import java.util.Map;

public interface ThreatService {
	
	List<Map<String,Object>> getThreatList(Map<String,Object> map) throws Exception;
	
	List<Map<String,Object>> getThreatCurrentList(Map<String,Object> map) throws Exception;
	
	List<Map<String, Object>> getDetectPriorStatus(Map<String,Object> map) throws Exception;
	
	Map<String, Object> getEventStatus(Map<String, Object> map) throws Exception;
	
	Map<String, Object> getThreatInfo(Map<String, Object> map) throws Exception;
	
	int saveThreatAnalysis(Map<String, Object> map) throws Exception;


}
