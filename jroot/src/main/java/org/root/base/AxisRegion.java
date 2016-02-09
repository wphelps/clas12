/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.root.attr.Attributes;
import org.root.attr.ColorPalette;
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
    
    private LatexText         axisTitleX      = new LatexText("",0.5,0.0);
    private LatexText         axisTitleY      = new LatexText("",0.0,0.5);
    private LatexText         frameTitle      = new LatexText("",0.5,1.0);
    
    
    private String            axisTicksFontName = "Avenir";
    private int               axisTicksFontSize = 14;
    
    private String            axisTitleFontName = "Avenir";
    private int               axisTitleFontSize = 14;
    
    private String            frameTitleFontName = "Avenir";
    private int               frameTitleFontSize = 18;
    
    //private Font              axisTickFont    = new Font("Bradley Hand",Font.PLAIN,24);
    
    private Font              axisTickFont     = new Font("Chalkduster", Font.PLAIN , 14);
    private Font              axisTitleFont    = new Font("Avenir"     , Font.PLAIN , 14);
    private Font              frameTitleFont   = new Font("Avenir"     , Font.PLAIN , 14);
    
    
    //*************************************************************** 
    //  MARGINs FOR THE MINIMUM DISTANCE FROM EDGES
    //*************************************************************** 
    private int               minMarginY = 0;
    private int               maxMarginY = 0;
    
    private int               minMarginX = 10;
    private int               maxMarginX = 5;
    
    private int               axisFrameFillColor      = 1;
    private int               axisFrameColor          = 0;
    private int               axisFrameTitleColor     = 0;
    private boolean           axisGridOptionX         = true;
    private boolean           axisGridOptionY         = true;
    private boolean           axisDrawOptionZ         = false;
    private boolean           axisFontAutoScale       = true;
    
    public static float[]  dashPattern1 = new float[]{10.0f,5.0f};
    public static float[]  dashPattern2 = new float[]{10.0f,5.0f,2.0f,5.0f};
    public static float[]  dashPattern3 = new float[]{2.0f,5.0f,2.0f,5.0f};

    
    public  AxisRegion(int xsize, int ysize){
        
        this.axisBoundaries.x = 0;
        this.axisBoundaries.y = 0;
        this.axisBoundaries.width  = xsize;
        this.axisBoundaries.height = ysize;
        this.axisFrame.setBounds(10, 10, 40, 40);
        
        this.axisAttributes.getProperties().setProperty("background-color", "0");
        this.axisAttributes.getProperties().setProperty("line-color", "1");
        this.axisAttributes.getProperties().setProperty("line-width", "2");
        
        axisTitleX.setFont("Arial");
        axisTitleY.setFont("Helvetica");
        frameTitle.setFont("Helvetica");
        
        axisTitleX.setFontSize(axisLabelSize);
        axisTitleY.setFontSize(axisLabelSize);
        frameTitle.setFontSize(axisLabelSize);
    }
    
    public void setZAxisDraw(boolean flag){
        this.axisDrawOptionZ = flag;
    }
    
    public  AxisRegion(){
        
        this.axisFrame.setBounds(10, 10, 20, 20);
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
    /**
     * Sets the width of the FRAME for axis
     * @param width
     * @param height 
     */
    public void setSize(int width, int height){
        this.axisBoundaries.width  = width;
        this.axisBoundaries.height = height;
    }
    /**
     * returns the title for the FRAME
     * @return 
     */
    public LatexText getTitle(){
        return this.frameTitle;
    }
    /**
     * Recalculates the margins for the axis, depending on the canvas size
     * if the auto font scaling is set, setAutoFontSize(true).
     * @param g2d
     * @param w
     * @param h 
     */
    public void update(Graphics2D g2d, int w, int h){

        //System.out.println("UPDATING SIZE = " + w + "  " + h);
        
        FontMetrics fma = g2d.getFontMetrics(axisTickFont);
        FontMetrics fmt = g2d.getFontMetrics(axisTitleFont);
        
        int  xAxisOffset = this.axisX.getAxisOffset(fma, false);
        int  yAxisOffset = this.axisY.getAxisOffset(fma, true);
        int  zAxisOffset = this.axisZ.getAxisOffset(fma, true);
        
        if(this.axisDrawOptionZ==false) zAxisOffset = 0;
        
        int fw = w; //g2d.getDeviceConfiguration().getBounds().width;
        int fh = h; //g2d.getDeviceConfiguration().getBounds().height;
        
        Rectangle2D  boundsX = this.axisTitleX.getBounds(fmt, g2d);
        Rectangle2D  boundsY = this.axisTitleY.getBounds(fmt, g2d);
        
        this.axisFrame.x      = fmt.getHeight() + yAxisOffset;
        this.axisFrame.width  = fw - axisFrame.x - this.minMarginX - (zAxisOffset + this.maxMarginX);
        
        this.axisFrame.y      = fmt.getHeight() + this.minMarginY;
        this.axisFrame.height = fh - axisFrame.y - ((int) (fmt.getHeight())) - fma.getHeight() - this.maxMarginY;
        
        this.getAxisX().updateWithFont(fma, this.getFrame().width, false);
        this.getAxisY().updateWithFont(fma, this.getFrame().height, true);
        this.getAxisZ().updateWithFont(fma, this.getFrame().height, true);
        /*System.out.println("W = " + fw + " H = " + fh +
                "  XOFF = " + xAxisOffset + "  YOFF = " + 
                yAxisOffset);
                */
    }
    
    public void setAxisFont(String fontname){
        this.axisTicksFontName = fontname;
        this.axisTickFont = new Font(this.axisTicksFontName,Font.PLAIN, this.axisTicksFontSize);
    }
    
    public void setAxisFontSize(int size){
        this.axisTicksFontSize = size;
        this.maxMarginX = size;
        this.axisTickFont = new Font(this.axisTicksFontName,Font.PLAIN, this.axisTicksFontSize);
    }
    
    public void setAxisTitleFont(String fontname){
        this.axisTitleFontName = fontname;
        this.axisTickFont = new Font(this.axisTitleFontName,Font.PLAIN,this.axisTitleFontSize);
    }
    
    public void setAxisTitleFont(int size){
        this.axisTitleFontSize = size;
        this.axisTitleFont = new Font(this.axisTitleFontName,Font.PLAIN,this.axisTitleFontSize);
    }
    
    public void setTitleFont(String fontname){
        this.frameTitleFontName = fontname;
        this.frameTitleFont     = new Font(this.frameTitleFontName,Font.PLAIN,this.frameTitleFontSize);
    }
    
    public void setTitleFont(int size){
        this.frameTitleFontSize = size;
        this.frameTitleFont     = new Font(this.frameTitleFontName,Font.PLAIN,this.frameTitleFontSize);
    }
    
    
    public void setMarginsX(int min, int max){
        this.minMarginX = min;
        this.maxMarginX = max;
    }
    
    public void setMarginsY(int min, int max){
        this.minMarginY = min;
        this.maxMarginY = max;
    }
    
    public void drawOnCanvasGrid(Graphics2D g2d, int w, int h, int xoffset, int yoffset){
        // Drawing GRID on X-axis
        List<Double>  xticksC = this.getAxisX().getCoordinates();
        for(int loop = 0; loop < xticksC.size(); loop++){
            double x = this.getDataPointX(xticksC.get(loop));
            g2d.drawLine(
                        xoffset + (int) x,
                        yoffset + this.axisFrame.y,
                        xoffset + (int) x,
                        yoffset + this.axisFrame.y + this.axisFrame.height
                );
        }
        
        if(this.getAxisX().getAxisLog()==true){
            List<Double>  xticksM = this.getAxisX().getMinorTicks();
            for(int loop = 0; loop < xticksM.size(); loop++){
            double x = this.getDataPointX(xticksM.get(loop));
            g2d.drawLine(
                        xoffset + (int) x,
                        yoffset + this.axisFrame.y,
                        xoffset + (int) x,
                        yoffset + this.axisFrame.y + this.axisFrame.height
                );
            }
        }
    }
    
    public void drawOnCanvasBackground(Graphics2D g2d, int w, int h, int xoffset, int yoffset){
        g2d.setColor(Color.white);
        g2d.drawRect(xoffset, yoffset, w, h);
    }
    
    public void drawOnCanvas(Graphics2D g2d, int w, int h, int xoffset, int yoffset){
        
        g2d.setColor(Color.BLACK);

        FontMetrics fma = g2d.getFontMetrics(axisTickFont);
        FontMetrics fmt = g2d.getFontMetrics(axisTitleFont);
        
        g2d.setFont(axisTickFont);
               BasicStroke   lineStroke = new BasicStroke(2);
        BasicStroke   gridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 20.0f, dashPattern1, 0.0f);
        //---------------------------------------------------------------------
        // Draging X axis 
        //---------------------------------------------------------------------
        List<String>  xticksL = this.getAxisX().getCoordinatesLabels();
        List<Double>  xticksC = this.getAxisX().getCoordinates();
        
        for(int loop = 0 ; loop < xticksC.size(); loop++){            
            double x = this.getDataPointX(xticksC.get(loop));
            //System.out.println(loop + "  " + xticksC.get(loop) + "  " + x);
            int    lw = fma.stringWidth(xticksL.get(loop));
            int    lh = fma.getHeight();

            if(this.axisGridOptionX==true){
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.setStroke(gridStroke);
                
                g2d.drawLine(
                        xoffset + (int) x,
                        yoffset + this.axisFrame.y,
                        xoffset + (int) x,
                        yoffset + this.axisFrame.y + this.axisFrame.height
                );
            }
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(lineStroke);
            g2d.drawLine(xoffset + (int) x,
                    yoffset + this.getFrame().y, 
                    xoffset + (int) x, 
                    yoffset + this.getFrame().y + 5);
            g2d.drawLine(xoffset + (int) x,
                    yoffset + this.getFrame().y+this.getFrame().height, 
                    xoffset + (int) x, 
                    yoffset + this.getFrame().y + this.getFrame().height - 10);
            g2d.drawString(
                    xticksL.get(loop),
                    xoffset + (int) x - lw/2, 
                    yoffset + this.getFrame().y + this.getFrame().height + lh);            
        }
        
        //---------------------------------------------------------------------
        // Draging Y axis 
        //---------------------------------------------------------------------
        List<String>  yticksL = this.getAxisY().getCoordinatesLabels();
        List<Double>  yticksC = this.getAxisY().getCoordinates();
        
        for(int loop = 0 ; loop < yticksC.size(); loop++){            
            double y = this.getDataPointY(yticksC.get(loop));
            //System.out.println(loop + "  " + xticksC.get(loop) + "  " + x);
            int    lw = fma.stringWidth(yticksL.get(loop));
            int    lh = fma.getHeight();
            
            if(this.axisGridOptionY==true){
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.setStroke(gridStroke);
                
                g2d.drawLine(
                        xoffset + this.axisFrame.x,
                        yoffset + (int) y,
                        xoffset + this.axisFrame.x + this.axisFrame.width,
                        yoffset + (int) y
                );
            }
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(lineStroke);
            g2d.drawLine(
                    xoffset + axisFrame.x + axisFrame.width - 5,
                    yoffset + (int) y, 
                    xoffset + axisFrame.x + axisFrame.width ,
                    yoffset + (int) y);
            
            g2d.drawLine(
                    xoffset + axisFrame.x ,
                    yoffset + (int) y, 
                    xoffset + axisFrame.x + 10,
                    yoffset + (int) y);
            
            g2d.drawString(
                    yticksL.get(loop),
                    xoffset + (int) axisFrame.x - lw - 5, 
                    yoffset + lh/2 + (int) y );            
        }
        
        //---------------------------------------------------------------------
        // Draging Z axis 
        //---------------------------------------------------------------------
        if(this.axisDrawOptionZ==true){
            List<String>  zticksL = this.getAxisZ().getCoordinatesLabels();
            List<Double>  zticksC = this.getAxisZ().getCoordinates();
            ColorPalette  palette = new ColorPalette();
            int ncolors = palette.getColor3DSize();
            double   step    = this.axisFrame.height/ncolors;
            int ypos = (int) (this.axisFrame.y + this.axisFrame.height - step);
            for(int c = 0 ; c < ncolors; c++){
                g2d.setColor(palette.getColor3D(c));
                g2d.fillRect(axisFrame.x + axisFrame.width,ypos,20,(int) step);
                ypos -= step;
            }
            
            g2d.setColor(Color.BLACK);
            g2d.drawRect(axisFrame.x + axisFrame.width, axisFrame.y, 20, axisFrame.height);
            for(int loop = 0 ; loop < zticksC.size(); loop++){ 
                double z = this.getDataPointZ(zticksC.get(loop));
                System.out.println(loop + "  " + zticksC.get(loop) + "  " + z);
                int    lw = fma.stringWidth(zticksL.get(loop));
                int    lh = fma.getHeight();
                g2d.setColor(Color.BLACK);
                g2d.setStroke(lineStroke);
                g2d.drawLine(
                        xoffset + axisFrame.x + axisFrame.width + 20,
                        yoffset + (int) z, 
                        xoffset + axisFrame.x + axisFrame.width + 20 + 5,
                        yoffset + (int) z);
                
                g2d.drawString(
                        zticksL.get(loop),
                        xoffset + (int) axisFrame.x + axisFrame.width + 20 + 10,
                        yoffset + lh/2 + (int) z );   
            }
        }
        
        g2d.drawRect(this.axisFrame.x + xoffset, this.axisFrame.y+yoffset,
                this.axisFrame.width,this.axisFrame.height);
        
        this.axisTitleX.setFont("Helvetica");
        this.axisTitleX.setFontSize(this.axisTitleFontSize);
        
        Rectangle2D rectX = axisTitleX.getBounds(fmt, g2d);
        int th = fmt.getHeight();
        g2d.drawString(this.axisTitleX.getText().getIterator(), 
                xoffset + (int) (axisFrame.x + axisFrame.width/2 - rectX.getWidth()/2), 
                yoffset + (int) (h - rectX.getHeight()/2 + rectX.getHeight()*0.2));
                
        //System.out.println("TITLE = " + this.getTitle());
    }
    
    public Font getAxisFont(){return this.axisTickFont;}
    
    public void update(FontMetrics fm){
        int height = fm.getHeight();
        int width  = fm.stringWidth("1000");
        
        int  xAxisOffset = this.axisX.getAxisOffset(fm, false);
        int  yAxisOffset = this.axisY.getAxisOffset(fm, true);
        int  zAxisOffset = this.axisZ.getAxisOffset(fm, true);
        
        this.axisFrame.x = xAxisOffset;
        this.axisFrame.y = yAxisOffset;
        
        this.axisFrame.width  = this.axisBoundaries.width  - xAxisOffset - 40 ;
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
        this.axisZ.setMinMaxPoints(this.frameDataRegion.MINIMUM_Z,this.
                frameDataRegion.MAXIMUM_Z);
    }
    
    public double getDataPointX(double dataX){
        double dataRelativeX = this.frameDataRegion.fractionX(dataX);
        return this.getFramePointX(dataRelativeX);
    }
    
    public double getDataPointY(double dataY){
        double dataRelativeY = this.frameDataRegion.fractionY(dataY);
        return this.getFramePointY(dataRelativeY);
    }
    
    public double getDataPointZ(double dataZ){
        double dataRelativeZ = this.frameDataRegion.fractionZ(dataZ);
        System.out.println(" Z = " + dataZ + "  fraction = " + dataRelativeZ );
        return this.getFramePointY(dataRelativeZ);
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
