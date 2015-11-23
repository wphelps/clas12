/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import java.util.ArrayList;
import java.util.List;
import org.jlab.clas.tools.benchmark.BenchmarkTimer;
import org.jlab.clas12.basic.IDetectorProcessor;
import org.jlab.data.io.DataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class DetectorEventProcessorThread extends Thread {
    private List<IDetectorProcessor>  processors = new ArrayList<IDetectorProcessor>();
    private String                    processFileName = "";
    private double                    progress        = 0.0;
    
    
    public DetectorEventProcessorThread(String filename, IDetectorProcessor proc){
        this.setFile(filename);
        this.addProcessor(proc);
    }
    
    
    public final void addProcessor(IDetectorProcessor proc){
        this.processors.add(proc);
    }
    
    public final void setFile(String filename){
        this.processFileName = filename;
    }
    
    @Override
    public void run(){
        
        System.out.println("[thread]---> starting thread with file : " + this.processFileName); 
        EvioSource reader = new EvioSource();
        reader.open(processFileName);
        BenchmarkTimer  timer = new BenchmarkTimer("Event-Thread");
        int   nprocessed  = 0;
        int   nexceptions = 0;
        this.progress = 0.0;
        int   nevents = reader.getSize();
        
        while(reader.hasEvent()){

            DataEvent  event = reader.getNextEvent();
            nprocessed++;
            timer.resume();
            for(IDetectorProcessor proc : this.processors){
                try {
                    proc.processEvent(event);
                } catch(Exception e){
                    nexceptions++;
                }
            }
            timer.pause();
            this.progress = (nprocessed*100.0)/(nevents);
        }
        System.out.println("[thread]---> PROCESSED EVENTS : " + nprocessed + 
                "  exceptions " + nexceptions);
        System.out.println("[thread]---> " + timer.toString());
        this.progress = 100.0;
    }
    
    public int getProgress(){
        return (int) this.progress;
    }
}
