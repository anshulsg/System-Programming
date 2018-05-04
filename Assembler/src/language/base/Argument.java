package language.base;

import language.exceptions.IllegalArgumentType;

import java.util.regex.Pattern;

/*
 *Arguments for a Instruction(Instruction).
 *Arguments can be literals, symbols or predefined registers.
 */
public class Argument implements Comparable<Argument>{
    public enum ArgumentType{
        REGISTER, LITERAL, SYMBOL
    }
    private static final Pattern format_reg_symbol= Pattern.compile("[A-Za-z][A-Za-z_0-9]*");
    private static final Pattern format_literal = Pattern.compile("[\"'].*[\"']");
    private static final Pattern format_literal_num = Pattern.compile("[0-9x]*");
    private String name;
    private ArgumentType type;
    private Argument(String name, ArgumentType type){
        this.name= name;
        this.type= type;
    }

    public String getName() {
        return name;
    }

    public ArgumentType getType() {
        return type;
    }

    public static Argument readSelfFrom(String text) throws IllegalArgumentType{
        if(text == null) return null;
        text = text.trim();
        if(text.length() == 0) return null;
        if(format_reg_symbol.matcher(text).matches()){
            for(Register reg: Register.values()){
                if(reg.toString().equalsIgnoreCase(text)){
                    return new Argument(text, ArgumentType.REGISTER);
                }
            }
            return new Argument(text, ArgumentType.SYMBOL);
        }
        if(format_literal.matcher(text).matches() || format_literal_num.matcher(text).matches()){
            return new Argument(text, ArgumentType.LITERAL);
        }
        throw new IllegalArgumentType();
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public int compareTo(Argument argument) {
        if(argument.type == type) return 0;
        else return type.toString().compareTo(argument.type.toString());
    }
    public static String getString(Argument[] args){
        if(args == null) return null;
        String printable="";
        for(Argument arg: args){
            printable+=String.format("%-20s", arg);
        }
        return printable;
    }
}
