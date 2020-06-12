package ru.otus.json.string.impl;

import ru.otus.JsonString;

import java.util.HashMap;

public class JsonStringFromMap implements JsonString {

    private StringBuilder s = new StringBuilder();
    private boolean firstField;

    @Override
    public String toJson(Object o) {
        HashMap map = (HashMap) o;
        s.append("{");
        firstField = true;
        map.forEach((key, value) -> {
            if (!firstField) {
                s.append(",");
            }
            s.append("\"" + key + "\":\"" + value + "\"");
            firstField = false;
        });
        s.append("}");
        return s.toString();
    }

}
