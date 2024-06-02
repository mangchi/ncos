package mil.ln.ncos.net.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.dao.DAO;

@RequiredArgsConstructor
@Service
public class NetServiceImpl implements NetService {
	
	private final DAO dao;

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<Map<String, Object>> getEventList(Map<String, Object>
	 * param) { return (List<Map<String,
	 * Object>>)dao.selectList("Net.selectEventList", param); }
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getNetEquipList(Map<String, Object> param) throws Exception{
		return (List<Map<String, Object>>)dao.selectList("Net.selectNetEquipList", param);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTopoList(Map<String, Object> param) throws Exception{
		return (List<Map<String, Object>>)dao.selectList("Net.selectTopoList", param);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getErrorAssetList(Map<String, Object> param) throws Exception{
		return (List<Map<String, Object>>)dao.selectList("Net.selectErrorAssetList", param);
	}


}