/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.detector;

import org.jlab.clas.detector.DetectorBankEntry;
import org.jlab.clas.detector.DetectorType;
import org.jlab.io.decode.SVTDataRecord;
import org.jlab.io.decode.TranslationTable;
import org.jlab.io.decode.TranslationTableEntry;

/**
 *
 * @author gavalian
 */
public class SVTTranslationTable extends TranslationTable {
    SVTDataRecord  record = new SVTDataRecord();
    
    public SVTTranslationTable(){
        this.init();
    }
    
    public final void init(){
        String  envCLAS     = System.getenv("CLAS12DIR");
        String  envCLASprop = System.getProperty("CLAS12DIR");
        //String  envCLAS = "/Users/gavalian/Work/Software/Release-7.0/COATJAVA/coatjava/";
        
        if(envCLAS!=null){
            String  tfile   = String.format("%s/etc/bankdefs/translation/SVT.table", envCLAS);                   
            this.readFile(tfile);
        } else {
            if(envCLASprop!=null){
                String  tfile   = String.format("%s/etc/bankdefs/translation/SVT.table", envCLASprop);                   
            this.readFile(tfile);
            }
        }
    }
    
    public void translate(DetectorBankEntry entry){

        TranslationTableEntry conversion = this.getEntry(entry.getDescriptor().getCrate(),
                    entry.getDescriptor().getSlot(),entry.getDescriptor().getChannel());
        //System.out.println(" LOOKING FOR ----------->  " + entry.getDescriptor());
        if(conversion!=null){
            //System.out.println(" FOUND ----------->  " + entry.getDescriptor());
            int[] svt = (int[]) entry.getDataObject();
            int firstWord   = svt[0];
            int chipid = (firstWord & 0x00000007);
            int half   = (firstWord & 0x00000008)>>3;
            int channel = svt[1];
            record.init(conversion.descriptor().getSector(), conversion.descriptor().getLayer(), 
                    half, chipid, channel);
            entry.getDescriptor().setSectorLayerComponent(record.SECTOR,record.LAYER,record.STRIP);
            entry.getDescriptor().setType(DetectorType.SVT);
        }
    }
}
