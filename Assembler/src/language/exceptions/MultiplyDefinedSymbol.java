package language.exceptions;

public class MultiplyDefinedSymbol extends Exception {
    public MultiplyDefinedSymbol(String name, int at){
        super("Symbol "+name+" redefined at "+ at);
    }
}
