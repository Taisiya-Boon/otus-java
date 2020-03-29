package ru.otus;

import java.util.ArrayList;
import java.util.Collections;

public class ATMImpl implements ATM {

    private CellImpl[] cellMasses;

    public ATMImpl(int[] quantity, ParBanknote[] parBanknotes){
        if (quantity.length != parBanknotes.length) {
            throw new IndexOutOfBoundsException("Недостаточное количество данных, для создания банкомата./nВведите числа означающие количество банкнот соответствующих номиналов");
        } else {
            cellMasses = new CellImpl[quantity.length];
            for (int i = 0; i < cellMasses.length; i++) {
                cellMasses[i] = new CellImpl(parBanknotes[i]);
                cellMasses[i].setQuantity(quantity[i]);
            }
        }
    }

    //new
    public ATMImpl(ATMImpl atm) {
        if (atm.quantityBanknoteInCell().length != atm.parBanknoteInCell().length) {
            throw new IndexOutOfBoundsException("Недостаточное количество данных, для создания банкомата./nВведите числа означающие количество банкнот соответствующих номиналов");
        } else {
            cellMasses = new CellImpl[atm.quantityBanknoteInCell().length];
            for (int i = 0; i < cellMasses.length; i++) {
                cellMasses[i] = new CellImpl(atm.parBanknoteInCell()[i]);
                cellMasses[i].setQuantity(atm.quantityBanknoteInCell()[i]);
            }
        }
    }

    private int[] quantityBanknoteInCell() {
        int[] quantity = new int[cellMasses.length];
        for (int i = 0; i < cellMasses.length; i++) {
            quantity[i] = cellMasses[i].getQuantity();
        }
        return quantity;
    }

    //new
    private ParBanknote[] parBanknoteInCell() {
        ParBanknote[] par = new ParBanknote[cellMasses.length];
        for (int i = 0; i < cellMasses.length; i++) {
            switch (cellMasses[i].getParBanknote()) {
                case 10:
                    par[i] = ParBanknote.TEN;
                    break;
                case 50:
                    par[i] = ParBanknote.FIFTY;
                    break;
                case 100:
                    par[i] = ParBanknote.ONE_HUNDRED;
                    break;
                case 500:
                    par[i] = ParBanknote.FIVE_HUNDRED;
                    break;
                case 1000:
                    par[i] = ParBanknote.THOUSAND;
                    break;
                case 5000:
                    par[i] = ParBanknote.FIVE_THOUSAND;
                    break;
                default:
                    throw new IllegalArgumentException("Нет банкноты твкого номинала.");
            }
        }
        return par;
    }

    @Override
    public ArrayList addMoney(int... money) {
        boolean flag = false;
        for (int banknote : money) {
            for (CellImpl cell : cellMasses) {
                if (banknote == cell.getParBanknote()) {
                    cell.addBanknote();
                    flag = true;
                }
            }
            if (flag == false) {
                throw new IllegalArgumentException("Банкомат не принимает банкноты такого номинала.");
            }
        }
        System.out.println("Операция прошла успешно");
        ArrayList result = new ArrayList();
        Collections.addAll(result, this.quantityBanknoteInCell());
        return result;
    }

    @Override
    public ArrayList takeMoney(int amountMoney) {
        if (amountMoney <= 0) {
            throw new IllegalArgumentException("Запросите положительное число банкнот.");
        }
        int[] quantity = new int[cellMasses.length];
        for (int i = cellMasses.length-1; i >= 0; i--){
            if (amountMoney > 0) {
                if (amountMoney >= cellMasses[i].getParBanknote()) {
                    quantity[i] = amountMoney / cellMasses[i].getParBanknote();
                    if (quantity[i] > cellMasses[i].getQuantity()) {
                        quantity[i] = cellMasses[i].getQuantity();
                    }
                    amountMoney -= cellMasses[i].getParBanknote() * quantity[i];
                }
            }
        }
        if (amountMoney > 0) {
            throw new IllegalArgumentException("Недостаточно денег в банкомате. \nВыдача средств невозможна.");
        } else {
            int i = 0;
            int sum = 0;
            for (CellImpl c : cellMasses) {
                sum += c.takeBanknote(quantity[i]);
                i++;
            }
            System.out.println("Выданы средства в количестве: " + sum);
            ArrayList result = new ArrayList();
            Collections.addAll(result, quantity);
            return result;
        }
    }

    @Override
    public int sumOut() {
        int sum = 0;
        for (CellImpl c : cellMasses) {
            sum += c.getQuantity()*c.getParBanknote();
        }
        System.out.println("Сумма средств в банкомате:" + sum);
        return sum;
    }

}
