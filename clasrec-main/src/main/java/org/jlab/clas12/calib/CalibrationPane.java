/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.root.histogram.H1D;
import org.root.pad.TBookCanvas;

/**
 *
 * @author gavalian
 */
public class CalibrationPane extends JPanel {
    
    JSplitPane splitPane = null;
    DetectorShapeTabView  detectorTabView = null;
    CalibrationControlPanel controlPanel  = null;
    
    public CalibrationPane(){
        super();
        this.setLayout(new BorderLayout());
        this.splitPane = new JSplitPane();
        this.detectorTabView = new DetectorShapeTabView();
        this.controlPanel    = new CalibrationControlPanel();
        this.splitPane.setLeftComponent(this.detectorTabView);
        this.splitPane.setRightComponent(this.controlPanel);
        this.add(this.splitPane,BorderLayout.CENTER);
    }
    
    public DetectorShapeTabView  getDetectorView(){return this.detectorTabView;}
    public JPanel                getCanvasPane(){ return this.controlPanel.getTopPane();}
    public JPanel                getTablePane() { return this.controlPanel.getButtomPane();}
    public JPanel                getBottonPane() {return this.controlPanel.getButtonPane();}
    public void                  addDetectorListener(IDetectorListener lt){
        
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        CalibrationPane pane = new CalibrationPane();
        
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
    }
    
    
}
