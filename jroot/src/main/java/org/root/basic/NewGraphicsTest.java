/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.basic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.root.base.LatexText;
import org.root.base.PaveText;

/**
 *
 * @author gavalian
 */
public class NewGraphicsTest extends JPanel implements MouseMotionListener {
    GraphicsAxis axisX = new GraphicsAxis();
    GraphicsAxis axisY = new GraphicsAxis();
    
    public NewGraphicsTest(){
        super();
        this.setSize(500, 500);
        this.setPreferredSize(new Dimension(600,600));
        this.addMouseMotionListener(this);
    }
    
    public void paintAxis(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        /*
        RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_TEXT_ANTIALIASING,
             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        */
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = this.getSize().width;
        int h = this.getSize().height;
        //System.err.println("PAINTING");
        axisX.setLength(w-100);
        axisX.setTitleFont("Avenir");
        axisX.setTitleSize(18);
        axisX.setAxis(new double[]{0.0,0.1,0.3,0.5,0.7,0.9,1.0}, 
                new String[]{"0.0","0.1","0.3","0.5","0.7","0.9","1.0"});

        axisY.setVertical(true);
        axisY.setLength(h-100);
        axisY.setTitleFont("Avenir");
        axisY.setTitleSize(18);
        axisY.setAxis(new double[]{0.0,0.1,0.3,0.5,0.7,0.9,1.0}, 
                new String[]{"0.0","0.1","0.3","0.5","0.7","0.9","1.0"});
        
        int y = h - 50;
        axisX.drawOnCanvas(g2d, 50, y);
        axisY.drawOnCanvas(g2d, 50, y);
    }
    
    public void paintPaveText(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        PaveText pave = new PaveText();
        pave.addText("ep#rarrowe'pX");
        LatexText text = pave.getTexts().get(0);
        text.setFont("Avenir");
        text.setFontSize(28);
        FontMetrics  fm = g2d.getFontMetrics(new Font("Avenir",Font.PLAIN,28));
        Rectangle2D  bounds = text.getBounds(fm, g2d);
        g2d.drawRect(100 + (int) bounds.getX(), 100 + (int) bounds.getY(),
                (int) bounds.getWidth(), (int) bounds.getHeight());
        g2d.drawString(text.getText().getIterator(), 100, 100);
    }
    
    @Override
    public void paint(Graphics g){        
        //this.paintAxis(g);
        this.paintPaveText(g);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(600,600);
        NewGraphicsTest panel = new NewGraphicsTest();
        frame.add(panel,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public void mouseDragged(MouseEvent e) {
        //System.out.println("----> mouse dragged");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseMoved(MouseEvent e) {
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
