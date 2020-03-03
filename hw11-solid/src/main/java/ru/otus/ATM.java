package ru.otus;

import java.util.Collection;

public interface ATM {

    public Collection addMoney(int... money);

    public Collection takeMoney(int amountMoney);

    public int sumOut();

}
