package ru.otus;

import lombok.Getter;

public class ATMAsDepartmentElement implements Listener {

    @Getter
    private ATMImpl atm;
    private Stack stack  = new Stack();

    public ATMAsDepartmentElement(ATMImpl atm) {
        this.atm = atm;
        stack.saveATM(atm);
    }

    @Override
    public int onUpdate(Opcode opcode) {
        switch (opcode) {
            case RETURN_SUM:
                return atm.sumOut();
            case RETURN_INITIAL_STATE:
                this.atm = (ATMImpl) stack.restoreATM();
                return 1;
            default:
                throw new IllegalArgumentException("Нет такой команды.");
        }
    }

}
