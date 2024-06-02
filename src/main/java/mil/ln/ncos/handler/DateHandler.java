package mil.ln.ncos.handler;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Package		: mil.ln.ncos.handler
 * @FileName	: DateHandler.java
 * @author		: mangchi
 * @Date		: 2022. 4. 18.
 * @Description	: springboot mybats 2 이후 자체에서 처리해줌
 * @deprecated
 */
@Deprecated
@Slf4j
@MappedTypes(value = Long.class)
@MappedJdbcTypes(value = {JdbcType.DATE, JdbcType.TIME, JdbcType.TIMESTAMP})
public class DateHandler extends BaseTypeHandler<Long> {

    /**
     *Convert Java type to JDBC type
     *
     * @param preparedStatement
     * @param i
     * @
     * @ param along millisecond timestamp
     * @ param JDBC type DB field type
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Long aLong, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.DATE) {
            preparedStatement.setDate(i, new Date(aLong));
        } else if (jdbcType == JdbcType.TIME) {
            preparedStatement.setTime(i, new Time(aLong));
        } else if (jdbcType == JdbcType.TIMESTAMP) {
            preparedStatement.setTimestamp(i, new Timestamp(aLong));
        }
    }

    @Override
    public Long getNullableResult(ResultSet resultSet, String s) throws SQLException {
    	log.debug("getNullableResult");
        return parse2time(resultSet.getObject(s));
    }

    @Override
    public Long getNullableResult(ResultSet resultSet, int i) throws SQLException {
    	log.debug("getNullableResult");
        return parse2time(resultSet.getObject(i));
    }

    @Override
    public Long getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return parse2time(callableStatement.getObject(i));
    }

    private Long parse2time(Object value) {
    	log.debug("parse2time");
        if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (value instanceof Time) {
            return ((Time) value).getTime();
        } else if (value instanceof Timestamp) {
        	log.debug("value:{}",((Timestamp) value).getTime());
            return ((Timestamp) value).getTime();
        }
        return null;
    }
}