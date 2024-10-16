package mil.ln.ncos.asset.service;

import java.util.List;
import java.util.Map;

public interface AssetService {
	
	List<Map<String,Object>> getAssetList(Map<String,Object> param) throws Exception;
	
	List<Map<String,Object>> getAssetDispoList(Map<String,Object> param) throws Exception;
	
	Map<String,Object> getAssetStatus(Map<String,Object> param) throws Exception;
	
	List<Map<String,Object>> getAssetAffair(Map<String,Object> param) throws Exception;
	
	List<Map<String,Object>> getZoneLocationList(Map<String,Object> param) throws Exception;
	
	List<Map<String,Object>> getAssetDispoZoneList(Map<String,Object> param) throws Exception;
	
	List<Map<String,Object>> getAssetDispoZoneGroup(Map<String,Object> param) throws Exception;
	
	long getSystemIdCount(Map<String,Object> param) throws Exception;
	
	int saveAsset(Map<String,Object> param) throws Exception;
	
	int saveZoneLocation(Map<String,Object> param) throws Exception;
	
	int deleteAsset(Map<String,Object> param) throws Exception;

}
