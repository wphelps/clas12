/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import org.jlab.clas.detector.DetectorDescriptor;

/**
 *
 * @author gavalian
 */
public interface IDetectorListener {
    
    void detectorSelected(DetectorDescriptor desc);
    void update(DetectorShape2D shape);
    
}
