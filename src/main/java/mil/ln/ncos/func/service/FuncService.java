package mil.ln.ncos.func.service;

import java.util.List;
import java.util.Map;

public interface FuncService {
	
	List<Map<String,Object>> getFuncList(Map<String,Object> map) throws Exception;
	
	List<Map<String,Object>> getCscStatus(Map<String,Object> map) throws Exception;
	
	List<Map<String,Object>> getShipStatusList(Map<String,Object> map) throws Exception;
	
	int saveFuncOperationJob() throws Exception;

	
	int saveFuncOperation(Map<String,Object> map) throws Exception;
}
