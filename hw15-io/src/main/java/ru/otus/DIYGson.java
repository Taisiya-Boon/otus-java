package ru.otus;

public class DIYGson {

    private StringBuilder string = new StringBuilder();
    private boolean firstField = true;
    private JsonString jsonString;

    public String toJson(Object obj) {
        jsonString = JsonStringFactory.getJsonString(obj);
        string.append(jsonString.toJson(obj));
        return string.toString();
    }

}
