/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import org.root.attr.Attributes;
import org.root.pad.AxisNiceScale;

/**
 *
 * @author gavalian
 */
public class AxisRegion {
    
    private final Rectangle   axisBoundaries  = new Rectangle();
    private final Rectangle   axisFrame       = new Rectangle();
    private final DataRegion  frameDataRegion = new DataRegion();
    private Attributes        axisAttributes  = new Attributes();
    
    private AxisNiceScale     axisX           = new AxisNiceScale(0.0,1.0);
    private AxisNiceScale     axisY           = new AxisNiceScale(0.0,2.0);
    private AxisNiceScale     axisZ           = new AxisNiceScale(0.0,2.0);
    
    public  Integer           axisLabelSize   = 18;
    /*
    private LatexText         axisTitleX      = new LatexText("#pi^2 [GeV]",0.5,0.0);
    private LatexText         axisTitleY      = new LatexText("#gamma^3 [deg]",0.0,0.5);
    private LatexText         frameTitle      = new LatexText("ep^#uarrow #rarrowe^'#pi^- X(#gamma)",0.5,1.0);
    */
    private LatexText         axisTitleX      = new LatexText(" ",0.5,0.0);
    private LatexText         axisTitleY      = new LatexText(" ",0.0,0.5);
    private LatexText         frameTitle      = new LatexText(" ",0.5,1.0);
    
    
    public  AxisRegion(int xsize, int ysize){
        this.axisBoundaries.x = 0;
        this.axisBoundaries.y = 0;
        this.axisBoundaries.width  = xsize;
        this.axisBoundaries.height = ysize;
        this.axisFrame.setBounds(10, 10, 40, 40);
        
        this.axisAttributes.getProperties().setProperty("background-color", "0");
        this.axisAttributes.getProperties().setProperty("line-color", "1");
        this.axisAttributes.getProperties().setProperty("line-width", "2");
        
        axisTitleX.setFont("Helvetica");
        axisTitleY.setFont("Helvetica");
        frameTitle.setFont("Helvetica");
        
        axisTitleX.setFontSize(axisLabelSize);
        axisTitleY.setFontSize(axisLabelSize);
        frameTitle.setFontSize(axisLabelSize);
    }
    
    public  AxisRegion(){
        
        this.axisFrame.setBounds(10, 10, 40, 40);
        this.axisAttributes.getProperties().setProperty("background-color", "0");
        this.axisAttributes.getProperties().setProperty("line-color", "1");
        this.axisAttributes.getProperties().setProperty("line-width", "2");
        
        axisTitleX.setFont("Helvetica");
        axisTitleY.setFont("Helvetica");
        frameTitle.setFont("Helvetica");
        
        axisTitleX.setFontSize(axisLabelSize);
        axisTitleY.setFontSize(axisLabelSize);
        frameTitle.setFontSize(axisLabelSize);
    }
    
    public void setSize(int width, int height){
        this.axisBoundaries.width  = width;
        this.axisBoundaries.height = height;
    }
    
    public LatexText getTitle(){
        return this.frameTitle;
    }
    
    
    public void update(FontMetrics fm){
        int height = fm.getHeight();
        int width  = fm.stringWidth("1000");
        
        int  xAxisOffset = this.axisX.getAxisOffset(fm, false);
        int  yAxisOffset = this.axisY.getAxisOffset(fm, true);
        
        this.axisFrame.x = xAxisOffset;
        this.axisFrame.y = yAxisOffset;
        
        this.axisFrame.width  = this.axisBoundaries.width  - xAxisOffset - 40;
        this.axisFrame.height = this.axisBoundaries.height - 2*yAxisOffset;
        System.out.println(this.axisFrame.x + " " + this.axisFrame.y 
                + " " + this.axisFrame.width + " " + this.axisFrame.height
                + "  "  + this.axisBoundaries.width + "  " + this.axisBoundaries.height);
    }
    
    public LatexText getXTitle(){ return this.axisTitleX;}
    public LatexText getYTitle(){ return this.axisTitleY;}
    
    public void setTitle(String title){
        this.frameTitle.setText(title);
    }
    
    public void setXTitle(String title){
        this.axisTitleX.setText(title);
    }
    
    public void setYTitle(String title){
        this.axisTitleY.setText(title);
    }
    
    public DataRegion getDataRegion(){ return this.frameDataRegion; }
    public Rectangle  getFrame() { return this.axisFrame;}
    public Attributes getAttributes(){ return this.axisAttributes;};
    
    public void setDataRegion(DataRegion region){
        this.frameDataRegion.copy(region);
        this.axisX.setMinMaxPoints(this.frameDataRegion.MINIMUM_X, 
                this.frameDataRegion.MAXIMUM_X);
        this.axisY.setMinMaxPoints(this.frameDataRegion.MINIMUM_Y, 
                this.frameDataRegion.MAXIMUM_Y);
    }
    
    public double getDataPointX(double dataX){
        double dataRelativeX = this.frameDataRegion.fractionX(dataX);
        return this.getFramePointX(dataRelativeX);
    }
    
    public double getDataPointY(double dataY){
        double dataRelativeY = this.frameDataRegion.fractionX(dataY);
        return this.getFramePointY(dataRelativeY);
    }
    
    public double   getFramePointX(double relX){
        //double length = 
        double xp = this.axisFrame.x+this.axisFrame.width*relX;
        return xp;
    }
    
    public AxisNiceScale  getAxisX(){ return axisX;}
    public AxisNiceScale  getAxisY(){ return axisY;}
    public AxisNiceScale  getAxisZ(){ return axisZ;}
    
    public double   getFramePointY(double relY){
        //double length = 
        double yp = this.axisFrame.y+ this.axisFrame.height - this.axisFrame.height*relY;
        return yp;
    }
    
    public void setDivisionsX(int div){
        this.axisX.setMaxTicks(div);
    }
    
    public void setDivisionsY(int div){
        this.axisY.setMaxTicks(div);
    }
            
}
