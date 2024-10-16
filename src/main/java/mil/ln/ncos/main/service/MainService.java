package mil.ln.ncos.main.service;

import java.util.List;
import java.util.Map;

public interface MainService {
	
	

	
	List<Map<String,Object>> getThreatStatusList(Map<String,Object> map) throws Exception;
	
	List<Map<String,Object>> getAssetStatusList(Map<String,Object> map) throws Exception;
	
	List<Map<String,Object>> getDataTransList(Map<String,Object> map) throws Exception;
	
	long getTotDataTrans(Map<String,Object> map) throws Exception;
	
	Map<String,Object> getStatus(Map<String,Object> map) throws Exception;
	


}
