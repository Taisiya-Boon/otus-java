package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса Call ")
class CellImplTest {

    private static final ParBanknote PAR_BANKNOTE = ParBanknote.ONE_HUNDRED;
    private static final int QUANTITY = 3;

    private CellImpl cellImpl;

    @BeforeEach
    void SetUp() {
        cellImpl = new CellImpl(PAR_BANKNOTE);
        cellImpl.setQuantity(QUANTITY);
    }

    @Test
    @DisplayName("должен добавлять банкноту в ячейку")
    void addBanknote() {
        int quantity = cellImpl.getQuantity();

        cellImpl.addBanknote();

        assertEquals(quantity+1, cellImpl.getQuantity());
    }

    @Test
    @DisplayName("должен выдавать заданое количество купюр нужного номинала")
    void takeBanknote() {
        int banknote = cellImpl.getQuantity() - 1;
        int money = cellImpl.takeBanknote(banknote);

        assertEquals(banknote*PAR_BANKNOTE.getPar(), money);
    }

    @Test
    @DisplayName("должен выдавать ошибку 'Нет столько банкнот'")
    void noTakeBanknote() {
        assertThrows(IndexOutOfBoundsException.class, () -> cellImpl.takeBanknote(cellImpl.getQuantity()+3), "Нет столько банкнот");
    }

}