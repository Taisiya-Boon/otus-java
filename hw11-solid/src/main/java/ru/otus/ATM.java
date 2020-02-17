package ru.otus;

public class ATM {

    private Cell cellNom10 = new Cell(10);

    private Cell cellNom50 = new Cell(50);

    private Cell cellNom100 = new Cell(100);

    private Cell cellNom500 = new Cell(500);

    private Cell cellNom1000 = new Cell(1000);

    private Cell cellNom5000 = new Cell(5000);

    private Cell[] cellMasses = {cellNom10, cellNom50, cellNom100, cellNom500, cellNom1000, cellNom5000};

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
        for (int banknote : money) {
            switch (banknote) {
                case 10 :
                    cellNom10.addBanknote();
                    break;
                case 50 :
                    cellNom50.addBanknote();
                    break;
                case 100 :
                    cellNom100.addBanknote();
                    break;
                case 500 :
                    cellNom500.addBanknote();
                    break;
                case 1000 :
                    cellNom1000.addBanknote();
                    break;
                case 5000 :
                    cellNom5000.addBanknote();
                    break;
                default:
                    throw new IllegalArgumentException("Банкомат не принимает банкноты такого номинала.");
            }
        }
        System.out.println("Операция прошла успешно");
        return this.quantityBanknoteInCell();
    }

    public int[] takeMoney(int amountMoney) {
        int[] quantity = {0, 0, 0, 0, 0, 0};
        if (amountMoney > 0) {
            if (amountMoney >= 5000) {
                quantity[5] = amountMoney / 5000;
                if (quantity[5] > cellNom5000.getQuantity()) {
                    quantity[5] = cellNom5000.getQuantity();
                }
                amountMoney -= 5000 * quantity[5];
            }
            if (amountMoney >= 1000) {
                quantity[4] = amountMoney / 1000;
                if (quantity[4] > cellNom1000.getQuantity()) {
                    quantity[4] = cellNom1000.getQuantity();
                }
                amountMoney -= 1000*quantity[4];
            }
            if (amountMoney >= 500) {
                quantity[3] = amountMoney / 500;
                if (quantity[3] > cellNom500.getQuantity()) {
                    quantity[3] = cellNom500.getQuantity();
                }
                amountMoney -= 500*quantity[3];
            }
            if (amountMoney >= 100) {
                quantity[2] = amountMoney / 100;
                if (quantity[2] > cellNom100.getQuantity()) {
                    quantity[2] = cellNom100.getQuantity();
                }
                amountMoney -= 100*quantity[2];
            }
            if (amountMoney >= 50) {
                quantity[1] = amountMoney / 50;
                if (quantity[1] > cellNom50.getQuantity()) {
                    quantity[1] = cellNom50.getQuantity();
                }
                amountMoney -= 50*quantity[1];
            }
            if (amountMoney >= 10) {
                quantity[0] = amountMoney / 10;
                if (quantity[0] > cellNom10.getQuantity()) {
                    quantity[0] = cellNom10.getQuantity();
                }
                amountMoney -= 10*quantity[0];
            }
            if (amountMoney > 0) {
                throw new IllegalArgumentException("Недостаточно денег в банкомате. \nВыдача средств невозможна.");
            } else {
                int i = 0;
                int sum = 0;
                for (Cell c : cellMasses) {
                    sum += c.takeBanknote(quantity[i]);
                    i++;
                }
                System.out.println("Выданы средства в количестве: " + sum);
                return quantity;
            }
        } else {
            throw new IllegalArgumentException("Запросите положительное число банкнот.");
        }
    }

    public int sumOut() {
        int sum = 0;
        for (Cell c : cellMasses) {
            sum += c.getQuantity()*c.getParBanknote();
        }
        System.out.println("Сумма средств в банкомате:" + sum);
        return sum;
    }

}
