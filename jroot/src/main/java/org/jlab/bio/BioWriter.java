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
    
    /**
     * output stream used for writing binary data to the file.
     */
    FileOutputStream  outStream    = null;
    /**
     * Internal record which is purged to the file based on the
     * maximum record size and/or maximum events desired in the 
     * record.
     */
    BioRecord         outputRecord = null;
    /**
     * maximum number of bytes allowed in the record. if the newly added
     * event exceeds the maximum size, the record will be written to the file. 
     */
    int               MAX_RECORD_SIZE  = 8*1024*1024;
    /**
     * Maximum number of the events to fit into one record. Once this limit
     * is reached the record will be flushed to the file stream. NOTE: the 
     * writer flushes the record to the file when either condition is met
     * record_size>MAX_RECORD_SIZE or n_records>MAX_RECORD_COUNT
     */
    int               MAX_RECORD_COUNT = 5000;
    
    /**
     * Create a writer object with initialized empty record, but no association
     * with a file. BioWriter.open(filename) - has to be called to be able to
     * write events to the file.
     */
    
    /**
     * parameters to keep track of the data that passes through the writing process.
     */
    long     totalByteWritten       = (long) 0;
    long     totalBytesInRecords    = (long) 0;
    long     numberOfRecords        = (long) 0;
    long     timeSpendOnWriting     = (long) 0;
    long     timeSpendOnCompression = (long) 0;
    
    boolean  streamCompression = false;
    
    public BioWriter(){
        this.outputRecord = new BioRecord(); 
    }
    /**
     * creates new Writer with an empty record store and associates with 
     * an external file with given name.
     * @param file 
     */
    public BioWriter(String file){
        this.outputRecord = new BioRecord();
        this.open(file);
    }
    /**
     * open a file and empty the record store.
     * @param name file name to write data in
     */
    public final void open(String name){
        try {
            outStream = new FileOutputStream(new File(name));
            this.outputRecord.reset();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BioWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Writes the content of the record into the file and resets the record buffer
     * so new data can be added.
     */
    public void write(){
        //this.outputRecord.show();
        if(this.outputRecord.getEventCount()!=0){
            try {
                long stime_compress = System.currentTimeMillis();
                byte[] array = this.outputRecord.getByteBuffer(streamCompression).array();
                long etime_compress = System.currentTimeMillis();
                this.timeSpendOnCompression += (etime_compress-stime_compress);
                //System.out.println("purging record with size = " + array.length );
                long stime_write = System.currentTimeMillis();
                this.outStream.write(array);
                long etime_write = System.currentTimeMillis();
                this.timeSpendOnWriting += (etime_write-stime_write);
                this.numberOfRecords++;
                this.totalByteWritten += array.length;
                this.outputRecord.reset();
            } catch (IOException ex) {
                Logger.getLogger(BioWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * adds an event to the records, not actually written to the disk at this
     * point. if the record size or record length exceed the default maximum
     * values the record will be written to the disk and the record container
     * will be reset.
     * @param event byte array to add to the events record.
     */
    public void writeEvent(byte[] event){
        this.outputRecord.addEvent(event);
        int size = this.outputRecord.getDataBytesSize();
        if(size>this.MAX_RECORD_SIZE){
            this.write();
            this.outputRecord.reset();
        }
    }
    /**
     * destructor substitute. it has to be called at the end of program
     * to make sure the incomplete record is flushed to the file, and file stream
     * is properly closed.
     */
    public void close(){
        if(this.outputRecord.getEventCount()>0){
            this.write();
        }
        try {
            this.outStream.close();
        } catch (IOException ex) {
            Logger.getLogger(BioWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(this.getStatusString());
    }
    /**
     * returns a status string containing statistics for all interactions with
     * the writer. number of records written, number of bytes written, and the
     * performance statistics.
     * @return 
     */
    public String getStatusString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("NRECORDS = %12d, ", this.numberOfRecords));
        double mb = ( (double) this.totalByteWritten)/1024/1024;
        str.append(String.format("BYTES = %8.2f Mb, ",mb));
        double wtime = (this.timeSpendOnWriting)/1000.0;
        double ctime = (this.timeSpendOnCompression)/1000.0;
        str.append(String.format("WTIME = %7.3f sec, ",wtime));
        str.append(String.format("CTIME = %7.3f sec, ",ctime));
        return str.toString();
    }
    /**
     * set compression flag for the writer, if set TRUE all records will be compressed
     * before being written into the file.
     * @param flag 
     */
    public void setCompression(boolean flag){
        this.streamCompression = flag;
    }
    /**
     * Main program to run internal tests and validations.
     * @param args 
     */
    public static void main(String[] args){
        BioWriter writer = new BioWriter();
        //writer.setCompression(true);
        writer.open("testfile.bio");
        
        for(int i = 0; i < 5*8000; i++){
            byte[] buffer = BioByteUtils.generateByteArray(45000);
            writer.writeEvent(buffer);
        }
        
        writer.close();
    }
}
