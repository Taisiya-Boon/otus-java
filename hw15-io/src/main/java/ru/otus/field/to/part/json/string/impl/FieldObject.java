package ru.otus.field.to.part.json.string.impl;

import ru.otus.FieldToPartJsonString;
import ru.otus.JsonString;
import ru.otus.JsonStringFactory;

import java.lang.reflect.Field;

public class FieldObject implements FieldToPartJsonString {

    private StringBuilder s = new StringBuilder();
    private JsonString jsonString;

    @Override
    public String toPartJson(Field field, Object obj) {
        s.append("\"" + field.getName() + "\":");
        field.setAccessible(true);
        jsonString = JsonStringFactory.getJsonString(field);
        s.append(jsonString.toJson(field));
        field.setAccessible(false);
        return s.toString();
    }
}
