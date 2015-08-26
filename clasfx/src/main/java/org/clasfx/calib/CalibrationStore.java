/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.calib;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clasfx.root.TEmbeddedCanvas;
import org.jlab.clas.detector.DetectorType;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class CalibrationStore {
    
    TreeMap<DetectorType,DetectorCalibration>  calibUtils = new 
            TreeMap<DetectorType,DetectorCalibration>();
    
    String selectedFile = "";
    
    public CalibrationStore(){
        
    }
    
    public void addClass(String name) {
        try {
            Class cclass = Class.forName(name);
            DetectorCalibration calibClass;
            calibClass =  (DetectorCalibration) cclass.newInstance();
            calibUtils.put(calibClass.type, calibClass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CalibrationStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CalibrationStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CalibrationStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(" [PLUGIN] Loaded plugin with name [" + name + "]");
    }
    
    public void addClass(DetectorCalibration calib){
        this.calibUtils.put(calib.getDetectorType(), calib);
    }
    public void callback(DetectorType type, int sector, int layer, int comp, TEmbeddedCanvas canvas){
        if(this.calibUtils.containsKey(type)==true){
            this.calibUtils.get(type).draw(sector, layer, comp, canvas);
        } else {
            //System.out.println();
        }
    }
    
    public void setFile(String filename){
        this.selectedFile = filename;
        System.out.println("[CalibrationStore] set file ----> " + this.selectedFile);
    }
    
    public void processFile(){
        this.processFile(selectedFile);
    }
    
    public void processFile(String filename){
        EvioSource reader = new EvioSource();
        reader.open(filename);
        int counter = 0;
        while(reader.hasEvent()){
            try {
                counter++;
                EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();                
                this.processEvent(event);
                if(counter%500==0){
                    System.out.println("---> events processed " + counter);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void processEvent(EvioDataEvent event){
        for(Map.Entry<DetectorType,DetectorCalibration> entry : this.calibUtils.entrySet()){
            try {
                entry.getValue().processEvent(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
}
