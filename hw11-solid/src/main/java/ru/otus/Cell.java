package ru.otus;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Cell {

    private final int parBanknote;

    @Setter
    private int quantity;

    Cell(int parBanknote){
        this.parBanknote = parBanknote;
        this.quantity = 0;
    }

    public int addBanknote() {
        return quantity++;
    }

    public int takeBanknote(int quantityBanknote) {
        if (quantityBanknote > quantity){
            throw new IndexOutOfBoundsException("Нет столько банкнот");
        } else {
            quantity -= quantityBanknote;
            return quantityBanknote * parBanknote;
        }
    }

}
