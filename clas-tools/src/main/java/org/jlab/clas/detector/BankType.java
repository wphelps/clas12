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
public enum BankType {
    
    UNDEFINED ( 0, "UNDEFINED"),
    TDC       ( 1, "TDC"),    
    ADC       ( 2, "ADC"),
    ADCPULSE  ( 3, "ADCPULSE");

    
    private final int banktype;
    private final String banktypename;
    
    BankType(){
        banktype = 0;
        banktypename = "UNDEFINED";
    }
    
    BankType(int id, String name){
        banktype = id;
        banktypename = name;
    }
    /**
     * Returns the name of the detector.
     * @return the name of the detector
     */
    public String getName() {
        return banktypename;
    }
    
     /**
     * Returns the id number of the detector.
     * @return the id number of the detector
     */
    public int getDetectorId() {
        return banktype;
    }
    
    public static BankType getType(String name) {
        name = name.trim();
        for(BankType id: BankType.values())
            if (id.getName().equalsIgnoreCase(name)) 
                return id;
        return UNDEFINED;
    }
}
