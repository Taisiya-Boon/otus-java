package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ATMImpl implements ATM {

    private List<CellImpl> cellMasses = new ArrayList<>();

    public ATMImpl(int[] quantity, ParBanknote[] parBanknotes){
        if (quantity.length != parBanknotes.length) {
            throw new IndexOutOfBoundsException("Недостаточное количество данных, для создания банкомата./nВведите числа означающие количество банкнот соответствующих номиналов");
        } else {
            for (int i = 0; i < quantity.length; i++) {
                cellMasses.add(new CellImpl(parBanknotes[i]));
                cellMasses.get(i).setQuantity(quantity[i]);
            }
        }
    }

    //new
    public ATMImpl(ATMImpl atm) {
        if (atm.quantityBanknoteInCell().length != atm.parBanknoteInCell().length) {
            throw new IndexOutOfBoundsException("Недостаточное количество данных, для создания банкомата./nВведите числа означающие количество банкнот соответствующих номиналов");
        } else {
            for (int i = 0; i < atm.quantityBanknoteInCell().length; i++) {
                cellMasses.add(new CellImpl(atm.parBanknoteInCell()[i]));
                cellMasses.get(i).setQuantity(atm.quantityBanknoteInCell()[i]);
            }
        }
    }

    private int[] quantityBanknoteInCell() {
        int[] quantity = new int[cellMasses.size()];
        for (int i = 0; i < cellMasses.size(); i++) {
            quantity[i] = cellMasses.get(i).getQuantity();
        }
        return quantity;
    }

    //new
    private ParBanknote[] parBanknoteInCell() {
        ParBanknote[] par = new ParBanknote[cellMasses.size()];
        for (int i = 0; i < cellMasses.size(); i++) {
            par[i] = cellMasses.get(i).getParBanknote();
        }
        return par;
    }

    @Override
    public ArrayList addMoney(int... money) {
        boolean flag = false;
        for (int banknote : money) {
            for (CellImpl cell : cellMasses) {
                if (banknote == cell.getParBanknote().getPar()) {
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
        int[] quantity = new int[cellMasses.size()];
        for (int i = cellMasses.size()-1; i >= 0; i--){
            if (amountMoney > 0) {
                if (amountMoney >= cellMasses.get(i).getParBanknote().getPar()) {
                    quantity[i] = amountMoney / cellMasses.get(i).getParBanknote().getPar();
                    if (quantity[i] > cellMasses.get(i).getQuantity()) {
                        quantity[i] = cellMasses.get(i).getQuantity();
                    }
                    amountMoney -= cellMasses.get(i).getParBanknote().getPar() * quantity[i];
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
            sum += c.getQuantity()*c.getParBanknote().getPar();
        }
        System.out.println("Сумма средств в банкомате:" + sum);
        return sum;
    }

}
