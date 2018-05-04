package macro.runtime;

import macro.base.MDT;
import macro.base.MNTEntry;

import java.util.HashMap;
import java.util.Map;

public class Tables {
    private static final HashMap<String, MNTEntry> nameTable = new HashMap<>();
    public static void makeMacroEntry(MNTEntry entry){
        nameTable.put(entry.getMacroName(), entry);
    }
    public static String getPrintable() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, MNTEntry> entry : nameTable.entrySet()) {
            builder.append("\nMACRO INFO: \n\n").append(entry.getValue().toString());
        }
        builder.append("\nMacro Definition Table : \n");
        builder.append(MDT.getPrintable());
        return builder.toString();
    }
    public static MNTEntry get(String macro_name){
        return nameTable.get(macro_name);
    }
}
