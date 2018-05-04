package language.exceptions;

import language.base.Instruction;

public class IllegalArgumentType extends Exception {
    private Instruction.ArgumentCount required;
    public IllegalArgumentType(){};
    public IllegalArgumentType(String message, Instruction.ArgumentCount req){
        super(message);
        required = req;
    }
    public void setRequired(Instruction.ArgumentCount required){
        this.required = required;
    }
    @Override
    public String toString() {
        return "Invalid arguments used: \nthe instruction does not support the given arguments. \n" +
                "Required arguments are"+
                getRequiredString()
                +super.getMessage();
    }
    private String getRequiredString(){
        return required.toString();
    }
}