package mil.ln.ncos.cmmn.service;


import java.util.List;


import org.springframework.cache.CacheManager;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.vo.CodeVo;
import mil.ln.ncos.dao.DAO;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

	private final CacheManager cacheManager;
	private final DAO dao;
	
	
	
	@SuppressWarnings("unchecked")
	@Cacheable(value = "codeCacheData", key = "#codeKey")
	public List<CodeVo> getCodeCacheData(final String codeKey) {
		return (List<CodeVo>)dao.selectList("Cmmn.selectCode",null);
	}

	public void evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}
	
	public void getAllCacheNames() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> log.debug(cacheName));
	}
	
	

}
