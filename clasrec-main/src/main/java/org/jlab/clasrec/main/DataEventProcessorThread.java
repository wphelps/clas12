/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jlab.clas.tools.benchmark.BenchmarkTimer;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioETSource;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class DataEventProcessorThread extends Thread {
    
    List<IDataEventProcessor>  processors = new ArrayList<IDataEventProcessor>();
    String                     filename   = "";
    Double                     threadProgress = 0.0;
    Boolean                    connectEtRing  = false;
    String                     connectionEtRingHostName = "";
    String                     connectionEtRingFile     = "";
    
    public DataEventProcessorThread(){
        
    }
    
    public void setFileName(String file){
        if(file.contains(":")==true){   
            String[] tokens = file.split(":");
            this.connectionEtRingHostName = tokens[0];
            this.connectionEtRingFile     = tokens[1];
            this.connectEtRing = true;
        } else {
            this.filename = file;
        }
    }
    
    public void addProcessor(IDataEventProcessor proc){
        this.processors.add(proc);
    }
    
    private void runOnEtRing() {                
        
        while(true){
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(DataEventProcessorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("---> getting next bunch of events");
            BenchmarkTimer  timer = new BenchmarkTimer("ET");
            timer.resume();
            EvioETSource reader = new EvioETSource(this.connectionEtRingHostName);
            reader.setRemote(true);
            reader.open(this.connectionEtRingFile);
            int counter = 0;
            while(reader.hasEvent()==true){
                EvioDataEvent  event = (EvioDataEvent) reader.getNextEvent();
                for(IDataEventProcessor proc : this.processors){
                    try {
                        proc.process(event);
                    } catch(Exception e){
                        
                    }
                }
                counter++;
            }
            timer.pause();
            System.out.println("--> processed events #  " + counter);
            System.out.println("--> " + timer.toString());
        }
    }
    
    private void runOnLocalFile(){
        EvioSource reader = new EvioSource();
        reader.open(this.filename);
        int   numberOfEvents = reader.getSize();
        int counter = 0;
        while(reader.hasEvent()){            
            try {
                System.out.println("----> sleeping");
                Thread.sleep(200);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(DataEventProcessorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            for(IDataEventProcessor proc : this.processors){
                try {
                    proc.process(event);
                } catch(Exception e){
                    
                }
            }
            counter++;
            this.threadProgress = ((double) 100*counter )/numberOfEvents;

        }
        reader.close();
    }
    
    @Override
    public void run() {
        
        if(this.connectEtRing==false){
            this.runOnLocalFile();
        } else {
            System.out.println("-----> starting processing on et-ring : " 
                    + this.connectionEtRingHostName + "/" + this.connectionEtRingFile);
            this.runOnEtRing();
        }
        
    }
    
    public double getProgress(){
        return this.threadProgress;
    }
}
