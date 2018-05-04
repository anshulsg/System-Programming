package pass_II;

import macro.base.MDT;
import macro.base.MDirectives;
import macro.base.MNTEntry;
import macro.base.Parameter;
import macro.runtime.Tables;
import pass_I.Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {
    private StringBuilder updatedSource;
    private BufferedReader br;
    private static final Pattern tokenizer = Pattern.compile("[ :,;]+");
    public Main() throws IOException{
        updatedSource = new StringBuilder();
        br=Files.newBufferedReader(Utility.SOURCE_FILE, Charset.defaultCharset());
    }
    public void beginExpansion(MNTEntry entry, String[] paramVals){
        int data_ptr= entry.getDefinition();
        HashMap<String, Parameter> actualParams = entry.createActualParameterTable(paramVals);

        String line;
        StringBuilder expansion = new StringBuilder();
        do{
            line=MDT.getLine(data_ptr++);
            if(line.equalsIgnoreCase("MEND")) break;
            if(MDirectives.checkDirectives(line)){
                data_ptr = MDirectives.handleDirectives(data_ptr);
                line = MDT.getLine(data_ptr++);

            }
            if(line.contains("&")){
                //System.out.println("Original:"+line);
                for(Map.Entry<String, Parameter> e : actualParams.entrySet()){
                    line =line.replace(e.getKey(), e.getValue().getValue());
                }
                //System.out.println("Replaced:"+line);
            }
            expansion.append(line).append("\n");
        }while (true);
        System.out.println("MACRO EXPANDED TO :\n "+expansion.toString());
        updatedSource.append(expansion.toString());
    }
    public void readSource() {
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = tokenizer.split(line.trim());
                System.out.println(Arrays.toString(tokens));
                MNTEntry macro;
                String[] paramVals;
                if(line.contains(":")){
                    macro= Tables.get(tokens[1]);
                    if(macro!= null){
                        paramVals= new String[tokens.length-2];
                        System.arraycopy(tokens, 2, paramVals, 0, paramVals.length);
                        beginExpansion(macro, paramVals);
                        continue;
                    }
                }
                else{
                    macro = Tables.get(tokens[0]);
                    if(macro!= null){
                        paramVals= new String[tokens.length-1];
                        System.arraycopy(tokens, 1, paramVals, 0, paramVals.length);
                        beginExpansion(macro, paramVals);
                        continue;
                    }
                }
                updatedSource.append(line).append("\n");
            }
        }
        catch (IOException exc){
            System.err.println("Error in reading file");
        }
    }
    public void commitSource() {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Utility.SOURCE_FILE, StandardOpenOption.TRUNCATE_EXISTING);
            bw.write(updatedSource.toString());
            bw.flush();
            bw.close();
        }
        catch (IOException exc){
            System.err.println("Error in committing source file.");
        }
    }
    public static void main(String[] args) {
        try {
            Main m= new Main();
            System.out.println("Reading Pass II file...");
            m.readSource();
            System.out.println("Applying changes...");
            m.commitSource();
        }
        catch (IOException exc){
            exc.printStackTrace();
        }
    }

}
