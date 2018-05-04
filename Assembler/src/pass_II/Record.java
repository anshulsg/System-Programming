package pass_II;

import language.base.Argument;
import language.base.Instruction;
import util.Utility;

import java.util.Arrays;

public class Record {
    private int loc;
    private Instruction.InstructionType type;
    private Instruction instruction;
    private Argument[] args;
    private Record(){}
    public static Record readSelfFrom(int loc, String type, String instr, String arg1, String arg2) {
        Record record = new Record();
        record.loc = loc;
        if(!Utility.compareNull(type)) {
            try {
                record.type = Instruction.InstructionType.valueOf(type);
                record.instruction = Instruction.readSelfFrom(instr);
                record.args = new Argument[2];
                record.args[0] = Argument.readSelfFrom(arg1);
                record.args[1] = Argument.readSelfFrom(arg2);
                return record;
            }
            catch (Exception exc){return null;}
        }
        else return null;
    }

    public static Record readSelfFrom(int loc, String type, String instr, String arg1){
        Record record = new Record();
        record.loc = loc;
        if(!Utility.compareNull(type)){
            try {
                record.type = Instruction.InstructionType.valueOf(type);
                record.instruction = Instruction.readSelfFrom(instr);
                record.args = new Argument[1];
                record.args[0] = Argument.readSelfFrom(arg1);
                return record;
            }
            catch (Exception exc) {
                return null;
            }
        }
        else return null;

    }
    public static Record readSelfFrom(int loc, String type, String instr){
        Record record = new Record();
        record.loc = loc;
        if(!Utility.compareNull(type)){
            try {
                record.type = Instruction.InstructionType.valueOf(type);
                record.instruction = Instruction.readSelfFrom(instr);
                return record;
            }
            catch (Exception exc) {
                return null;
            }
        }
        else return null;

    }
    public int getLoc() {
        return loc;
    }

    public Instruction.InstructionType getType() {
        return type;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public Argument[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(48).append(loc)
                .append("\t").append(type)
                .append("\t").append(instruction)
                .append("\t").append(Arrays.toString(args));
        return builder.toString();
    }
}
