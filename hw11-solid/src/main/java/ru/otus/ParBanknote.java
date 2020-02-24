package ru.otus;

public enum ParBanknote {
    TEN(10),
    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    private int par;

    ParBanknote(int par){
        this.par = par;
    }

    public int getPar() {
        return par;
    }
}
