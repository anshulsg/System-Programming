import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BankersAlgorithm {

    private int[] resources;
    private Process[] processes;
    private String[] safeSequence;

    public BankersAlgorithm(int resourceCount, int processCount){
        resources = new int[resourceCount];
        processes = new Process[processCount];
        safeSequence = new String[processCount];
    }

    public void readProcessAttrs() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for(int p=0; p<processes.length; p++){
            System.out.print("\n\nFor Process P"+p+",\nEnter the allocated and maximum resources:");
            int[] allocated= new int[resources.length], max=new int[resources.length];
            for(int i=0; i<resources.length; i++){
                System.out.print("\n------------------------------------\n\tResource R"+(i+1)+"\n");
                System.out.print("\n\tAllocated:");
                allocated[i] = Integer.parseInt(br.readLine());
                System.out.print("\n\tMaximum:");
                max[i] = Integer.parseInt(br.readLine());
            }
            processes[p] = new Process("P"+p,allocated, max);
        }
        System.out.println("\nEnter the currently available resource count:");
        for(int r=0; r<resources.length; r++){
            System.out.print("\n\tResource R"+(r+1)+":");
            resources[r] = Integer.parseInt(br.readLine());
        }
    }
    public boolean checkSafeState(){
        boolean[] scheduled = new boolean[processes.length];
        for (boolean b : scheduled) {
            b = false;
        }

        int safeIndex = 0;
        boolean flag = true;
        while (safeIndex != processes.length && flag){
            flag = false;
            for (int i = 0; i < processes.length; i++) {
                if(safeIndex==processes.length){
                    flag=true;
                    break;
                }
                if(processes[i].canAllocate(resources) && scheduled[i]==false){
                    safeSequence[safeIndex++] = processes[i].getName();
                    processes[i].allocate(resources);
                    scheduled[i] = true;
                    flag = true;
                }
            }
        }
        return flag;
    }
    public String getSafeSequence(){
        String list= "";
        for(String s : safeSequence){
            if(s!=null){
                list+=s+"|\t";
            }
        }
        return list;
    }
    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Banker's Algorithm:\nPlease input the following details:\n");
        int num_res, num_proc;
        System.out.print("\n\tNumber of processes: ");
        num_proc = Integer.parseInt(br.readLine());
        System.out.print("\n\tNumber of resources: ");
        num_res = Integer.parseInt(br.readLine());

        BankersAlgorithm alg= new BankersAlgorithm(num_res, num_proc);
        alg.readProcessAttrs();
        if(alg.checkSafeState()){
            System.out.println("Safe sequences exist. Safe Sequence :");
            System.out.println(alg.getSafeSequence());
        }
    }
}

