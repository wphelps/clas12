/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class ConstantsTableEntry  {
    private DetectorDescriptor  descriptor = new DetectorDescriptor();
    private double[]            entryData  = null;
    public ConstantsTableEntry(int sector, int layer, int component, int size){
        this.descriptor.setSectorLayerComponent(sector, layer, component);
        this.entryData = new double[size];
    }
    
    public void setData(int index, double value){
        this.entryData[index] = value;
    }
    public DetectorDescriptor  getDescriptor(){ return this.descriptor;}
    public int getSize(){ return this.entryData.length;}
    public double getData(int index) { return this.entryData[index];} 
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(this.descriptor.toString());
        for(int loop = 0; loop < this.entryData.length;loop++){
            str.append(String.format(" %8.5f ", this.entryData[loop]));
        }
        str.append("\n");
        return str.toString();
    }
}
