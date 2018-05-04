package language.base;

public enum Register {
    RA(0x01), RB(0x02), RC(0x03), RD(0x04);
    private int value;
    Register( int value){
        this.value = value;
    }
    public int valueOf(){
        return value;
    }
}
