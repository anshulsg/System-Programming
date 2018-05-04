package macro.base;

public class Parameter {
    protected String name;
    protected String value;

    public Parameter(String name){
        this.name = name;
        value = null;
    }
    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String str="";
        str+=String.format("%20s", name);
        str+="|";
        str+=String.format("%20s", "Positional Pm");
        str+="|";
        str+=String.format("%20s", "-N/A-");
        str+="|";
        return str;
    }
}
