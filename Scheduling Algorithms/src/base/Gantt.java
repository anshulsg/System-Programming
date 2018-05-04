package base;

import java.util.ArrayList;

public class Gantt {
    //handles making, displaying gantt charts
    private ArrayList<String> procName;
    private int currTime;
    private int quantum;
    public Gantt(){
        currTime=0;
        procName= new ArrayList<>();
        quantum=1;
    }
    //quantum will be a user input in RoundRobin
    public Gantt(int quantum){
        currTime=0;
        procName= new ArrayList<>();
        this.quantum= quantum;
    }
    public void add(String proc, int time_from, int time_to){
        while(time_from>currTime){
            currTime+=quantum;
            procName.add("Idle");
        }
        while(time_from<time_to){
            time_from+=quantum;
            procName.add(proc);
        }
        currTime= time_to;
    }
    public String toString(){
        String str="Gantt Chart: \nTime\tProcess\n";
        int i=0;
        for(String s: procName){
            str += i+" : "+s+"\n";
            i+=quantum;
        }
        return str;
    }
}