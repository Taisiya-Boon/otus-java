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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

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
                SetOnTypeFactory.Setter setter = SetOnTypeFactory.getSetter(field.getType());
                list.add(setter.toObject(resultSet, field));
                field.setAccessible(false);
                i++;
                if (i == parameterTypes.length) {
                    break;
                }
            }
        }
        return list.toArray();
    }

    private static class SetOnTypeFactory{
         private static Setter getSetter(Type type) {
             return setOnTypeMap.getOrDefault(type, new SetString());
         }

        private static Map<Class, Setter> setOnTypeMap = Map.ofEntries(
                entry(int.class, new SetInt()),
                entry(Integer.class, new SetInt()),
                entry(long.class, new SetLong()),
                entry(Long.class, new SetLong()),
                entry(String.class, new SetString()),
                entry(char.class, new SetString()),
                entry(double.class, new SetDouble()),
                entry(Double.class, new SetDouble()),
                entry(boolean.class, new SetBoolean()),
                entry(Boolean.class, new SetBoolean()),
                entry(float.class, new SetFloat()),
                entry(Float.class, new SetFloat()),
                entry(BigDecimal.class, new SetBigDecimal()),
                entry(Object.class, new SetString())
        );

         private interface Setter {
             Object toObject (ResultSet resultSet, Field field) throws SQLException;
         }

         private static class SetString implements Setter {
             @Override
             public Object toObject(ResultSet resultSet, Field field) throws SQLException {
                 return resultSet.getString(field.getName());
             }
         }

        private static class SetInt implements Setter {
            @Override
            public Object toObject(ResultSet resultSet, Field field) throws SQLException {
                return resultSet.getInt(field.getName());
            }
        }

        private static class SetLong implements Setter {
            @Override
            public Object toObject(ResultSet resultSet, Field field) throws SQLException {
                return resultSet.getLong(field.getName());
            }
        }

        private static class SetDouble implements Setter {
            @Override
            public Object toObject(ResultSet resultSet, Field field) throws SQLException {
                return resultSet.getDouble(field.getName());
            }
        }

        private static class SetFloat implements Setter {
            @Override
            public Object toObject(ResultSet resultSet, Field field) throws SQLException {
                return resultSet.getFloat(field.getName());
            }
        }

        private static class SetBoolean implements Setter {
            @Override
            public Object toObject(ResultSet resultSet, Field field) throws SQLException {
                return resultSet.getBoolean(field.getName());
            }
        }

        private static class SetBigDecimal implements Setter {
            @Override
            public Object toObject(ResultSet resultSet, Field field) throws SQLException {
                return resultSet.getBigDecimal(field.getName());
            }
        }
    }

}
