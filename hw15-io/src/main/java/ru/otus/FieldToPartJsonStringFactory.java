package ru.otus;

import ru.otus.field.to.part.json.string.impl.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class FieldToPartJsonStringFactory {

    public static FieldToPartJsonString getFieldToPartJsonString(Field field, Object obj) {
        if (field.getType() == String.class) {
            return new FieldString();
        } else if (field.getType() == Integer.class || field.getType() == int.class ||
                field.getType() == Long.class || field.getType() == long.class ||
                field.getType() == Boolean.class || field.getType() == boolean.class) {
            return new FieldPrimitives();
        } else if (field.getType().toString().contains("[")) {
            return new FieldArrays();
        } else if (field.getType() == HashMap.class) {
            return new FieldMap();
        } else if (field.getType() == ArrayList.class) {
            return new FieldArrayList();
        } else {
            return new FieldObject();
        }
    }

}
