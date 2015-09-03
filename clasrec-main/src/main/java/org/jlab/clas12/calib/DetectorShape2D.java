/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.awt.Color;
import org.jlab.clas.detector.DetectorDescriptor;
import org.jlab.geom.prim.Path3D;

/**
 *
 * @author gavalian
 */
public class DetectorShape2D {
    
    DetectorDescriptor  desc = new DetectorDescriptor();
    Path3D              shapePath = new Path3D();
    int                 colorRed    = 20;
    int                 colorGreen  = 20;
    int                 colorBlue   = 20;
    
    public DetectorShape2D(){
        
    }
        
    public DetectorDescriptor  getDescriptor(){ return desc;}
    public Path3D              getShapePath(){ return shapePath;}
    
    public void setColor(int r, int g, int b){
        this.colorRed = r;
        this.colorGreen = g;
        this.colorBlue  = b;
    }
    
    public Color getSwingColor(){
        return new Color(this.colorRed,this.colorGreen,this.colorBlue);
    }
    
    public void createBarXY(double width, double height){
        this.shapePath.clear();
        this.shapePath.addPoint(-width/2.0, -height/2.0,0.0);
        this.shapePath.addPoint(-width/2.0,  height/2.0,0.0);
        this.shapePath.addPoint( width/2.0,  height/2.0,0.0);
        this.shapePath.addPoint( width/2.0, -height/2.0,0.0);        
    }
    
    
    
    public boolean isContained(double x, double y){
        int i, j;
        boolean c = false;
        int nvert = shapePath.size();
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( (( shapePath.point(i).y()>y) != (shapePath.point(j).y()>y)) &&
                    (x < ( shapePath.point(j).x()-shapePath.point(i).x()) * 
                    (y-shapePath.point(i).y()) / (shapePath.point(j).y()-shapePath.point(i).y()) +
                    shapePath.point(i).x()))
                c = !c;
        }
        return c;
        //return false;
    }
}
