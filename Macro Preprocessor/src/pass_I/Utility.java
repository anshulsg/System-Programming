package pass_I;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Utility {
    public static final Path SOURCE_FILE = Paths.get("assembly.txt");
    public static String soManyDashes(int howMany){
        StringBuilder builder = new StringBuilder(howMany);
        while (howMany > 0){
            builder.append("-");
            howMany--;
        }
        return builder.append("\n").toString();
    }
}
