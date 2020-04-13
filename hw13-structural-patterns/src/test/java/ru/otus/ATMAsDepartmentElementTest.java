package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса ATMAsDepartmentElement ")
class ATMAsDepartmentElementTest {

    private ATMAsDepartmentElement atmAsDepartmentElement;
    private ATMImpl atm;
    private int[] quantityBanknote;
    private ParBanknote[] parBanknotes;

    @BeforeEach
    public void setUp() {
        quantityBanknote = new int[]{3, 2, 0, 4, 2, 5};
        parBanknotes = new ParBanknote[]{ParBanknote.TEN, ParBanknote.FIFTY, ParBanknote.ONE_HUNDRED, ParBanknote.FIVE_HUNDRED, ParBanknote.THOUSAND, ParBanknote.FIVE_THOUSAND};
        atm = new ATMImpl(quantityBanknote, parBanknotes);
        atmAsDepartmentElement = new ATMAsDepartmentElement(atm);
    }

    @Test
    @DisplayName("должен выдать сумму имеющихся банкнот")
    void onUpdateReturnSum() {
        assertEquals(atm.sumOut(), atmAsDepartmentElement.onUpdate(Opcode.RETURN_SUM));
    }

    @Test
    @DisplayName("должен возвращать банкомат к первоначальному состоянию")
    void onUpdateReturnInitialState() {
        int[] userMoney = {10, 50, 100, 100, 10, 1000, 10};
        ArrayList quantityBanknotes = new ArrayList();

        Collections.addAll(quantityBanknotes, quantityBanknote);
        atmAsDepartmentElement.getAtm().addMoney(userMoney);
        atmAsDepartmentElement.onUpdate(Opcode.RETURN_INITIAL_STATE);

        assertArrayEquals(quantityBanknotes.toArray(), atmAsDepartmentElement.getAtm().addMoney(new int[]{}).toArray());
    }

}