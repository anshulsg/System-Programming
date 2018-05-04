package base;

import java.io.*;

public class Process {
    private String processName;
    private float arrivalTime, burstTime;
    private float waitingTime, turnAroundTime;
    private float remainingTime;
    private int priority;
    public Process(){
        waitingTime= 0;
        priority=0;
    }
    public Process(String name, float a, float b){
        processName= name;
        arrivalTime= a;
        burstTime= b;
        remainingTime=b;
        waitingTime= 0;
        priority=0;
    }
    public Process(String name, float a, float b, int p){
        processName= name;
        arrivalTime= a;
        burstTime= b;
        remainingTime=b;
        waitingTime= 0;
        priority=p;
    }

    public void setArrivalTime(float time){
        arrivalTime= time;
    }
    public void setBurstTime(float time){
        burstTime= time;
    }

    public int getPriority(){
        return priority;
    }
    public float getArrivalTime(){
        return arrivalTime;
    }
    public float getBurstTime(){
        return burstTime;
    }
    public float getRemainingTime(){
        return remainingTime;
    }
    public float getWaitingTime(){
        return waitingTime;
    }
    public float getTurnaround(){
        return turnAroundTime;
    }
    public String getName(){
        return processName;
    }

    //non preemptive: executes the complete job at given time
    public void startJobAt(float time){
        waitingTime= time- arrivalTime;
        turnAroundTime= waitingTime + burstTime;
    }
    //starts the job at given time for a given time quantum
    public void startPreemptiveJobAt(float time, int quantum){
        if(remainingTime>0){
            remainingTime-=1;
            if(remainingTime<=0){
                waitingTime= time-arrivalTime-burstTime+1;
                turnAroundTime= waitingTime+burstTime;
            }
        }
    }

    @Override
    public String toString(){
        return "----------------------------------------------------------------\n"+
                processName+"\t"+arrivalTime+"\t"+burstTime+"\t"+waitingTime+"\t"+turnAroundTime+"\n"
                +"----------------------------------------------------------------\n";

    }

    public void read(String processName, BufferedReader br, PrintStream out) throws IOException{
        out.println("Enter details for the new process:");
        out.println("Arrival Time:");
        arrivalTime= Float.parseFloat(br.readLine());
        System.out.println("Burst Time:");
        burstTime= Float.parseFloat(br.readLine());
        System.out.println("Priority( 0 if NA):");
        priority= Integer.parseInt(br.readLine());
        this.processName = processName;
        remainingTime=burstTime;
        waitingTime= 0;
        turnAroundTime=0;
    }
}