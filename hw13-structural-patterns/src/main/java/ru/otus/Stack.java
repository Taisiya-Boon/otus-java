package ru.otus;

import java.util.ArrayDeque;
import java.util.Deque;

public class Stack {

    private final Deque<Memento> stack = new ArrayDeque<>();

    void saveATM(ATM atm) {
        stack.push(new Memento(atm));
    }

    ATM restoreATM() {
        return stack.pop().getATM();
    }

    public void remove(ATM atm){
        stack.remove(atm);
    }
}
