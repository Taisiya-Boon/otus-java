package ru.otus.field.to.part.json.string.impl;

import ru.otus.FieldToPartJsonString;
import ru.otus.JsonString;
import ru.otus.JsonStringFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FieldArrayList implements FieldToPartJsonString {

    private StringBuilder s = new StringBuilder();
    private JsonString jsonString;

    @Override
    public String toPartJson(Field field, Object obj) {
        s.append("\"" + field.getName() + "\":");
        try {
            field.setAccessible(true);
            ArrayList arrays = (ArrayList) field.get(obj);
            jsonString = JsonStringFactory.getJsonString(arrays);
            s.append(jsonString.toJson(arrays));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s.toString();
    }
}
