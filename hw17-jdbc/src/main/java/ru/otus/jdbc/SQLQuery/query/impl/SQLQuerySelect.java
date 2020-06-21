package ru.otus.jdbc.SQLQuery.query.impl;

import ru.otus.core.model.Id;
import ru.otus.jdbc.SQLQuery.SQlQuery;

import java.lang.reflect.Field;

public class SQLQuerySelect implements SQlQuery {

    private StringBuilder s = new StringBuilder();
    private StringBuilder sField = new StringBuilder();

    private Field idField;

    private boolean firstFlag = true;

    @Override
    public String toSQLString(Class clazz) {
        s.append("select ");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (!firstFlag) {
                sField.append(", ");
            }
            sField.append(field.getName());
            firstFlag = false;
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
            field.setAccessible(false);
        }
        s.append(sField).append(" ").append("from ");
        s.append(clazz.getSimpleName()).append(" where ").append(idField.getName()).append(" = ?");
        return s.toString();
    }

}
