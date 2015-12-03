/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.detector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class DetectorChannel {
    
    private double         gain       = 1.0;
    private double         conversion = 1.0;
    
    private short[]        rawPULSE      = null;
    private List<Integer>  photoTubeADC  = new ArrayList<Integer>();
    private List<Integer>  photoTubeTDC  = new ArrayList<Integer>();

    public DetectorChannel(){
        
    }
    
    public void setPulse(short[] data){
        rawPULSE = data;
    }
    
    public short[] getPulse(){
        return this.rawPULSE;
    }
    
    public List<Integer> getADC(){
        return this.photoTubeADC;
    }
    
    public List<Integer> getTDC(){
        return this.photoTubeTDC;
    }
    
    public DetectorChannel setGain(double g){
        this.gain = g;
        return this;
    }
    
    public DetectorChannel setConversion(double c){
        this.conversion = c;
        return this;
    }
    
    public double getEnergy(int index){
        return this.photoTubeADC.get(index)*gain;
    }
    
    public double getTime(int index){
        return this.photoTubeTDC.get(index)*conversion;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("GAIN/TIMECONST : (%6.3f/%6.3f) \n", this.gain,this.conversion));
        str.append("\t\t PULSE : SIZE = ");
        if(this.rawPULSE==null){
            str.append("0");
        } else {
            str.append(rawPULSE.length);
        }
        str.append("\n");
        str.append("\t\t ADC   : ");
        for(Integer adc : this.photoTubeADC) str.append(String.format(" %6d ", adc));
        str.append("\n");
        str.append("\t\t TDC   : ");
        for(Integer tdc : this.photoTubeTDC) str.append(String.format(" %6d ", tdc));
        str.append("\n");
        return str.toString();
    }
}
