public class Process {
    private String name;
    private int[] allocated, maximum;

    public Process(String name, int[] curr,int[] max){
        this.name = name;
        allocated = curr;
        maximum = max;
    }

    public String getName() {
        return name;
    }

    public boolean canAllocate(int[] available){
        int[] need= new int[maximum.length];

        for(int i=0; i<need.length; i++){
            need[i] = maximum[i]-allocated[i];
            if(need[i]>available[i]) return false;
        }
        return true;
    }

    public void allocate(int[] available){
        for(int i=0; i<allocated.length; i++){
            available[i]+=allocated[i];
            allocated[i] = 0;
        }
    }
}
