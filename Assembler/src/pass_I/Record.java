package pass_I;

import language.aggregation.Line;
import language.aggregation.Mnemonic;
import language.base.Argument;
import language.base.Label;
import util.Utility;

public class Record {
    private int location;
    private Line line;
    private boolean hasError;
    private Exception exception;
    Record(int location){
        this.location = location;
    }
    public void setError(Exception exc) {
        this.hasError = true;
        exception = exc;
    }

    public boolean hasError() {
        return hasError;
    }

    public Exception getException() {
        return exception;
    }

    public void set(int lc, Line line){
        location = lc;
        this.line = line;
    }

    @Override
    public String toString() {

        String recordType, opcode, args, label;

        if(line == null) return null;
        if(line.getLabel() == null){
            label=Utility.null_str;
        }
        else label= line.getLabel().toString();
        Mnemonic mnemonic = line.getMnemonic();
        if(mnemonic!=null){
            recordType = mnemonic.getInstruction().getInstructionType().toString();
            opcode = mnemonic.getInstruction().toString();
            args = Argument.getString(mnemonic.getArguments());
                    /*"("
                            +mnemonic.getInstruction().getInstructionType().toString()
                            +","+String.format("0x%02X", mnemonic.getInstruction().getHexcode())
                            +")";*/
        }
        else{
            recordType = Utility.null_str;
            opcode = Utility.null_str;
            args = Utility.null_str;
        }
        if(hasError) recordType = "INVALID";

        return String.format("%-4d", location)
                +String.format("%-20s", recordType)
                +String.format("%-20s", label)
                +String.format("%-20s", opcode)
                +String.format("%-40s", args);
    }
}
