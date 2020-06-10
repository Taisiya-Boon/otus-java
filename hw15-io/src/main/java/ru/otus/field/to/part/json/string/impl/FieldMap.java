package ru.otus.field.to.part.json.string.impl;

import ru.otus.FieldToPartJsonString;
import ru.otus.JsonString;
import ru.otus.JsonStringFactory;

import java.lang.reflect.Field;
import java.util.HashMap;

public class FieldMap implements FieldToPartJsonString {

    private StringBuilder s = new StringBuilder();
    private JsonString jsonString;

    @Override
    public String toPartJson(Field field, Object obj) {
        s.append("\"" + field.getName() + "\":");
        try {
            field.setAccessible(true);
            HashMap map = (HashMap) field.get(obj);
            jsonString = JsonStringFactory.getJsonString(map);
            s.append(jsonString.toJson(map));
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s.toString();
    }
}
