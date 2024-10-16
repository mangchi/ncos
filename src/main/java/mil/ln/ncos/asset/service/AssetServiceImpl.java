package mil.ln.ncos.asset.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.dao.DAO;

@RequiredArgsConstructor
@Service
public class AssetServiceImpl implements AssetService {

	private final DAO dao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAssetList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectPage("Asset.selectAssetCount", "Asset.selectAssetList", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAssetDispoList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectPage("Asset.selectAssetDispoCount", "Asset.selectAssetDispoList",
				map);
	}

	@Override
	public Map<String, Object> getAssetStatus(Map<String, Object> map) throws Exception {
		return dao.selectMap("Asset.selectAssetStatus", map).orElseGet(() -> new HashMap<String, Object>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAssetAffair(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Asset.selectAssetAffair", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getZoneLocationList(Map<String, Object> param) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Asset.selectZoneLocationList", param);
	}

	@Override
	public int saveAsset(Map<String, Object> param) throws Exception {
		if (param.containsKey("assetId") && param.get("assetId") != null
				&& !param.get("assetId").toString().trim().equals("")) {
			return dao.update("Asset.updateAsset", param);
		}
		return dao.update("Asset.insertAsset", param);
	}

	@Override
	public int deleteAsset(Map<String, Object> param) throws Exception {
		return dao.delete("Asset.deleteAsset", param);
	}

	@Override
	public long getSystemIdCount(Map<String, Object> param) throws Exception {
		return dao.selectCount("Asset.selectSystemIdCount", param);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public int saveZoneLocation(Map<String, Object> param) throws Exception {
		int rtn = 0;
		rtn += dao.delete("Asset.deleteZoneLocation", param);
		List<Map<String, Object>> paramList = (List<Map<String, Object>>) param.get("list");
		for (Map<String, Object> item : paramList) {
			if (null != item.get("delYn") && item.get("delYn").equals("N")) {
				rtn += dao.update("Asset.updateZoneLocation", item);
			} else {
				rtn += dao.update("Asset.insertZoneLocation", item);
			}
		}

		return rtn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAssetDispoZoneList(Map<String, Object> param) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Asset.selectAssetDispoZoneList", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAssetDispoZoneGroup(Map<String, Object> param) throws Exception {
		return (List<Map<String, Object>>) dao.selectList("Asset.selectAssetDispoZoneGroup", param);
	}

}
