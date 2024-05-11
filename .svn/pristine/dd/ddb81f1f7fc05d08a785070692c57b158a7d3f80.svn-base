package mil.ln.ncos.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.dao.DAO;

@RequiredArgsConstructor
@Service
public class RptServiceImpl implements RptService {
	
	private final DAO dao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getRptMngList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectPage("Rpt.selectRptMngCount", "Rpt.selectRptMngList",map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getRptSchList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectPage("Rpt.selectRptSchCount", "Rpt.selectRptSchList",map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getRptFrmList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectPage("Rpt.selectRptFrmCount", "Rpt.selectRptFrmList",map);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getRptFrms(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectList("Rpt.selectRptFrms",map);
	}
	

	@Override
	public int saveRptSch(Map<String, Object> map) throws Exception {
		if(map.containsKey("scheduleId") && map.get("scheduleId") != null && !map.get("scheduleId").toString().trim().equals("")) {

			return dao.update("Rpt.updateRptSch", map);
		}
		return dao.update("Rpt.insertRptSch", map);
	}

	@Override
	public int saveRptFrm(Map<String, Object> map) throws Exception {
		if(map.containsKey("formId") && map.get("formId") != null && !map.get("formId").toString().trim().equals("")) {
			return dao.update("Rpt.updateRptFrm", map);
		}
		return dao.update("Rpt.insertRptFrm", map);
	
	}

	@Override
	public int deleteRptSch(Map<String, Object> map) throws Exception {
		return dao.update("Rpt.deleteRptSch", map);
	}

	@Override
	public int deleteRptFrm(Map<String, Object> map) throws Exception {
		return dao.update("Rpt.deleteRptFrm", map);
	}
	
	@Override
	public int saveRptMng(Map<String, Object> map) throws Exception {
		return dao.update("Rpt.updateRptMng", map);

	}

	@Override
	public int deleteRptMng(Map<String, Object> map) throws Exception {
		return dao.delete("Rpt.deleteRptMng", map);
	}
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getReportView(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = new HashMap();
		Map<String,Object> user = new HashMap();
		user.put("username", map.get("username"));
		int threatLevel = (map.get("threatLevel") == null || map.get("threatLevel").equals("")) ? 0:Integer.parseInt(String.valueOf(map.get("threatLevel")));
		int assetLevel = (map.get("assetLevel") == null || map.get("assetLevel").equals("")) ? 0:Integer.parseInt(String.valueOf(map.get("assetLevel")));
		if(map.get("reportType").equals("1")){  //Detail
			if(threatLevel > 0 && assetLevel > 0){
				rtnMap.put("json1", dao.selectList("Rpt.selectRptDetectThreat", map));     //탐지위험발생현황
				rtnMap.put("json2", dao.selectList("Rpt.selectRptThreatAnalysis", map));    // 정오탐발생현황
				rtnMap.put("json3", dao.selectList("Rpt.selectRptThreatImportance", map));  //위협중요도발생현
				rtnMap.put("json4", dao.selectList("Rpt.selectRptPeriodThreat", map));     //기간별 위협종류별발생현
				rtnMap.put("json5", dao.selectList("Rpt.selectRptThreatMain", map));        //rawdata
				rtnMap.put("json6", dao.selectList("Rpt.selectRptAsset", map));     //자산종류별현황
				rtnMap.put("json7", dao.selectList("Rpt.selectRptAssetPerHour", map));    // 시간_자산종류별 발생
				rtnMap.put("json8", dao.selectList("Rpt.selectRptAssetArea", map));     //구역별 발생
				rtnMap.put("json9", dao.selectList("Rpt.selectRptAssetMain", map));        //rawdata
				rtnMap.put("json10", user);
			}
			else if(threatLevel > 0 && assetLevel == 0) {
				rtnMap.put("json1", dao.selectList("Rpt.selectRptDetectThreat", map));     //탐지위험발생현황
				rtnMap.put("json2", dao.selectList("Rpt.selectRptThreatAnalysis", map));    // 정오탐발생현황
				rtnMap.put("json3", dao.selectList("Rpt.selectRptThreatImportance", map));  //위협중요도발생현
				rtnMap.put("json4", dao.selectList("Rpt.selectRptPeriodThreat", map));     //기간별 위협종류별발생현
				rtnMap.put("json5", dao.selectList("Rpt.selectRptThreatMain", map));        //rawdata
				rtnMap.put("json6", user);
			}
            else if(assetLevel > 0 && threatLevel == 0) {
            	rtnMap.put("json1", dao.selectList("Rpt.selectRptAsset", map));     //자산종류별현황
				rtnMap.put("json2", dao.selectList("Rpt.selectRptAssetPerHour", map));    // 시간_자산종류별 발생
				rtnMap.put("json3", dao.selectList("Rpt.selectRptAssetArea", map));     //구역별 발생
				rtnMap.put("json4", dao.selectList("Rpt.selectRptAssetMain", map));        //rawdata
				rtnMap.put("json5", user);
			}
            else {
            	throw new Exception("위협 및 자산 정보가 적절하지 않습니다.");
            }
			
			
		}
		else if(map.get("reportType").equals("2")) { //Summary
            if(threatLevel > 0 && assetLevel > 0){
            	rtnMap.put("json1", dao.selectList("Rpt.selectRptDetectThreat", map));     //탐지위험발생현황
				rtnMap.put("json2", dao.selectList("Rpt.selectRptThreatAnalysis", map));    // 정오탐발생현황
				rtnMap.put("json3", dao.selectList("Rpt.selectRptThreatImportance", map));  //위협중요도발생현
				rtnMap.put("json4", dao.selectList("Rpt.selectRptPeriodThreat", map));     //기간별 위협종류별발생현
				rtnMap.put("json5", dao.selectList("Rpt.selectRptThreatMain", map));        //rawdata
				rtnMap.put("json6", dao.selectList("Rpt.selectRptAsset", map));     //자산종류별현황
				rtnMap.put("json7", dao.selectList("Rpt.selectRptAssetPerHour", map));    // 시간_자산종류별 발생
				rtnMap.put("json8", dao.selectList("Rpt.selectRptAssetArea", map));     //구역별 발생
				rtnMap.put("json9", dao.selectList("Rpt.selectRptAssetMain", map));        //rawdata
				rtnMap.put("json10", user);
			}
			else if(threatLevel > 0 && assetLevel == 0) {
				rtnMap.put("json1", dao.selectList("Rpt.selectRptDetectThreat", map));     //탐지위험발생현황
				rtnMap.put("json2", dao.selectList("Rpt.selectRptThreatAnalysis", map));    // 정오탐발생현황
				rtnMap.put("json3", dao.selectList("Rpt.selectRptThreatImportance", map));  //위협중요도발생현
				rtnMap.put("json4", dao.selectList("Rpt.selectRptPeriodThreat", map));     //기간별 위협종류별발생현
				rtnMap.put("json5", dao.selectList("Rpt.selectRptThreatMain", map));        //rawdata
				rtnMap.put("json6", user);
			}
			else if(assetLevel > 0 && threatLevel == 0) {
				rtnMap.put("json1", dao.selectList("Rpt.selectRptAsset", map));     //자산종류별현황
				rtnMap.put("json2", dao.selectList("Rpt.selectRptAssetPerHour", map));    // 시간_자산종류별 발생
				rtnMap.put("json3", dao.selectList("Rpt.selectRptAssetArea", map));     //구역별 발생
				rtnMap.put("json4", dao.selectList("Rpt.selectRptAssetMain", map));        //rawdata
				rtnMap.put("json5", user);
			}
			else {
            	throw new Exception("위협 및 자산 정보가 적절하지 않습니다.");
            }
			
		}
		else {
			
		}
		
		return rtnMap;
	}

	@Override
	public int updateClickCount(Map<String, Object> map) throws Exception {
		int rtn = 0;
		if(SessionData.getUserVo().getAccountId().equals(map.get("accountId"))){
			rtn += dao.update("Rpt.updatePrintYn", map);
		}
		rtn += dao.update("Rpt.updateClickCount", map);
		return rtn;
	}
	

}
