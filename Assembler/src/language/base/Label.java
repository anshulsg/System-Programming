package language.base;

import java.util.regex.Pattern;

public class Label {
    private String name;
    private static final Pattern format= Pattern.compile("[a-zA-Z][a-zA-Z_0-9]*");

    private Label(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Label readSelfFrom(String text){
        //TODO: Match using regex, and return label object
        if(text == null) return null;
        text= text.trim();
        if(format.matcher(text).matches()){
            return new Label(text);
        }
        else return null;
    }

}

