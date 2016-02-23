/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gavalian
 */
public class BioReader {
    FileInputStream  inputStream = null;
    List<Long>    recordList   = new ArrayList<Long>();
    List<Long>    recordLength = new ArrayList<Long>(); 
    
    public BioReader(){
        
    }
    
    public void open(String name){
        try {
            this.inputStream = new FileInputStream(new File(name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readRecordTable(){
        
        byte[]  recordHeader = new byte[8];
        this.recordList.clear();
        try {
            
            int nread = 1;
            ByteBuffer ib = null;
            
            while(nread>0){
                //System.out.println("Position = " + this.inputStream.getChannel().position());
                nread = this.inputStream.read(recordHeader);
                //System.out.println("Position = " + this.inputStream.getChannel().position());
                if(nread>0){
                    //System.out.println("NREAD = " + nread);
                    this.recordList.add(this.inputStream.getChannel().position()-8);
                    ib = ByteBuffer.wrap(recordHeader);

                    ib.order(ByteOrder.LITTLE_ENDIAN);
                    int headerH = ib.getInt(4);
                    int headerL = ib.getInt(0);
                    int dataLength = BioByteUtils.read(headerH,
                            BioHeaderConstants.LOWBYTE_RECORD_SIZE,
                            BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
                    
                    int indexCount = BioByteUtils.read(headerL, 
                            BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                            BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT
                            );
                    this.recordLength.add((long) 8 + indexCount*4 + dataLength);
                    //System.out.println(" Data Length = " + dataLength + "  INDEX COUNT = " 
                    //        + indexCount + "  INDEXLENGTH = " + (indexCount*4));
                    this.inputStream.skip(dataLength + indexCount*4);
                    //System.out.println("Position = " + this.inputStream.getChannel().position());
                }
            }            
        } catch (IOException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Long offset : this.recordList){
            System.out.println(offset );
        }
    }
    
    public int getRecordCount(){
        return this.recordList.size();
    }
    
    public BioRecord  readRecord(int record){
        try {
            long record_offset = this.recordList.get(record);
            long record_length = this.recordLength.get(record);
            this.inputStream.getChannel().position(record_offset);
            byte[]  buffer = new byte[(int) record_length];
            this.inputStream.read(buffer);
            return new BioRecord(buffer);
        } catch (IOException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void main(String[] args){
        BioReader reader = new BioReader();
        reader.open("testfile.bio");
        reader.readRecordTable();
        
        int nrecords = reader.getRecordCount();
        System.out.println(" RECORDS = " + nrecords);
        for(int loop = 0; loop < nrecords;loop++){
            BioRecord record = reader.readRecord(loop);
        }
        //record.show();
    }
}
