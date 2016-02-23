/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.bio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gavalian
 */
public class BioWriter {
    
    FileOutputStream  outStream = null;
    BioRecord         outputRecord = null;
    int               MAX_RECORD_SIZE  = 8*1024*1024;
    int               MAX_RECORD_COUNT = 5000;
    
    public BioWriter(){
        this.outputRecord = new BioRecord(); 
    }
    
    public void open(String name){
        try {
            outStream = new FileOutputStream(new File(name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BioWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void write(){
        //this.outputRecord.show();
        if(this.outputRecord.getEventCount()!=0){
            try {
                byte[] array = this.outputRecord.getByteBuffer().array();
                System.out.println("purging record with size = " + array.length );
                this.outStream.write(array);
            } catch (IOException ex) {
                Logger.getLogger(BioWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void writeEvent(byte[] event){
        this.outputRecord.addEvent(event);
        int size = this.outputRecord.getDataBytesSize();
        if(size>this.MAX_RECORD_SIZE){
            this.write();
            this.outputRecord.reset();
        }
    }
    
    public void close(){
        if(this.outputRecord.getEventCount()>0){
            this.write();
        }
        try {
            this.outStream.close();
        } catch (IOException ex) {
            Logger.getLogger(BioWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        BioWriter writer = new BioWriter();
        writer.open("testfile.bio");
        
        for(int i = 0; i < 20000; i++){
            byte[] buffer = BioByteUtils.generateByteArray(32000);
            writer.writeEvent(buffer);
        }
        writer.close();
    }
}
