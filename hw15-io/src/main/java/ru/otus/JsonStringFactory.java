package ru.otus;

import ru.otus.json.string.impl.*;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonStringFactory {

    public static JsonString getJsonString(Object o) {
        if (o.getClass() == String.class) {
            return new JsonStringFromString();
        } else if (o.getClass() == Integer.class ||
                o.getClass() == Long.class || 
                o.getClass() == Boolean.class) {
            return new JsonStringFromPrimitives();
        } else if (o.getClass() == ArrayList.class) {
            return new JsonStringFromArrayList();
        } else  if (o.getClass().toString().contains("[")) {
            return new JsonStringFromArrays();
        } else if (o.getClass() == HashMap.class) {
            return new JsonStringFromMap();
        } else {
            return new JsonStringFromObject();
        }
    }

}
