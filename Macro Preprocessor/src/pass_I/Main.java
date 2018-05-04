package pass_I;

import macro.base.MDT;
import macro.base.MNTEntry;
import macro.runtime.Tables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
    /*
    * Handles macro definitions.
    * Can be merged with macro expansions unless forward macro references are permitted( Design decision).
    * Output is the changed source file which has no macro definitions,
    * along with the MNT, MDT
    * */
    private StringBuilder updatedSourceText;
    private BufferedReader br;
    public Main() throws IOException{
        MDT.init();
        updatedSourceText = new StringBuilder();
        br = Files.newBufferedReader(Utility.SOURCE_FILE, Charset.defaultCharset());
    }
    private void readMacroDefinition(){
        String line;
        MNTEntry currentMacro;
        try{
            currentMacro = MNTEntry.readSelfFrom(br.readLine());
            if(currentMacro == null) return;
            currentMacro.setDefinition(MDT.getCurrentIndex());
            while ((line= br.readLine())!=null){
                if(line.trim().equalsIgnoreCase("MEND")) break;
                MDT.append(line);
            }
            MDT.append("MEND");
            Tables.makeMacroEntry(currentMacro);
        }
        catch (IOException exc){
            System.err.println("Error in reading macro definition / header");
        }
    }
    public void readSource(){
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String temp= line.trim();
                if(temp.equalsIgnoreCase("MACRO")){
                    readMacroDefinition();
                    continue;
                }
                updatedSourceText.append(line).append("\n");

            }
        }
        catch (IOException exc){}
    }
    public void commitSource() {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Utility.SOURCE_FILE, StandardOpenOption.TRUNCATE_EXISTING);
            bw.write(updatedSourceText.toString());
            bw.flush();
            bw.close();
        }
        catch (IOException exc){
            System.err.println("Error in committing source file.");
        }
    }
    public static void main(String[] args) throws Exception {
        Main reader = new Main();
        System.out.println("Starting Pass I...");
        reader.readSource();
        reader.commitSource();
        System.out.println("Pass I finished.\nTable Contents \n"+Tables.getPrintable());
        System.out.println("Starting Pass II...");
        pass_II.Main.main(null);
        System.out.println("Done.");
    }

}
