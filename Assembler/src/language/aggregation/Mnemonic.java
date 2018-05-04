package language.aggregation;

import language.base.Argument;
import language.base.Instruction;
import language.exceptions.IllegalArgumentCount;
import language.exceptions.IllegalArgumentType;
import language.exceptions.InvalidInstruction;
import language.runtime.RuntimeTables;
import java.util.regex.Pattern;

public class Mnemonic implements Comparable<Mnemonic> {

    private Argument[] arguments;
    private Instruction instruction;

    private Mnemonic(){
        instruction = null;
        arguments = null;
    }

    @Override
    public int compareTo(Mnemonic mnemonic) {
        if(instruction.compareTo(mnemonic.instruction)==0){
            if(mnemonic.arguments.length == arguments.length){
                for (int i=0; i<arguments.length; i++){
                    if(arguments[i]!= mnemonic.arguments[i]){
                        return arguments[i].compareTo(mnemonic.arguments[i]);
                    }
                }
                return 0;
            }
            return Integer.compare(arguments.length, mnemonic.arguments.length);
        }
        return instruction.compareTo(mnemonic.instruction);
    }

    public static Mnemonic readSelfFrom(String text) throws IllegalArgumentCount, IllegalArgumentType, InvalidInstruction{
        /*
        * Read a mnemonic from its source. Algorithm:
        * 1. check for empty/blank/false syntax instances
        * 2. obtain tokens by splitting about ' ' and ','
        * 3. call readSelfFrom for the respective base types of tokens
        * 4. aggregate base arguments and resolve instruction type
        * 5. done
        * */
        if(text == null) return null;
        text = text.trim();
        if(text.length() == 0) return null;
        Pattern pattern = Pattern.compile("[, +]");
        String[] tokens = pattern.split(text, 3);
        Mnemonic mnemonic = new Mnemonic();
        Argument[] arguments = new Argument[tokens.length-1];
        Instruction instruction = Instruction.readSelfFrom(tokens[0]);
        for(int i=1; i<tokens.length; i++){
            try {
                arguments[i - 1] = Argument.readSelfFrom(tokens[i]);
            }
            catch (IllegalArgumentType exc){
                exc.setRequired(instruction.getCount());
            }
        }
        switch (arguments.length){
            case 0:
                if(instruction.getCount() != Instruction.ArgumentCount.NO_ARGS) throw new IllegalArgumentCount(text, instruction.getCount());
                break;
            case 1:
                if(!instruction.getCount().getValue().contains("ONE_ARG")) throw new IllegalArgumentCount(text, instruction.getCount());
                switch (instruction.getCount()){
                    case ONE_ARG_L:
                        if(arguments[0].getType() != Argument.ArgumentType.LITERAL) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case ONE_ARG_R:
                        if(arguments[0].getType() != Argument.ArgumentType.REGISTER) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case ONE_ARG_S:
                        if(arguments[0].getType() != Argument.ArgumentType.SYMBOL) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case ONE_ARG_ANY:
                        break;
                }
                break;
            case 2:
                if(!instruction.getCount().getValue().contains("TWO_ARG")) throw new IllegalArgumentCount(text, instruction.getCount());
                switch (instruction.getCount()){
                    case TWO_ARGS_R_L:
                        if(arguments[0].getType() != Argument.ArgumentType.REGISTER || arguments[1].getType() != Argument.ArgumentType.LITERAL) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case TWO_ARGS_R_R:
                        if(arguments[0].getType() != Argument.ArgumentType.REGISTER || arguments[1].getType() != Argument.ArgumentType.REGISTER) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case TWO_ARGS_R_S:
                        if(arguments[0].getType() != Argument.ArgumentType.REGISTER || arguments[1].getType() != Argument.ArgumentType.SYMBOL) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case TWO_ARGS_R_ANY:
                        if(arguments[0].getType() != Argument.ArgumentType.REGISTER) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case TWO_ARGS_S_ANY:
                        if(arguments[0].getType() != Argument.ArgumentType.SYMBOL) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                    case TWO_ARGS_S_L:
                        if(arguments[0].getType() != Argument.ArgumentType.SYMBOL || arguments[1].getType() != Argument.ArgumentType.LITERAL) throw new IllegalArgumentType(text, instruction.getCount());
                        break;
                }
        }
        mnemonic.setArguments(arguments);
        mnemonic.setInstruction(instruction);

        return mnemonic;
    }

    public Argument[] getArguments() {
        return arguments;
    }

    private void setArguments(Argument[] arguments) {
        this.arguments = arguments;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    private void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public static void main(String[] args) throws Exception{
        RuntimeTables.initialise();
        System.out.println(readSelfFrom("JNZ ROW, ABC"));
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(instruction.toString()).append("\t");
        if(arguments.length==1){
            builder.append(arguments[0]);
        }
        if(arguments.length==2){
            builder.append(arguments[0]).append(",").append(arguments[1]);
        }
        return builder.toString();
    }
}
