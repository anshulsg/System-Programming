package pass_II;

import language.exceptions.TablesNotReady;
import language.runtime.RuntimeTables;
import language.runtime.SymbolTableEntry;
import util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class Reader {
    private static Pattern tokenizer= Pattern.compile(" +");
    public static boolean readSymbols(){
        try {
            BufferedReader br = Files.newBufferedReader(Utility.FILE_SYMBOL, Charset.defaultCharset());
            removeHeader(br);
            String line;
            while ((line = br.readLine())!= null){
                SymbolTableEntry entry = SymbolTableEntry.readSelfFrom(line);
                RuntimeTables.putSymbol(entry);
            }
            System.out.println("SYMBOLS READ : "+RuntimeTables.getPrintableSymbols());
            return true;
        }
        catch (IOException| TablesNotReady exc){
            return false;
        }
    }
    public static boolean readLiterals(){
        try{
            BufferedReader br = Files.newBufferedReader(Utility.FILE_LITERAL, Charset.defaultCharset());
            removeHeader(br);
            String line;
            while ((line = br.readLine())!= null){
                String[] tokens= tokenizer.split(line);
                RuntimeTables.putLiteral(tokens[2], Integer.parseInt(tokens[0]));
            }
            System.out.println("LITERALS READ : "+ RuntimeTables.getPrintableLiterals());
            return true;
        }
        catch (IOException | TablesNotReady exc){
            return false;
        }
    }
    public static Record readRecordFrom(String text){
        String[] tokens= tokenizer.split(text);
        try {
            switch (tokens.length){
                case 4:
                    return Record.readSelfFrom(Integer.parseInt(tokens[0]), tokens[1], tokens[3]);
                case 5:
                    return Record.readSelfFrom(Integer.parseInt(tokens[0]), tokens[1], tokens[3], tokens[4]);
                case 6:
                    return Record.readSelfFrom(Integer.parseInt(tokens[0]), tokens[1], tokens[3], tokens[4], tokens[5]);
                default:
                    return null;

            }
        }
        catch (Exception exc){
            exc.printStackTrace();
            return null;
        }
        //pass_II.Record record = Record.readSelfFrom()
    }
    private static void removeHeader(BufferedReader br) throws IOException{
        if(br==null) return;
        br.readLine();
        br.readLine();
    }
    public static void main(String[] args) throws Exception{

    }

}
