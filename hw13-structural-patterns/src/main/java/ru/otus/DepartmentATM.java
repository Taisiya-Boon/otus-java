package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class DepartmentATM implements Department {

    private List<Listener> atms = new ArrayList<>();

    public void addATM(ATMAsDepartmentElement listener){
        atms.add(listener);
    }

    public void removeATM(ATMAsDepartmentElement listener){
        atms.remove(listener);
    }

    public ATMImpl getATM(int index) {
        return ((ATMAsDepartmentElement)atms.get(index)).getAtm();
    }

    @Override
    public int sumTheResiduals() {
        int sum = 0;
        for(Listener listener: atms) {
            sum += listener.onUpdate(Opcode.RETURN_SUM);
        }
        return sum;
    }

    @Override
    public void initialState() {
        for(Listener listener: atms) {
            listener.onUpdate(Opcode.RETURN_INITIAL_STATE);
        }
    }

}
