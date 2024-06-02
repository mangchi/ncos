package mil.ln.ncos.cmmn.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.error.ErrorCode;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.vo.CodeVo;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.exception.BizException;
import mil.ln.ncos.user.vo.UserVo;

@Slf4j
@RequiredArgsConstructor
@Service
public class CmmnServiceImpl implements CmmnService {

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
	@Value("${crypto.key1}")
	private String cryptoModeKey2;

	@Override
	@CacheEvict(value = "codeCacheData", allEntries = true)
	public CodeVo saveCode(CodeVo vo) {
		dao.update("Cmmn.mergeCode", vo);
		return vo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodeVo> getSelectCodeList() {
		List<CodeVo> list = (List<CodeVo>) dao.selectList("Cmmn.selectCode", null);
		if (cryptoMode.equals("Y")) {
			for (CodeVo vo : list) {
				if (vo.getGrpCd().equals("UA")) {
					if (activeProfile.equals("navy")) {
						vo.setCdNm(ScpDbUtil.scpDec(vo.getCdNm(), cryptoModeKey1));
					} else {
						vo.setCdNm(ScpDbUtil.scpDec(vo.getCdNm(), cryptoModeKey2));
					}
				}

			}
		}

		return list;
	}

	@Override
	public List<CodeVo> getSelectCode(String grpCd) {
		List<CodeVo> codeList = cacheService.getCodeCacheData(codeKey);
		if (grpCd == null || grpCd.equals("")) {
			return codeList;
		}
		return codeList.stream().filter(c -> grpCd.equals(c.getGrpCd())).collect(Collectors.toList());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> getNetEquipList(Map<String, Object> map) throws Exception {

		List<Map<String, Object>> list = (List<Map<String, Object>>) dao.selectList("Cmmn.selectEquipList", map);
		if (list == null || list.size() == 0) {
			throw new BizException("장비정보가 없습니다.", ErrorCode.UNDEFINED_ERROR);
		}

		List<Map<String, Object>> toList = new ArrayList();
		for (int i = list.size() - 1; i >= 0; i--) {
			if (!list.get(i).containsKey("upperId") || list.get(i).get("upperId").equals("")
					|| list.get(i).get("upperId") == null) {
				toList.add(list.get(i));
				list.remove(i);
			}
		}
		toList = toJsonMap(list, toList);
		return toList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> getLinkInfoList(Map<String, Object> map) throws Exception {

		List<Map<String, Object>> list = (List<Map<String, Object>>) dao.selectList("Cmmn.selectLinkInfoList", map);
		if (list == null || list.size() == 0) {
			return list;
			// throw new Exception("장비연결정보가 없습니다.");
		}
		if (cryptoMode.equals("Y")) {
			for (Map<String, Object> m : list) {
				String classNm = String.valueOf(m.get("classNm"));
				if (m.containsKey("codeKr")) {
					log.debug("classNm:{}", classNm);
					if (activeProfile.equals("navy")) {
						classNm = ScpDbUtil.scpDec(classNm, cryptoModeKey1);
					} else {
						classNm = ScpDbUtil.scpDec(classNm, cryptoModeKey2);
					}

					classNm = classNm + "(" + m.get("codeKr") + ")";
					m.put("classNm", classNm);
				}

			}
		}

		List<Map<String, Object>> toList = new ArrayList();
		for (int i = list.size() - 1; i >= 0; i--) {
			if (!list.get(i).containsKey("upperId") || list.get(i).get("upperId").equals("")
					|| list.get(i).get("upperId") == null) {
				toList.add(list.get(i));
				list.remove(i);
			}
		}
		toList = toJsonMap(list, toList);
		return toList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveLinkInfo(Map<String, Object> map) throws Exception {
		int rtn = 0;
		rtn += dao.delete("Cmmn.deleteLinkInfo", map);
		List<Map<String, Object>> parseList = (List<Map<String, Object>>) map.get("treeData");
		// List<Map<String,Object>> list = this.parseList(parseList,"");
		List<Map<String, Object>> list = this.parseList(parseList, "0");
		for (Map<String, Object> m : list) {
			log.debug("m:{}", m);
			rtn += dao.update("Cmmn.insertLinkInfo", m);
		}
		return rtn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String, Object>> parseList(List<Map<String, Object>> list, String upperId) {
		List<Map<String, Object>> rtnList = new ArrayList();
		int i = 0;
		for (Map<String, Object> map : list) {
			i += 1;
			if (upperId.equals("")) {
				map.put("id", "" + i);
			} else {
				map.put("id", upperId + i);
			}
			map.put("upperId", upperId);
			map.put("assetId", map.get("assetId"));
			map.put("linkType", "");
			if (map.containsKey("children")) {
				List<Map<String, Object>> childList = (List<Map<String, Object>>) map.get("children");
				rtnList.addAll(parseList(childList, (String) map.get("id")));
			}
			rtnList.add(map);
		}
		return rtnList;
	}

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * private List<Map<String,Object>> parseList(List<Map<String,Object>>
	 * list,String upperId){
	 * List<Map<String,Object>> rtnList = new ArrayList();
	 * 
	 * for(Map<String,Object> map:list) {
	 * map.put("id", map.get("id"));
	 * map.put("upperId", upperId);
	 * map.put("linkType", "");
	 * if(map.containsKey("children")) {
	 * List<Map<String,Object>> childList =
	 * (List<Map<String,Object>>)map.get("children");
	 * rtnList.addAll(parseList(childList,(String)map.get("id")));
	 * }
	 * rtnList.add(map);
	 * }
	 * return rtnList;
	 * }
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String, Object>> toJsonMap(List<Map<String, Object>> fromList, List<Map<String, Object>> toList) {
		List<Map<String, Object>> childList = null;
		for (Map<String, Object> toMap : toList) {
			childList = new ArrayList();
			for (int i = fromList.size() - 1; i >= 0; i--) {
				if (toMap.get("id").equals(fromList.get(i).get("upperId"))) {
					childList.add(fromList.get(i));
					fromList.remove(i);
				}
			}
			if (childList.size() > 0) {
				toMap.put("children", childList);
			}

			if (fromList.size() > 0) {
				toJsonMap(fromList, childList);
			}
		}
		return toList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getWhiteList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Cmmn.selectWhiteList", map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getWatchList(Map<String, Object> map) throws Exception {
		Map<String, Object> watchInfo = new HashMap();
		UserVo user = SessionData.getUserVo();
		log.debug("user:{}", user);
		if (user.getAlarmStatus() == 1) {
			map.put("alarmLevel", user.getAlarmLevel());
			map.put("warningLevel", user.getWarningLevel());
			map.put("alarmSch", "Y");
			map.put("warningSch", "N");
			if (activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1) {
				List<Map<String, Object>> assetList = (List<Map<String, Object>>) dao
						.selectList("Cmmn.selectWatchAssetList", map);
				watchInfo.put("alarmAssetCount", (assetList == null) ? 0 : assetList.size());
			}
			log.debug("pram:{}", map);
			List<Map<String, Object>> threatList = (List<Map<String, Object>>) dao
					.selectList("Cmmn.selectWatchThreatList", map);
			watchInfo.put("alarmThreatCount", (threatList == null) ? 0 : threatList.size());

		}
		if (user.getWarningStatus() == 1) {
			map.put("alarmLevel", user.getAlarmLevel());
			map.put("warningLevel", user.getWarningLevel());
			map.put("alarmSch", "N");
			map.put("warningSch", "Y");
			if (activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1) {
				watchInfo.put("warningAssetList", dao.selectList("Cmmn.selectWatchAssetList", map));
			}
			watchInfo.put("warningThreatList", dao.selectList("Cmmn.selectWatchThreatList", map));

		}
		watchInfo.put("reportList", dao.selectList("Cmmn.selectWatchReportList", map));
		return watchInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getNetworkEquipementStatus(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Cmmn.selectNetworkEquipmentStatus", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSatelliteTrans(Map<String, Object> map) throws Exception {
		return (Map<String, Object>) dao.selectOptionalObject("Cmmn.selectSatelliteTrans", map).orElseGet(() -> null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSatelliteTransByJob(Map<String, Object> map) throws Exception {
		return (Map<String, Object>) dao.selectOptionalObjectByJob("Cmmn.selectSatelliteTrans", map)
				.orElseGet(() -> null);
	}

	@Override
	public int saveAlarmCheck(Map<String, Object> map) throws Exception {
		return dao.update("Cmmn.insertAlarmCheck", map);
	}

	@Override
	public int saveWhiteList(Map<String, Object> map) throws Exception {

		if (cryptoMode.equals("N")) {
			if (map.containsKey("policyId") && map.get("policyId") != null
					&& !map.get("policyId").toString().trim().equals("")) {
				return dao.update("Cmmn.updateWhiteList", map);
			} else {
				return dao.update("Cmmn.insertWhiteList", map);
			}
		} else {// ncos 무결성검증.
			Map<String, Object> salt = dao.selectMap("Cmmn.selectIntegritySalt", null)
					.orElseGet(() -> new HashMap<String, Object>());
			final String saltKey = "whitelistPolicy";
			String integrityKey = (String) map.get("policyId")
					+ (String) map.get("policyName")
					+ (map.get("srcIp") == null ? "" : ("" + map.get("srcIp")))
					+ (map.get("srcMask") == null ? "" : ("" + map.get("srcMask")))
					+ (map.get("dstIp") == null ? "" : ("" + map.get("dstIp")))
					+ (map.get("dstMask") == null ? "" : ("" + map.get("dstMask")))
					+ (map.get("srcStartPort") == null ? "" : ((String) map.get("srcStartPort")))
					+ (map.get("srcEndPort") == null ? "" : ((String) map.get("srcEndPort")))
					+ (map.get("dstStartPort") == null ? "" : ((String) map.get("dstStartPort")))
					+ (map.get("dstEndPort") == null ? "" : ((String) map.get("dstEndPort")))
					+ (map.get("protocol") == null ? "" : ((String) map.get("protocol")))
					+ (map.get("policy") == null ? "" : ((String) map.get("policy")));
			if (activeProfile.equals("navy")) {
				integrityKey = integrityKey + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey1);
			} else {
				integrityKey = integrityKey + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey2);
			}
			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);

			map.put("integrity", integrity);
			if (activeProfile.equals("navy")) {
				map.put("srcIpEnc", ScpDbUtil.scpEnc("" + map.get("srcIp"), cryptoModeKey1));
				map.put("dstIpEnc", ScpDbUtil.scpEnc("" + map.get("dstIp"), cryptoModeKey1));
			} else {
				map.put("srcIpEnc", ScpDbUtil.scpEnc("" + map.get("srcIp"), cryptoModeKey2));
				map.put("dstIpEnc", ScpDbUtil.scpEnc("" + map.get("dstIp"), cryptoModeKey2));
			}

			if (map.containsKey("policyId") && map.get("policyId") != null
					&& !map.get("policyId").toString().trim().equals("")) {
				return dao.update("Cmmn.updateWhiteListNcos", map);
			} else {
				return dao.update("Cmmn.insertWhiteListNcos", map);
			}
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
		if (map.get("threatImportanceGradeH").toString().trim().equals("1")) {
			threatImportanceGrade += 5;
		}
		if (map.get("threatImportanceGradeM").toString().trim().equals("1")) {
			threatImportanceGrade += 3;
		}
		if (map.get("threatImportanceGradeL").toString().trim().equals("1")) {
			threatImportanceGrade += 1;
		}
		map.put("threatImportanceGrade", threatImportanceGrade);

		if (cryptoMode.equals("N")) {
			if (map.containsKey("settingId") && map.get("settingId") != null
					&& !map.get("settingId").toString().trim().equals("")) {
				return dao.update("Cmmn.updateSatelliteTrans", map);
			} else {
				return dao.update("Cmmn.insertSatelliteTrans", map);
			}
		} else {// ncos 무결성검증.
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Date date = new Date(ts.getTime());
			String settingTime = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm.ss");
			Map<String, Object> salt = dao.selectMap("Cmmn.selectIntegritySalt", null)
					.orElseGet(() -> new HashMap<String, Object>());
			final String saltKey = "satelliteTrans";
			String integrityKey = (String) map.get("settingId")
					+ settingTime.substring(0, 10)// yyyy-mm-dd
					+ (String) map.get("transSpeed")
					+ (String) map.get("systemOpStatus")
					+ (String) map.get("systemOpStatusDelay")
					+ (String) map.get("uintId")
					+ (String) map.get("shipId")
					+ (String) map.get("assetStatus")
					+ (String) map.get("threatImportanceGrade")
					+ (String) map.get("detectionTime")
					+ ("" + map.get("srcIp"))
					+ ("" + map.get("dstIp"))
					+ (String) map.get("srcPort")
					+ (String) map.get("dstPort")
					+ (String) map.get("protocol")
					+ (String) map.get("payloadSize")
					+ (String) map.get("fragmentation")
					+ (String) map.get("fragmentId")
					+ (String) map.get("detectionThreatName")
					+ (String) map.get("threatDetectionMethod")
					+ (String) map.get("threatImportance")
					+ (String) map.get("payload");
			if (activeProfile.equals("navy")) {
				integrityKey = integrityKey + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey1);
			} else {
				integrityKey = integrityKey + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey2);
			}
			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
			map.put("settingTime", settingTime);
			map.put("integrity", integrity);
			if (map.containsKey("settingId") && map.get("settingId") != null
					&& !map.get("settingId").toString().trim().equals("")) {
				return dao.update("Cmmn.updateSatelliteTransNcos", map);
			} else {
				return dao.update("Cmmn.insertSatelliteTransNcos", map);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getZoneList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Cmmn.selectZoneList", map);
	}

	@Override
	public long getCscStatusByJob() throws Exception {
		return dao.selectCountByJob("Cmmn.selectCscStatus", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getTransmitDataByJob(Map<String, Object> map) throws Exception {
		map.put("threatImportanceGrade", dao.selectOneByJob("Cmmn.selectThreatImportanceGrade", map));
		return (Map<String, Object>) dao.selectOptionalObjectByJob("Cmmn.selectTransmitData", map)
				.orElseGet(() -> null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSendTransmitDataByJob(Map<String, Object> map) throws Exception {
		return (Map<String, Object>) dao.selectOptionalObjectByJob("Cmmn.selectSendTransmitData", map)
				.orElseGet(() -> null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNavyTransmitDataByJob(Map<String, Object> map) throws Exception {
		return (Map<String, Object>) dao.selectOptionalObjectByJob("Cmmn.selectNavyTransmitData", map)
				.orElseGet(() -> null);
	}

	@Override
	public int saveTransmitData(Map<String, Object> map) throws Exception {
		if (map.containsKey("dataId") && map.get("dataId") != null && !map.get("dataId").toString().trim().equals("")) {
			return dao.updateByJob("Cmmn.updateTransmitData", map);
		}
		return dao.updateByJob("Cmmn.insertTransmitData", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setReportKey(Map<String, Object> map, HttpServletRequest request) {
		log.debug("setReportKey map:{}", map);
		OOFDocument oof = null;
		oof = OOFDocument.newOOF();
		String propertyPath = request.getSession().getServletContext().getRealPath("/") + "WEB-INF" + File.separator
				+ "clipreport5" + File.separator + "clipreport5.properties";
		try {
			OOFFile oofFile = oof.addFile("crf.root", "%root%/crf/" + map.get("fileNm") + ".crf");
			Map<String, Object> param = (Map<String, Object>) map.get("param");
			oof.addField("PERIOD_ID", String.valueOf(param.get("periodId")));
			oof.addField("CREATE_TIME", String.valueOf(param.get("toDate")));
			oof.addField("ACCOUNT_ID", String.valueOf(param.get("accountId")));
			oof.addField("THREAT_LEVEL", String.valueOf(param.get("threatLevel")));
			oof.addField("ASSET_LEVEL", String.valueOf(param.get("assetLevel")));

			Map<String, Object> rtnMap = (Map<String, Object>) map.get("rtn");
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writeValueAsString(rtnMap);
			log.debug("jsonStr:{}", jsonStr);
			OOFConnectionMemo memo = oofFile.addConnectionMemo("*", jsonStr);
			memo.addContentParamJSON("*", "utf-8", "{%dataset.json.root%}");

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		String reportKey = ReportUtil.createReport(request, oof, "false", "false", request.getRemoteAddr(),
				propertyPath);
		map.put("reportKey", reportKey);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getNetEquipStatusList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> list = (List<Map<String, Object>>) dao.selectList("Cmmn.selectEquipStatusList", map);
		if (list == null || list.size() == 0) {
			throw new BizException("장비정보가 없습니다.", ErrorCode.UNDEFINED_ERROR);
		}

		if (cryptoMode.equals("Y")) {
			for (Map<String, Object> m : list) {
				String classNm = String.valueOf(m.get("classNm"));
				if (m.containsKey("upperId")) {
					if (activeProfile.equals("navy")) {
						classNm = ScpDbUtil.scpDec(classNm, cryptoModeKey1);
					} else {
						classNm = ScpDbUtil.scpDec(classNm, cryptoModeKey2);
					}

					m.put("classNm", classNm);
				}

			}
		}

		@SuppressWarnings({ "rawtypes" })
		List<Map<String, Object>> toList = new ArrayList();
		for (int i = list.size() - 1; i >= 0; i--) {
			if (!list.get(i).containsKey("upperId") || list.get(i).get("upperId").equals("")
					|| list.get(i).get("upperId") == null) {
				toList.add(list.get(i));
				list.remove(i);
			}
		}
		toList = toJsonMap(list, toList);
		return toList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getShipStatusList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Cmmn.selectShipStatusList", map);
	}

	@Override
	public int setNavyTransmitDataSetStartTimeByJob(Map<String, Object> map) throws Exception {
		return dao.updateByJob("Cmmn.updateNavyTransmitDataSetStartTime", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getManagerList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Cmmn.selectManagerList", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getUnitInfos(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Cmmn.selectUnitInfos", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getShipInfos(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Cmmn.selectShipInfos", map);
	}

}