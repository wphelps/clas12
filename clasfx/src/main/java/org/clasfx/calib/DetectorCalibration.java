/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.calib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.control.TreeItem;
import org.clasfx.detector.DetectorShape3D;
import org.clasfx.root.TEmbeddedCanvas;
import org.clasfx.tools.IDetectorDraw;
import org.jlab.clas.detector.DetectorType;
import org.jlab.clas12.calib.ComponentH1D;
import org.jlab.clas12.calib.DetectorH1D;
import org.jlab.evio.clas12.EvioDataEvent;

/**
 *
 * @author gavalian
 */
public abstract class DetectorCalibration implements IDetectorDraw {

    TreeMap<String,DetectorH1D>  calibHistograms = new TreeMap<String,DetectorH1D>();
    
    DetectorType  type = DetectorType.UNDEFINED;
    public DetectorCalibration(){
        
    }

    public DetectorCalibration(DetectorType t){
        this.type = t;
    }
    
    public DetectorType getDetectorType(){return type;}
    
    abstract public void processEvent(EvioDataEvent event);
    abstract public void init();        
        
    public void createHistogram(String name, int sector, int layer,
            int component, int bins, double xmin, double xmax, String func){
        if(this.calibHistograms.containsKey(name)==false){
            this.calibHistograms.put(name, new DetectorH1D(this.getDetectorType(),
            name));
        }
        
        this.calibHistograms.get(name).add(sector, layer, component, func, bins, xmin, xmax);
    }
    
    public void fill(String name, int sector, int layer, int component, double value){
        ComponentH1D comph1d = this.getHistogram(name, sector, layer, component);
        if(comph1d!=null){
            comph1d.fill(value);
        } else {
            System.out.println("[WARNING] could not find histogram [" + name + "] "
            + "  SECTOR = " + sector + "  LAYER = " + layer + "  COMPONENT = " 
            + component);
        }
    }
    
    public ComponentH1D  getHistogram(String name, int sector, int layer, int component){
        if(this.calibHistograms.containsKey(name)==true){
            DetectorH1D  container = this.calibHistograms.get(name);
            return container.get(sector, layer, component);
        }
        return null;
    }
    
    public TreeMap<String,List<DetectorShape3D>> getDetectorShapes(){
        ArrayList<DetectorShape3D>  shapes = new ArrayList<DetectorShape3D>();
        TreeMap<String,List<DetectorShape3D>> map = new TreeMap<String,List<DetectorShape3D>>();
        if(this.type==DetectorType.CTOF){
            double startangle   = 0.0;
            double counterangle = 360.0/48;
            for(int loop = 0; loop < 48; loop++){
                DetectorShape3D shape = new DetectorShape3D(DetectorType.CTOF,0,0,loop);
                shape.createPathArc(100, 20, startangle + counterangle*loop, 
                        startangle + counterangle*(loop+1), 20);
                shapes.add(shape);
            }
            map.put("CTOF", shapes);
        }
        

        return map;
    }
    
    @Override
    public void draw(int sector, int layer, int component, TEmbeddedCanvas canvas) {
        System.out.println("Drawing Sector/Layer/Component " + sector +
                "  " + layer + " " + component);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public TreeItem<String>  getTree(){
        TreeItem<String> root = new TreeItem<String>("ROOT");
        for(Map.Entry<String,DetectorH1D>  dh1d : this.calibHistograms.entrySet()){
            List<String>  array = dh1d.getValue().getList();
            TreeItem<String> comp = new TreeItem<String>(dh1d.getKey());
            
            for(String slc : array){
                comp.getChildren().add(new TreeItem<String>(slc));
            }
            root.getChildren().add(comp);
        }
        return root;
    }
}
