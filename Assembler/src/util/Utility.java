package util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Utility {
    public static final Path FILE_INPUT= Paths.get("/home/anshulsg/sample.txt");
    public static final Path FILE_IC = Paths.get("/home/anshulsg/file_ic.txt");
    public static final Path FILE_SYMBOL = Paths.get("/home/anshulsg/file_symbol.txt");
    public static final Path FILE_LITERAL = Paths.get("/home/anshulsg/file_literal.txt");
    public static final Path FILE_OUTPUT = Paths.get("/home/anshulsg/file_output.txt");
    public static final String null_str= "-NA-";
    public static String soManyDashes(int howMany){
        StringBuilder builder = new StringBuilder(howMany);
        while (howMany > 0){
            builder.append("-");
            howMany--;
        }
        return builder.append("\n").toString();
    }
    public static boolean compareNull(String to){
        return null_str.equalsIgnoreCase(to);
    }
}
