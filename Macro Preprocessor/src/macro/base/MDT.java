package macro.base;

import pass_I.Utility;

public class MDT{
    private static String[] lines;
    private static final int EXPANSION_UNIT =16;
    private static int currentIndex;
    private static int size;
    public static void init(){
        currentIndex = 0;
        size = EXPANSION_UNIT;
        lines = new String[size];
    }
    public static boolean checkInit(){
        return lines!=null;
    }
    public static String getLine(int pos){
        if(checkInit() &&(pos<=currentIndex && pos>=0)) return lines[pos];
        return null;
    }
    private static void expandArray(){
        if(!checkInit()) return;
        String[] temp = new String[size+EXPANSION_UNIT];
        System.arraycopy(lines, 0, temp, 0, currentIndex);
        lines = temp;
        size+= EXPANSION_UNIT;
    }
    public static void append(String line){
        if(!checkInit()) return;
        if(currentIndex == (int)(7*size/8)){
            expandArray();
        }
        lines[currentIndex] = line;
        currentIndex++;
    }
    public static int getCurrentIndex()
    {
        return currentIndex;
    }

    public static String getPrintable() {
        StringBuilder builder = new StringBuilder();
        builder.append(Utility.soManyDashes(63));
        for(int i=0; i<currentIndex; i++){
            builder.append(i).append(":").append(lines[i]).append("\n");
        }
        builder.append(Utility.soManyDashes(63));
        return builder.toString();
    }
}