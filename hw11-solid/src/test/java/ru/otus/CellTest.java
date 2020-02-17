package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса Call ")
class CellTest {

    private static final int PAR_BANKNOTE = 100;
    private static final int QUANTITY = 3;

    private Cell cell;

    @BeforeEach
    void SetUp() {
        cell = new Cell(PAR_BANKNOTE);
        cell.setQuantity(QUANTITY);
    }

    @Test
    @DisplayName("должен добавлять банкноту в ячейку")
    void addBanknote() {
        int quantity = cell.getQuantity();

        cell.addBanknote();

        assertEquals(quantity+1, cell.getQuantity());
    }

    @Test
    @DisplayName("должен выдавать заданое количество купюр нужного номинала")
    void takeBanknote() {
        int banknote = cell.getQuantity() - 1;
        int money = cell.takeBanknote(banknote);

        assertEquals(banknote*PAR_BANKNOTE, money);
    }

    @Test
    @DisplayName("должен выдавать ошибку 'Нет столько банкнот'")
    void noTakeBanknote() {
        assertThrows(IndexOutOfBoundsException.class, () -> cell.takeBanknote(cell.getQuantity()+3), "Нет столько банкнот");
    }

}