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
import org.jlab.evio.clas12.EvioETSource;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class DetectorEventProcessorThread extends Thread {
    private List<IDetectorProcessor>  processors = new ArrayList<IDetectorProcessor>();
    private String                    processFileName = "";
    private double                    progress        = 0.0;
    private boolean                   runOnEt         = false;
    private String                    etHostName      = "";
    private String                    etFileName      = "";
    
    
    public DetectorEventProcessorThread(String filename, IDetectorProcessor proc){
        this.setFile(filename);
        this.addProcessor(proc);
    }
    
    
    public final void addProcessor(IDetectorProcessor proc){
        this.processors.add(proc);
    }
    
    public final void setFile(String filename){
        if(filename.contains(":")==false){
            this.processFileName = filename;
            this.runOnEt         = false;
        } else {
            String[] tokens = filename.split(":");
            this.etHostName = tokens[0].trim();
            this.etFileName = tokens[1].trim();
            this.runOnEt    = true;
        }
    }
    
    private void runOnEt(String hostname, String filename){
        EvioETSource reader = new EvioETSource(this.etHostName);
        reader.setRemote(true);
        reader.open(this.etFileName);
        int nprocessed  = 0;
        int nexceptions = 0;
        while(true){
            reader.loadEvents();
            
            while(reader.hasEvent()){
                DataEvent  event = reader.getNextEvent();
                nprocessed++;
                
                for(IDetectorProcessor proc : this.processors){
                    try {
                        proc.processEvent(event);
                    } catch(Exception e){
                        nexceptions++;
                    }
                }
            }
        }
    }
    
    private void runOnFile(String filename){
        
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
    
    @Override
    public void run(){
        
        if(this.runOnEt==false){
            this.runOnFile(this.processFileName);
        } else {
            this.runOnEt(this.etHostName, this.etFileName);
        }
        
        
    }
    
    public int getProgress(){
        return (int) this.progress;
    }
}
