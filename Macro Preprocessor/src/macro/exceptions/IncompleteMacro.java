package macro.exceptions;

public class IncompleteMacro extends Exception {
    public IncompleteMacro(String about){
        super(about);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
