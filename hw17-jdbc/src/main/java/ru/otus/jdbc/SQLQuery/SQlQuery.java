package ru.otus.jdbc.SQLQuery;

public interface SQlQuery {

    String toSQLString(Class clazz);

    default String toSQLUpdateString(Object o) {
        return "";
    }

}
