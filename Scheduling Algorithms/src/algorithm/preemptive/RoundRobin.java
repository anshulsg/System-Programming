package algorithm.preemptive;

import algorithm.nonpreemptive.FCFS;
import base.Gantt;
import base.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundRobin {
    public void doRoundRobin(List<Process> processList, int quantum){
        System.out.println("Performing RR Scheduling on the data...");
        System.out.println("ProcessName\tArrival\tBurst\tWaiting\tTurnaround");
        float time= 0;
        float waiting=0;
        float turnaround=0;
        boolean done= false;
        Gantt chart= new Gantt();
        List<Process> processes= new ArrayList<>(processList);
        List<Process> ready = new ArrayList<>();
        Collections.sort(processes, new FCFS());

        while(ready.isEmpty()){
            for(Process p: processes){
                if(p.getArrivalTime()<=time){
                    ready.add(p);
                }
            }
            time+=quantum;
        }


    }
}