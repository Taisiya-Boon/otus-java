package ru.otus;

public class MyClass {

    @Log
    public void calculation(int param) {
        System.out.println("metod work. param: " + param);
    }

    @Log
    public void writer(String string) {
        System.out.println("write " + string);
    }

    public void doubleReader(double number) {
        System.out.println("number = " + number);
    }

}
