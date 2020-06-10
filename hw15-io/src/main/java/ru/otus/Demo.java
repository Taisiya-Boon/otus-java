package ru.otus;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Demo {

    public static void main(String[] args) {
//        toJsonBagOfPrimitives();
//        toJsonIntArray();
//        toJsonArrayList();
        toJsonMap();
    }


    private static void toJsonBagOfPrimitives() {
        DIYGson gson = new DIYGson();
        BagOfPrimitives obj = new BagOfPrimitives(22, "test", 10);
        System.out.println(obj);

        String json = gson.toJson(obj);
        System.out.println(json);

        BagOfPrimitives obj2 = new Gson().fromJson(json, BagOfPrimitives.class);
        System.out.println(obj.equals(obj2));
        System.out.println(obj2);
    }

    private static void toJsonIntArray() {
        DIYGson gson = new DIYGson();
        Integer[] obj = new Integer[]{0, 5, 8};
        System.out.println(obj);

        String json = gson.toJson(obj);
        System.out.println(json);

        Integer[] obj2 = new Gson().fromJson(json, Integer[].class);
        System.out.println(Arrays.equals(obj, obj2));
        System.out.println(obj2);
    }

    private static void toJsonArrayList() {
        DIYGson gson = new DIYGson();
        ArrayList obj = new ArrayList();
        obj.add(54.0);
        obj.add("test");
        obj.add("abc");
        obj.add(123.0);
        System.out.println(obj);

        String json = gson.toJson(obj);
        System.out.println(json);

        ArrayList obj2 = new Gson().fromJson(json, ArrayList.class);
        System.out.println(obj.equals(obj2));
        System.out.println(obj2);
    }

    private static void toJsonMap() {
        DIYGson gson = new DIYGson();
        HashMap obj = new HashMap();
        obj.put(1, "value1");
        obj.put(2, "value2");
        obj.put(3, "value3");
        System.out.println(obj);

        String json = gson.toJson(obj);
        System.out.println(json);

        HashMap obj2 = new Gson().fromJson(json, HashMap.class);
        System.out.println(obj.equals(obj2));
        System.out.println(obj2);
    }

}
