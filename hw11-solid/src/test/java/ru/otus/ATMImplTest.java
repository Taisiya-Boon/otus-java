package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса ATM ")
class ATMImplTest {

    private ATMImpl atmImpl;
    private int[] quantityBanknote;
    private ParBanknote[] parBanknotes;

    @BeforeEach
    public void setUp() {
        quantityBanknote = new int[]{3, 2, 0, 4, 2, 5};
        parBanknotes = new ParBanknote[]{ParBanknote.TEN, ParBanknote.FIFTY, ParBanknote.ONE_HUNDRED, ParBanknote.FIVE_HUNDRED, ParBanknote.THOUSAND, ParBanknote.FIVE_THOUSAND};
        atmImpl = new ATMImpl(quantityBanknote, parBanknotes);
    }

    @Test
    @DisplayName("должен принять банкноты и положить их в соотвествующие ячейки")
    void addMoney() {
        int[] userMoney = {10, 50, 100, 100, 10, 1000, 10};

        int[] atmMoney = atmImpl.addMoney(userMoney);
        quantityBanknote[0] += 3;
        quantityBanknote[1] += 1;
        quantityBanknote[2] += 2;
        quantityBanknote[4] += 1;

        assertArrayEquals(quantityBanknote, atmMoney);
    }

    @Test
    @DisplayName("должен принять не банкноту, так как это купюра несуществующего номинала")
    void errorAddMoney() {
        assertThrows(IllegalArgumentException.class, () -> atmImpl.addMoney(20), "Банкомат не принимает банкноты такого номинала.");
    }

    @Test
    @DisplayName("должен выдать заданную сумму имеющимися банкнотами")
    void takeMoney() {
        int[] buffer = atmImpl.takeMoney(3100);

        assertArrayEquals(new int[]{0, 2, 0, 2, 2, 0}, buffer);
    }

    @Test
    @DisplayName("должен выдать ошибку 'Недостаточно денег в банкомате. \nВыдача средств невозможна.'")
    void noMoney() {
        assertThrows(IllegalArgumentException.class, () -> atmImpl.takeMoney(140), "Недостаточно денег в банкомате. \nВыдача средств невозможна.");
    }

    @Test
    @DisplayName("должен выдать ошибку 'Запросите положительное число банкнот.'")
    void takeNegativeMoney() {
        assertThrows(IllegalArgumentException.class, () -> atmImpl.takeMoney(-100), "Запросите положительное число банкнот.");
    }

    @Test
    @DisplayName("должен выдать сумму имеющихся банкнот")
    void sumOut() {
        int sum = atmImpl.sumOut();

        assertEquals(3*10 + 2*50 + 0*100 + 4*500 + 2*1000 + 5*5000, sum);
    }
}