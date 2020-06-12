package ru.otus.json.string.impl;

import ru.otus.JsonString;

public class JsonStringFromArrays implements JsonString {

    private StringBuilder s = new StringBuilder();

    @Override
    public String toJson(Object o) {
        Object[] array = (Object[]) o;
        s.append("[");
        for (int i = 0; i < array.length; i++) {
            if (i != 0) s.append(",");
            if (array[i].getClass() == String.class) {
                s.append("\"" + array[i] + "\"");
            } else if (array[i].getClass() == Object.class) {
                toJson(array[i]);
            } else {
                s.append(array[i]);
            }
        }
        s.append("]");
        return s.toString();
    }

}
