package mil.ln.ncos.cmmn.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mil.ln.ncos.cmmn.vo.CodeVo;

public interface CmmnService {
	

	
	CodeVo saveCode(CodeVo vo);
	
    List<CodeVo> getSelectCodeList() ;
	
	List<CodeVo> getSelectCode(String grpCd);
	
	List<Map<String, Object>> getNetEquipList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getNetEquipStatusList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getShipStatusList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getLinkInfoList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getWhiteList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getManagerList(Map<String, Object> map) throws Exception;
	
	
	Map<String, Object> getWatchList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getZoneList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getNetworkEquipementStatus(Map<String, Object> map) throws Exception;
	
	
	Map<String, Object> getTransmitDataByJob(Map<String, Object> map) throws Exception;
	
	Map<String, Object> getSendTransmitDataByJob(Map<String, Object> map) throws Exception;
	
	Map<String, Object> getNavyTransmitDataByJob(Map<String, Object> map) throws Exception;
	
	int setNavyTransmitDataSetStartTimeByJob(Map<String, Object> map) throws Exception;
	
	int saveTransmitData(Map<String, Object> map) throws Exception;
	
	int saveCyberDefense(Map<String, Object> map) throws Exception;
	
	int saveWhiteList(Map<String, Object> map) throws Exception;
	
	Map<String, Object> getSatelliteTrans(Map<String, Object> map) throws Exception ;

	List<Map<String, Object>> getUnitInfos(Map<String, Object> map) throws Exception ;

	List<Map<String, Object>> getShipInfos(Map<String, Object> map) throws Exception ;

	Map<String, Object> getSatelliteTransByJob(Map<String, Object> map) throws Exception ;
	
	long getCscStatusByJob() throws Exception ;
	
	int deleteWhiteList(Map<String, Object> map) throws Exception;
	
	int updateWhitePolicyList(Map<String, Object> map) throws Exception ;
	
	int saveSatelliteTrans(Map<String, Object> map) throws Exception;
	
	int saveAlarmCheck(Map<String, Object> map) throws Exception;
	
	int saveLinkInfo(Map<String, Object> map) throws Exception;
	
	void setReportKey(Map<String, Object> map, HttpServletRequest request);
	
}
 