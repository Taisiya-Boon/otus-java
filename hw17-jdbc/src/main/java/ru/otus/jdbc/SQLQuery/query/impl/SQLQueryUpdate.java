package ru.otus.jdbc.SQLQuery.query.impl;

import ru.otus.core.model.Id;
import ru.otus.jdbc.SQLQuery.SQlQuery;

import java.lang.reflect.Field;

public class SQLQueryUpdate implements SQlQuery {

    private StringBuilder s = new StringBuilder();
    private StringBuilder sField = new StringBuilder();
    private StringBuilder sIdAnnotated = new StringBuilder();

    private boolean firstFlag = true;

    @Override
    public String toSQLString(Class o) {
        return toSQLUpdateString(o);
    }

    @Override
    public String toSQLUpdateString(Object o) {
        s.append("update ");
        s.append(o.getClass().getSimpleName());
        s.append(" set ");
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!firstFlag) {
                sField.append(", ");
            }
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    field.setAccessible(true);
                    if (field.getType() == String.class) {
                        sIdAnnotated.append(field.getName()).append(" = ").append("\'" + field.get(o) + "\'");
                    } else {
                        sIdAnnotated.append(field.getName()).append(" = ").append(field.get(o));
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                field.setAccessible(true);
                sField.append(field.getName()).append(" = ").append("?");
                field.setAccessible(false);
                firstFlag = false;
            }
            field.setAccessible(false);
        }
        s.append(sField);
        s.append(" where ").append(sIdAnnotated);
        return s.toString();
    }
}
