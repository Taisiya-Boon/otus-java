package ru.otus.jdbc.SQLQuery.query.impl;

import ru.otus.core.model.Id;
import ru.otus.jdbc.SQLQuery.SQlQuery;

import java.lang.reflect.Field;

public class SQLQueryInsert implements SQlQuery {

    private StringBuilder s = new StringBuilder();
    private StringBuilder sField = new StringBuilder();
    private StringBuilder sHandler = new StringBuilder();

    private boolean firstFlag = true;

    @Override
    public String toSQLString(Class clazz) {
        s.append("insert into ");
        s.append(clazz.getSimpleName());
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (!firstFlag) {
                sField.append(", ");
                sHandler.append(", ");
            }
            if (!field.isAnnotationPresent(Id.class)) {
                sField.append(field.getName());
                sHandler.append("?");
                firstFlag = false;
            }
            field.setAccessible(false);
        }
        s.append("(").append(sField).append(") ");
        s.append("values ").append("(").append(sHandler).append(")");
        return s.toString();
    }

}
