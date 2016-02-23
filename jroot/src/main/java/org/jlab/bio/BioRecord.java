/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.bio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class BioRecord {
    
    int  headerL = 0;
    int  headerH = 0;
    boolean  isEditable = true;
    List<Integer>  index  = new ArrayList<Integer>();
    List<byte[]>   events = new ArrayList<byte[]>();
    
    public BioRecord(){
        reset();
    }
    
    
    public BioRecord(byte[] array){
        ByteBuffer  buffer = ByteBuffer.wrap(array);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.headerL = buffer.getInt(0);
        this.headerH = buffer.getInt(4);
        System.out.println(BioByteUtils.getByteString(headerL));
        int bufferLength = BioByteUtils.read(headerH,
                BioHeaderConstants.LOWBYTE_RECORD_SIZE,
                BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
        int bufferCount  = BioByteUtils.read(headerL, 
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT);
        //System.out.println(" ARRAY LENGTH = " + array.length);
        //System.out.println(" LENGTH = " + bufferLength + " COUNT = " + bufferCount);
        int indexOffset = 8;
        this.index.clear();
        this.events.clear();
        for(int i = 0; i < bufferCount; i++){
            int nextIndex = buffer.getInt(indexOffset);
            this.index.add(nextIndex);
            indexOffset +=4;
        }
        
        for(int i = 0 ; i < bufferCount; i++){
            int end = 0;
            if(i!=(bufferCount-1)){
                end = this.index.get(i+1);
            } else {
                end = bufferLength;
            }
            
            int size = end - this.index.get(i);
            System.out.println( i + " size = " + size);
            byte[] event = new byte[size];
            buffer.position(indexOffset);
            buffer.get(event);
            this.events.add(event);
            indexOffset += size;
        }
    }
    
    public final void reset(){
        this.index.clear();
        this.events.clear();
        byte[]  recs = new byte[]{'R','C','_','G'};
        this.headerL = 0;
        headerL = headerL|(recs[0]);
        headerL = headerL|(recs[1]<<8);
        //headerL = headerL|(recs[2]<<16);
        //headerL = headerL|(recs[3]<<24);
        this.headerH = 0;
    }
    /**
     * add an byte array into the record.
     * @param array 
     */
    public void addEvent(byte[] array){
        int length = array.length;
        //System.out.println(BioByteUtils.getByteString(headerH));
        
        int previousLength = BioByteUtils.read(headerH, 
                BioHeaderConstants.LOWBYTE_RECORD_SIZE ,
                BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
        
        int previousCount  = BioByteUtils.read(headerL, 
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT);
        
        
        //System.out.println("PREVIOUS LENGTH =  " + previousLength);
        events.add(array);
        index.add(previousLength);
        previousCount++;
        
        headerH = BioByteUtils.write(headerH, previousLength+length,
                BioHeaderConstants.LOWBYTE_RECORD_SIZE ,
                BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
        
        headerL = BioByteUtils.write(headerL, previousCount,
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT);
        
        //System.out.println(BioByteUtils.getByteString(headerH));
    }
    /**
     * return ByteBuffer representation of the Record
     * @return 
     */
    public ByteBuffer getByteBuffer(){
        
        byte[]  dataBytes = this.getDataBytes();
        int  size = 2*4 + this.index.size()*4 + dataBytes.length;
        
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        //System.out.println("WR L = " + BioByteUtils.getByteString(headerL));
        //System.out.println("WR H = " + BioByteUtils.getByteString(headerH));
        buffer.putInt(0, this.headerL);
        buffer.putInt(4, this.headerH);
        
        int initPos = 8;
        for(int i = 0; i < this.index.size(); i++){
            buffer.putInt(initPos, this.index.get(i));
            initPos += 4;
        }
        buffer.position(initPos);
        buffer.put(dataBytes);
        //System.out.println(" POSITION = " + initPos);
        return buffer;
    }
    /**
     * returns the total size of the all events combined
     * @return 
     */
    public int    getDataBytesSize(){
        int size = 0;
        for(byte[] array : this.events) size += array.length;
        return size;
    }
    /**
     * returns one byte[] containing all the events chained together
     * @return 
     */
    public byte[] getDataBytes(){
       int size = this.getDataBytesSize();
       byte[]  dataBytes = new byte[size];
       int position = 0;
       for(int i = 0; i < this.events.size(); i++){
           int len = this.events.get(i).length;
           System.arraycopy(this.events.get(i), 0, dataBytes, position, len);
           position += len;
       }
       return dataBytes;
    }
    /**
     * returns number of events contained in the record
     * @return 
     */
    public int getEventCount(){
        return this.events.size();
    }
    /**
     * returns event byte array for given index
     * @param index
     * @return 
     */
    public byte[] getEvent(int index){
        byte[]  eventArray = new byte[this.events.get(index).length];
        System.arraycopy(this.events.get(index), 0, eventArray, 0, eventArray.length);
        return eventArray;
    }
    /**
     * prints on the screen information about record.
     */
    public void show(){
        
        int count = BioByteUtils.read(headerL, 
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT
                );
        System.out.println(
                "HL = " + BioByteUtils.getByteString(headerL) + 
                        "HH = " + BioByteUtils.getByteString(headerH)
                
        );
        System.out.println(String.format(" H L/H %X %X",headerL,headerH));
        System.out.println("RECORD ELEMENTS = " + count + "  LENGTH = " + BioByteUtils.read(headerH,0,23));
        for(int i = 0 ; i < this.events.size();i++){
            System.out.println(" EVENT " + i + "  LENGTH = " + 
                    this.events.get(i).length + "  OFFSET = " + this.index.get(i));
        }
    }
    
    public static void main(String[] args){
        BioRecord  record = new BioRecord();
        //record.show();
        record.addEvent(new byte[]{1,2,3,4,5});
        record.addEvent(new byte[]{11,12,13,14,15,16,17,18,19});
        //record.show();
        /*
        record.addEvent(new byte[]{1,2,3,4,5});
        record.addEvent(new byte[]{11,12,13,14,15,16,17,18,19});
        record.addEvent(new byte[]{21,22,23,24,25});
        record.show();
        ByteBuffer  buffer = record.getByteBuffer();
        System.out.println("LENGTH = " + buffer.capacity());
        
        BioRecord  rec = new BioRecord(buffer.array());
        rec.show();*/
    }
}
