package mil.ln.ncos.dao;

import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.vo.PageInfo;

@Slf4j
@Component

@Intercepts ( { @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}) 
               ,@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}) 
               ,@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}) } )
public class DAOInterceptor implements Interceptor{

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(invocation.getArgs().length > 2 && invocation.getArgs()[2] instanceof PageInfo) {
			return invocation.proceed(); 
		}
		Object param = invocation.getArgs()[1];
		BoundSql boundSql = ((MappedStatement)invocation.getArgs()[0]).getBoundSql(param);
		SqlLogPrinter.logPrint(boundSql,boundSql.getSql(),((MappedStatement)invocation.getArgs()[0]).getId());
	    return invocation.proceed(); 
   }

   @Override
   public Object plugin(Object target) {
       return Plugin.wrap(target, this);
   }

   @Override
   public void setProperties(Properties properties) {
	   log.info("properties => {}", properties);
   }
}

