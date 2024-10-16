package mil.ln.ncos.func.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import mil.ln.ncos.NcosApplication;
import mil.ln.ncos.dao.DAO;

@lombok.extern.slf4j.Slf4j
@RequiredArgsConstructor
@Service
public class FuncServiceImpl implements FuncService {

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
		Map<String,Object> param = new HashMap();
		param.put("status", "2");
		param.put("cscName", "4");
		param.put("workType", "8");
		log.debug("transStatus{}",NcosApplication.transStatus);
		if(NcosApplication.transStatus.get("heartbeatStatus") !=null && NcosApplication.transStatus.get("heartbeatStatus").equals("Y")
				&& NcosApplication.transStatus.get("threatStatus") != null && NcosApplication.transStatus.get("threatStatus").equals("Y")
				&& NcosApplication.transStatus.get("assetStatus") != null && NcosApplication.transStatus.get("assetStatus").equals("Y")) {
			this.saveFuncOperation(param);
		}

		param.put("cscName", "3");
		param.put("workType", "9");
		if(dao.selectOneByJob("Func.selectFuncOperationResult").equals("N")){
			param.put("status", "1");
			param.put("reason", "화면 조회 실패");
		}
		return this.saveFuncOperation(param);
	}

	@Override
	public int saveFuncOperation(Map<String, Object> map) throws Exception {
		return dao.insertByJob("Func.insertFuncOperationState", map);
	}


}
