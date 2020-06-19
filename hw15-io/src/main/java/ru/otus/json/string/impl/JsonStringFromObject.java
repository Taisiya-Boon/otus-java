package ru.otus.json.string.impl;

import ru.otus.FieldToPartJsonString;
import ru.otus.FieldToPartJsonStringFactory;
import ru.otus.JsonString;

import java.lang.reflect.Field;

public class JsonStringFromObject implements JsonString {

    private StringBuilder s = new StringBuilder();
    private boolean firstField = true;
    private FieldToPartJsonString fieldToPartJsonString;

    @Override
    public String toJson(Object o) {
        s.append("{");
        for (Field field : o.getClass().getDeclaredFields()) {
            if (!firstField) {
                s.append(",");
            }
            fieldToPartJsonString = FieldToPartJsonStringFactory.getFieldToPartJsonString(field, o);
            s.append(fieldToPartJsonString.toPartJson(field, o));
            firstField = false;
        }
        s.append("}");
        return s.toString();
    }

}
