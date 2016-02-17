/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import org.root.base.DataRegion;
import org.root.base.DataSetCollection;
import org.root.base.IDataSet;
import org.root.func.F1D;
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;
import org.root.histogram.H2D;

/**
 *
 * @author gavalian
 */
public class DataSetFrame {
    
    private  DataSetCollection   collection = new DataSetCollection();
    private  GraphicsAxisFrame   axisFrame  = new GraphicsAxisFrame();
    
    public  DataSetFrame(){
        
    }
    
    
    public GraphicsAxisFrame getAxisFrame(){
        return this.axisFrame;
    }
    
    public void drawOnCanvas(Graphics2D g2d, int xoffset, int yoffset, int w, int h){
        if(collection.getCount()==0){
            g2d.setColor(Color.WHITE);
            g2d.fillRect(xoffset, yoffset, w, h);
            return;
        }
        
        DataRegion  region = this.collection.getDataRegion();
        ( (GraphicsAxisNumber) this.axisFrame.getAxisX()).setRange(
                region.MINIMUM_X,region.MAXIMUM_X);
        
        ( (GraphicsAxisNumber) this.axisFrame.getAxisY()).setRange(
                region.MINIMUM_Y,this.collection.getDataRegion().MAXIMUM_Y);
        
        //System.out.println(region);
        
        this.axisFrame.drawOnCanvas(g2d, xoffset, yoffset, w, h);
        g2d.setClip(
                this.axisFrame.getMargins().x + 1, 
                this.axisFrame.getMargins().y + 1 , 
                this.axisFrame.getMargins().width  - 1, 
                this.axisFrame.getMargins().height - 1
                );
        for(int loop = 0; loop < this.collection.getCount(); loop++){
            if(this.collection.getDataSet(loop) instanceof H1D){
                if(this.collection.getDataSetOption(loop).contains("E")==true){
                    AbstractGraphicsFrameDraw.drawOnCanvasHistogram1D_EP(
                            g2d, axisFrame, this.collection.getDataSet(loop), 
                            0, 0,w, h);
                } else {
                    AbstractGraphicsFrameDraw.drawOnCanvasHistogram1D(
                            g2d, axisFrame, this.collection.getDataSet(loop), 
                            0, 0,w, h);
                }
            }
            
            if(this.collection.getDataSet(loop) instanceof F1D){
                AbstractGraphicsFrameDraw.drawOnCanvasFunction(
                        g2d, axisFrame, this.collection.getDataSet(loop), 
                        0, 0,w, h);
            }
            
            if(this.collection.getDataSet(loop) instanceof GraphErrors){
                AbstractGraphicsFrameDraw.drawOnCanvasGraph(
                        g2d, axisFrame, this.collection.getDataSet(loop), 
                        0, 0,w, h);
            }
            
            if(this.collection.getDataSet(loop) instanceof H2D){
                AbstractGraphicsFrameDraw.drawOnCanvasHistogram2D(
                            g2d, axisFrame, this.collection.getDataSet(loop), 
                            0, 0,w, h);
            }
        }
        g2d.setClip(null);
        //this.axisFrame.drawOnCanvas(g2d, xoffset, yoffset, w, h);
    }
    
    public void add(IDataSet ds){
        this.collection.clear();
        this.add(ds, "");
    }
    
    public void add(IDataSet ds,String option){
        this.collection.addDataSet(ds,option);
        
        if(this.collection.getCount()==1){
            //System.out.println("Adding the title");
            try {
                this.axisFrame.setTitle(ds.getAttributes().getProperties().getProperty("title"));
                this.axisFrame.getAxisX().setTitle(ds.getAttributes().getProperties().getProperty("xtitle"));
                this.axisFrame.getAxisY().setTitle(ds.getAttributes().getProperties().getProperty("ytitle"));           
            } catch (Exception e){
                
            }            
        }
        //this.updateStatBox();
    }
    
}
