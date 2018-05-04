package language.exceptions;

public class InvalidInstruction extends Exception {
    public InvalidInstruction(String message){
        super(message);
    }

    @Override
    public String toString() {
        return "Invalid instruction used: \nthe instruction is not a valid Opcode or Directive. \n"+super.getMessage();
    }
}
