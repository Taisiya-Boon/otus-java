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
        for(int i = 0; i < atms.size(); i++) {
            sum += atms.get(i).onUpdate(Opcode.RETURN_SUM);
        }
//        int sum = atms.stream().mapToInt(atms -> atms.onUpdate(Opcode.RETURN_SUM)).sum();
        return sum;
    }

    @Override
    public void initialState() {
        for(int i = 0; i < atms.size(); i++) {
            atms.get(i).onUpdate(Opcode.RETURN_INITIAL_STATE);
        }
//        atms.stream().map(atms -> atms.onUpdate(Opcode.RETURN_INITIAL_STATE));
    }

}
