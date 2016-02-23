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
    List<BioRecordIndex>  inRecords = new ArrayList<BioRecordIndex>();
    int                   debugMode = 10;
    
    /**
     * parameters to keep statistics information about the performance
     * of the reader
     */
    
    long  timeSpendOnIndexing = (long) 0;
    
    
    public BioReader(){
        
    }
    
    public void open(String name){
        try {
            this.inputStream = new FileInputStream(new File(name));
            this.readRecordIndex(inRecords);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readRecordIndex(List<BioRecordIndex>  index){
        byte[]  recordHeader = new byte[BioHeaderConstants.RECORD_HEADER_SIZE];
        long stime_indexing = System.currentTimeMillis();
        index.clear();
        try {
            
            int nread = 1;
            ByteBuffer ib = null;
            
            while(nread>0){
                nread = this.inputStream.read(recordHeader);                            
                if(nread>0){    
                    BioRecordIndex ri = new BioRecordIndex(
                            this.inputStream.getChannel().position()-
                                    BioHeaderConstants.RECORD_HEADER_SIZE);
                    
                    ib = ByteBuffer.wrap(recordHeader);
                    ib.order(ByteOrder.LITTLE_ENDIAN);
                    int headerH = ib.getInt(8);
                    int headerM = ib.getInt(4);
                    int headerL = ib.getInt(0);
                    
                    int dataLength = BioByteUtils.read(headerH,
                            BioHeaderConstants.LOWBYTE_RECORD_SIZE,
                            BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
                    
                    int indexCount = BioByteUtils.read(headerM, 
                            BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                            BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT
                            );
                    ri.setLength(
                            BioHeaderConstants.RECORD_HEADER_SIZE // header byte size
                                    + indexCount*4 // event index is an integer  
                                    + dataLength);
                    index.add(ri);
                    //System.out.println(" Data Length = " + dataLength + "  INDEX COUNT = " 
                    //        + indexCount + "  INDEXLENGTH = " + (indexCount*4));
                    this.inputStream.skip(dataLength + indexCount*4);
                    //System.out.println("Position = " + this.inputStream.getChannel().position());
                }
            }            
        } catch (IOException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        long etime_indexing = System.currentTimeMillis();
        this.timeSpendOnIndexing = (etime_indexing-stime_indexing);
    }
    
    public void show(){
        for(int i = 0; i < this.inRecords.size();i++){
            System.out.println(String.format(" %5d : %s" ,i, this.inRecords.get(i).toString()));
        }
    }
    
    
    public String getStatusString(){
        StringBuilder str = new StringBuilder();
        double time = ((double) this.timeSpendOnIndexing)/1000.0;
        str.append(String.format("[BIO-READER]  NRECORDS = %12d, ",this.inRecords.size() ));
        str.append(String.format("TIME INDEXING = %7.3f sec",time));
        return str.toString();
    }
    
    public void readRecordTable(){
        
        byte[]  recordHeader = new byte[BioHeaderConstants.RECORD_HEADER_SIZE];
        
        //this.recordList.clear();
        
        this.inRecords.clear();
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
        /*
        for(Long offset : this.recordList){
            System.out.println(offset );
        }*/
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
        //reader.show();
        System.out.println(reader.getStatusString());
        //reader.readRecordTable();
        /*
        int nrecords = reader.getRecordCount();
        System.out.println(" RECORDS = " + nrecords);
        for(int loop = 0; loop < nrecords;loop++){
            BioRecord record = reader.readRecord(loop);
        }*/
        //record.show();
    }
}
