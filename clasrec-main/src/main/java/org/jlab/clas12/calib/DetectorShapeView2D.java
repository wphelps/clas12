/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import org.jlab.geom.prim.Point3D;

/**
 *
 * @author gavalian
 */
public class DetectorShapeView2D {
    
    public  Rectangle  drawRegion = new Rectangle();
    private String     canvasName = "undefined";
    private List<DetectorShape2D>   shapes = new ArrayList<DetectorShape2D>();
            
    public DetectorShapeView2D(String name){
        canvasName = name;
    }
    
    public String getName(){ return this.canvasName;}
    
    public void addShape(DetectorShape2D shape){
        this.shapes.add(shape);
        this.updateDrawRegion();
    }
    
    public void updateDrawRegion(){
        
        drawRegion.x = 0;
        drawRegion.y = 0;
        drawRegion.width = 0;
        drawRegion.width = 0;
        
        for(DetectorShape2D shape : shapes){
            int npoints = shape.getShapePath().size();
            for(int p = 0; p < npoints; p++){
                Point3D point = shape.getShapePath().point(p);
                if(point.x()<drawRegion.x) drawRegion.x = (int) point.x();
                if(point.y()<drawRegion.y) drawRegion.y = (int) point.y();
                
                if(point.x()> (drawRegion.x + drawRegion.width)){
                    drawRegion.width = (int) (point.x() - drawRegion.x);
                }
                
                if(point.y()> (drawRegion.y + drawRegion.height)){
                    drawRegion.height = (int) (point.y() - drawRegion.y);
                }
            }
        }
    }
    
    public int getX(float x, int w){        
        double relX = (x - this.drawRegion.x)/this.drawRegion.width;
        return (int) (relX*w);
    }
    
    public int getY(float y, int h){
        double relY = (y - this.drawRegion.y)/this.drawRegion.height;
        return (int) (relY*h);
    }
    
    public void draw2D(Graphics2D g2d, int xoff, int yoff, int width, int height){
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        
        g2d.setColor(new Color(165,155,155));
        g2d.fillRect(xoff, yoff, width, height);
        
        for(DetectorShape2D shape : shapes){
            
            GeneralPath path = new GeneralPath();
            int npoints = shape.getShapePath().size();
            for(int p = 0; p < npoints; p++){
                Point3D  point = shape.getShapePath().point(p);
                int x = this.getX( (int) point.x(), width);
                int y = this.getY( (int) point.y(), height);
                if(p==0){
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            
            g2d.setColor(new Color(100,240,100));
            g2d.fill(path);
            g2d.setColor(Color.BLACK);
            g2d.draw(path);
        }
    }
}
