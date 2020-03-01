package ru.otus;

import java.util.ArrayList;

public interface ATM {

    public ArrayList addMoney(int... money);

    public ArrayList takeMoney(int amountMoney);

    public int sumOut();

}
