package pass_II;

import language.base.Argument;
import language.base.Instruction;
import language.base.Register;
import language.exceptions.TablesNotReady;
import language.runtime.RuntimeTables;
import util.Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) throws Exception{
        RuntimeTables.initialise();
        Reader.readLiterals();
        Reader.readSymbols();
        BufferedReader br= Files.newBufferedReader(Utility.FILE_IC, Charset.defaultCharset());
        BufferedWriter bw= Files.newBufferedWriter(Utility.FILE_OUTPUT, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        makeHeader(bw);
        String line;
        StringBuilder mlang = new StringBuilder(64);
        while ((line = br.readLine())!=null) {
            Record curr = Reader.readRecordFrom(line);
            if(curr != null){
                if(curr.getInstruction().getInstructionType() == Instruction.InstructionType.OPCODE){
                    String lcString= String.format("%-8X",curr.getLoc());
                    String instructionHex = String.format("%-2X", curr.getInstruction().getHexcode());
                    Argument[] arguments = curr.getArgs();
                    //System.out.println("Assembling: "+curr.getInstruction()+" "+Arrays.toString(curr.getArgs()));
                    String argumentHex[];
                    if(arguments!=null){
                        argumentHex = new String[arguments.length];
                        switch (arguments.length){
                            case 2:
                                argumentHex[1] = toHexString(arguments[1]);
                                argumentHex[0] = toHexString(arguments[0]);
                                addAssembledTo(bw, lcString, instructionHex, argumentHex[0], argumentHex[1]);
                                break;
                                //Let it flow
                            case 1:
                                argumentHex[0] = toHexString(arguments[0]);
                                addAssembledTo(bw, lcString, instructionHex, argumentHex[0]);
                                break;
                        }
                    }

                }
            }
        }
        bw.flush();
        bw.close();
    }
    private static void addAssembledTo(BufferedWriter bw, String... tokens) throws IOException{
        StringBuilder builder = new StringBuilder(64);
        for(String token : tokens){
            builder.append(String.format("%-8s", token));
        }
        bw.write(builder.toString());
        bw.newLine();
    }
    private static void makeHeader(BufferedWriter bw) throws IOException{
        StringBuilder builder =
                new StringBuilder("128")
                        .append(String.format("%-8s","LC"))
                        .append(String.format("%-8s","OpCode"))
                        .append(String.format("%-16s", "Argument(s)"))
                        .append("\n")
                        .append(Utility.soManyDashes(32));
        bw.write(builder.toString());
        bw.newLine();
    }
    private static String toHexString(Argument argument) throws TablesNotReady{

        switch (argument.getType()){
            case REGISTER:
                return String.format("%-2X",Register.valueOf(argument.getName()).valueOf());
            case LITERAL:
                //Let it flow
            case SYMBOL:
                int location = RuntimeTables.findLocationFor(argument);
                if(location == -1){
                    return String.format("%-8s", "???");
                }
                return String.format("%-8X", location);
        }
        return null;
    }
}
