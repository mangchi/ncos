package mil.ln.ncos.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.util.StringUtil;
import mil.ln.ncos.dao.DAO;

@Slf4j
@RequiredArgsConstructor
@Service
public class RptServiceImpl implements RptService {
	
	private final DAO dao;
	
	@Value("${spring.profiles.active}") 
	private String activeProfile;
	
	@Value("${cryptoMode}")
	private String cryptoMode;
	
	@Value("${crypto.key1}")
	private String cryptoModeKey1;

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
				List<Map<String,Object>> list1 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptDetectThreat", map);
				list1.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list2 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatAnalysis", map);
				list2.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list3 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatImportance", map);
				list3.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list4 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptPeriodThreat", map);
				list4.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list5 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatMain", map);
				list5.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list6 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAsset", map);
				list6.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list7 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetPerHour", map);
				list7.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list8 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetArea", map);
				list8.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list9 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetMain", map);
				list9.forEach(m -> {
					toDecList(m);
				});

				rtnMap.put("json1", list1);     //탐지위험발생현황
				rtnMap.put("json2", list2);    // 정오탐발생현황
				rtnMap.put("json3", list3);  //위협중요도발생현
				rtnMap.put("json4", list4);     //기간별 위협종류별발생현
				rtnMap.put("json5", list5);        //rawdata
				rtnMap.put("json6", list6);     //자산종류별현황
				rtnMap.put("json7", list7);    // 시간_자산종류별 발생
				rtnMap.put("json8", list8);     //구역별 발생
				rtnMap.put("json9", list9);        //rawdata
				rtnMap.put("json10", user);
			}
			else if(threatLevel > 0 && assetLevel == 0) {
				List<Map<String,Object>> list1 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptDetectThreat", map);
				list1.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list2 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatAnalysis", map);
				list2.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list3 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatImportance", map);
				list3.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list4 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptPeriodThreat", map);
				list4.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list5 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatMain", map);
				list5.forEach(m -> {
					toDecList(m);
				});

				rtnMap.put("json1", list1);     //탐지위험발생현황
				rtnMap.put("json2", list2);    // 정오탐발생현황
				rtnMap.put("json3", list3);  //위협중요도발생현
				rtnMap.put("json4", list4);     //기간별 위협종류별발생현
				rtnMap.put("json5", list5);        //rawdata
				rtnMap.put("json6", user);
			}
            else if(assetLevel > 0 && threatLevel == 0) {
            	List<Map<String,Object>> list1 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAsset", map);
            	list1.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list2 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetPerHour", map);
				list2.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list3 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetArea", map);
				list3.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list4 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetMain", map);
				list4.forEach(m -> {
					toDecList(m);
				});

            	rtnMap.put("json1", list1);     //자산종류별현황
				rtnMap.put("json2", list2);    // 시간_자산종류별 발생
				rtnMap.put("json3", list3);     //구역별 발생
				rtnMap.put("json4", list4);        //rawdata
				rtnMap.put("json5", user);
			}
            else {
            	throw new Exception("위협 및 자산 정보가 적절하지 않습니다.");
            }
			
			
		}
		else if(map.get("reportType").equals("2")) { //Summary
			
            if(threatLevel > 0 && assetLevel > 0){
            	List<Map<String,Object>> list1 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptDetectThreat", map);
    			list1.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list2 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatAnalysis", map);
    			list2.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list3 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatImportance", map);
    			list3.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list4 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptPeriodThreat", map);
    			list4.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list5 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatMain", map);
    			list5.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list6 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAsset", map);
    			list6.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list7 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetPerHour", map);
    			list7.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list8 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetArea", map);
    			list8.forEach(m -> {
    				toDecList(m);
    			});
    			List<Map<String,Object>> list9 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetMain", map);
    			list9.forEach(m -> {
    				toDecList(m);
    			});

            	rtnMap.put("json1", list1);     //탐지위험발생현황
				rtnMap.put("json2", list2);    // 정오탐발생현황
				rtnMap.put("json3", list3);  //위협중요도발생현
				rtnMap.put("json4", list4);     //기간별 위협종류별발생현
				rtnMap.put("json5", list5);        //rawdata
				rtnMap.put("json6", list6);     //자산종류별현황
				rtnMap.put("json7", list7);    // 시간_자산종류별 발생
				rtnMap.put("json8", list8);     //구역별 발생
				rtnMap.put("json9", list9);        //rawdata
				rtnMap.put("json10", user);
			}
			else if(threatLevel > 0 && assetLevel == 0) {
				List<Map<String,Object>> list1 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptDetectThreat", map);
				list1.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list2 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatAnalysis", map);
				list2.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list3 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatImportance", map);
				list3.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list4 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptPeriodThreat", map);
				list4.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list5 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptThreatMain", map);
				list5.forEach(m -> {
					toDecList(m);
				});

				rtnMap.put("json1", list1);     //탐지위험발생현황
				rtnMap.put("json2", list2);    // 정오탐발생현황
				rtnMap.put("json3", list3);  //위협중요도발생현
				rtnMap.put("json4", list4);     //기간별 위협종류별발생현
				rtnMap.put("json5", list5);        //rawdata
				rtnMap.put("json6", user);
			}
			else if(assetLevel > 0 && threatLevel == 0) {
				List<Map<String,Object>> list1 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAsset", map);
            	list1.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list2 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetPerHour", map);
				list2.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list3 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetArea", map);
				list3.forEach(m -> {
					toDecList(m);
				});
				List<Map<String,Object>> list4 = (List<Map<String,Object>>)dao.selectList("Rpt.selectRptAssetMain", map);
				list4.forEach(m -> {
					toDecList(m);
				});

				rtnMap.put("json1", list1);     //자산종류별현황
				rtnMap.put("json2", list2);    // 시간_자산종류별 발생
				rtnMap.put("json3", list3);     //구역별 발생
				rtnMap.put("json4", list4);        //rawdata
				rtnMap.put("json5", user);
			}
			else {
            	throw new Exception("위협 및 자산 정보가 적절하지 않습니다.");
            }
			
		}
		else {
			log.error("reportType is not valid");
		}
		Map<String,Object> afterMap = dao.selectMap("Rpt.selectRptAfter",map).orElseGet(() -> new HashMap<String, Object>()); 
		map.put("curTime", afterMap.get("curTime"));
		if(afterMap.containsKey("frTime")) {
			map.put("frTime", afterMap.get("frTime"));
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
	
	private void toDecList(Map<String, Object> m) {
		Map<String, Object> item = (Map<String, Object>) m;

		if (cryptoMode.equals("Y")) {
			if (item.containsKey("assetName")) {
				item.put("assetName", ScpDbUtil.scpDec(String.valueOf(item.get("assetName")), cryptoModeKey1));
			}
			if (item.containsKey("affiliation")) {
				item.put("affiliation", ScpDbUtil.scpDec(String.valueOf(item.get("affiliation")), cryptoModeKey1));
			}
			if (item.containsKey("userId")) {
				item.put("userId", ScpDbUtil.scpDec(String.valueOf(item.get("userId")), cryptoModeKey1));
			}
			if (item.containsKey("userName")) {
				item.put("userName", ScpDbUtil.scpDec(String.valueOf(item.get("userName")), cryptoModeKey1));
			}
			if (item.containsKey("username")) {
				item.put("username", ScpDbUtil.scpDec(String.valueOf(item.get("username")), cryptoModeKey1));
			}

			if (item.containsKey("srcIp") && item.get("srcIp") instanceof Boolean == false) {
				log.debug("rpt before srcIp:{}",item.get("srcIp"));
				String srcIp = ScpDbUtil.scpDec(String.valueOf(item.get("srcIp")), cryptoModeKey1);
				log.debug("rpt after srcIp:{}",srcIp);
				item.put("srcIp", StringUtil.longToIp(Long.parseLong(srcIp)));
	

			}
			if (item.containsKey("dstIp") && item.get("dstIp") instanceof Boolean == false) {
				log.debug("rpt before dstIp:{}",item.get("dstIp"));
				String dstIp = ScpDbUtil.scpDec(String.valueOf(item.get("dstIp")), cryptoModeKey1);
				log.debug("rpt after dstIp:{}",dstIp);
				item.put("dstIp", StringUtil.longToIp(Long.parseLong(dstIp)));
			}
			if (item.containsKey("ipAddress")) {
				item.put("ipAddress", StringUtil.longToIp(Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("ipAddress")), cryptoModeKey1))));

			}
			if (item.containsKey("ipaddress")) {
				log.debug("rpt before ipaddress:{}",item.get("ipaddress"));
				item.put("ipaddress", StringUtil.longToIp(Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("ipaddress")), cryptoModeKey1))));
				log.debug("rpt after ipaddress:{}",item.get("ipaddress"));

			}
		}
		else {
			if (item.containsKey("srcIp") && item.get("srcIp") instanceof Boolean == false) {
				item.put("srcIp", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("srcIp")))));
			}
			
			if (item.containsKey("dstIp") && item.get("dstIp") instanceof Boolean == false) {
				item.put("dstIp", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("dstIp")))));

			}
			if (item.containsKey("ipAddress")) {
				item.put("ipAddress", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("ipAddress")))));
		
			}
			if (item.containsKey("ipaddress")) {
				item.put("ipaddress", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("ipaddress")))));
		
			}
		}

	}
	

}
