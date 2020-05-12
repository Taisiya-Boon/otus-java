package ru.otus;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DIYGson {

    private StringBuilder jsonString = new StringBuilder();
    private boolean firstField = true;

    public String toJson(Object obj) {
        if (obj.getClass().toString().contains("[")) {
            if (obj.getClass().getComponentType() == String.class) {
                arraysAndListToJson((String[]) obj);
            } else if (obj.getClass().getComponentType() == Integer.class) {
                arraysAndListToJson((Integer[]) obj);
            } else if (obj.getClass().getComponentType() == Long.class) {
                arraysAndListToJson((Long[]) obj);
            } else if (obj.getClass().getComponentType() == Boolean.class) {
                arraysAndListToJson((Boolean[]) obj);
            } else {
                arraysAndListToJson((Object[]) obj);
            }
        } else if (obj.getClass() == ArrayList.class) {
            ArrayList arrayList = (ArrayList) obj;
            arraysAndListToJson(arrayList.toArray());
        } else if (obj.getClass() == Map.class) {
            mapToJson((Map) obj);
        } else {
            jsonString.append("{");
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (firstField == false) {
                    jsonString.append(",");
                }
                if (field.getType() == String.class) {
                    try {
                        field.setAccessible(true);
                        jsonString.append("\"" + field.getName() + "\":\"" + field.get(obj) + "\"");
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (obj.getClass().toString().contains("[")) {
                    jsonString.append("\"" + field.getName() + "\":");
                    try {
                        field.setAccessible(true);
                        Object[] arrays = (Object[]) field.get(obj);
                        arraysAndListToJson(arrays);
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (field.getType() == Map.class) {
                    jsonString.append("\"" + field.getName() + "\":");
                    try {
                        field.setAccessible(true);
                        Map map = (Map) field.get(obj);
                        mapToJson(map);
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (field.getType() == ArrayList.class) {
                    jsonString.append("\"" + field.getName() + "\":");
                    try {
                        field.setAccessible(true);
                        ArrayList arrays = (ArrayList) field.get(obj);
                        arraysAndListToJson(arrays.toArray());
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        field.setAccessible(true);
                        jsonString.append("\"" + field.getName() + "\":" + field.get(obj));
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                firstField = false;
            }
            jsonString.append("}");
        }
        return jsonString.toString();
    }

    private void arraysAndListToJson(Object[] array) {
        jsonString.append("[");
        for (int i = 0; i < array.length; i++) {
            if (i != 0) jsonString.append(",");
            if (array[i].getClass() == String.class) {
                jsonString.append("{\"" + array[1] + "\"}");
            } else if (array[i].getClass() == Object.class) {
                toJson(array[i]);
            } else {
                jsonString.append(array[1]);
            }
        }
        jsonString.append("]");
    }

    private void mapToJson(Map map) {
        jsonString.append("{");
        firstField = true;
        map.forEach((key, value) -> {
            if (firstField == false) {
                jsonString.append(",");
            }
            jsonString.append("{\"" + key + "\":" + toJson(value) + "}");
            firstField = false;
        });
        jsonString.append("}");
    }

}
