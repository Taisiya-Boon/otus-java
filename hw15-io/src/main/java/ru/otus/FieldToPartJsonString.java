package ru.otus;

import java.lang.reflect.Field;

public interface FieldToPartJsonString {
    String toPartJson(Field field, Object obj);
}
