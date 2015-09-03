/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import org.jlab.clasrec.main.DetectorCalibration;
import org.jlab.evio.clas12.EvioDataEvent;
import org.root.pad.EmbeddedCanvas;

/**
 *
 * @author gavalian
 */
public class DetectorCalibrationFrame extends JInternalFrame {
    
    JSplitPane       splitPane = new JSplitPane();
    EmbeddedCanvas   canvas    = new EmbeddedCanvas();
    DetectorShapeTabView tabView = null;
    private DetectorCalibration  calibration = null;
    
    public DetectorCalibrationFrame(DetectorCalibration calib){        
        super("Detector Plugin",true,true,true,true);
        tabView = new DetectorShapeTabView();
        this.calibration = calib;
        tabView.initWith(calib); 
        splitPane = new JSplitPane();
        splitPane.setSize(1200, 600);
        splitPane.setPreferredSize(new Dimension(1200,600));
        splitPane.setDividerLocation(550);
        splitPane.setLeftComponent(this.tabView);
        splitPane.setRightComponent(this.canvas);
        
        this.add(splitPane);
        this.pack();
        this.setVisible(true);
    }
    
    public void processEvent(EvioDataEvent event){
        try{
            this.calibration.processEvent(event);
        } catch (Exception e){
            
        }
    }
}
