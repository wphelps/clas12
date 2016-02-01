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
public class FADCBasicFitter implements IFADCFitter {
    
    int pedistalStart = 30;
    int pedistalEnd   = 35;
    int signalStart   = 70;
    int signalEnd     = 75;
    
    public FADCBasicFitter(){
        
    }

    public FADCBasicFitter(int ps, int pe, int ss, int se){
        this.setParams(ps, pe, ss, se);
    }
    
    public final void setParams(int ps, int pe, int ss, int se){
        this.pedistalStart = ps;
        this.pedistalEnd   = pe;
        this.signalStart   = ss;
        this.signalEnd     = se;
    }
    
    public void fit(DetectorChannel tube) {
        short[] pulse = tube.getPulse();
        if(pulse==null){
            System.out.println("---> error fitting the pulse. the array is null");
        } else {
            int pedCount = 0;
            for(int loop = pedistalStart; loop <= pedistalEnd; loop++){
                pedCount += pulse[loop];
            }
            int sigCount = 0;
            for(int loop = signalStart; loop <= signalEnd; loop++){
                sigCount += pulse[loop];
            }
            
            double pedWidth = (double) (pedistalEnd-pedistalStart+1);
            double sigWidth = (double) (signalEnd-signalStart+1);
            double normalized = sigCount - pedCount*(sigWidth/pedWidth);
            
            tube.getADC().clear();
            tube.getADC().add((int) normalized);
        }
    }
}
