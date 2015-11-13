/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class AxisRegionFrameTest extends JPanel {
    
    public AxisRegion  axis = new AxisRegion();
    
    public AxisRegionFrameTest(){
        super();
        this.setPreferredSize(new Dimension(600,600));
    }
    
    @Override
    public void paint(Graphics g){        
        Graphics2D g2d = (Graphics2D) g;
        /*
        RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_TEXT_ANTIALIASING,
             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        */
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /*
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
             RenderingHints.VALUE_STROKE_NORMALIZE);
        */
        int w = this.getSize().width;
        int h = this.getSize().height;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, w, h);
        
        axis.setDataRegion(new DataRegion(5.0,25.0,0.0,25.0,0.0,25.0));
        axis.getAxisX().setAxisLog(true);
        axis.update(g2d,w,h);        
        axis.drawOnCanvas(g2d, w, h, 0, 0);

        /*
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, w, h);
        
        g2d.setColor(Color.black);

        
        axis.setSize(w, h);

                
        g2d.setFont(axis.getAxisFont());
        FontMetrics fm = g2d.getFontMetrics(axis.getAxisFont());
        */
        /*
        axis.update(fm);
        axis.getAxisX().updateWithFont(fm, axis.getFrame().width, false);
        axis.getAxisY().updateWithFont(fm, axis.getFrame().height, true);
        */
        /*
        g2d.drawLine(0, 0, w, h);
        g2d.drawRect(axis.getFrame().x,axis.getFrame().y,
                axis.getFrame().width,axis.getFrame().height);
        
        List<String>  xticksL = axis.getAxisX().getCoordinatesLabels();
        List<Double>  xticksC = axis.getAxisX().getCoordinates();
        
        for(int loop = 0 ; loop < xticksC.size(); loop++){            
            double x = axis.getDataPointX(xticksC.get(loop));
            //System.out.println(loop + "  " + xticksC.get(loop) + "  " + x);
            int    lw = fm.stringWidth(xticksL.get(loop));
            int    lh = fm.getHeight();
            g2d.drawLine((int) x,axis.getFrame().y, (int) x, 
                    axis.getFrame().y + 5);
            g2d.drawLine((int) x,axis.getFrame().y+axis.getFrame().height, (int) x, 
                    axis.getFrame().y + axis.getFrame().height - 10);
            g2d.drawString(xticksL.get(loop),(int) x - lw/2, axis.getFrame().y + axis.getFrame().height + lh);
        }
        
        List<String>  yticksL = axis.getAxisY().getCoordinatesLabels();
        List<Double>  yticksC = axis.getAxisY().getCoordinates();
        
        for(int loop = 0 ; loop < yticksC.size(); loop++){            
            double y  = axis.getDataPointY(yticksC.get(loop));
            int    lw = fm.stringWidth(yticksL.get(loop));
            int    lh = fm.getHeight();
            System.out.println(loop + " y tick " + yticksC.get(loop) + "  " + y);
            g2d.drawLine(axis.getFrame().x, (int) y, axis.getFrame().x + 20, (int) y);
            g2d.drawString(yticksL.get(loop),axis.getFrame().x - lw - 10, (int) (y+lh/2));
        }
        */
        /*
        for(int loop = 0; loop < 120000; loop++){
            int rX = (int) (100+Math.random()*300);
            int rY = (int) (100+Math.random()*300);
            g2d.drawLine(rX,rY,rX,rY);
        }*/
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(600,600);
        AxisRegionFrameTest panel = new AxisRegionFrameTest();
        frame.add(panel,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
