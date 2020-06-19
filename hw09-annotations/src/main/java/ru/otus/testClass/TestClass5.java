package ru.otus.testClass;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestClass5 {

    @Test
    public static void method1(String s) {
        System.out.println("ABC " + s);
    }

    @Test
    public static void method2(String s) {
        System.out.println(s + " TEST");
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
