package language.base;

import language.exceptions.InvalidInstruction;
import language.exceptions.TablesNotReady;
import language.runtime.RuntimeTables;

public class Instruction implements Comparable<Instruction>{
/*
	Instruction are classified as Opcodes, variable declarations, ans assembler directives.
	ArgumentCount specifies the type of arguments an instruction may take. Elaborated further at definition.
	InstructionType:
		1.OPCODE - A standard machine instruction to be translated in pass II.
		2.DIRECTIVE - An instruction meant for the assembler only. Will be interpreted by assembler and then discarded.
				It does not have a hexcode or a size. However it may have a literal argument in cases.
		3.DECLARATIVE - A variable declaration. <instr> <var-name> <bytes/value> 
*/
    public enum ArgumentCount {
        //Classify on the basis of the count of arguments
        NO_ARGS("No Arguments"),
        //Further classify based on type of argument: R-Register, L-Literal, S-Symbol, A-Any
        ONE_ARG_R("Single Register Argument"),
        ONE_ARG_L("Single Literal Argument"),
        ONE_ARG_S("Single Symbol Argument"),
        ONE_ARG_ANY("Single Argument"),

        TWO_ARGS_R_R("Register, Register as Arguments"),
        TWO_ARGS_R_L("Register, Literal as Arguments"),
        TWO_ARGS_R_S("Register, Symbol as Arguments"),
        TWO_ARGS_S_ANY("Symbol, Any-Type as Arguments"),
        TWO_ARGS_R_ANY("Register, Any-Type as Arguments"),
        TWO_ARGS_S_L("Symbol, Literal as Arguments");


        private String string;
        ArgumentCount(String string){
            this.string = string;
        }
        public String getValue(){
            return super.toString();
        }

        @Override
        public String toString() {
            return string;
        }
    }
    public enum InstructionType {
        DIRECTIVE, OPCODE, DECLARATIVE
    }
    private ArgumentCount count;
    private InstructionType instructionType;
    private String name;
    private int size;
    private int hexcode;

    public Instruction(ArgumentCount count, InstructionType instructionType, String name, int size, int hexcode) {
        this.count = count;
        this.name = name;
        this.instructionType = instructionType;
        this.size =size;
	    this.hexcode=hexcode;
    }
    public Instruction(ArgumentCount count, InstructionType instructionType, String name, int hexcode) {
        this.count = count;
        this.name = name;
        this.instructionType = instructionType;
        if(instructionType != InstructionType.OPCODE)
            this.size = 0;
        this.hexcode=hexcode;
    }
    public Instruction(ArgumentCount count, InstructionType instructionType, String name) {
        this.count = count;
        this.name = name;
        this.instructionType = instructionType;
        if(instructionType != InstructionType.OPCODE)
            this.size = 0;
	    this.hexcode=0;
    }

    public int getSize() {
        return size;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }

    public ArgumentCount getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public int getHexcode() {
        return hexcode;
    }

    public static Instruction readSelfFrom(String text) throws InvalidInstruction{
        if(text == null) return null;
        text = text.trim().toUpperCase();
        Instruction instruction = null;
        try {
            instruction = RuntimeTables.get(text);
            if(instruction==null) throw new InvalidInstruction(text);
        }
        catch (TablesNotReady exc){
            System.err.println(exc.getMessage());
            System.exit(1);
        }
        return instruction;
    }
    @Override
    public int compareTo(Instruction instruction) {
        if(instruction.name.equalsIgnoreCase(name)){
            if(instruction.count == count) return 0;
            else return count.compareTo(instruction.count);
        }
        return name.compareTo(instruction.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
