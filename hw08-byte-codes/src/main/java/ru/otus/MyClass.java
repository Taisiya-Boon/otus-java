package ru.otus;

public class MyClass {

    public void calculation(int param) {
        System.out.println("metod work. param: " + param);
    }

    @Log
    public void calculation(int param1, int param2) {
        System.out.println("metod work. param 1: " + param1 + ", param 2:" + param2);
    }

    @Log
    public void writer(String string) {
        System.out.println("write " + string);
    }

    public void doubleReader(double number) {
        System.out.println("number = " + number);
    }

}
