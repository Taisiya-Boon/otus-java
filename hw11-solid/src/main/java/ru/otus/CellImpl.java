package ru.otus;

import lombok.Getter;
import lombok.Setter;

public class CellImpl implements Cell {

    private final int parBanknote;

    @Getter
    @Setter
    private int quantity;

    CellImpl(int parBanknote){
        int bufferPar = 0;
        for (ParBanknote par : ParBanknote.values()) {
            if (parBanknote == par.getPar()) {
                bufferPar = par.getPar();
            }
        }
        if (bufferPar == 0) {
            throw new IllegalArgumentException("Нет банкноты такого номинала.");
        } else {
            this.parBanknote = bufferPar;
        }
        this.quantity = 0;
    }

    @Override
    public int addBanknote() {
        return quantity++;
    }

    @Override
    public int takeBanknote(int quantityBanknote) {
        if (quantityBanknote > quantity){
            throw new IndexOutOfBoundsException("Нет столько банкнот");
        } else {
            quantity -= quantityBanknote;
            return quantityBanknote * parBanknote;
        }
    }

    public int getParBanknote() {
        return parBanknote;
    }

    private enum ParBanknote {
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

}

