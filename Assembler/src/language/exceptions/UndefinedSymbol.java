package language.exceptions;

public class UndefinedSymbol extends Exception {
    public UndefinedSymbol(String name){
        super("Symbol "+name+" is not defined");
    }

}
