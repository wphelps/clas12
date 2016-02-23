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
    int  headerM = 0;
    int  headerH = 0;
    
    boolean  isEditable = true;
    
    List<Integer>  index  = new ArrayList<Integer>();
    List<byte[]>   events = new ArrayList<byte[]>();
    
    /**
     * creates and empty record ready for adding events and removing events.
     */
    public BioRecord(){
        reset();
    }
    
    /**
     * Initializes a record from it's binary form and creates arrays as events
     * that can be accessed through the interface. also contains options flags
     * such as version, type of data stored an compression flag.
     * @param array 
     */
    
    public BioRecord(byte[] array){
        this.initFromBinary(array);
        /*
        ByteBuffer  buffer = ByteBuffer.wrap(array);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        this.headerL = buffer.getInt(0);
        this.headerM = buffer.getInt(4);
        this.headerH = buffer.getInt(8);
        //System.out.println(BioByteUtils.getByteString(headerL));
        int bufferLength = BioByteUtils.read(headerH,
                BioHeaderConstants.LOWBYTE_RECORD_SIZE,
                BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
        int bufferCount  = BioByteUtils.read(headerM, 
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT);
        //System.out.println(" ARRAY LENGTH = " + array.length);
        //System.out.println(" LENGTH = " + bufferLength + " COUNT = " + bufferCount);
        int indexOffset = BioHeaderConstants.RECORD_HEADER_SIZE;
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
            //System.out.println( i + " size = " + size);
            byte[] event = new byte[size];
            buffer.position(indexOffset);
            buffer.get(event);
            this.events.add(event);
            indexOffset += size;
        }*/
    }
    /**
     * initializes an empty record. writes the identifying string "RC_G" to
     * the first byte, this makes it easy to search in binary buffer for record
     * start bytes in case there is a corruption in the stream.
     */
    public final void reset(){
        this.index.clear();
        this.events.clear();
        byte[]  recs = new byte[]{'R','C','_','G'};
        this.headerL = 0;
        headerL = headerL|(recs[0]);
        headerL = headerL|(recs[1]<<8);
        headerL = headerL|(recs[2]<<16);
        headerL = headerL|(recs[3]<<24);
        this.headerM = 0;
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
        
        int previousCount  = BioByteUtils.read(headerM, 
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT);
        
        
        //System.out.println("PREVIOUS LENGTH =  " + previousLength);
        events.add(array);
        index.add(previousLength);
        previousCount++;
        
        headerH = BioByteUtils.write(headerH, previousLength+length,
                BioHeaderConstants.LOWBYTE_RECORD_SIZE ,
                BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
        
        headerM = BioByteUtils.write(headerM, previousCount,
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT);
        
        //System.out.println(BioByteUtils.getByteString(headerH));
    }
    
    public ByteBuffer  getByteBuffer(boolean compressed){
        if(compressed == false) return this.getByteBuffer();
        
        byte[]  dataBytes = this.getDataBytes();
        byte[]  dataBytesCompressed = BioByteUtils.gzip(dataBytes);
        
        int  size = BioHeaderConstants.RECORD_HEADER_SIZE + 
                this.index.size()*4 + 
                dataBytesCompressed.length;
        
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        int ihH = BioByteUtils.write(0, dataBytesCompressed.length, 
                BioHeaderConstants.LOWBYTE_RECORD_SIZE, 
                BioHeaderConstants.HIGHBYTE_RECORD_SIZE
                );
        
        int ihM = BioByteUtils.write(0, this.index.size(),
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT, 
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT
                );
        
        ihM = BioByteUtils.write(ihM, 1, 24, 24);
        
        buffer.putInt(0, this.headerL);
        buffer.putInt(4, ihM);
        buffer.putInt(8, ihH);

        int initPos = BioHeaderConstants.RECORD_HEADER_SIZE;
        
        for(int i = 0; i < this.index.size(); i++){
            buffer.putInt(initPos, this.index.get(i));
            initPos += 4;
        }
        
        buffer.position(initPos);
        buffer.put(dataBytesCompressed);
        //System.out.println(" POSITION = " + initPos);

        return buffer;
    }
    
    private void initFromBinary(byte[] binary){
        
                        
        ByteBuffer  buffer = ByteBuffer.wrap(binary);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        this.headerL = buffer.getInt(0);
        this.headerM = buffer.getInt(4);
        this.headerH = buffer.getInt(8);
        
        int isCompressed = BioByteUtils.read(headerM, 
                BioHeaderConstants.LOWBYTE_RECORD_COMPRESSION,
                BioHeaderConstants.HIGHBYTE_RECORD_COMPRESSION);
        int indexCount = BioByteUtils.read(headerM, 
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT);
        int dataLength = BioByteUtils.read(headerH,
                BioHeaderConstants.LOWBYTE_RECORD_SIZE,
                BioHeaderConstants.HIGHBYTE_RECORD_SIZE);
        //int indexOffset = BioHeaderConstants.RECORD_HEADER_SIZE;
        
        int  position = BioHeaderConstants.RECORD_HEADER_SIZE;
        
        this.index.clear();
        this.events.clear();
        
        for(int i = 0; i < indexCount; i++){
            int nextIndex = buffer.getInt(position);
            this.index.add(nextIndex);
            position +=4;
        }
        
        position =  BioHeaderConstants.RECORD_HEADER_SIZE;
        
        
        byte[] eventdata = new byte[dataLength];
        System.arraycopy( binary, position + indexCount * 4, eventdata, 0, dataLength);
        /**
         * Check if the buffer was compressed. then uncompress the data array.
         * and retrieve byte arrays from indecies. 
         */
        if(isCompressed==1){
            byte[]  gunzipped = BioByteUtils.ungzip(eventdata);
            if(gunzipped.length==0){
                System.out.println("[BioRecord] ---> error : something went wrong with unzip.");
                this.reset();
                return;
            }
            eventdata = gunzipped;
        }
        
        int totalDataLength = eventdata.length;
        int datapos         = 0;
        
        for(int i = 0 ; i < indexCount; i++){
            int end = 0;
            
            if(i!=(indexCount-1)){
                end = this.index.get(i+1);
            } else {
                end = totalDataLength;
            }
            
            int size = end - this.index.get(i);
            //System.out.println( i + " size = " + size);
            byte[] event = new byte[size];
            System.arraycopy(eventdata, datapos, event, 0, event.length);
            this.events.add(event);
            datapos += size;
        }
    }
    /**
     * return ByteBuffer representation of the Record
     * @return 
     */
    public ByteBuffer getByteBuffer(){
        
        byte[]  dataBytes = this.getDataBytes();
        
        int  size = BioHeaderConstants.RECORD_HEADER_SIZE + this.index.size()*4 + dataBytes.length;
        
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        //System.out.println("WR L = " + BioByteUtils.getByteString(headerL));
        //System.out.println("WR H = " + BioByteUtils.getByteString(headerH));
        buffer.putInt(0, this.headerL);
        buffer.putInt(4, this.headerM);
        buffer.putInt(8, this.headerH);
        
        int initPos = BioHeaderConstants.RECORD_HEADER_SIZE;
        
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
        
        int count = BioByteUtils.read(headerM, 
                BioHeaderConstants.LOWBYTE_RECORD_EVENTCOUNT,
                BioHeaderConstants.HIGHBYTE_RECORD_EVENTCOUNT
                );
        System.out.println(
                "HL = " + BioByteUtils.getByteString(headerL) + "\n"
                        + "HM = " + BioByteUtils.getByteString(headerM) + "\n"
                        + "HH = " + BioByteUtils.getByteString(headerH)
        );

        System.out.println(String.format(" H L/H %X %X",headerL,headerH));
        System.out.println("RECORD ELEMENTS = " + count + "  LENGTH = " + BioByteUtils.read(headerH,0,23));
        for(int i = 0 ; i < this.events.size();i++){
            System.out.println(" EVENT " + i + "  LENGTH = " + 
                    this.events.get(i).length + "  OFFSET = " + this.index.get(i));
        }
    }
    
    /**
     * set compression flag for the record
     * @param flag 
     */
    public void     compressed(boolean flag){
        if(flag==true){
            headerM = BioByteUtils.write(headerM, 1, 
                    BioHeaderConstants.LOWBYTE_RECORD_COMPRESSION,
                    BioHeaderConstants.HIGHBYTE_RECORD_COMPRESSION);
        } else {
            headerM = BioByteUtils.write(headerM, 0, 
                    BioHeaderConstants.LOWBYTE_RECORD_COMPRESSION,
                    BioHeaderConstants.HIGHBYTE_RECORD_COMPRESSION);
        }
    }
    /**
     * returns the value of the compression flag bit
     * @return 0 if compression flag is not set, 1 - if set
     */
    public boolean  compressed(){
        int ic = BioByteUtils.read(headerM,
                BioHeaderConstants.LOWBYTE_RECORD_COMPRESSION,
                    BioHeaderConstants.HIGHBYTE_RECORD_COMPRESSION);
        return (ic==1);
    }
    
    public static void main(String[] args){
        
        BioRecord  record = new BioRecord();
        //record.show();
        record.addEvent(BioByteUtils.generateByteArray(2450));
        record.addEvent(BioByteUtils.generateByteArray(5450));

        //record.compressed(true);
        byte[]  record_bytes_u = record.getByteBuffer().array();
        byte[]  record_bytes_c = record.getByteBuffer(true).array();
        
        record.show();
        
        System.out.println(" SIZE = " + record_bytes_u.length + "  " +
                record_bytes_c.length);
        

        
        System.out.println("----------->   Record uncompressed");
        BioRecord  rru = new BioRecord(record_bytes_u);
        rru.show();
        System.out.println("----------->   Record  compressed");
        BioRecord  rrc = new BioRecord(record_bytes_c);
        rrc.show();
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
