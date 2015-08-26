/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.detector;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jlab.clas.detector.DetectorDescriptor;
import org.jlab.clas.detector.DetectorType;
import org.jlab.geom.prim.Path3D;


/**
 *
 * @author gavalian
 */
public class DetectorShape3D {
    
    DetectorDescriptor  descriptor = new DetectorDescriptor();
    Path3D  detectorPath  = new Path3D();
    Paint   detectorColor =  Color.rgb(100,100,200);

    public DetectorShape3D(DetectorType type,int sector, int layer, int component){
        descriptor.setType(type);
        descriptor.setSectorLayerComponent(sector, layer, component);
    }

    
    public void clear(){ detectorPath.clear();}
    public Path3D getPath(){ return this.detectorPath;}
    public Paint getColor(){return this.detectorColor;}
    public void setColor(Paint p){ this.detectorColor = p;}
    public void setColor(int r, int g, int b){ this.detectorColor = Color.rgb(r, g, b);}
    
    public void addPoint(double x, double y, double z){
        this.detectorPath.addPoint(x, y, z);
    }
    
    public DetectorDescriptor getDescriptor(){
        return this.descriptor;
    }
    
    public void createPathArc(double radius, double width, 
            double startAngle, double endAngle, int divisions){
        this.detectorPath.clear();
        double stepAngle = (endAngle - startAngle)/divisions;
        double rlow  = radius - width/2.0;
        double rhigh = radius + width/2.0;
        double xp = 0;
        double yp = 0;
        double zp = 0;
        
        this.detectorPath.addPoint(
                rlow  * Math.cos(Math.toRadians(startAngle)),
                rlow  * Math.sin(Math.toRadians(startAngle)),
                0.0
                );
        /*
        this.detectorPath.addPoint(
                rhigh * Math.cos(Math.toRadians(startAngle)),
                rhigh * Math.cos(Math.toRadians(startAngle)),
                0.0
                );*/
        for(double angle = startAngle; angle < endAngle; angle += stepAngle){
            xp = rhigh * Math.cos(Math.toRadians(angle));
            yp = rhigh * Math.sin(Math.toRadians(angle));
            this.detectorPath.addPoint(xp,yp,0.0);
        }
        
        
        this.detectorPath.addPoint(
                rhigh  * Math.cos(Math.toRadians(endAngle)),
                rhigh  * Math.sin(Math.toRadians(endAngle)),
                0.0
        );
        
        this.detectorPath.addPoint(
                rlow  * Math.cos(Math.toRadians(endAngle)),
                rlow  * Math.sin(Math.toRadians(endAngle)),
                0.0
        );
         
        for(double angle = endAngle; angle > startAngle; angle -= stepAngle){
            xp = rlow * Math.cos(Math.toRadians(angle));
            yp = rlow * Math.sin(Math.toRadians(angle));
            this.detectorPath.addPoint(xp,yp,0.0);
        }
    }
}
