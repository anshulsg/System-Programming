package macro.base;

import java.util.regex.Pattern;

public class MDirectives {
    public enum Directives{
        AGO
    }
    private static Pattern tokenizer=Pattern.compile("[, ]+");
    public static boolean checkDirectives(String line){
        if(line.contains("AGO")) return true;
        else return false;
    }
    public static int handleDirectives(int curr_pos){
        String line= MDT.getLine(curr_pos);
        if(line.contains("AGO")){
            String[] tokens= tokenizer.split(line);
            for(int i= curr_pos+1;
                !MDT.getLine(i).equalsIgnoreCase("MEND");
                i++) {
                if (MDT.getLine(i).contains(tokens[1])) return i;
            }
        }
        return curr_pos;
    }
}
