package algorithm.preemptive;

import algorithm.nonpreemptive.FCFS;
import algorithm.nonpreemptive.SJF;
import base.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SJFPreemptive {
    public void doSJF(List<Process> processList){
        List<Process> processes= new ArrayList<>(processList);
        List<Process> ready = new ArrayList<>();
        float time=0;
        float waiting=0;
        float turnaround=0;
        boolean done= false;
        Collections.sort(processes, new FCFS());
        System.out.println("ProcessName|\tArrival|\tBurst|\tWaiting|\tTurnaround");
        while(!done){
            if(ready.size()==0){
                if(processes.isEmpty()){
                    done= true;
                    break;
                }
            }
            for(Process p: processes){
                if(p.getArrivalTime()<=time) ready.add(p);
            }
            if(ready.size()==0){
                time= processes.get(0).getArrivalTime();
                continue;
            }
            processes.removeAll(ready);
            Collections.sort(ready, new SJF());

            Process curr= ready.get(0);
            //curr.startPreemptiveJobAt(time);
            time+=1;
            if(curr.getRemainingTime()==0){
                System.out.println(curr);
                ready.remove(curr);
                waiting+=curr.getWaitingTime();
                turnaround+=curr.getTurnaround();
            }
        }
        waiting/=processList.size();
        turnaround/=processList.size();
        System.out.println("Total Time: "+time+"\n"
                + "Average Waiting Time: "+ waiting+"\n"
                + "Average Turnaround Time: "+turnaround+"\n");

    }
}
