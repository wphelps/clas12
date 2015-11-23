/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

/**
 *
 * @author gavalian
 */
public class DetectorBankEntry implements IDetectorUnit {

    DetectorDescriptor  dataDesc   = new DetectorDescriptor();
    BankType            dataType   =  BankType.UNDEFINED;
    Object              dataObject = null;
    
    
    public DetectorBankEntry(int crate, int slot, int channel){
        this.dataDesc.setCrateSlotChannel(crate, slot, channel);
    }
    
    public DetectorDescriptor getDescriptor() {
        return this.dataDesc;
    }
    
    
    public void setData(BankType type,  int[] values){
        this.dataObject = values;
        this.dataType   = type;
    }
    
    public BankType getType(){
        return this.dataType;
    }
    
    public Object  getDataObject(){
        return this.dataObject;
    }
        
    public void setData(short[] values){        
        this.dataType   = BankType.ADCPULSE;
        this.dataObject = values;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(this.dataDesc.toString());
        str.append(String.format(" -->>>  TYPE = %8s", this.dataType.getName()));
        if(this.dataType==BankType.TDC){
            str.append(String.format("  VALUE = %8d", ((int[]) this.dataObject)[0] ));
        }
        
        if(this.dataType==BankType.ADCPULSE){
            str.append(String.format("   SIZE = %8d", ((short[]) this.dataObject).length ));
        }
        return str.toString();
    }
}
