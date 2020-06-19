package ru.otus.json.string.impl;

import ru.otus.JsonString;

import java.util.ArrayList;

public class JsonStringFromArrayList implements JsonString {

    private StringBuilder s = new StringBuilder();

    @Override
    public String toJson(Object obj) {
        ArrayList arrayList = (ArrayList) obj;
        Object[] array = arrayList.toArray();
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
