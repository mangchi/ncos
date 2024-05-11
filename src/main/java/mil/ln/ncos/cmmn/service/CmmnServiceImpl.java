package mil.ln.ncos.cmmn.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.clipsoft.clipreport.oof.OOFDocument;
import com.clipsoft.clipreport.oof.OOFFile;
import com.clipsoft.clipreport.oof.connection.OOFConnectionMemo;
import com.clipsoft.clipreport.server.service.ReportUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.compiler.ast.StringL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.error.ErrorCode;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.util.StringUtil;
import mil.ln.ncos.cmmn.vo.CodeVo;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.exception.BizException;
import mil.ln.ncos.user.vo.UserVo;

@Slf4j
@RequiredArgsConstructor
@Service
public class CmmnServiceImpl implements CmmnService{
    
	private final DAO dao;
	private final CacheService cacheService;
	@Value("${code.codeKey}")
    private String codeKey;
	
	@Value("${spring.profiles.active}") 
	private String activeProfile;
	
	@Value("${cryptoMode}")
	private String cryptoMode;
	@Value("${crypto.key1}")
	private String cryptoModeKey1;


	@Override
	@CacheEvict(value = "codeCacheData", allEntries = true)
	public CodeVo saveCode(CodeVo vo) {
		dao.update("Cmmn.mergeCode",vo);
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CodeVo> getSelectCodeList() {
		List<CodeVo> list = (List<CodeVo>)dao.selectList("Cmmn.selectCode",null);
		if(cryptoMode.equals("Y")) {
			for(CodeVo vo : list) {
				if(vo.getGrpCd().equals("UA")) {
					vo.setCdNm(ScpDbUtil.scpDec(vo.getCdNm(), cryptoModeKey1));
				}
				
			}
		}
		
		return list;
	}

	@Override
	public List<CodeVo> getSelectCode(String grpCd) {
		List<CodeVo> codeList = cacheService.getCodeCacheData(codeKey);
		if(grpCd == null || grpCd.equals("")) {
			return codeList;
		}
		return codeList.stream().filter(c -> grpCd.equals(c.getGrpCd())).collect(Collectors.toList());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> getNetEquipList(Map<String, Object> map) throws Exception {

		List<Map<String,Object>> list = (List<Map<String,Object>>)dao.selectList("Cmmn.selectEquipList", map);
		if(list == null || list.size() == 0) {
			throw new BizException("장비정보가 없습니다.", ErrorCode.UNDEFINED_ERROR);
		}
		
		List<Map<String,Object>> toList = new ArrayList();
		for(int i=list.size()-1;i>=0;i--) {
			if(!list.get(i).containsKey("upperId") || list.get(i).get("upperId").equals("") || list.get(i).get("upperId") == null) {
				toList.add(list.get(i));
				list.remove(i);
			}	
		}
		toList = toJsonMap(list,toList);
		return toList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> getLinkInfoList(Map<String, Object> map) throws Exception {

		List<Map<String,Object>> list = (List<Map<String,Object>>)dao.selectList("Cmmn.selectLinkInfoList", map);
		if(list == null || list.size() == 0) {
			return list;
		}
		

	    Map<String,Object> lastItm = list.get(list.size()-1);
	    int lastId = Integer.parseInt(String.valueOf(lastItm.get("upperId")));
		
	 
		if(cryptoMode.equals("Y")) {
			for(Map<String,Object> m: list) {
				String classNm = String.valueOf(m.get("classNm"));
				if(m.containsKey("codeKr")) {
					log.debug("classNm:{}",classNm);
					classNm = ScpDbUtil.scpDec(classNm, cryptoModeKey1);
					classNm = classNm +"("+m.get("codeKr")+")";
					m.put("classNm", classNm);
				}
				
			}
		}
		
	    Map<String,Object> lastMap = new HashMap();
	    lastMap.put("id", lastId);
	    lastMap.put("upperId", "");
	    lastMap.put("assetId", "0");
	    lastMap.put("classNm", "장비연결");
	    lastMap.put("linkType", "");
	    lastMap.put("codeKr", "");
	    lastMap.put("upperAsset", "0");
	    list.add(lastMap);

		
		List<Map<String,Object>> toList = new ArrayList();
		for(int i=list.size()-1;i>=0;i--) {
			Map<String,Object> itm = list.get(i);
			if(!list.get(i).containsKey("upperId") || list.get(i).get("upperId").equals("") || list.get(i).get("upperId") == null ) {
				toList.add(list.get(i));
				list.remove(i);
			}	
		}
		toList = toJsonMap(list,toList);
		log.debug("toList:{}",toList);
		return toList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int saveLinkInfo(Map<String, Object> map) throws Exception {
		int rtn = 0;
		rtn += dao.delete("Cmmn.deleteLinkInfo",map);
		List<Map<String,Object>> parseList = (List<Map<String,Object>>)map.get("treeData");
		//List<Map<String,Object>> list = this.parseList(parseList,"");
		List<Map<String,Object>> list = this.parseList(parseList,"0");
		for(Map<String,Object> m:list) {
			log.debug("m:{}",m);
			rtn += dao.update("Cmmn.insertLinkInfo",m);
		}
		return rtn;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private  List<Map<String,Object>> parseList(List<Map<String,Object>> list,String upperId){
		List<Map<String,Object>> rtnList = new ArrayList();
		int i=0;
		for(Map<String,Object> map:list) {
			i += 1;
			if(upperId.equals("")) {
				map.put("id", ""+i);
			}
			else {
				map.put("id", upperId+i);
			}
			map.put("upperId", upperId);		
			map.put("assetId", map.get("assetId"));
			map.put("linkType", "");
			if(map.containsKey("children")) {
				List<Map<String,Object>> childList = (List<Map<String,Object>>)map.get("children");
				rtnList.addAll(parseList(childList,(String)map.get("id")));
			}
			rtnList.add(map);
		}
		return rtnList;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String,Object>> toJsonMap(List<Map<String,Object>> fromList,List<Map<String,Object>> toList){
		List<Map<String,Object>> childList = null;
			for(Map<String,Object> toMap:toList) {
			childList = new ArrayList();
			for(int i=fromList.size()-1;i>=0;i--) {
				if(toMap.get("id").equals(fromList.get(i).get("upperId"))) {
					childList.add(fromList.get(i));
					fromList.remove(i);
				}					
			}
			if(childList.size() > 0) {
				toMap.put("children", childList);
			}
			
			if(fromList.size() > 0) {
				toJsonMap(fromList,childList);
			}
		}
		return toList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getWhiteList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectList("Cmmn.selectWhiteList",map);
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getWatchList(Map<String, Object> map) throws Exception {
		Map<String,Object> watchInfo = new HashMap();
		UserVo user = SessionData.getUserVo();
		Map<String,Object> acountSetting = (Map<String, Object>)dao.selectOptionalObject("Cmmn.selectAccountSetting",user).orElseGet(() -> null);
		log.debug("acountSetting:{}",acountSetting);
		if(null != acountSetting) {
			if(null != acountSetting.get("alarmStatus")) {
				log.debug("alarmStatus:{}",acountSetting.get("alarmStatus"));
				int alarmStatus = 0;
				if(acountSetting.get("alarmStatus") instanceof Boolean) {					
					if((boolean)acountSetting.get("alarmStatus")) {
						alarmStatus = 1;
				    }
				}
				else {
					alarmStatus = Integer.parseInt(String.valueOf(acountSetting.get("alarmStatus")));
				}
				if(alarmStatus == 1) {
					map.put("alarmLevel", acountSetting.get("alarmLevel"));
					map.put("warningLevel", acountSetting.get("warningLevel"));
					map.put("alarmSch","Y");
					map.put("warningSch","N");
					if(activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1) {
						List<Map<String,Object>> assetList = (List<Map<String,Object>>)dao.selectList("Cmmn.selectWatchAssetList",map);
						watchInfo.put("alarmAssetCount",(assetList == null)?0:assetList.size());
					}
					//log.debug("pram:{}",map);
					List<Map<String,Object>> threatList = (List<Map<String,Object>>)dao.selectList("Cmmn.selectWatchThreatList",map);
					watchInfo.put("alarmThreatCount",(threatList == null)?0:threatList.size());
				}
			    
			}
			if(null != acountSetting.get("warningStatus")) {
				log.debug("warningStatus:{}",acountSetting.get("warningStatus"));
				int warningStatus = 0;
				if(acountSetting.get("warningStatus") instanceof Boolean) {
					if((boolean)acountSetting.get("warningStatus")) {
						warningStatus = 1;
				    }
				}
				else {
					warningStatus = Integer.parseInt(String.valueOf(acountSetting.get("warningStatus")));
				}
				if(warningStatus == 1) {
					map.put("alarmLevel", acountSetting.get("alarmLevel"));
					map.put("warningLevel", acountSetting.get("warningLevel"));
					map.put("alarmSch","N");
					map.put("warningSch","Y");
					if(activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1) {
						watchInfo.put("warningAssetList",dao.selectList("Cmmn.selectWatchAssetList",map));
					}
					watchInfo.put("warningThreatList",dao.selectList("Cmmn.selectWatchThreatList",map));
				}
			}
			
		}

		watchInfo.put("reportList",dao.selectList("Cmmn.selectWatchReportList",map));
        return watchInfo;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getNetworkEquipementStatus(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectList("Cmmn.selectNetworkEquipmentStatus",map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSatelliteTrans(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)dao.selectOptionalObject("Cmmn.selectSatelliteTrans",map).orElseGet(() -> null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSatelliteTransByJob(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)dao.selectOptionalObjectByJob("Cmmn.selectSatelliteTrans",map).orElseGet(() -> null);
	}


	@Override
	public int saveAlarmCheck(Map<String, Object> map) throws Exception {
		return dao.update("Cmmn.insertAlarmCheck", map);
	}

	@Override
	public int saveWhiteList(Map<String, Object> map) throws Exception {
		
		if(cryptoMode.equals("N")) {
			if(map.containsKey("policyId") && map.get("policyId") != null && !map.get("policyId").toString().trim().equals("")) {
				return dao.update("Cmmn.updateWhiteList", map);
			}else {
				return dao.update("Cmmn.insertWhiteList", map);
			}
		}else{//ncos 무결성검증.
			int rtn =  0;
			Map<String, Object> salt = dao.selectMapByJob("Cmmn.selectIntegritySalt", null).orElseGet(() -> new HashMap<String, Object>());
			map.put("srcIpEnc", map.get("srcIp"));
			map.put("dstIpEnc", map.get("dstIp"));

			map.put("srcIp", ScpDbUtil.scpDec(""+map.get("srcIp"),cryptoModeKey1));
			map.put("dstIp", ScpDbUtil.scpDec(""+map.get("dstIp"),cryptoModeKey1));
			
			final String saltKey="whitelistPolicy";
			
			if(map.containsKey("policyId") && map.get("policyId") != null && !map.get("policyId").toString().trim().equals("")) {
				rtn = dao.update("Cmmn.updateWhiteListNcos", map);
			}else {
				map.put("integrity", "temp");
				rtn = dao.update("Cmmn.insertWhiteListNcos", map);
			}
			String integrityKey = String.valueOf(map.get("policyId"))
								+ String.valueOf(map.get("policyName"))
								+ String.valueOf(map.get("srcIp"))
								+ String.valueOf(map.get("srcMask"))
								+ String.valueOf(map.get("dstIp"))
								+ String.valueOf(map.get("dstMask"))
								+ String.valueOf(map.get("srcStartPort"))
								+ String.valueOf(map.get("srcEndPort"))
								+ String.valueOf(map.get("dstStartPort"))
								+ String.valueOf(map.get("dstEndPort"))
								+ String.valueOf(map.get("protocol"))
								+ String.valueOf(map.get("policy"));
			integrityKey = integrityKey +ScpDbUtil.scpDec((String)salt.get(saltKey),cryptoModeKey1);
		    log.debug("saveWhiteList integrityKey:{}",integrityKey);
		    String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
		    map.put("integrity", integrity);
		    rtn = dao.update("Cmmn.updateWhiteListIntegrity", map);
			return rtn;
		}
	}

	@Override
	public int saveCyberDefense(Map<String, Object> map) throws Exception {
		return dao.update("Cmmn.updateCyberDefense", map);
	}

	@Override
	public int deleteWhiteList(Map<String, Object> map) throws Exception {
		return dao.delete("Cmmn.deleteWhiteList", map);
	}
	
	@Override
	public int updateWhitePolicyList(Map<String, Object> map) throws Exception {
		return dao.update("Cmmn.updateWhitePolicyList", map);
	}

	@Override
	public int saveSatelliteTrans(Map<String, Object> map) throws Exception {
		int threatImportanceGrade = 0;
		if(map.get("threatImportanceGradeH").toString().trim().equals("1")) {
			threatImportanceGrade += 5;
		}
	    if(map.get("threatImportanceGradeM").toString().trim().equals("1")) {
	    	threatImportanceGrade += 3;
		}
	    if(map.get("threatImportanceGradeL").toString().trim().equals("1")) {
	    	threatImportanceGrade += 1;
		}
	    map.put("threatImportanceGrade", threatImportanceGrade);
	    
	    Timestamp ts = new Timestamp(System.currentTimeMillis());
		Date date = new Date(ts.getTime());
		String settingTime = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm.ss");

		if(cryptoMode.equals("N")) {
			if(map.containsKey("settingId") && map.get("settingId") != null && !map.get("settingId").toString().trim().equals("")) {
				return dao.update("Cmmn.updateSatelliteTrans", map);
			}else {
				return dao.update("Cmmn.insertSatelliteTrans", map);
			}
		}else{ 
			int rtn = 0;
			Map<String, Object> salt = dao.selectMapByJob("Cmmn.selectIntegritySalt", null).orElseGet(() -> new HashMap<String, Object>());
			log.debug("slat:{}",salt);
			log.debug("map:{}",map);
			final String saltKey="satelliteTrans";
			
			map.put("settingTime", settingTime);
			
			
			if(map.containsKey("settingId") && map.get("settingId") != null && !map.get("settingId").toString().trim().equals("")) {
				rtn = dao.update("Cmmn.updateSatelliteTransNcos", map);
			}else {
				map.put("integrity", "temp");
				rtn = dao.update("Cmmn.insertSatelliteTransNcos", map);
			}
			String integrityKey = "";
			try {
				integrityKey = String.valueOf(map.get("settingId"))
						+settingTime.substring(0,10)
						+String.valueOf(map.get("setter"))
						+String.valueOf(map.get("transSpeed"))
						+StringUtil.getBoolean(String.valueOf(map.get("systemOpStatus")))
						+String.valueOf(map.get("unitId"))
						+String.valueOf(map.get("shipId"))
						+StringUtil.getBoolean(String.valueOf(map.get("assetStatus")))
						+String.valueOf(map.get("threatImportanceGrade"))
						+StringUtil.getBoolean(String.valueOf(map.get("detectionTime")))
						+StringUtil.getBoolean(String.valueOf(map.get("srcIp")))
						+StringUtil.getBoolean(String.valueOf(map.get("dstIp")))
						+StringUtil.getBoolean(String.valueOf(map.get("srcPort")))
						+StringUtil.getBoolean(String.valueOf(map.get("dstPort")))
						+StringUtil.getBoolean(String.valueOf(map.get("protocol")))
						+StringUtil.getBoolean(String.valueOf(map.get("payloadSize")))
						+StringUtil.getBoolean(String.valueOf(map.get("fragmentation")))
						+StringUtil.getBoolean(String.valueOf(map.get("fragmentId")))
						+StringUtil.getBoolean(String.valueOf(map.get("detectionThreatName")))
						+StringUtil.getBoolean(String.valueOf(map.get("threatDetectionMethod")))
						+StringUtil.getBoolean(String.valueOf(map.get("threatImportance")));
			}catch(Exception e) {
				e.printStackTrace();
			}

			log.debug("integrityKey:{}",integrityKey);
			String decSalgtKey = "";
			log.debug("saltKey:{}",salt.get(saltKey));
			decSalgtKey = ScpDbUtil.scpDec((String)salt.get(saltKey),cryptoModeKey1);
			log.debug("decSalgtKey:{}",decSalgtKey);
			integrityKey = integrityKey +decSalgtKey;
			log.debug("saveSatelliteTrans integrityKey:{}",integrityKey);
			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
			map.put("integrity", integrity);
			log.debug("saveSatelliteTrans integrity:::::{}",integrity);
			rtn = dao.update("Cmmn.updateSatelliteTransIntegrity", map);
			return rtn;
		
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getZoneList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectList("Cmmn.selectZoneList", map);
	}

	@Override
	public long getCscStatusByJob() throws Exception {
		return dao.selectCountByJob("Cmmn.selectCscStatus",null);
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getTransmitDataByJob(Map<String, Object> map) throws Exception {
		map.put("threatImportanceGrade",dao.selectOneByJob("Cmmn.selectThreatImportanceGrade",map));
		return (Map<String, Object>)dao.selectOptionalObjectByJob("Cmmn.selectTransmitData",map).orElseGet(() -> null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSendTransmitDataByJob(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)dao.selectOptionalObjectByJob("Cmmn.selectSendTransmitData",map).orElseGet(() -> null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNavyTransmitDataByJob(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)dao.selectOptionalObjectByJob("Cmmn.selectNavyTransmitData",map).orElseGet(() -> null);
	}
	
	@Override
	public int saveTransmitData(Map<String, Object> map) throws Exception {
		if(map.containsKey("dataId") && map.get("dataId") != null && !map.get("dataId").toString().trim().equals("")) {
			return dao.updateByJob("Cmmn.updateTransmitData", map);
		}
		return dao.updateByJob("Cmmn.insertTransmitData", map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setReportKey(Map<String, Object> map, HttpServletRequest request) {
		log.debug("setReportKey map:{}",map);
		OOFDocument oof = null;
	  	oof = OOFDocument.newOOF();
	    String propertyPath  = request.getSession().getServletContext().getRealPath("/") +  "WEB-INF" + File.separator + "clipreport5" + File.separator + "clipreport5.properties";
	    try {
	    	OOFFile oofFile = oof.addFile("crf.root", "%root%/crf/" + map.get("fileNm") + ".crf");
	    	Map<String,Object> param =  (Map<String,Object>)map.get("param");
	    	Map<String,Object> rtnMap =  (Map<String,Object>)map.get("rtn");
	    	
	    	oof.addField("PERIOD_ID", String.valueOf(param.get("periodId")));
	    	if(param.get("frDate") == null) {
	    		oof.addField("FR_TIME", String.valueOf(param.get("frTime")));
	    		oof.addField("TO_TIME", String.valueOf(param.get("toDate")));
	    		oof.addField("CREATE_TIME", String.valueOf(param.get("toDate")));
	    		
	    	}
	    	else {
	    		oof.addField("FR_TIME", String.valueOf(param.get("frDate")));
	    		oof.addField("TO_TIME", String.valueOf(param.get("toDate")));
	    		oof.addField("CREATE_TIME", String.valueOf(param.get("curTime")));
	    	}
	    	
	    	oof.addField("ACCOUNT_ID", String.valueOf(param.get("accountId")));
	    	oof.addField("THREAT_LEVEL", String.valueOf(param.get("threatLevel")));
			oof.addField("ASSET_LEVEL", String.valueOf(param.get("assetLevel")));
	    	
	    	
	    	ObjectMapper mapper = new ObjectMapper();
    		String jsonStr = mapper.writeValueAsString(rtnMap);
		    log.debug("jsonStr:{}",jsonStr);
			OOFConnectionMemo memo = oofFile.addConnectionMemo("*", jsonStr);
			memo.addContentParamJSON("*", "utf-8", "{%dataset.json.root%}");
		    
	    }
	  	catch(Exception e) {
	  		e.printStackTrace();
	  		log.error(e.getMessage());
	  	}
	    
     	
		String reportKey =  ReportUtil.createReport(request, oof, "false", "false", request.getRemoteAddr(), propertyPath);
		map.put("reportKey", reportKey);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> getNetEquipStatusList(Map<String, Object> map) throws Exception {
		List<Map<String,Object>> list = (List<Map<String,Object>>)dao.selectList("Cmmn.selectEquipStatusList", map);
		if(list == null || list.size() == 0) {
			throw new BizException("장비정보가 없습니다.", ErrorCode.UNDEFINED_ERROR);
		}
		
		if(cryptoMode.equals("Y")) {
			for(Map<String,Object> m: list) {
				String classNm = String.valueOf(m.get("classNm"));
				if(m.containsKey("upperId")) {
					classNm = ScpDbUtil.scpDec(classNm, cryptoModeKey1);
					m.put("classNm", classNm);
				}
				
			}
		}
		
		List<Map<String,Object>> toList = new ArrayList();
		for(int i=list.size()-1;i>=0;i--) {
			if(!list.get(i).containsKey("upperId") || list.get(i).get("upperId").equals("") || list.get(i).get("upperId") == null) {
				toList.add(list.get(i));
				list.remove(i);
			}	
		}
		toList = toJsonMap(list,toList);
		return toList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getShipStatusList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectList("Cmmn.selectShipStatusList", map);
	}

	@Override 
	public int setNavyTransmitDataSetStartTimeByJob(Map<String, Object> map) throws Exception {
		return dao.updateByJob("Cmmn.updateNavyTransmitDataSetStartTime", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getManagerList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectList("Cmmn.selectManagerList", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getUnitInfos(Map<String, Object> map) throws Exception {
		if(activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
			map.put("systemId", "hmm");
		}
		List<Map<String,Object>> list = (List<Map<String,Object>>)dao.selectList("Cmmn.selectUnitInfos", map);
		if(cryptoMode.equals("Y")) {
			for(Map<String,Object> m: list) {
				String cdNm = String.valueOf(m.get("cdNm"));
				cdNm = ScpDbUtil.scpDec(cdNm, cryptoModeKey1);
				m.put("cdNm", cdNm);
			}
			
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getShipInfos(Map<String, Object> map) throws Exception {
		if(activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
			map.put("systemId", "hmm");
		}
		List<Map<String,Object>> list = (List<Map<String,Object>>)dao.selectList("Cmmn.selectShipInfos", map);
		if(cryptoMode.equals("Y")) {
			for(Map<String,Object> m: list) {
				String cdNm = String.valueOf(m.get("cdNm"));
				cdNm = ScpDbUtil.scpDec(cdNm, cryptoModeKey1);
				m.put("cdNm", cdNm);
			}
			
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getUserCodeList(Map<String, Object> map) throws Exception {
		List<Map<String,Object>> list = (List<Map<String,Object>>)dao.selectList("Cmmn.selectUserCodeList", map);
		if(cryptoMode.equals("Y")) {
			for(Map<String,Object> m: list) {
				String cdNm = String.valueOf(m.get("cdNm"));
				cdNm = ScpDbUtil.scpDec(cdNm, cryptoModeKey1);
				m.put("cdNm", cdNm);
			}
			
		}
		return list;
		
	}

	@Override
	public int saveAggregationStandard(Map<String, Object> map) throws Exception {
		if(null == dao.selectOne("Cmmn.selectAggregationStandard")) {
			return dao.update("Cmmn.insertAggregationStandard", map);
		}else {
			return dao.update("Cmmn.updateAggregationStandard", map);
		}
		
	}

	@Override
	public int selectAggregationStandard() throws Exception {
		return Integer.parseInt(String.valueOf(Optional.ofNullable(dao.selectOne("Cmmn.selectAggregationStandard")).orElseGet(() -> "0")));
	}


}