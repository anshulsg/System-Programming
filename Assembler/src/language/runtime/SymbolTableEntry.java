package language.runtime;

import util.Utility;

import java.util.regex.Pattern;

public class SymbolTableEntry {
    public enum SymbolType{
        UNDEFINED, MULTIPLY_DEFINED, DEFINED
    }

    private String name;
    private int location;
    private SymbolType type;
    private SymbolTableEntry(){}
    SymbolTableEntry(String name) {
        this.name = name;
        type = SymbolType.UNDEFINED;
        location = -1;
    }

    SymbolTableEntry(String name, int location) {
        this.name = name;
        this.location = location;
        type = SymbolType.DEFINED;

    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public int getLocation() {
        return location;
    }

    public SymbolType getType() {
        return type;
    }

    public void addDefinitionPoint(int location){
        if(type == SymbolType.UNDEFINED){
            type = SymbolType.DEFINED;
            this.location = location;
        }
        else type = SymbolType.MULTIPLY_DEFINED;
    }
    @Override
    public String toString() {
        String loc_prefix;
        if(location!=-1) loc_prefix = String.format("%-4d",location);
        else loc_prefix= String.format("%-4s", Utility.null_str);
        loc_prefix+=" : "
                +String.format("%-20s",name)
                +String.format("%-20s", type.toString());
        return loc_prefix;
    }

    public static SymbolTableEntry readSelfFrom(String text){
        Pattern pat = Pattern.compile(" +");
        String[] tokens = pat.split(text);
        SymbolTableEntry e= new SymbolTableEntry();
        e.setType(SymbolType.valueOf(tokens[3]));
        e.setName(tokens[2]);
        if(Utility.compareNull(tokens[0])) e.location = -1;
        else e.setLocation(Integer.parseInt(tokens[0]));
        return e;
    }
}
