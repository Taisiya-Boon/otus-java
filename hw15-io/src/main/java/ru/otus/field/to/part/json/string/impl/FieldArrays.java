package ru.otus.field.to.part.json.string.impl;

import ru.otus.FieldToPartJsonString;
import ru.otus.JsonString;
import ru.otus.JsonStringFactory;

import java.lang.reflect.Field;

public class FieldArrays implements FieldToPartJsonString {

    private StringBuilder s = new StringBuilder();
    private JsonString jsonString;

    @Override
    public String toPartJson(Field field, Object obj) {
        s.append("\"" + field.getName() + "\":");
        try {
            field.setAccessible(true);
            Object[] arrays = (Object[]) field.get(obj);
            jsonString = JsonStringFactory.getJsonString(arrays);
            s.append(jsonString.toJson(arrays));
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s.toString();
    }
}
