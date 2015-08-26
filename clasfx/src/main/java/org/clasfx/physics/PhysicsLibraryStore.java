/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.physics;

import java.util.Map;
import java.util.TreeMap;
import javafx.scene.control.TreeItem;
import org.clasfx.root.TEmbeddedCanvas;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class PhysicsLibraryStore {
    TreeMap<String,AbsEventMonitor>   monitoring = new TreeMap<String,AbsEventMonitor>();
    
    public PhysicsLibraryStore(){
        
    }
    
    public void add(AbsEventMonitor mon){
        this.monitoring.put(mon.getName(), mon);
    }
    
    public TreeItem<String>  getTree(){
        TreeItem<String>  root = new TreeItem<String>("root");
        for(Map.Entry<String,AbsEventMonitor> entry : this.monitoring.entrySet()){
            root.getChildren().add(new TreeItem<String>(entry.getKey()));
        }
        return root;
    }
    
    public void draw(String entry, TEmbeddedCanvas canvas){
        this.monitoring.get(entry).draw(canvas);
    }
    
    public void processFile(String filename){
        EvioSource reader = new EvioSource();
        reader.open(filename);
        
        for(Map.Entry<String,AbsEventMonitor> entry : this.monitoring.entrySet()){
            entry.getValue().init();
        }
        
        while(reader.hasEvent()){
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            for(Map.Entry<String,AbsEventMonitor> entry : this.monitoring.entrySet()){
                try {
                    entry.getValue().process(event);
                } catch (Exception e){
                    
                }
            }
        }
    }
}
