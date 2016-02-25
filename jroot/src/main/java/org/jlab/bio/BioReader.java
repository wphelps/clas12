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
    
    List<BioRecordIndex>  inRecords        = new ArrayList<BioRecordIndex>();
    List<BioRecordIndex>  corruptedRecords = new ArrayList<BioRecordIndex>();
    
    int                   debugMode = 10;
    
    
    /**
     * parameters to keep statistics information about the performance
     * of the reader
     */
    
    long  timeSpendOnIndexing = (long) 0;
    long  timeSpendOnReading  = (long) 0;
    
    
    public BioReader(){
        
    }
    /**
     * Open file for reading. automatically reads file index in of records.
     * the event index is not actually read.
     * @param name file names to read
     */
    public void open(String name){
        if(this.debugMode>0) System.out.println("[bio-reader] ---> openning file : " + name);
        try {
            this.inputStream = new FileInputStream(new File(name));
            this.readRecordIndex(inRecords);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(this.debugMode>0) System.out.println("[bio-reader] ---> recovered records : " + this.inRecords.size());
    }
    /**
     * Reads the index of the records from the file. if problem occurs, the 
     * then the problematic record is written to the list containing corrupt
     * records. This records will contain a string describing the problem.
     * @param index 
     */
    public void readRecordIndex(List<BioRecordIndex>  index){
        
        byte[]  fileHeader   = new byte[BioHeaderConstants.FILE_HEADER_SIZE];
        
        int     firstRecordOffset = 0;
        
        try {
            this.inputStream.read(fileHeader);
            
            ByteBuffer  hb =  ByteBuffer.wrap(fileHeader);
            hb.order(ByteOrder.LITTLE_ENDIAN);
            
            int identifier   = hb.getInt(0);
            int sizeWord     = hb.get(8);
            
            int headerLength = BioByteUtils.read(sizeWord,
                    BioHeaderConstants.FILE_HEADER_LENGTH_LB,
                    BioHeaderConstants.FILE_HEADER_LENGTH_HB);
            
            if(identifier==BioHeaderConstants.FILE_ID_STRING){
                firstRecordOffset = BioHeaderConstants.FILE_HEADER_SIZE + headerLength;
            } else {
                System.out.println("[bio-reader] ---> errors. the provided file is not BIO format.");
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Assumin that we passed successfully the event 
        
        byte[]  recordHeader = new byte[BioHeaderConstants.RECORD_HEADER_SIZE];
        //System.out.println("[bio-reader] --->  reading file index ");
        long stime_indexing = System.currentTimeMillis();
        index.clear();
        try {
            this.inputStream.getChannel().position(firstRecordOffset);    
            
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
                                        
                    if(ri.parseHeader(headerL, headerM, headerH)==false) return;
                    index.add(ri);
                    this.inputStream.skip(ri.getLength() + ri.getNumberOfEvents()*4);

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
        double time  = ((double) this.timeSpendOnIndexing)/1000.0;
        double timer = ((double) this.timeSpendOnReading)/1000.0;
        str.append(String.format("[BIO-READER]  NRECORDS = %12d, ",this.inRecords.size() ));
        str.append(String.format(" TIME INDEXING = %7.3f sec",time));
        str.append(String.format(" TIME READING = %7.3f sec",timer));
        return str.toString();
    }
    
    
    public int getRecordCount(){
        return this.inRecords.size();
    }
    
    public BioRecord  readRecord(int record){
        long stime_reading = System.currentTimeMillis();
        try {
            //long record_offset = this.recordList.get(record);
            //long record_length = this.recordLength.get(record);
            
            long record_offset = this.inRecords.get(record).getPosition();
            long record_length = this.inRecords.get(record).getLength();
            long record_nindex = this.inRecords.get(record).getNumberOfEvents();
            long record_size   = BioHeaderConstants.RECORD_HEADER_SIZE + 
                    record_length + 4*record_nindex;
            
            this.inputStream.getChannel().position(record_offset);
            
            byte[]  buffer = new byte[(int) record_size];
            this.inputStream.read(buffer);
            long etime_reading = System.currentTimeMillis();
            this.timeSpendOnReading += (etime_reading-stime_reading);
            return new BioRecord(buffer);

        } catch (IOException ex) {
            Logger.getLogger(BioReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void main(String[] args){
        BioReader reader = new BioReader();
        reader.open("testfile.bio");
        reader.show();
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
