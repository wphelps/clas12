/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class JavaFontViewer extends JPanel implements ActionListener {
    String fonts[] = null;
    int    currentFont = 0;
    public JavaFontViewer(){
        super();
        this.setSize(500,500);
        this.setPreferredSize(new Dimension(500,500));
        fonts = 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for(String font : fonts){
            System.out.println("font name = " + font);
        }
    }
    
    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_TEXT_ANTIALIASING,
             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
		g2d.setColor(Color.BLACK);
		g2d.fillOval(0, 0, 30, 30);
		g2d.drawOval(0, 50, 30, 30);		
		g2d.fillRect(50, 0, 30, 30);
		g2d.drawRect(50, 50, 30, 30);

		g2d.draw(new Ellipse2D.Double(0, 100, 30, 30));
                String fontname = fonts[currentFont];
                Font font = new Font(fontname,Font.PLAIN,32);
                g2d.setFont(font);
                String[] axisText = new String[]{
                    "Entries     12345",
                    "Mean        0.7891245",
                    "Error    =  0.45/0.56"
                };
                g2d.drawString(fontname, 100, 100);
                int counter = 40;
                for(String text : axisText){
                    g2d.drawString(text, 100, 100+counter);
                    counter += 40;
                }
                Font fontb = new Font(fontname,Font.BOLD,32);
                g2d.setFont(fontb);
                
                //g2d.drawString(fontname, 100, 100);
                counter = 260;
                for(String text : axisText){
                    g2d.drawString(text, 100, 100+counter);
                    counter += 40;
                }
                
    }
    
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        
        
        JavaFontViewer viewer = new JavaFontViewer();
        frame.add(viewer, BorderLayout.CENTER);
        
        JButton button = new JButton("Next >");
        button.addActionListener(viewer);
        frame.add(button,BorderLayout.PAGE_START);
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("SOME ACTION PERFORMED");
        this.currentFont++;
        if(this.currentFont>=this.fonts.length) this.currentFont = 0;
        this.repaint();
    }
}
