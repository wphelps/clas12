/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class DetectorShapeCanvas2D extends JPanel implements MouseListener , MouseMotionListener{

    DetectorShapeView2D  detectorShapeView = null;
    
    public DetectorShapeCanvas2D(DetectorShapeView2D view){
        this.detectorShapeView = view;
    }
    
    
    public String getName(){
        return this.detectorShapeView.getName();
    }
    
    public void mouseClicked(MouseEvent e) {
        // Do Nothing
    }

    public void mousePressed(MouseEvent e) {
          // Do Nothing
    }

    public void mouseReleased(MouseEvent e) {
        // Do Nothing
    }

    public void mouseEntered(MouseEvent e) {
        // Do nothing
    }

    public void mouseExited(MouseEvent e) {
        
    }

    public void mouseDragged(MouseEvent e) {
        
    }

    public void mouseMoved(MouseEvent e) {
        
    }
    
}
