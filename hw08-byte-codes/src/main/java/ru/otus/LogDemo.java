package ru.otus;

/*
    java -javaagent:logDemo.jar -jar logDemo.jar
*/
public class LogDemo {

    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        myClass.calculation(4);
        myClass.doubleReader(2.5);
        myClass.writer("hello");
    }

}
