/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.detector;

/**
 *
 * @author gavalian
 */
public class FADCMaxFinder implements IFADCFitter {

    public void fit(DetectorChannel tube) {
        short[] pulse = tube.getPulse();
        int     max   = 0;
        for(short amp : pulse){
            if(amp>max) max= amp;
        }
        tube.getADC().clear();
        tube.getADC().add((int) max);
    }
    
}
