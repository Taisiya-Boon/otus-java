package ru.otus;

public class ATM {

    private CellImpl cellNom10 = new CellImpl(10);

    private CellImpl cellNom50 = new CellImpl(50);

    private CellImpl cellNom100 = new CellImpl(100);

    private CellImpl cellNom500 = new CellImpl(500);

    private CellImpl cellNom1000 = new CellImpl(1000);

    private CellImpl cellNom5000 = new CellImpl(5000);

    private CellImpl[] cellMasses = {cellNom10, cellNom50, cellNom100, cellNom500, cellNom1000, cellNom5000};

    public ATM(int[] quantity){
        if (quantity.length != cellMasses.length) {
            throw new IndexOutOfBoundsException("Недостаточное количество данных, для создания банкомата./nВведите числа означающие количество банкнот номиналом 10, 50, 100, 500, 1000 и 5000 соответственно");
        }
        for (int i = 0; i < cellMasses.length; i++) {
            cellMasses[i].setQuantity(quantity[i]);
        }
    }

    private int[] quantityBanknoteInCell() {
        int[] quantity = new int[cellMasses.length];
        for (int i = 0; i < cellMasses.length; i++) {
            quantity[i] = cellMasses[i].getQuantity();
        }
        return quantity;
    }

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

    public int sumOut() {
        int sum = 0;
        for (CellImpl c : cellMasses) {
            sum += c.getQuantity()*c.getParBanknote();
        }
        System.out.println("Сумма средств в банкомате:" + sum);
        return sum;
    }

}
