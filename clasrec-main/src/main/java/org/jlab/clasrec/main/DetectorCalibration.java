/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import java.util.List;
import java.util.TreeMap;
import org.jlab.clas12.calib.DetectorShape2D;
import org.jlab.clas12.calib.DetectorShapeView2D;
import org.jlab.data.detector.DetectorType;
import org.jlab.evio.clas12.EvioDataEvent;

/**
 *
 * @author gavalian
 */
public abstract class DetectorCalibration {
    
    DetectorType  type = DetectorType.UNDEFINED;
    String        moduleName    = "UNKNOWN";
    String        moduleAuthor  = "UNKNOWN";
    String        moduleVersion = "UNKNOWN";
    
    TreeMap<String, DetectorShapeView2D >  detectors = new TreeMap<String, 
            DetectorShapeView2D>();
    
    public DetectorCalibration(String name, String author, String version){
        this.moduleName = name;
        this.moduleAuthor = author;
        this.moduleVersion = version;
        //this.initDummyGeometry();
    }
    
    public  void initGeometry(DetectorType type){
        if(type==DetectorType.CTOF){
           this.initGeometryCTOF();
        }
    }
    
    public abstract void processEvent(EvioDataEvent event);
    
    public  void  update(DetectorShape2D  shape){
        
        if(shape.getDescriptor().getComponent()%2==0){
            shape.setColor(100,200,100);
        } else {
            shape.setColor(100,100,200);
        }
    }
    
    private void initGeometryCTOF(){
        
        for(int loop = 0 ; loop < 48; loop++){
                DetectorShape2D shape = new DetectorShape2D();
                double length = 20.0;
                shape.createBarXY(20, length);
                shape.getShapePath().translateXYZ(220.0, 0.0, 0.0);
                shape.getShapePath().rotateZ(loop*Math.toRadians(-360.0/(48.0)));
                //System.out.println("COMPONENT # " + loop);
                //System.out.println(shape.getShapePath());
                shape.getDescriptor().setType(org.jlab.clas.detector.DetectorType.CTOF);
                shape.getDescriptor().setSectorLayerComponent(0,0,loop);
                this.addShape("CTOF", shape);
        }
    }
    
    public void initGeometryFTOF(){
        int[] paddles   = new int[]{65,23,5};
        double[] minLength = new double[]{23.0,34.0,40.0};
        String[] names     = new String[]{"FTOF1A","FTOF1B","FTOF2"};
    }
    
    private void initDummyGeometry(){

            for(int loop = 0 ; loop < 48; loop++){
                DetectorShape2D shape = new DetectorShape2D();
                double length = 20.0;
                shape.createBarXY(20, length);
                shape.getShapePath().translateXYZ(220.0, 0.0, 0.0);
                shape.getShapePath().rotateZ(loop*Math.toRadians(-360.0/(48.0)));
                //System.out.println("COMPONENT # " + loop);
                //System.out.println(shape.getShapePath());
                shape.getDescriptor().setType(org.jlab.clas.detector.DetectorType.CTOF);
                shape.getDescriptor().setSectorLayerComponent(0,0,loop);
                this.addShape("CTOF", shape);
            }
            
    }
    
    public void addShape(String name, DetectorShape2D shape){
        if(detectors.containsKey(name)==false){
            detectors.put(name, new DetectorShapeView2D(shape.getDescriptor().getType().getName()));
        }
        detectors.get(name).addShape(shape);
    }
    
    public TreeMap<String, DetectorShapeView2D> getDetectorList(){
        return this.detectors;
    }
}
