package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DIYTestFramework {

    private final HashMap<Method, Annotation> methodAnnotationHashMap = new HashMap<>();

    private int beforeFlag = 0;
    private int testFlag = 0;
    private int afterFlag = 0;
    private  boolean testFellFlag = false;

    private int theTestPassed = 0;
    private int theTestFell = 0;

    public <T> void run(T object) {

        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeFlag++;
                methodAnnotationHashMap.put(method, method.getAnnotation(Before.class));
            } else if (method.isAnnotationPresent(Test.class)) {
                testFlag++;
                methodAnnotationHashMap.put(method, method.getAnnotation(Test.class));
            } else if (method.isAnnotationPresent(After.class)) {
                afterFlag++;
                methodAnnotationHashMap.put(method, method.getAnnotation(After.class));
            }
        }

        if (beforeFlag * testFlag * afterFlag != 0) {
            while (testFlag != 0) {
                testFellFlag = false;

                for (Map.Entry<Method, Annotation> method : methodAnnotationHashMap.entrySet()) {
                    if (method.getValue() == (method.getKey().getAnnotation(Before.class))) {
                        try {
                            method.getKey().invoke(null);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            System.out.println("Test Fell");
                            theTestFell++;
                            testFellFlag = true;
                            break;
                        }
                    }
                }
                for (Map.Entry<Method, Annotation> method : methodAnnotationHashMap.entrySet()) {
                    if (!testFellFlag) {
                        if (method.getValue() == (method.getKey().getAnnotation(Test.class))) {
                            try {
                                method.getKey().invoke(null);
                                theTestPassed++;
                            } catch (Exception e) {
                                System.out.println("Test Fell");
                                theTestFell++;
                            }
                            methodAnnotationHashMap.remove(method.getKey());
                            testFlag--;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                for (Map.Entry<Method, Annotation> method : methodAnnotationHashMap.entrySet()) {
                    if (!testFellFlag) {
                        if (method.getValue() == (method.getKey().getAnnotation(After.class))) {
                            try {
                                method.getKey().invoke(null);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                System.out.println("Test Fell");
                                theTestFell++;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        } else {
            System.out.println("object class haven't annotations before, test or after");
        }

        System.out.println("the Test Passed: " + theTestPassed);
        System.out.println("the Test Fell: " + theTestFell);

    }

}
