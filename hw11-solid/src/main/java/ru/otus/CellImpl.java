package ru.otus;

import lombok.Getter;
import lombok.Setter;

class CellImpl implements Cell {

    private final ParBanknote parBanknote;

    @Getter
    @Setter
    private int quantity;

    CellImpl(ParBanknote parBanknote){
        this.parBanknote = parBanknote;
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
            return quantityBanknote * parBanknote.getPar();
        }
    }

    public int getParBanknote() {
        return parBanknote.getPar();
    }
}

