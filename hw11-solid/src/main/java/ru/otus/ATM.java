package ru.otus;

public interface ATM {

    public int [] addMoney(int... money);

    public int[] takeMoney(int amountMoney);

    public int sumOut();

}
