package language.exceptions;

import language.base.Instruction;

public class IllegalArgumentCount extends Exception {
    private String requiredCount;
    public IllegalArgumentCount(String message, Instruction.ArgumentCount required){
        super(message);
        requiredCount = required.toString();
    }

    @Override
    public String toString() {
        return "Too Many/Too Few Arguments: \nThe opcode does not support the number of arguments specified. \n" +
                "Instruction requires: "+requiredCount+" arguments.\n"+super.getMessage();
    }
}
