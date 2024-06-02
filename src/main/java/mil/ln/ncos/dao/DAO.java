package mil.ln.ncos.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.vo.PageInfo;

/**
 * @Package : mil.gdl.dis.cmmn.dao
 * @FileName : DAO.java
 * @author : kdh
 * @Date : 2022. 4. 10.
 * @Description :
 */
@RequiredArgsConstructor
@Repository("dao")
public class DAO {

	private final SqlSessionTemplate sqlSession;

	/**
	 * Insert.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the int
	 */
	public int insert(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.insert(queryId, params);
	
	}
	
	public int insertByJob(String queryId, Object params) {
		return sqlSession.insert(queryId, params);
	
	}

	/**
	 * Update.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the int
	 */
	public int update(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.update(queryId, params);
	}
	
	public int updateByJob(String queryId, Object params) {
		return sqlSession.update(queryId, params);
	}

	/**
	 * Delete.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the int
	 */
	public int delete(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.delete(queryId, params);

	}

	public int delete(String queryId) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.delete(queryId);
	}

	/**
	 * Select one.
	 *
	 * @param queryId the query id
	 * @return the object
	 */
	public Object selectOne(String queryId) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.selectOne(queryId);
	}
	
	public Object selectOneByJob(String queryId) {
		return sqlSession.selectOne(queryId);
	}

	/**
	 * Select one.
	 *
	 * @param queryId the query id
	 * @return the Object
	 */
	public Optional<Object> selectObject(String queryId) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		// preInsertPsnInfoLog(queryId, null);
		return sqlSession.selectOne(queryId);
	}

	/**
	 * Select one.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the map
	 */
	public Object selectOne(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.selectOne(queryId, params);

	}
	
	public Object selectOneByJob(String queryId, Object params) {
		return sqlSession.selectOne(queryId, params);

	}

	/**
	 * Select Count.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the Integer
	 */
	public long selectCount(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.selectOne(queryId, params);
	}
	
	public long selectCountByJob(String queryId, Object params) {
		return sqlSession.selectOne(queryId, params);
	}


	/**
	 * Select one.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the Object
	 */
	public Object selectObject(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.selectOne(queryId, params);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<?> selectPage(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		Map<String,Object> mapParam = (Map<String,Object>)params;
		PageInfo pageInfo = (PageInfo)mapParam.get("pageInfo");
		return sqlSession.selectList(queryId, params,pageInfo);
	}
	
	@SuppressWarnings("unchecked")
	public List<?> selectPage(String countQueryId,String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		Map<String,Object> mapParam = (Map<String,Object>)params;
		PageInfo pageInfo = (PageInfo)mapParam.get("pageInfo");
		pageInfo.setTotCount(this.selectCount(countQueryId, params));
		return sqlSession.selectList(queryId, params);
	}


	/**
	 * Select one.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the Optional<Object>
	 */
	public Optional<Object> selectOptionalObject(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return Optional.ofNullable(sqlSession.selectOne(queryId, params));
	}
	

	public Optional<Object> selectOptionalObjectByJob(String queryId, Object params) {
		return Optional.ofNullable(sqlSession.selectOne(queryId, params));
	}


	/**
	 * Select one.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the Object
	 */
	public Optional<Map<String, Object>> selectMap(String queryId, Object params) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return Optional.ofNullable(sqlSession.selectOne(queryId, params));
	}

	/**
	 * Select list.
	 *
	 * @param queryId the query id
	 * @return the list
	 */
	public List<?> selectList(String queryId) {
		if(null != SessionData.getUserVo()) SessionData.getUserVo().setSqlId(queryId);
		return sqlSession.selectList(queryId);
	}

	/**
	 * Select list.
	 *
	 * @param queryId the query id
	 * @param params  the params
	 * @return the list
	 */
	public List<?> selectList(String queryId, Object params) {
		return sqlSession.selectList(queryId, params);
	}
	

}