package language.aggregation;

import language.base.Label;
import language.exceptions.IllegalArgumentCount;
import language.exceptions.IllegalArgumentType;
import language.exceptions.InvalidInstruction;

public class Line {
    private Label label;
    private Mnemonic mnemonic;
    private String comment;

    public static Line readSelfFrom(String text)
            throws IllegalArgumentCount, IllegalArgumentType, InvalidInstruction{
        //check null
        if(text == null) return null;
        //check if atleast one valid character exists
        if(text.trim().length() == 0) return null;
        //look for label
        int ind_colon = text.indexOf(":");
        //look for comments
        int ind_semicolon= text.indexOf(";");
        String label_text = null, comment_text = null, opcode_text;
        if(ind_colon>=0){
            label_text = text.substring(0, ind_colon);
        }
        if(ind_semicolon >=0){
            comment_text = text.substring(ind_semicolon+1);
            opcode_text = text.substring(Math.max(ind_colon+1, 0), ind_semicolon);
        }
        else opcode_text = text.substring(Math.max(ind_colon+1, 0), text.length());

        //obtain a line instance from all the above info
        Line line = new Line();
        line.setComment(comment_text);
        line.setLabel(Label.readSelfFrom(label_text));
        line.setMnemonic(Mnemonic.readSelfFrom(opcode_text));
        return line;
    }

    public Label getLabel() {
        return label;
    }

    private void setLabel(Label label) {
        this.label = label;
    }

    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    private void setMnemonic(Mnemonic mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getComment() {
        return comment;
    }

    private void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        StringBuilder str= new StringBuilder();
        if(label != null) str.append(label).append(" : ");
        if(mnemonic != null) str.append(mnemonic).append(" ");
        if(comment != null) str.append(comment);
        return str.toString();
    }
}
