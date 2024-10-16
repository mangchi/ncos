package mil.ln.ncos.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.dao.DAO;


@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService{

    private final DAO dao;
    

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getThreatStatusList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>)dao.selectList("Main.selectThreatStatus", map);
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAssetStatusList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>)dao.selectList("Main.selectAssetStatus", map);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDataTransList(Map<String, Object> map) throws Exception {
		List<Map<String,Object>> list = (List<Map<String, Object>>)dao.selectList("Main.selectDataTransStatus", map);
		return list;
	}

	@Override
	public long getTotDataTrans(Map<String, Object> map) throws Exception {
		return dao.selectCountByJob("Main.selectTotDataTransStatus",map);


	}


	@Override
	public Map<String, Object> getStatus(Map<String, Object> map) throws Exception {
		return dao.selectMap("Main.selectStatus", map).orElseGet(() -> new HashMap<String, Object>());
	}

	



}
