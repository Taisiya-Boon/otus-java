package ru.otus.field.to.part.json.string.impl;

import ru.otus.FieldToPartJsonString;

import java.lang.reflect.Field;

public class FieldString implements FieldToPartJsonString {

    private String s;

    @Override
    public String toPartJson(Field field, Object obj) {
        try {
            field.setAccessible(true);
            s = "\"" + field.getName() + "\":\"" + field.get(obj) + "\"";
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }

}
