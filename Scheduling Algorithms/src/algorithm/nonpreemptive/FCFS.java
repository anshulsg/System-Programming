package algorithm.nonpreemptive;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import base.Gantt;
import base.Process;

public class FCFS implements Comparator<Process> {
    /* First-Come First-Serve
     * Compares two processes on the basis of their arrival time
     * */
    @Override
    public int compare(Process p, Process q) {
        float time_p, time_q;
        time_p= p.getArrivalTime();
        time_q= q.getArrivalTime();
        return Float.compare(time_p, time_q);
    }

    public void doFCFS(List<Process> processes){
        System.out.println("Performing FCFS Scheduling on the data...");
        Collections.sort(processes, this);
        //print heading for the scheduling table
        System.out.println("ProcessName\tArrival\tBurst\tWaiting\tTurnaround");
        float time= 0;
        float waiting=0;
        float turnaround=0;
        Gantt chart= new Gantt();
        for(Process p: processes){
            //if no processes arrive right now, forward time till the arrival time of next process
            if(p.getArrivalTime() > time) time= p.getArrivalTime();
            p.startJobAt(time);
            chart.add(p.getName(), (int)time , (int)(time+p.getBurstTime()));
            //adjust total time, total waiting and turnaround
            time += p.getBurstTime();
            waiting+= p.getWaitingTime();
            turnaround+=p.getTurnaround();
            //sequentially print each process as it executes
            System.out.println(p);
        }

        waiting/=processes.size();
        turnaround/=processes.size();
        //Print Statistics
        System.out.println("Total Time: "+time+"\n"
                + "Average Waiting Time: "+ waiting+"\n"
                + "Average Turnaround Time: "+turnaround+"\n");
        //Print Gantt Chart
        System.out.println(chart);

    }
}