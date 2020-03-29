package ru.otus;

public class Memento {

    private ATM atm;

    Memento(ATM atm) {
        this.atm = new ATMImpl((ATMImpl) atm);
    }

    ATM getATM() {
        return atm;
    }

}
