/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
        
        int w = this.getSize().width;
        int h = this.getSize().height;
        
        axis.setSize(w, h);
        axis.setDataRegion(new DataRegion(0.0,5.0,0.0,5.0));
        Font  font = new Font("Helvetica",Font.PLAIN,24);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics(font);
        
        axis.update(fm);
        axis.getAxisX().updateWithFont(fm, axis.getFrame().width, false);
        axis.getAxisY().updateWithFont(fm, axis.getFrame().height, true);
        g2d.drawLine(0, 0, w, h);
        g2d.drawRect(axis.getFrame().x,axis.getFrame().y,
                axis.getFrame().width,axis.getFrame().height);
        
        List<String>  xticksL = axis.getAxisX().getCoordinatesLabels();
        List<Double>  xticksC = axis.getAxisX().getCoordinates();
        for(int loop = 0 ; loop < xticksC.size(); loop++){
            
            double x = axis.getDataPointX(xticksC.get(loop));
            System.out.println(loop + "  " + xticksC.get(loop) + "  " + x);
            g2d.drawString(xticksL.get(loop),(int) x, 100);
        }
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
