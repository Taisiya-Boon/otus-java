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

    private int testFlag = 0;
    private  boolean testFellFlag = false;

    private int theTestPassed = 0;
    private int theTestFell = 0;

    public <T> void run(T object) {

        for (Method method : object.getClass().getMethods()) {
            searchAnnotation(method, methodAnnotationHashMap);
        }

        if (testFlag != 0) {
            while (testFlag != 0) {
                testFellFlag = false;
                runAnnotatedMethodBefore(methodAnnotationHashMap);
                runAnnotatedMethodTest(methodAnnotationHashMap);
                runAnnotatedMethodAfter(methodAnnotationHashMap);
            }
        } else {
            System.out.println("object class haven't annotation test");
        }

        System.out.println("the Test Passed: " + theTestPassed);
        System.out.println("the Test Fell: " + theTestFell);

    }

    private void searchAnnotation(Method method, HashMap<Method, Annotation> hashMap) {
        if (method.isAnnotationPresent(Before.class)) {
            hashMap.put(method, method.getAnnotation(Before.class));
        } else if (method.isAnnotationPresent(Test.class)) {
            testFlag++;
            hashMap.put(method, method.getAnnotation(Test.class));
        } else if (method.isAnnotationPresent(After.class)) {
            hashMap.put(method, method.getAnnotation(After.class));
        }
    }

    private void runAnnotatedMethodBefore(HashMap<Method, Annotation> hashMap) {
        for (Map.Entry<Method, Annotation> method : hashMap.entrySet()) {
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
    }

    private void runAnnotatedMethodTest(HashMap<Method, Annotation> hashMap) {
        for (Map.Entry<Method, Annotation> method : hashMap.entrySet()) {
            if (!testFellFlag) {
                if (method.getValue() == (method.getKey().getAnnotation(Test.class))) {
                    try {
                        method.getKey().invoke(null);
                        theTestPassed++;
                    } catch (Exception e) {
                        System.out.println("Test Fell");
                        theTestFell++;
                    }
                    hashMap.remove(method.getKey());
                    testFlag--;
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void runAnnotatedMethodAfter(HashMap<Method, Annotation> hashMap) {
        for (Map.Entry<Method, Annotation> method : hashMap.entrySet()) {
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

}
