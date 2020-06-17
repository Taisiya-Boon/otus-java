package ru.otus.jdbc.SQLQuery.query.impl;

import ru.otus.core.model.Id;
import ru.otus.jdbc.SQLQuery.SQlQuery;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class SQLQueryCreateTable implements SQlQuery {

    private StringBuilder s = new StringBuilder();
    private StringBuilder sField = new StringBuilder();

    private boolean firstFlag = true;

    @Override
    public String toSQLString(Class clazz) {
        s.append("create table ");
        s.append(clazz.getSimpleName()).append("(");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (!firstFlag) {
                sField.append(", ");
            } else {
                firstFlag = false;
            }
            if (field.isAnnotationPresent(Id.class)) {
                sField.append(field.getName()).append(toSQLType(field.getType())).append(" NOT NULL auto_increment");
            } else {
                sField.append(field.getName()).append(toSQLType(field.getType()));
            }
            field.setAccessible(false);
        }
        s.append(sField).append(")");
        return s.toString();
    }

    private String toSQLType(Type type) {
        if (type == int.class || type == Integer.class) {
            return " int(3)";
        } else if (type == long.class || type == Long.class) {
            return " bigint(20)";
        } else if (type == String.class || type == char.class) {
            return " varchar(255)";
        } else if (type == double.class || type == Double.class) {
            return " double";
        } else if (type == boolean.class || type == Boolean.class) {
            return " bit";
        } else if (type == float.class || type == Float.class) {
            return " float";
        } else if (type == BigDecimal.class) {
            return " number";
        } else {
            return " varchar(255)";
        }
    }

}
