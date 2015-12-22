/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.detector;

import org.jlab.clas.detector.DetectorCollection;
import org.jlab.clas.detector.DetectorDescriptor;
import org.jlab.clasrec.utils.DatabaseConstantProvider;

/**
 *
 * @author gavalian
 */
public class FADCConfigLoader {
    DetectorCollection<FADCConfig>  configStore = new DetectorCollection<FADCConfig>();
    
    public FADCConfigLoader(){
        
    }
    
    public final void load(String dbTable, int run, String variation){
        
        DatabaseConstantProvider  db = new DatabaseConstantProvider(run,variation);        
        db.loadTable(dbTable);
        db.disconnect();
        
        int nrows = db.length(dbTable+"/crate");
        System.out.println("LEN = " + nrows);
        for(int row = 0; row < nrows; row++){
            int crate = db.getInteger(dbTable + "/crate", row);
            int slot  = db.getInteger(dbTable + "/slot", row);
            int chan  = db.getInteger(dbTable + "/chan", row);
            int nsa   = db.getInteger(dbTable + "/nsa", row);
            int nsb   = db.getInteger(dbTable + "/nsb", row);
            int tet   = db.getInteger(dbTable + "/tet", row);
            double ped   = db.getDouble(dbTable + "/pedestal", row);
            configStore.add(crate, slot, chan, new FADCConfig(ped,nsa,nsb,tet));
        }
        
        configStore.show();
    }
    
    public DetectorCollection<FADCConfig> getMap(){return this.configStore;}
    
    public static void main(String[] args){
        FADCConfigLoader  loader = new FADCConfigLoader();
        loader.load("/test/fc/fadc", 10, "default");
    }
}
