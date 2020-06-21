package ru.otus.jdbc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbExecutor<T> {
  private static Logger logger = LoggerFactory.getLogger(DbExecutor.class);

  public long insertRecord(Connection connection, String sql, Object obj) throws SQLException {
    Savepoint savePoint = connection.setSavepoint("savePointName");
    try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      Field[] fields = obj.getClass().getDeclaredFields(); int idx = 1;
      for (int i = 0; i < fields.length; i++) {
        if (!fields[i].isAnnotationPresent(Id.class)) {
          try {
            fields[i].setAccessible(true);
            setOnType(idx, (T) fields[i].get(obj), pst);
            fields[i].setAccessible(false);
            idx++;
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
      pst.executeUpdate();
      try (ResultSet rs = pst.getGeneratedKeys()) {
        rs.next();
        return rs.getInt(1);
      }
    } catch (SQLException ex) {
      connection.rollback(savePoint);
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public Optional<T> selectRecord(Connection connection, String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException {
    try (PreparedStatement pst = connection.prepareStatement(sql)) {
      pst.setLong(1, id);
      try (ResultSet rs = pst.executeQuery()) {
        return Optional.ofNullable(rsHandler.apply(rs));
      }
    }
  }

  private void setOnType(int idx, T obj, PreparedStatement pst) throws SQLException {
    if (obj.getClass() == Integer.class) {
      pst.setInt(idx, (Integer) obj);
    } else if ( obj.getClass() == Long.class) {
      pst.setLong(idx, (Long) obj);
    } else if (obj.getClass() == BigDecimal.class) {
      pst.setBigDecimal(idx, (BigDecimal) obj);
    } else {
      pst.setString(idx, (String) obj);
    }
  }

}
