/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jlab.clasrec.main.DetectorCalibration;
import org.jlab.geom.gui.DetectorComponentUI;

/**
 *
 * @author gavalian
 */
public class DetectorShapeTabView extends JPanel {
    
    private		JTabbedPane tabbedPane;
    
    public DetectorShapeTabView(){
        super();
        this.setPreferredSize(new Dimension(600,600));
        this.setLayout(new BorderLayout());
        this.initComponents();
    }
    
    private void initComponents(){
        tabbedPane = new JTabbedPane();
        this.add(tabbedPane,BorderLayout.CENTER);
    }
    
    public void addDetectorLayer( DetectorShapeView2D view){
        tabbedPane.addTab( view.getName(), view);
    }
    
    public void initWith(DetectorCalibration calib){
        TreeMap<String,DetectorShapeView2D>  map = calib.getDetectorList();
        for(Map.Entry<String,DetectorShapeView2D> entry : map.entrySet()){
            this.addDetectorLayer(entry.getValue());
        }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        DetectorShapeTabView  tab = new DetectorShapeTabView();
        //DetectorCalibration calib = new DetectorCalibration("a","b","1.0");
        //tab.initWith(calib);
        frame.add(tab);
        frame.pack();
        frame.setVisible(true);
    }
}
