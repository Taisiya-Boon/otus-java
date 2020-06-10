package ru.otus;

import ru.otus.testClass.*;

public class DIYTestFreworkDemo {

    public static void main(String[] args) {
        test1();
        System.out.println();
        test2();
        System.out.println();
        test3();
        System.out.println();
        test4(); // "object class haven't annotations before, test or after"
        System.out.println();
        test5();
        System.out.println();
        test6();
    }

    private static void test1() {
        DIYTestFramework diyTestFramework = new DIYTestFramework();
        TestClass1 testClass1 = new TestClass1();

        diyTestFramework.run(testClass1);
    }

    private static void test2() {
        DIYTestFramework diyTestFramework = new DIYTestFramework();
        TestClass2 testClass2 = new TestClass2();

        diyTestFramework.run(testClass2);
    }

    private static void test3() {
        DIYTestFramework diyTestFramework = new DIYTestFramework();
        TestClass3 testClass3 = new TestClass3();

        diyTestFramework.run(testClass3);
    }

    private static void test4() {
        DIYTestFramework diyTestFramework = new DIYTestFramework();
        TestClass4 testClass4 = new TestClass4();

        diyTestFramework.run(testClass4);
    }

    private static void test5() {
        DIYTestFramework diyTestFramework = new DIYTestFramework();
        TestClass5 testClass5 = new TestClass5();

        diyTestFramework.run(testClass5);
    }

    private static void test6() {
        DIYTestFramework diyTestFramework = new DIYTestFramework();
        TestClass6 testClass6 = new TestClass6();

        diyTestFramework.run(testClass6);
    }

}
