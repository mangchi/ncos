package mil.ln.ncos.dao;

import java.sql.Connection;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.vo.PageInfo;

@Slf4j
@Component
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PrepareInterceptor implements Interceptor {
	private static final DefaultObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

	private String attachCountQuery(BoundSql boundSql, PageInfo pageInfo,String sqlId) throws Exception{
		StringBuilder sb = new StringBuilder("SELECT COUNT(1) FROM ( \n");
		sb.append(boundSql.getSql());
		sb.append("\n ) ");
		String attachedCountQuery = sb.toString();
		SqlLogPrinter.logPrint(boundSql,sb.toString(),sqlId);
		return attachedCountQuery;
	}


	private String attachLimitQuery(BoundSql boundSql, PageInfo pageInfo,String sqlId) throws Exception{
		StringBuilder sb = new StringBuilder("SELECT * FROM ( \n");
		sb.append(boundSql.getSql());
		sb.append("\n )");
		sb.append("\nWHERE RN > ");
		sb.append((pageInfo.getPageNo() - 1) * pageInfo.getRowPerPage());
		sb.append("\nAND RN <= ");
		sb.append(pageInfo.getPageNo()*pageInfo.getRowPerPage());
		sb.append("\nORDER BY RN ").append(pageInfo.getOrderBy());
		String attachLimitQuery = sb.toString();
		SqlLogPrinter.logPrint(boundSql,sb.toString(),sqlId);
		return attachLimitQuery;
	}

	private String attachQuery(BoundSql boundSql, PageInfo pageInfo,String sqlId) throws Exception{
		if (pageInfo.getTotCount() == -1)
			return attachCountQuery(boundSql, pageInfo,sqlId);

		if (!isNullPageInfo(pageInfo))
			return attachLimitQuery(boundSql, pageInfo,sqlId);

		return boundSql.getSql();
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			MetaObject metaStatementHandler = MetaObject.forObject(invocation.getTarget(), DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			if (isSelectSqlCommandType(metaStatementHandler)) {
				if(metaStatementHandler.getValue("delegate.rowBounds") instanceof PageInfo) {
					PageInfo pageInfo = (PageInfo) metaStatementHandler.getValue("delegate.rowBounds");
					//String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
					BoundSql boundSql = (BoundSql)metaStatementHandler.getValue("delegate.boundSql");
					MappedStatement ms = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
					// limit 추가된 query
					metaStatementHandler.setValue("delegate.boundSql.sql", attachQuery(boundSql, pageInfo,ms.getId()));
				}				
			}
		} catch (ClassCastException e) {
			log.error("error:", e );
		}
		return invocation.proceed();
	}

	private boolean isSelectSqlCommandType(MetaObject metaStatementHandler) {
		SqlCommandType sqlCommandType = (SqlCommandType) metaStatementHandler
				.getValue("delegate.mappedStatement.sqlCommandType");

		return sqlCommandType.equals(SqlCommandType.SELECT);
	}

	private boolean isNullPageInfo(PageInfo pageInfo) {
		return pageInfo.getPageNo() == null || pageInfo.getRowPerPage() == null;
	}

}
