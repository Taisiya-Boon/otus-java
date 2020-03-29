package ru.otus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса DepartmentATM ")
public class DepartmentATMTest {

    private final static ParBanknote[] parBanknotes = new ParBanknote[]{ParBanknote.TEN, ParBanknote.FIFTY, ParBanknote.ONE_HUNDRED, ParBanknote.FIVE_HUNDRED, ParBanknote.THOUSAND, ParBanknote.FIVE_THOUSAND};

    private DepartmentATM departmentATM;
    private ATMAsDepartmentElement depATM1;
    private ATMAsDepartmentElement depATM2;
    private ATMAsDepartmentElement depATM3;
    private ATMImpl atm1;
    private ATMImpl atm2;
    private ATMImpl atm3;
    private int[] quantityBanknote1;
    private int[] quantityBanknote2;
    private int[] quantityBanknote3;

    @BeforeEach
    public void setUp(){
        quantityBanknote1 = new int[]{0, 0, 0, 0, 0, 0};
        quantityBanknote2 = new int[]{2, 4, 3, 2, 4, 1};
        quantityBanknote3 = new int[]{3, 0, 7, 5, 0, 3};
        atm1 = new ATMImpl(quantityBanknote1, parBanknotes);
        atm2 = new ATMImpl(quantityBanknote2, parBanknotes);
        atm3 = new ATMImpl(quantityBanknote3, parBanknotes);
        depATM1 = new ATMAsDepartmentElement(atm1);
        depATM2 = new ATMAsDepartmentElement(atm2);
        depATM3 = new ATMAsDepartmentElement(atm3);
        departmentATM = new DepartmentATM();
        departmentATM.addATM(depATM1);
        departmentATM.addATM(depATM2);
        departmentATM.addATM(depATM3);
    }

    @Test
    @DisplayName("должен возвращать сумму остатков на счёте всех ATM")
    void sumTheResiduals() {
        int sum = departmentATM.sumTheResiduals();

        assertEquals(0 + 10520 + 18230, sum);
    }

    @Test
    @DisplayName("должен возвращать все ATM к начальному состоянию")
    void initialState() {
        int[] money = {10, 50, 100, 100, 10, 1000, 10};
        ArrayList quantityBanknotes1 = new ArrayList();
        ArrayList quantityBanknotes2 = new ArrayList();
        ArrayList quantityBanknotes3 = new ArrayList();

        Collections.addAll(quantityBanknotes1, quantityBanknote1);
        Collections.addAll(quantityBanknotes2, quantityBanknote2);
        Collections.addAll(quantityBanknotes3, quantityBanknote3);
        departmentATM.getATM(0).addMoney(money);
        departmentATM.getATM(2).addMoney(money);
        departmentATM.getATM(2).addMoney(money);
        departmentATM.getATM(1).addMoney(money);
        departmentATM.initialState();

        assertArrayEquals(quantityBanknotes1.toArray(), departmentATM.getATM(0).addMoney(new int[]{}).toArray());
        assertArrayEquals(quantityBanknotes2.toArray(), departmentATM.getATM(1).addMoney(new int[]{}).toArray());
        assertArrayEquals(quantityBanknotes3.toArray(), departmentATM.getATM(2).addMoney(new int[]{}).toArray());
    }

    @AfterEach
    public void tearDown() {
        departmentATM.removeATM(depATM1);
        departmentATM.removeATM(depATM2);
        departmentATM.removeATM(depATM3);
    }

}
