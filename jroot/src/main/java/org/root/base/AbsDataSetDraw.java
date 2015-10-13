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
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.root.attr.ColorPalette;
import org.root.attr.MarkerPainter;
import org.root.attr.TStyle;
import org.root.func.F1D;
import org.root.histogram.H2D;

/**
 *
 * @author gavalian
 */
public class AbsDataSetDraw {
    
    public static float[]  dashPattern1 = new float[]{10.0f,5.0f};
    public static float[]  dashPattern2 = new float[]{10.0f,5.0f,2.0f,5.0f};
    public static float[]  dashPattern3 = new float[]{2.0f,5.0f,2.0f,5.0f};
    
    public static void drawAxisBackGround(AxisRegion axis, Graphics2D g2d, 
            int startX, int startY, int gWidth, int gHeight){
        Color background  = ColorPalette.getColor(axis.getAttributes().getAsInt("background-color"));
        Color linecolor   = ColorPalette.getColor(axis.getAttributes().getAsInt("line-color"));
        Integer lineWidth = axis.getAttributes().getAsInt("line-width");
        Color backgroundColor = TStyle.getFrameBackgroundColor();
        g2d.setColor(backgroundColor);
        g2d.fillRect(startX,startY,gWidth,gHeight);
        
         Color   frameColor = TStyle.getFrameFillColor();
         g2d.setColor(frameColor);
        g2d.fillRect(axis.getFrame().x, axis.getFrame().y, 
                axis.getFrame().width, axis.getFrame().height);
        g2d.setColor(linecolor);
        
    }
    
    public static void drawText(AxisRegion axis, Graphics2D g2d, LatexText text, 
            int startX, int startY, int gWidth, int gHeight){
        g2d.setColor(ColorPalette.getColor(text.getColor()));
        double x = axis.getFramePointX(text.getX());
        double y = axis.getFramePointY(text.getY());
        g2d.drawString(text.getText().getIterator(),(int) x,(int) y);
    }
    /**
     * Plotting PaveText Object on the canvas
     * @param axis
     * @param text
     * @param g2d
     * @param startX
     * @param startY
     * @param gWidth
     * @param gHeight 
     */
    public static void drawPaveText(AxisRegion axis, PaveText text, Graphics2D g2d,
            int startX, int startY, int gWidth, int gHeight){

        
        
        Font  axisFont = new Font(TStyle.getStatBoxFontName(),Font.PLAIN,TStyle.getStatBoxFontSize());
        FontMetrics  fm = g2d.getFontMetrics(axisFont);
        
        text.setFont(TStyle.getStatBoxFontName(),TStyle.getStatBoxFontSize());
        
        Rectangle2D  bounds = text.getBounds(fm, g2d);
        /*
        System.out.println(" PAVETEXT BOUNDS = " 
                + bounds.getX() + "  " 
                + bounds.getY() + "  " 
                + bounds.getCenterX() + "  " 
                + bounds.getCenterY() + "  " 
                + bounds.getMaxX() + "  "
                + bounds.getMaxY() + "  "
                
                + bounds.getWidth() + "  " 
                + bounds.getHeight()
        );
        */
        
        double w = bounds.getWidth()*1.2;
        double h = bounds.getHeight()*1.0;
        double xoffset = bounds.getWidth()*0.1;
        double yoffset = bounds.getHeight()*0.1;
        
        double originXbox = axis.getFrame().width + axis.getFrame().x - w;
        double originYbox = axis.getFrame().y ;
        double originX = originXbox + xoffset;
        double originY = originYbox + + TStyle.getStatBoxTextGap()*TStyle.getStatBoxFontSize();
        
        g2d.setColor(TStyle.getAxisColor());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(
                (int) originXbox, (int) originYbox,
                (int) w, (int) h);

        //g2d.setFont(axisFont);
        List<LatexText> texts = text.getTexts();
        
        int counter = 0;
        for(LatexText t : texts){
            int ypos = (int) (originY + counter*TStyle.getStatBoxTextGap()*TStyle.getStatBoxFontSize());
            counter++;
            g2d.drawString(t.getText().getIterator(), (int) originX, ypos);
        }
        //for(int loop = 0; loop )
    }
    /**
     * Drawing axis frame on the pad.
     * @param axis
     * @param g2d
     * @param startX
     * @param startY
     * @param gWidth
     * @param gHeight 
     */
    public static void drawAxisFrame(AxisRegion axis, Graphics2D g2d, 
            int startX, int startY, int gWidth, int gHeight){
        
        Color background  = ColorPalette.getColor(axis.getAttributes().getAsInt("background-color"));
        Color linecolor   = ColorPalette.getColor(axis.getAttributes().getAsInt("line-color"));
        Integer lineWidth = axis.getAttributes().getAsInt("line-width");
        Color   axisColor = TStyle.getAxisColor();
       
        
        //g2d.setColor(background);
        //g2d.fillRect(startX,startY,gWidth,gHeight);
        /*
        g2d.setColor(frameColor);
        g2d.fillRect(axis.getFrame().x, axis.getFrame().y, 
                axis.getFrame().width, axis.getFrame().height);
        */
        g2d.setColor(axisColor);
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.drawRect(axis.getFrame().x, axis.getFrame().y, 
                axis.getFrame().width, axis.getFrame().height);
        
        ArrayList<Double>  ticksX = axis.getAxisX().getCoordinates();
        ArrayList<Double>  ticksY = axis.getAxisY().getCoordinates();
        
        ArrayList<String>  stringX = axis.getAxisX().getCoordinatesLabels();
        ArrayList<String>  stringY = axis.getAxisY().getCoordinatesLabels();
        
        //Font  axisFont = new Font("Helvetica",Font.PLAIN,axis.axisLabelSize);
        Font  axisFont = new Font("Helvetica",Font.PLAIN,TStyle.getAxisFontSize());
        //Font  axisFont = new Font(Font.SANS_SERIF,Font.PLAIN,axis.axisLabelSize);
        FontMetrics  fm = g2d.getFontMetrics(axisFont);
        
        axis.getAxisX().updateWithFont(fm, axis.getFrame().width, false);
        axis.getAxisY().updateWithFont(fm, axis.getFrame().height, true);
        
        g2d.setFont(axisFont);
        
        
        /*
        * Drawing minor ticks for X-axis
        */
        List<Double>  minorTicksX = axis.getAxisX().getMinorTicks();
        List<Double>  minorTicksY = axis.getAxisY().getMinorTicks();
        
        for(int loop = 0; loop < minorTicksX.size(); loop++){
            double xr = axis.getDataRegion().fractionX(minorTicksX.get(loop),
                    axis.getAxisX().getAxisLog());
            double xv = axis.getFramePointX(xr);
            double yv = axis.getFrame().height + axis.getFrame().y;            
            g2d.drawLine((int) xv, (int) yv, (int) xv, (int) yv-5);            
        }
        
        for(int loop = 0; loop < minorTicksY.size(); loop++){
            double yr = axis.getDataRegion().fractionY(minorTicksY.get(loop),
                    axis.getAxisY().getAxisLog());
            //System.out.println(" Y axis LOOP =  " + loop + "  ticks = " + minorTicksY.get(loop)
            //+ "  " + yr);
            int offset = 5;
            
            double yv = axis.getFramePointY(yr);
            double xv = axis.getFrame().x;            
            g2d.drawLine((int) xv, (int) yv, (int) xv+5, (int) yv);
        }
        
        /*
         * Drawing Major ticks for X-axis. 
         */
        for(int loop = 0; loop < ticksX.size(); loop++){
            double xr = axis.getDataRegion().fractionX(ticksX.get(loop));
            double xv = axis.getFramePointX(xr);
            double yv = axis.getFrame().height + axis.getFrame().y;
                        
            g2d.drawLine((int) xv, (int) yv, (int) xv, (int) yv-10);
            String label = stringX.get(loop);
            
            int xoff = (int) fm.stringWidth(label)/2;
            int yoff = fm.getHeight();
            g2d.drawString(stringX.get(loop), (int) xv-xoff, (int) yv+yoff);
        }
        //System.out.println(" SIZE OF Y = " + ticksY.size());
        /*
        *  Drawing Major ticks for Y axis.
        */
        for(int loop = 0; loop < ticksY.size(); loop++){
            double yr = axis.getDataRegion().fractionY(ticksY.get(loop),axis.getAxisY().getAxisLog());
            double yv = axis.getFramePointY(yr);
            double xv = axis.getFrame().x;            
            g2d.drawLine((int) xv, (int) yv, (int) xv+10, (int) yv);
            //System.out.println("  AXIS TICKS = " + loop + "  " + ticksY.get(loop) + "  " + yr);
            String label = stringY.get(loop);
            int xoff = (int) fm.stringWidth(label) + 10;
            int yoff = (int) (fm.getHeight()/2.0 - fm.getHeight()*0.2);
            g2d.drawString(label, (int) xv-xoff, (int) yv+yoff);
        }
        

        FontMetrics  fma = g2d.getFontMetrics();
        //g2d.setFont(axisFont);
        
        axis.getTitle().setFont("Helvetica");
        axis.getTitle().setFontSize(axis.axisLabelSize);
        
        axis.getXTitle().setFont("Helvetica");
        axis.getXTitle().setFontSize(axis.axisLabelSize);
        
        axis.getYTitle().setFont("Helvetica");
        axis.getYTitle().setFontSize(axis.axisLabelSize);
        
        Rectangle2D rect = fma.getStringBounds(axis.getTitle().getText().getIterator(), 0,
                axis.getTitle().getText().getIterator().getEndIndex(),g2d);
        
        
        Rectangle2D rectY = fma.getStringBounds(axis.getYTitle().getText().getIterator(), 0,
                axis.getYTitle().getText().getIterator().getEndIndex(),g2d);
        
        Rectangle2D rectX = fma.getStringBounds(axis.getXTitle().getText().getIterator(), 0,
                axis.getXTitle().getText().getIterator().getEndIndex(),g2d);
         
        double xt = axis.getFramePointX(axis.getXTitle().getX());
        double yt = axis.getFramePointY(axis.getXTitle().getY());
        
        g2d.drawString(axis.getXTitle().getText().getIterator(), (int) (xt - rectX.getWidth()/2.0), (int) yt+48);
        
        xt = axis.getFramePointX(axis.getTitle().getX());
        yt = axis.getFramePointY(axis.getTitle().getY());
        
        xt = axis.getFrame().x + axis.getFrame().width/2 - rect.getWidth()/2;
        yt = axis.getFrame().y - 0.8*rect.getHeight();
        g2d.drawString(axis.getTitle().getText().getIterator(), (int) xt, (int) yt);
        
        xt = -axis.getFrame().height-axis.getFrame().y + axis.getFrame().height/2.0 - rectY.getWidth()/2.0;
        yt = 50;
        AffineTransform orig = g2d.getTransform();
        g2d.rotate(-Math.PI/2);
        //g2d.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
        g2d.drawString(axis.getYTitle().getText().getIterator(), (int) (xt ),
                (int) yt);
        g2d.setTransform(orig);
        
    }
    
    public static void drawDataSetAsGraph(AxisRegion axis, Graphics2D g2d, IDataSet ds,
            int startX, int startY, int gWidth, int gHeight){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        MarkerPainter  mPainter = new MarkerPainter();
        int markerStyle = ds.getAttributes().getAsInt("marker-style");
        int markerColor = ds.getAttributes().getAsInt("marker-color");
        int markerSize  = ds.getAttributes().getAsInt("marker-size");
        int npoints = ds.getDataSize();
        //System.out.println("Graph points = " + npoints);
        for(int loop = 0; loop < npoints; loop++){
            if(axis.getDataRegion().contains(ds.getDataX(loop),ds.getDataY(loop)) == true){
                double xr = axis.getDataRegion().fractionX(ds.getDataX(loop),axis.getAxisX().getAxisLog());
                double yr = axis.getDataRegion().fractionY(ds.getDataY(loop),axis.getAxisY().getAxisLog());
                double xf = axis.getFramePointX(xr);
                double yf = axis.getFramePointY(yr);
                //System.out.println(" POINT = " + loop + "  " + xr + "  " + yr
                //+ " " + xf + " " + yf);
                mPainter.drawMarker(g2d, (int) xf, (int) yf, markerStyle, 
                        markerSize, markerColor, 1,1);

            }
        }
    }
    
    public static void drawDataSetAsFunction(AxisRegion axis, Graphics2D g2d, IDataSet ds,
            int startX, int startY, int gWidth, int gHeight){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int line_color = 1;
        int line_width = 1;
        int line_style = 1;
        if(ds.getClass().isAssignableFrom(F1D.class)==true){
            F1D func = (F1D) ds;
            line_color = func.getLineColor();
            line_width = func.getLineWidth();
            line_style = func.getLineStyle();
        }
        
        MarkerPainter  mPainter = new MarkerPainter();
        //int markerStyle = ds.getAttributes().getAsInt("marker-style");
        //int markerColor = ds.getAttributes().getAsInt("marker-color");
        //int markerSize  = ds.getAttributes().getAsInt("marker-size");
        
        int npoints = ds.getDataSize();
        //System.out.println("Graph points = " + npoints);
        GeneralPath  path = new GeneralPath();
        g2d.setStroke(new BasicStroke(line_width));
        /*
        float dash[] = { 10.0f, 5.0f , 2.0f , 5.0f};
        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER, 20.0f, dash, 0.0f));
        */
        if(line_style==2){
            g2d.setStroke(new BasicStroke(line_width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 20.0f, dashPattern1, 0.0f));
        }
        
        if(line_style==3){
            g2d.setStroke(new BasicStroke(line_width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 20.0f, dashPattern2, 0.0f));
        }
        
        if(line_style==4){
            g2d.setStroke(new BasicStroke(line_width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 20.0f, dashPattern3, 0.0f));
        }
        
        g2d.setColor(ColorPalette.getColor(line_color));
        for(int loop = 0; loop < npoints; loop++){
            if(axis.getDataRegion().contains(ds.getDataX(loop),ds.getDataY(loop)) == true){
                double xr = axis.getDataRegion().fractionX(ds.getDataX(loop));
                double yr = axis.getDataRegion().fractionY(ds.getDataY(loop));
                double xf = axis.getFramePointX(xr);
                double yf = axis.getFramePointY(yr);
                //System.out.println(" POINT = " + loop + "  " + xr + "  " + yr
                //+ " " + xf + " " + yf);
                if(loop == 0){
                    path.moveTo( (int) xf, (int) yf);
                } else {
                    //System.out.println("adding point " + xf + " " + yf);
                    path.lineTo((int) xf, (int) yf);
                }
                /*
                mPainter.drawMarker(g2d, (int) xf, (int) yf, markerStyle, 
                        markerSize, markerColor, 1,1);
                */
            }           
        }
        g2d.draw(path);
    }
    
    public static void drawDataSetAsHistogram1D(AxisRegion axis, Graphics2D g2d, IDataSet ds,
            int startX, int startY, int gWidth, int gHeight){
        int lineColor   = ds.getAttributes().getAsInt("line-color");
        int lineWidth   = ds.getAttributes().getAsInt("line-width");
        
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.setColor(ColorPalette.getColor(lineColor));
        
        int npoints = ds.getDataSize();
        GeneralPath path = new GeneralPath();
        double xf = axis.getDataRegion().fractionX(ds.getDataX(0)-ds.getErrorX(0)/2.0,axis.getAxisX().getAxisLog());
        double yf = axis.getDataRegion().fractionY(0.0,axis.getAxisY().getAxisLog());
        double xv = axis.getFramePointX(xf);
        double yv = axis.getFramePointY(yf);
        path.moveTo(xv, yv);
        for(int loop = 0; loop < npoints; loop++){
            xf = axis.getDataRegion().fractionX(ds.getDataX(loop)-ds.getErrorX(loop)/2.0,axis.getAxisX().getAxisLog());
            yf = axis.getDataRegion().fractionY(ds.getDataY(loop),axis.getAxisY().getAxisLog());
            xv = axis.getFramePointX(xf);
            yv = axis.getFramePointY(yf);
            //System.out.println(" POINT " + loop + "  " + ds.getDataX(loop) 
            //        + "  " + ds.getDataY(loop) + "  FRACTIONS = " + xf + "  " + yf);
            path.lineTo(xv, yv);
            xf = axis.getDataRegion().fractionX(ds.getDataX(loop)+ds.getErrorX(loop)/2.0,axis.getAxisX().getAxisLog());
            xv = axis.getFramePointX(xf);
            
            path.lineTo(xv, yv);
        }
        xf = axis.getDataRegion().fractionX(ds.getDataX(npoints-1)+ds.getErrorX(npoints-1)/2.0);
        yf = axis.getDataRegion().fractionY(0.0);
        xv = axis.getFramePointX(xf);
        yv = axis.getFramePointY(yf);
        path.lineTo(xv, yv);
        
        int fillColor = ds.getAttributes().getAsInt("fill-color");
        if(fillColor>0){
            g2d.setColor(ColorPalette.getColor(fillColor));
            g2d.fill(path);
        }
        g2d.setColor(ColorPalette.getColor(lineColor));
        g2d.draw(path);
        //g2d.fill(path);
    }
    
     public static void drawDataSetAsHistogram2D(AxisRegion axis, Graphics2D g2d, IDataSet ds,
            int startX, int startY, int gWidth, int gHeight){
         
         
         int dataSizeX = ds.getDataSize(0);
         int dataSizeY = ds.getDataSize(1);
         DataRegion  region = ds.getDataRegion();
         double fractionX   = ( (double) axis.getFrame().width )/ dataSizeX;
         double fractionY   = ((double) axis.getFrame().height)/( (double) dataSizeY);
         double xr = 0.0;
         double yr = 0.0;
         double wr = fractionX;         
         double hr = fractionY;
         double maxValue = region.MAXIMUM_Z;
         boolean isLogZ = axis.getAxisZ().getAxisLog();
         
         H2D  h2d = (H2D) ds;
         //System.out.println("HISTOGRAM MAXIMUM = " + h2d.getMaximum()
         //+ " FRACTION X = " + fractionX + "  Y = " + fractionY);
         ColorPalette map = new ColorPalette();
         
         for(int Lx = 0; Lx < dataSizeX; Lx++){
             for(int Ly = 0; Ly < dataSizeY; Ly++){
                 double xc = axis.getFrame().x + xr;
                 double yc = axis.getFrame().y + yr;
                 double value = ds.getData(Lx, Ly);
                 //Paint  color = TStyle.getColorMap(value, maxValue, true);
                 //gc.setFill(color);
                 //System.out.println(" COLOR " + color);
                 Color  color = map.getColor3D(value, maxValue, isLogZ);
                 g2d.setColor(color);
                 g2d.fillRect( (int) xc, (int) yc, (int) wr+1, (int) hr+1);
                 //System.out.println(" DRAWING " + Lx + " " + Ly + "  "
                 //+ xr + " " + yr + "  " + wr + "  " + hr + "  " + color);
                 //xr = xr + wr;
                 yr = yr + hr;
             }
             yr = 0.0;
             xr = xr + wr;
         }
     }
    
}
