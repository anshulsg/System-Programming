package macro.base;

public class KeywordParameter extends Parameter {
    private String defaultValue;
    public KeywordParameter(String name, String defaultValue){
        super(name);
        this.defaultValue = defaultValue;
    }

    @Override
    public String getValue(){
        if(this.value == null) return defaultValue;
        else return value;
    }

    @Override
    public String toString() {
        String str="";
        str+=String.format("%20s", name);
        str+="|";
        str+=String.format("%20s", "Keyword Pm");
        str+="|";
        str+=String.format("%20s", ""+defaultValue);
        str+="|";
        return str;
    }
}
