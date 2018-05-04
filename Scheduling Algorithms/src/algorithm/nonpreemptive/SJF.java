package algorithm.nonpreemptive;

import base.Gantt;
import base.Process;

import java.util.*;

public class SJF implements Comparator<Process>{
    /* First-Come First-Serve
     * Compares two processes on the basis of their burst time
     * */

    public int compare(Process p, Process q ) {
        return Float.compare(p.getBurstTime(), q.getBurstTime());
    }

    public void doSJF(List<Process> processList){
        List<Process> processes= new ArrayList<>(processList);
        List<Process> ready = new ArrayList<>();
        float time=0;
        float waiting=0;
        float turnaround=0;
        boolean done= false;
        Gantt chart= new Gantt();
        //sort first by arrival time
        Collections.sort(processes, new FCFS());
        System.out.println("ProcessName\tArrival\tBurst\tWaiting\tTurnaround");
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
            Collections.sort(ready, this);
            Process curr= ready.remove(0);
            curr.startJobAt(time);
            System.out.println(curr);
            time+=curr.getBurstTime();
            waiting+=curr.getWaitingTime();
            turnaround+=curr.getTurnaround();
        }
        waiting/=processList.size();
        turnaround/=processList.size();
        System.out.println("Total Time: "+time+"\n"
                + "Average Waiting Time: "+ waiting+"\n"
                + "Average Turnaround Time: "+turnaround+"\n");
    }
}