package mil.ln.ncos.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, LocalDateTime localDateTime, JdbcType jdbcType) throws SQLException {
        preparedStatement.setTimestamp(i,Timestamp.valueOf(localDateTime));
    }

    @Override
    public LocalDateTime getResult(ResultSet resultSet, String s) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(s);
        return timestamp.toLocalDateTime();
    }

    @Override
    public LocalDateTime getResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getTimestamp(i).toLocalDateTime();
    }

    @Override
    public LocalDateTime getResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getTimestamp(i).toLocalDateTime();
    }
}