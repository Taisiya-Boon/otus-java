package ru.otus.testClass;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestClass3 {

    @Test
    public static void method1() {
        System.out.println("Test");
    }

    @After
    public static void method2() {
        System.out.println("After");
    }

    @After
    public static void method3() {
        System.out.println("Final");
    }

    @Before
    public static void method4() {
        System.out.println("Begin");
    }

}
