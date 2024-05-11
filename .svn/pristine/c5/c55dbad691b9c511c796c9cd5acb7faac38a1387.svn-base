package mil.ln.ncos.func.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import mil.ln.ncos.NcosApplication;
import mil.ln.ncos.dao.DAO;

@lombok.extern.slf4j.Slf4j
@RequiredArgsConstructor
@Service
public class FuncServiceImpl implements FuncService {
    
	@Value("${spring.profiles.active}")
	private String activeProfile;
	private final DAO dao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getFuncList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectPage("Func.selectFuncCount","Func.selectFuncList",map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getCscStatus(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>)dao.selectList("Func.selectCscStatus", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getShipStatusList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectList("Func.selectShipStatusList",map);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int saveFuncOperationJob() throws Exception {
		if(activeProfile.equals("hmmLand")) {
			return 1;
		}
		Map<String,Object> param = new HashMap();
		
		if(activeProfile.equals("land")) {
			param.put("cscName", "2");
			param.put("workType", "210");
			try {
				dao.selectOne("Func.selectHeartbeatDb");
				param.put("status", "2");
				param.put("reason", "모든 기능들이 현재 정상 동작중입니다.");
			}catch(Exception e) {
				e.printStackTrace();
				param.put("status", "1");
				param.put("reason", e.getMessage());
			}finally {
				this.saveFuncOperation(param);
			}
			
		}
		param.put("status", "2");
		param.put("cscName", "4");
		param.put("workType", "8");
		param.put("reason", "모든 기능들이 현재 정상 동작중입니다.");
		log.debug("transStatus{}",NcosApplication.transStatus);
		if(activeProfile.equals("land")) {
			this.saveFuncOperation(param);
		}
		if(activeProfile.equals("navy") || activeProfile.equals("hmmNavy")) {
			if(NcosApplication.transStatus.get("heartbeatStatus") !=null && NcosApplication.transStatus.get("heartbeatStatus").equals("Y")
					&& NcosApplication.transStatus.get("threatStatus") != null && NcosApplication.transStatus.get("threatStatus").equals("Y")
					&& NcosApplication.transStatus.get("assetStatus") != null && NcosApplication.transStatus.get("assetStatus").equals("Y")) {
				this.saveFuncOperation(param);
			}

		}
		
		param.put("cscName", "3");
		param.put("workType", "9");
		param.put("status", "2");
		param.put("reason", "모든 기능들이 현재 정상 동작중입니다.");
		String result = String.valueOf(dao.selectOneByJob("Func.selectFuncOperationResult"));
		log.debug("saveFuncOperationJob result:{}",result);
		if(result.equals("N")){
			log.error("관제 정보 분석 및 가시화 오류 발생..................");
			param.put("status", "1");
			param.put("reason", "화면 조회 실패");
		}
		return this.saveFuncOperation(param);
	}

	@Override
	public int saveFuncOperation(Map<String, Object> map) throws Exception {
		if(activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {
			map.put("systemId","hmm");
		}
		
		return dao.updateByJob("Func.insertFuncOperationState", map);
	}


}
