package ru.otus.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Id;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.SQLQuery.Query;
import ru.otus.jdbc.SQLQuery.SQLQueryFactory;
import ru.otus.jdbc.SQLQuery.SQlQuery;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class JdbcMapper<T> {

    private static Logger logger = LoggerFactory.getLogger(JdbcMapper.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;

    private boolean idFlag = false;

    public JdbcMapper(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    public long create(T objectData) {
        for (Field field : objectData.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                idFlag = true;
                field.setAccessible(false);
                break;
            }
            field.setAccessible(false);
        }
        if (!idFlag) {
            throw new IllegalArgumentException("object class haven't annotation id");
        } else {
            try {
                SQlQuery sQlQuery = new SQLQueryFactory().getSQLQuery(Query.INSERT);
                return dbExecutor.insertRecord(getConnection(), sQlQuery.toSQLString(objectData.getClass()), objectData);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void update(T objectData) {
        for (Field field : objectData.getClass().getFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idFlag = true;
                break;
            }
        }
        if (!idFlag) {
            throw new IllegalArgumentException("object class haven't annotation id");
        } else {
            try {
                SQlQuery sQlQuery = new SQLQueryFactory().getSQLQuery(Query.UPDATE);
                dbExecutor.insertRecord(getConnection(), sQlQuery.toSQLUpdateString(objectData), objectData);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new IllegalArgumentException(e);
            }
        }
    }

    public Optional<T> load(long id, Class<T> clazz) throws SQLException {
        SQlQuery sQlQuery = new SQLQueryFactory().getSQLQuery(Query.SELECT);
        return dbExecutor.selectRecord(getConnection(), sQlQuery.toSQLString(clazz), id, resultSet -> {
            try {
                Constructor<T>[] constructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
                Class[] parameterTypes = constructors[0].getParameterTypes();
                if (resultSet.next()) {
                    return constructors[0].newInstance(setOnType(resultSet, clazz, parameterTypes));
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        });
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    private <T> Object[] setOnType(ResultSet resultSet, Class<T> clazz, Class[] parameterTypes) throws SQLException {
        ArrayList<Object> list = new ArrayList();
        int i = 0;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == parameterTypes[i]) {
                if (field.getType() == int.class || field.getType() == Integer.class) {
                    list.add(resultSet.getInt(field.getName()));
                } else if (field.getType() == long.class || field.getType() == Long.class) {
                    list.add(resultSet.getLong(field.getName()));
                } else if (field.getType() == String.class || field.getType() == char.class) {
                    list.add(resultSet.getString(field.getName()));
                } else if (field.getType() == double.class || field.getType() == Double.class) {
                    list.add(resultSet.getDouble(field.getName()));
                } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    list.add(resultSet.getBoolean(field.getName()));
                } else if (field.getType() == float.class || field.getType() == Float.class) {
                    list.add(resultSet.getFloat(field.getName()));
                } else if (field.getType() == BigDecimal.class) {
                    list.add(resultSet.getBigDecimal(field.getName()));
                } else {
                    list.add(resultSet.getString(field.getName()));
                }
                field.setAccessible(false);
                i++;
                if (i == parameterTypes.length) {
                    break;
                }
            }
        }
        return list.toArray();
    }

}
