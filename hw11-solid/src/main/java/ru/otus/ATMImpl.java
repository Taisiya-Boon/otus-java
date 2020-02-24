package ru.otus;

public class ATMImpl implements ATM {

    private CellImpl[] cellMasses;

    public ATMImpl(int[] quantity, ParBanknote[] parBanknotes){
        if (quantity.length != parBanknotes.length) {
            throw new IndexOutOfBoundsException("Недостаточное количество данных, для создания банкомата./nВведите числа означающие количество банкнот соответствующих номиналов");
        } else {
            cellMasses = new CellImpl[quantity.length];
            for (int i = 0; i < cellMasses.length; i++) {
                cellMasses[i] = new CellImpl(parBanknotes[i].getPar());
                cellMasses[i].setQuantity(quantity[i]);
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

    @Override
    public int [] addMoney(int... money) {
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
        return this.quantityBanknoteInCell();
    }

    @Override
    public int[] takeMoney(int amountMoney) {
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
            return quantity;
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
