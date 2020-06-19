package ru.otus.json.string.impl;

import ru.otus.JsonString;

public class JsonStringFromPrimitives implements JsonString {

    @Override
    public String toJson(Object o) {
        return "{" + o + "}";
    }

}
