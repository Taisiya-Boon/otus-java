package ru.otus.jdbc.SQLQuery.query.impl;

import ru.otus.core.model.Id;
import ru.otus.jdbc.SQLQuery.SQlQuery;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

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
                sField.append(field.getName()).append(typeToSqlStrin.getOrDefault(field.getType(), " varchar(255)")).append(" NOT NULL auto_increment");
            } else {
                sField.append(field.getName()).append(typeToSqlStrin.getOrDefault(field.getType(), " varchar(255)"));
            }
            field.setAccessible(false);
        }
        s.append(sField).append(")");
        return s.toString();
    }

    private Map<Class, String> typeToSqlStrin = Map.ofEntries(
            entry(int.class, " int(3)"),
            entry(Integer.class, " int(3)"),
            entry(long.class, " bigint(20)"),
            entry(Long.class, " bigint(20)"),
            entry(String.class, " varchar(255)"),
            entry(char.class, " varchar(255)"),
            entry(double.class, " double"),
            entry(Double.class, " double"),
            entry(boolean.class, " bit"),
            entry(Boolean.class, " bit"),
            entry(float.class, " float"),
            entry(Float.class, " float"),
            entry(BigDecimal.class, " number"),
            entry(Object.class, " varchar(255)")
    );

}
