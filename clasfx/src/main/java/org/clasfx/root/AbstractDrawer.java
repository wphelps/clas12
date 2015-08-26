/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.root.base.AxisRegion;
import org.root.base.IDataSet;
import com.sun.javafx.tk.FontMetrics;
import javafx.scene.paint.Paint;
import org.root.base.DataRegion;
import org.root.histogram.H2D;
/**
 *
 * @author gavalian
 */
public class AbstractDrawer {
    
    public static void drawAxis(double xstart, double ystart, double width,
            double height, AxisRegion axis, GraphicsContext gc){
        
        gc.strokeRect(axis.getFrame().x,axis.getFrame().y,
                axis.getFrame().width,axis.getFrame().height);
        
        double xp = axis.getFrame().x;
        
        Font axisFont = TStyle.getAxisFont();
        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(axisFont);
        gc.setFont(axisFont);
        
        for(int cp = 0; cp < axis.getAxisY().getCoordinates().size();cp++){
            
            double yp = axis.getAxisY().getCoordinates().get(cp);
            double yr = axis.getDataRegion().fractionY(yp);
            double yf = axis.getFramePointY(yr);
            String label = axis.getAxisY().getCoordinatesLabels().get(cp);
            gc.setFill(Color.rgb(0, 0, 0));
            double th = fm.getXheight();
            double tw = fm.computeStringWidth(label);
            gc.strokeLine(axis.getFrame().x, yf, axis.getFrame().x+5, yf);
            //Text   label = new Text(axis.getAxisY().getCoordinatesLabels().get(cp));
            gc.fillText(label, xp - tw - 10, yf + th/2.0);
        }
        
        for(int cp = 0; cp < axis.getAxisX().getCoordinates().size();cp++){
            
            double xxp = axis.getAxisX().getCoordinates().get(cp);
            double xr = axis.getDataRegion().fractionX(xxp);
            double xf = axis.getFramePointX(xr);
            String label = axis.getAxisX().getCoordinatesLabels().get(cp);
            gc.setFill(Color.rgb(0, 0, 0));
            double th = fm.getXheight();
            double tw = fm.computeStringWidth(label);
            gc.strokeLine(xf,axis.getFrame().y + axis.getFrame().height, 
                    xf, axis.getFrame().y + axis.getFrame().height - 5);
            //Text   label = new Text(axis.getAxisY().getCoordinatesLabels().get(cp));
            gc.fillText(label, xf - (tw*0.5), axis.getFrame().y + axis.getFrame().height
                    + th*2.0);
            
        }
        
    }
    
    
    public static void updateAxisFrameBounds(double xstart, double ystart, double width,
            double height, AxisRegion axis, GraphicsContext gc){
        
        axis.getAxisX().getCoordinatesLabels();
        Font axisFont = TStyle.getAxisFont();
        Text axisLabel = new Text();
        axisLabel.setFont(axisFont);
        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(axisFont);
        double distance = 10;
        for(String text : axis.getAxisY().getCoordinatesLabels()){
            axisLabel.setText(text);
            //System.out.println(" TEXT = " + text + "  width = " + axisLabel.getWrappingWidth());
            if(fm.computeStringWidth(text)>distance){
                distance = fm.computeStringWidth(text);
            }
        }
        
        //System.out.println("distance = " + distance);
        axis.getFrame().x      = (int) (distance*2.0 + fm.getXheight()*3.0);
        axis.getFrame().width  = (int) (width - axis.getFrame().x - 30);
        axis.getFrame().y      = 30;
        axis.getFrame().height = (int) (height - (6.0*fm.getXheight()+30));
        
        double overlap = 0.6;
        int icounter   = 0;
        int maxticks   = 10;
        
        while(overlap>0.5){
        
            axis.getAxisX().setMaxTicks(maxticks-icounter);
            
            double overlapLocal = 0.0;
            for(String text : axis.getAxisX().getCoordinatesLabels()){
                axisLabel.setText(text);
                double tw = fm.computeStringWidth(text);
                overlapLocal += tw;
                //System.out.println(" TEXT = " + text + "  width = " + axisLabel.getWrappingWidth());
                if(tw>distance){
                    distance = tw;
                }
            }
            overlapLocal = overlapLocal/axis.getFrame().width;
            overlap      = overlapLocal;
            icounter++;
            if(icounter==9) break;
        }
        //double length = 
        //for(int loop = 0; loop < axis)
    }
    
    public static void drawGraphPoints(IDataSet ds, AxisRegion axis, GraphicsContext gc){
        int npoints = ds.getDataSize();
        gc.setFill(Color.rgb(0, 0, 0));
        //System.out.println("  DRAWING GRAPH POINTS = " + npoints);
        for(int loop = 0; loop < npoints; loop++){
            if(axis.getDataRegion().contains(ds.getDataX(loop),ds.getDataY(loop)) == true){
                double xr = axis.getDataRegion().fractionX(ds.getDataX(loop));
                double yr = axis.getDataRegion().fractionY(ds.getDataY(loop));
                double xf = axis.getFramePointX(xr);
                double yf = axis.getFramePointY(yr);
                //System.out.println("POINT " + loop + "  " + xf + "  " + yf);
                //System.out.println(" POINT = " + loop + "  " + xr + "  " + yr
                //+ " " + xf + " " + yf);
                gc.fillOval(xf, yf, 10, 10);
            }
        }
        
    }
    
    public static void drawFunction(IDataSet ds, AxisRegion axis, GraphicsContext gc){
        int npoints = ds.getDataSize();
        double xp = ds.getDataX(0);
        double yp = ds.getDataY(0);
        gc.setStroke(Color.rgb(0, 0, 0));
        gc.setLineWidth(2.0);
        for(int loop = 0; loop < npoints; loop++){
            double xps = axis.getDataRegion().fractionX(xp);
            double yps = axis.getDataRegion().fractionY(yp);
            double xpe = axis.getDataRegion().fractionX(ds.getDataX(loop));
            double ype = axis.getDataRegion().fractionY(ds.getDataY(loop));
            gc.strokeLine(axis.getFramePointX(xps), 
                    axis.getFramePointY(yps), 
                    axis.getFramePointX(xpe), 
                    axis.getFramePointY(ype));
            //System.out.println(" DRAWING LINE " + xps + " : " + yps + " ---> ");
            xp = ds.getDataX(loop);
            yp = ds.getDataY(loop);
        }
    }
    
    public static void drawHistogram(IDataSet ds, AxisRegion axis, GraphicsContext gc){
        int lineColor   = ds.getAttributes().getAsInt("line-color");
        int lineWidth   = ds.getAttributes().getAsInt("line-width");
        int fillColor   = ds.getAttributes().getAsInt("fill-color");
        
        //g2d.setStroke(new BasicStroke(lineWidth));
        //g2d.setColor(ColorPalette.getColor(lineColor));
        
        int npoints = ds.getDataSize();
        //GeneralPath path = new GeneralPath();
        double xf = axis.getDataRegion().fractionX(ds.getDataX(0)-ds.getErrorX(0)/2.0);
        double yf = axis.getDataRegion().fractionY(0.0);
        double xv = axis.getFramePointX(xf);
        double yv = axis.getFramePointY(yf);
        
        ArrayList<Double>  xPoints = new ArrayList<Double>();
        ArrayList<Double>  yPoints = new ArrayList<Double>();
        
        //path.moveTo(xv, yv);
        xPoints.add(xv);
        yPoints.add(yv);
        
        for(int loop = 0; loop < npoints; loop++){
            xf = axis.getDataRegion().fractionX(ds.getDataX(loop)-ds.getErrorX(loop)/2.0);
            yf = axis.getDataRegion().fractionY(ds.getDataY(loop));
            xv = axis.getFramePointX(xf);
            yv = axis.getFramePointY(yf);
            //System.out.println(" POINT " + loop + "  " + ds.getDataX(loop) 
            //        + "  " + ds.getDataY(loop));
            xPoints.add(xv);
            yPoints.add(yv);
            //path.lineTo(xv, yv);
            xf = axis.getDataRegion().fractionX(ds.getDataX(loop)+ds.getErrorX(loop)/2.0);
            xv = axis.getFramePointX(xf);
            xPoints.add(xv);
            yPoints.add(yv);
            //path.lineTo(xv, yv);
        }
        
        xf = axis.getDataRegion().fractionX(ds.getDataX(npoints-1)+ds.getErrorX(npoints-1)/2.0);
        yf = axis.getDataRegion().fractionY(0.0);
        xv = axis.getFramePointX(xf);
        yv = axis.getFramePointY(yf);
        xPoints.add(xv);
        yPoints.add(yv);
        
        gc.setStroke(Color.rgb(0, 0, 0));
        double[] xp = new double[xPoints.size()];
        double[] yp = new double[yPoints.size()];
        for(int loop = 0; loop < xp.length; loop++){
            xp[loop] = xPoints.get(loop);
            yp[loop] = yPoints.get(loop);
        }
        
        //gc.setFill(Color.SKYBLUE);
        gc.setFill(TStyle.getColor(fillColor));
        gc.fillPolygon(xp, yp, xPoints.size());
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.strokePolyline( xp, yp, xPoints.size());
        //path.lineTo(xv, yv);
        
        /*
        int fillColor = ds.getAttributes().getAsInt("fill-color");
        if(fillColor>0){
            g2d.setColor(ColorPalette.getColor(fillColor));
            g2d.fill(path);
        }
        g2d.setColor(ColorPalette.getColor(lineColor));
        g2d.draw(path);*/
    }
    
     public static void drawHistogram2D(IDataSet ds, AxisRegion axis, GraphicsContext gc){
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
         
         H2D  h2d = (H2D) ds;
         //System.out.println("HISTOGRAM MAXIMUM = " + h2d.getMaximum()
         //+ " FRACTION X = " + fractionX + "  Y = " + fractionY);

         
         for(int Lx = 0; Lx < dataSizeX; Lx++){
             for(int Ly = 0; Ly < dataSizeY; Ly++){
                 double xc = axis.getFrame().x + xr;
                 double yc = axis.getFrame().y + yr;
                 double value = ds.getData(Lx, Ly);
                 Paint  color = TStyle.getColorMap(value, maxValue, true);
                 gc.setFill(color);
                 //System.out.println(" COLOR " + color);
                 gc.fillRect(xc, yc, wr+1.0, hr+1.0);
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
