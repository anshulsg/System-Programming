package macro.base;

import pass_I.Utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;

public class MNTEntry {
    private static Pattern comma= Pattern.compile(","),
            spaces= Pattern.compile(" +"),
            keyword= Pattern.compile("[= ]+");
    private String macroName;
    private int paramCount;
    private Vector<Parameter> parameters;
    private HashMap<String, Parameter> parameterTable;
    private MDT definition;

    private MNTEntry(String name, int params){
        macroName= name;
        paramCount = params;
        parameters = new Vector<>();
        parameterTable = new HashMap<>();
    }
    public void setDefinition(MDT definition){
        this.definition = definition;
    }

    public String getMacroName() {
        return macroName;
    }

    private void addParameter(Parameter parameter){
        
        parameterTable.put(parameter.name, parameter);
    }
    public boolean matches(String macro){
        return macroName.equals(macro);
    }
    public int getParamCount() {
        return paramCount;
    }
    public static MNTEntry readSelfFrom(String text){
        if (text == null) return null;
        text = text.trim();
        String[] tokens = text.split(",");
        String name = spaces.split(tokens[0])[0];
        try {
            tokens[0] = spaces.split(tokens[0])[1];
        }
        catch (ArrayIndexOutOfBoundsException exc){
            MNTEntry entry = new MNTEntry(name, 0);
            return entry;
        }
        MNTEntry entry = new MNTEntry(name, tokens.length);
        for (String token : tokens) {
            token = token.trim();
            if(token.contains("=")){
                String[] keyparam = keyword.split(token);
                entry.addParameter(new KeywordParameter(keyparam[0], keyparam[1]));
                continue;
            }
            entry.addParameter(new Parameter(token.trim()));
        }
        return entry;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Utility.soManyDashes(63))
                .append("MACRO NAME:"+ macroName).append("\n")
                .append(Utility.soManyDashes(63)).append("\n")
                .append("PARAMETER TABLE").append("\n")
                .append(Utility.soManyDashes(63))
                .append(String.format("%20s", "Name")).append("|")
                .append(String.format("%20s", "Type")).append("|")
                .append(String.format("%20s", "Value(Default)")).append("|").append("\n")
                .append(Utility.soManyDashes(63));
        for (Parameter parameter : parameters) {
            builder.append(parameter).append("\n");
        }
        builder.append("Macro Definition: ").append("\n");
        builder.append(definition);
        return builder.toString();
    }

    public static void main(String[] args) {
        readSelfFrom("ADDER &K1, &K2 =17, &K3");
    }
}
