package language.runtime;

import language.base.Argument;
import language.base.Instruction;
import language.exceptions.MultiplyDefinedSymbol;
import language.exceptions.TablesNotReady;
import util.Utility;

import java.util.*;

public class RuntimeTables {
    /*
    * The assembler uses/manipulates the following tables at runtime:
    * 1.commands : Stores the predefined opcodes, directives that the assembler supports
    * 2.symbolTable: maps symbol names to SymbolTableEntry
    * 3.literalTable: maps literal value to stored location*/
    private static boolean ready;
    private static final HashMap<String, Instruction> commands = new HashMap<>();
    private static HashMap<String, SymbolTableEntry> symbolTable;
    private static HashMap<String, Integer> literalTable;

    public static void initialise(){
        commands.put("JMP", new Instruction(Instruction.ArgumentCount.ONE_ARG_S, Instruction.InstructionType.OPCODE, "JMP", 6, 0x51));
        commands.put("JNZ", new Instruction(Instruction.ArgumentCount.ONE_ARG_S, Instruction.InstructionType.OPCODE, "JNZ", 6, 0x52));

        commands.put("MOVR", new Instruction(Instruction.ArgumentCount.TWO_ARGS_R_ANY, Instruction.InstructionType.OPCODE, "MOVR", 10, 0xA1));
        commands.put("MOVM", new Instruction(Instruction.ArgumentCount.TWO_ARGS_R_S, Instruction.InstructionType.OPCODE, "MOVM", 10, 0xA2));
        commands.put("ADD", new Instruction(Instruction.ArgumentCount.TWO_ARGS_R_ANY, Instruction.InstructionType.OPCODE, "ADD", 6, 0xA3));
        commands.put("SUB", new Instruction(Instruction.ArgumentCount.TWO_ARGS_R_ANY, Instruction.InstructionType.OPCODE, "SUB", 6, 0xA4));
        commands.put("MUL", new Instruction(Instruction.ArgumentCount.TWO_ARGS_R_R, Instruction.InstructionType.OPCODE, "MUL", 4, 0xA5));
        commands.put("DIV", new Instruction(Instruction.ArgumentCount.TWO_ARGS_R_R, Instruction.InstructionType.OPCODE, "DIV", 4, 0xA6));

        commands.put("START", new Instruction(Instruction.ArgumentCount.ONE_ARG_L, Instruction.InstructionType.DIRECTIVE, "START"));
        commands.put("END", new Instruction(Instruction.ArgumentCount.NO_ARGS, Instruction.InstructionType.DIRECTIVE, "END"));
        commands.put("STOP", new Instruction(Instruction.ArgumentCount.NO_ARGS, Instruction.InstructionType.DIRECTIVE, "STOP"));
        commands.put("LTORG", new Instruction(Instruction.ArgumentCount.NO_ARGS, Instruction.InstructionType.DIRECTIVE, "LTORG"));

        commands.put("DS", new Instruction(Instruction.ArgumentCount.ONE_ARG_L, Instruction.InstructionType.DECLARATIVE, "DD", 0xF1));
        commands.put("DC", new Instruction(Instruction.ArgumentCount.ONE_ARG_L, Instruction.InstructionType.DECLARATIVE, "DC", 0xF2));

        symbolTable = new HashMap<>();
        literalTable = new HashMap<>();

        ready = true;
    }

    private static void checkReady() throws TablesNotReady{
        if(!ready) throw new TablesNotReady();
    }
    public static Instruction get(String command) throws TablesNotReady{
        checkReady();
        if(command == null) return null;
        return commands.get(command);
    }

    public static void assertSymbol(String symbolName) throws TablesNotReady{
        checkReady();
        symbolTable.putIfAbsent(symbolName, new SymbolTableEntry(symbolName));
    }
    public static void defineSymbol(String symbolName, int loc) throws TablesNotReady, MultiplyDefinedSymbol{
        checkReady();
        SymbolTableEntry entry = symbolTable.putIfAbsent(symbolName, new SymbolTableEntry(symbolName, loc));
        if(entry!=null) {
            entry.addDefinitionPoint(loc);
            if (entry.getType() == SymbolTableEntry.SymbolType.MULTIPLY_DEFINED)
                throw new MultiplyDefinedSymbol(symbolName, loc);
        }
    }
    public static void putSymbol(SymbolTableEntry e) throws TablesNotReady{
        checkReady();
        symbolTable.put(e.getName(), e);
    }
    public static String getPrintableSymbols(){
        Set<Map.Entry<String, SymbolTableEntry>> entries= symbolTable.entrySet();
        if(entries.size() == 0) return "EMPTY";
        StringBuilder printable= new StringBuilder(String.format("%-4s", "LC"))
                .append(" : ")
                .append(String.format("%-20s", "SYMBOL NAME"))
                .append(String.format("%-20s\n", "SYMBOL TYPE"));
        printable.append(Utility.soManyDashes(47));
        for(Map.Entry<String, SymbolTableEntry> e: entries ){
            printable.append(e.getValue().toString()).append("\n");
        }
        return printable.toString();
    }

    public static void assertLiteral(String literal) throws TablesNotReady{
        checkReady();
        literalTable.putIfAbsent(literal, -1);
    }
    public static int defineLiterals(int from){
        Set<Map.Entry<String, Integer>> entries = literalTable.entrySet();
        int pos_curr= from;
        for(Map.Entry<String, Integer> e: entries){
            if(e.getValue() == -1){
                e.setValue(pos_curr);
                pos_curr += 4;
            }
        }
        return pos_curr;
    }
    public static void putLiteral(String name, int pos) throws TablesNotReady{
        checkReady();
        literalTable.put(name, pos);
    }
    public static String getPrintableLiterals(){
        Set<Map.Entry<String, Integer>> entries = literalTable.entrySet();
        if(entries.size() == 0) return "EMPTY";
        StringBuilder printable = new StringBuilder(String.format("%-4s", "LC"))
                .append(" : ")
                .append(String.format("%-20s\n", "LITERAL VALUE"));
        printable.append(Utility.soManyDashes(27));
        for(Map.Entry<String, Integer> e: entries){
            printable.append(String.format("%-4d", e.getValue())).append(" : ").append(String.format("%-20s", e.getKey())).append("\n");
        }
        return printable.toString();
    }

    public static int findLocationFor(Argument argument){
        System.out.println("resolving "+argument.getType()+":"+argument.getName());
        switch (argument.getType()){
            case SYMBOL:
                SymbolTableEntry entry = symbolTable.get(argument.getName());
                if(entry!=null)
                    return symbolTable.get(argument.getName()).getLocation();
                break;
            case LITERAL:
                return literalTable.get(argument.getName());
        }
        return -1;
    }
}
