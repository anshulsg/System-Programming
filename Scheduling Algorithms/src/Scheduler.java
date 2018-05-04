import algorithm.nonpreemptive.FCFS;
import base.Process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    //create instances and call appropriate scheduler here.
    private static final BufferedReader br=
            new BufferedReader(new InputStreamReader(System.in));
    private List<Process> readProcesses(){
        List<Process> processes = new ArrayList<>();
        boolean flag = true;
        do{
            try{
                Process process= new Process();
                process.read("Process"+(processes.size()-1), Scheduler.br, System.out);
                processes.add(process);
                System.out.println("Press Y to add more processes");
                char c= (char) br.read();
                if(c!='Y' && c!='y') flag= false;
                br.readLine();

            }
            catch(IOException exc){

            }
        }while(flag);
        return processes;
    }
    public static void main(String[] args) {
    /*1.Read Processes
     *2.Invoke doPriority(), doSJF(), doFCFS(),.. etc on appropriate instances
     */
        List<Process> processes= new ArrayList<>();
        processes.add(new Process("Process1", 0, 1, 0));
        processes.add(new Process("Process2", 3, 4, 0));
        processes.add(new Process("Process3", 3, 2, 0));

        FCFS algFCFS= new FCFS();
        algFCFS.doFCFS(processes);

    }
}
