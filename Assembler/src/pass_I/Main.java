package pass_I;

import language.aggregation.Line;
import language.aggregation.Mnemonic;
import language.base.Argument;
import language.base.AssemblerDirectives;
import language.base.Label;
import language.exceptions.*;
import language.runtime.RuntimeTables;
import util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Vector;

import static language.base.AssemblerDirectives.END;
import static language.base.AssemblerDirectives.START;
import static language.base.AssemblerDirectives.STOP;
import static util.Utility.FILE_SYMBOL;

public class Main {
    /*
    Main Class for pass-1.
    Transforms a readable text file into an array of records -> (equivalent to an intermediate file)
    Also creates entries for the symbol table
    * */
    /*
    * Steps to occur for each line:
    * 1.read line
    * 2.verify basic syntax
    * 3.extract tokens
    * 4.if label tokens are found create symbol table entry
    * 5.find instruction size from MOT if opcodes exist
    * 6.update LC
    * 7.Create record and append
    * */


    private BufferedReader fileStream;
    private int locationCounter;
    private Vector<Record> intermediateCode;
    private AssemblerDirectives assemblerState;
    private boolean hasErrors;
    private Main(){
        assemblerState = null;
        locationCounter = 0;
        intermediateCode = new Vector<>();
    }
    private void setStreamFor(Path path) {
        Charset charset = Charset.forName("US-ASCII");
        try {
            fileStream = Files.newBufferedReader(path, charset);
        }
        catch (IOException exc){
            System.out.println("Error when reading from file. Try:\n" +
                    "1.checking for file permissions\n" +
                    "2.checking for file existence\n");
            fileStream = null;
        }
    }
    private void makeFiles(){
        try{

            Files.write(
                    Utility.FILE_IC,
                    toString().getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE
            );
            Files.write(
                    Utility.FILE_SYMBOL,
                    RuntimeTables.getPrintableSymbols().getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE
            );
            Files.write(
                    Utility.FILE_LITERAL,
                    RuntimeTables.getPrintableLiterals().getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE
            );
        }
        catch (IOException exc){
            System.out.println("Error when making intermediate file");
            exc.printStackTrace();
        }
    }
    private boolean assemble() throws IOException{
        if(fileStream == null) return false;
        String line_source;
        while ((line_source = fileStream.readLine()) != null) {
            Record record = new Record(locationCounter);
            try {
                Line curr = Line.readSelfFrom(line_source);
                if(curr != null){
                    record.set(locationCounter, curr);
                    Label label = curr.getLabel();
                    if(label!=null){
                        try {
                            RuntimeTables.defineSymbol(label.getName(), locationCounter);
                        }
                        catch (MultiplyDefinedSymbol exc){
                            record.setError(exc);
                        }
                    }
                    Mnemonic mnemonic = curr.getMnemonic();
                    if(mnemonic != null){
                        switch (mnemonic.getInstruction().getInstructionType()){
                            case DECLARATIVE:
                                if(assemblerState== START || assemblerState == STOP) {
                                    Argument[] args = mnemonic.getArguments();
                                    try {
                                        if(label == null) throw new NullPointerException();
                                        if(mnemonic.getInstruction().getName().equalsIgnoreCase("DC")){
                                            locationCounter+= 4;
                                        }
                                        else{
                                            locationCounter+= Integer.parseInt(args[0].toString());
                                        }
                                    }
                                    catch (NumberFormatException exc){
                                        record.setError(new Exception("Incorrect argument type for line"+ line_source+"at"+locationCounter));
                                    }
                                    catch (NullPointerException exc){
                                        record.setError(new Exception("No Symbol found for line"+ line_source+"at"+locationCounter));
                                    }
                                }
                                else {
                                    record.setError(new Exception("Wrong point for variable declaration."));
                                }
                                break;
                            case DIRECTIVE:
                                switch (AssemblerDirectives.valueOf(mnemonic.getInstruction().getName())){
                                    case START:
                                        if(locationCounter != 0 || assemblerState !=null){
                                            record.setError(new Exception("Invalid use of directive START at "+ locationCounter));
                                        }
                                        assemblerState = AssemblerDirectives.START;
                                        try{
                                            locationCounter=Integer.parseInt(mnemonic.getArguments()[0].getName().trim());
                                        }
                                        catch (NumberFormatException exc){
                                            record.setError(new Exception("Argument of START must be an integer"));
                                            break;
                                        }
                                        break;
                                    case END:
                                        if(assemblerState!=STOP){
                                            record.setError(new Exception("No stop before end."+locationCounter));
                                        }
                                        assemblerState = END;
                                        break;
                                    case STOP:
                                        if(assemblerState != START){
                                            record.setError(new Exception("Invalid use of STOP before START. at"+locationCounter));
                                        }
                                        assemblerState = STOP;
                                        locationCounter = RuntimeTables.defineLiterals(locationCounter);
                                        break;
                                    case LTORG:
                                        locationCounter = RuntimeTables.defineLiterals(locationCounter);
                                        break;
                                }
                                break;
                            case OPCODE:
                                if(assemblerState != START){
                                    record.setError(new Exception("Expected START before this instruction"));
                                }
                                for (Argument arg: mnemonic.getArguments()){
                                    switch (arg.getType()){
                                        case REGISTER: continue;
                                        case LITERAL:
                                            RuntimeTables.assertLiteral(arg.getName());
                                            break;
                                        case SYMBOL:
                                            RuntimeTables.assertSymbol(arg.getName());
                                            break;
                                    }
                                }
                                break;
                        }
                        locationCounter += mnemonic.getInstruction().getSize();
                    }
                }
            }
            catch (IllegalArgumentType | IllegalArgumentCount | InvalidInstruction exc){
                record.setError(exc);
            }
            catch (TablesNotReady exc){
                System.err.print(exc.getMessage());
                System.exit(1);
            }
            if(record.hasError()) hasErrors = true;
            intermediateCode.add(record);
        }
        return !hasErrors;
    }

    @Override
    public String toString() {
        StringBuilder str= new StringBuilder(String.format("%-4s", "LC"))
                .append(String.format("%-20s", "INSTRUCTION TYPE"))
                .append(String.format("%-20s", "LABEL/SYMBOL DEFN"))
                .append(String.format("%-20s", "OPCODE"))
                .append(String.format("%-40s\n", "ARGUMENT(S)"));
        str.append(Utility.soManyDashes(104));
        for(Record record : intermediateCode){
            String recordText= record.toString();
            if(recordText!=null)
                str.append(record).append("\n");
        }
        return str.toString();
    }

    private String getAllErrors() {
        if(!hasErrors) return "No Errors";
        StringBuilder str= new StringBuilder();
        int line = 1;
        for(Record record : intermediateCode){
            if(record.hasError()){
                str.append("At Line ").append(line).append(":");
                str.append(record.getException().toString());
            }
            line++;
        }
        return str.toString();
    }


    public static void main(String[] args) {

        //String filename;
        Main main = new Main();
        RuntimeTables.initialise();
        //BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        try {
            //System.out.println("Enter the path and name for the file to be assembled:");
            //filename = br.readLine();
            main.setStreamFor(Utility.FILE_INPUT);
            if(main.assemble()){
                System.out.println("Assembled without any errors.");
            }

            System.out.println(main);
            System.out.println(RuntimeTables.getPrintableSymbols());
            System.out.println(RuntimeTables.getPrintableLiterals());
            System.out.println("ERRORS: \n"+main.getAllErrors());

            main.makeFiles();
        }
        catch (IOException exc){
            System.out.println("Error when reading from keyboard. Please try again.");
        }
    }

}
