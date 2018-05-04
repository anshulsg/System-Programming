package algorithm.nonpreemptive;

import base.Gantt;
import base.Process;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Priority implements Comparator<Process> {

    /* Priority Scheduler
     * Compares two processes on the basis of their priority
     * */
    @Override
    public int compare(Process p, Process q) {
        int priority_p, priority_q;
        priority_p= p.getPriority();
        priority_q= q.getPriority();
        return Integer.compare(priority_p, priority_q);
    }

    public void doPriority(List<Process> processes){
        System.out.println("Performing Priority Scheduling on the data...");
        Collections.sort(processes, this);
        float time= 0;
        float waiting=0;
        float turnaround=0;
        Gantt chart= new Gantt();
        System.out.println("ProcessName\tArrival\tBurst\tWaiting\tTurnaround");
        for(Process p: processes){
            if(p.getArrivalTime() > time) time= p.getArrivalTime();
            p.startJobAt(time);
            chart.add(p.getName(), (int)time , (int)(time+p.getBurstTime()));
            time += p.getBurstTime();
            waiting+= p.getWaitingTime();
            turnaround+=p.getTurnaround();
            System.out.println(p);
        }

        waiting/=processes.size();
        turnaround/=processes.size();
        System.out.println("Total Time: "+time+"\n"
                + "Average Waiting Time: "+ waiting+"\n"
                + "Average Turnaround Time: "+turnaround+"\n");
        System.out.println(chart);
    }
}
