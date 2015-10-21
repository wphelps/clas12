/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.evio.clas12;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.coda.et.EtAttachment;
import org.jlab.coda.et.EtConstants;
import org.jlab.coda.et.EtEvent;
import org.jlab.coda.et.EtStation;
import org.jlab.coda.et.EtStationConfig;
import org.jlab.coda.et.EtSystem;
import org.jlab.coda.et.EtSystemOpenConfig;
import org.jlab.coda.et.enums.Mode;
import org.jlab.coda.et.enums.Modify;
import org.jlab.coda.et.exception.EtBusyException;
import org.jlab.coda.et.exception.EtClosedException;
import org.jlab.coda.et.exception.EtDeadException;
import org.jlab.coda.et.exception.EtEmptyException;
import org.jlab.coda.et.exception.EtException;
import org.jlab.coda.et.exception.EtExistsException;
import org.jlab.coda.et.exception.EtTimeoutException;
import org.jlab.coda.et.exception.EtTooManyException;
import org.jlab.coda.et.exception.EtWakeUpException;
import org.jlab.coda.jevio.EvioEvent;
import org.jlab.coda.jevio.EvioException;
import org.jlab.coda.jevio.EvioReader;
import org.jlab.data.io.DataEvent;
import org.jlab.data.io.DataEventList;
import org.jlab.data.io.DataSource;

/**
 *
 * @author gavalian
 */
public class EvioETSource implements DataSource {
    
    private Boolean  connectionOK = false;
    private String   etRingHost   = "";
    private Integer  etRingPort   = 11111;
    private EtSystem sys = null;
    private EtAttachment  myAttachment = null;
    private Boolean       remoteConnection = false;
    
    
    public EvioETSource(){
        this.etRingPort = EtConstants.serverPort;
    }
    
    public EvioETSource(String host){
        this.etRingHost = host;
        this.etRingPort = EtConstants.serverPort;
    }
    
    public void setRemote(Boolean flag){
        this.remoteConnection = flag;
    }
    
    public boolean hasEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void open(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void open(String filename) {
        System.out.println("[ETSOURCE] --> connecting to host : [" +
                this.etRingHost + "]  FILE [" + filename + "]  PORT [" + 
                this.etRingPort + "]");
        System.out.println("[ETSOURCE] --> connecting remotely : " + this.remoteConnection);
        try {
            this.connectionOK = true;
            String etFile = filename;
            
            EtSystemOpenConfig config = new EtSystemOpenConfig( etFile,this.etRingHost,this.etRingPort);
            if(this.remoteConnection==true){
                config.setConnectRemotely(true);
            }
            //config.setConnectRemotely(true);
            //System.out.println("-------------->>>>> CONNECTING REMOTELY");
            
            //System.out.println("-------------->>>>> CONNECTING LOCALY");
            
            sys = new EtSystem(config);
            sys.setDebug(EtConstants.debugInfo);
            sys.open();
            
            EtStationConfig statConfig = new EtStationConfig();
            statConfig.setBlockMode(EtConstants.stationBlocking);
            //statConfig.setBlockMode(EtConstants.stationNonBlocking);
            
            statConfig.setUserMode(EtConstants.stationUserSingle);
            statConfig.setRestoreMode(EtConstants.stationRestoreOut);
            //EtStation station = sys.createStation(statConfig, "GRAND_CENTRAL");
            EtStation station = sys.createStation(statConfig, "reader_station");
            
            myAttachment = sys.attach(station);
        } catch (EtException ex) {
            this.connectionOK = false;
            ex.printStackTrace();
        } catch (IOException ex) {
            this.connectionOK = false;
            Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EtTooManyException ex) {
            this.connectionOK = false;
            Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EtDeadException ex) {
            Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EtClosedException ex) {
            Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EtExistsException ex) {
            Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void open(ByteBuffer buff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getSize() {
        if(sys.alive()==true){
            
        }
        return 0;
    }

    public DataEventList getEventList(int start, int stop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DataEventList getEventList(int nrecords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void putEvents(){
        
    }
    
    public DataEvent getNextEvent() {
        if(this.connectionOK == false){
            System.out.println("[EvioETSource] ---->  connection was not estabilished...");
        }
        if(sys.alive()==true){
            try {
                EtEvent[] events = sys.getEvents(myAttachment, Mode.SLEEP, null, 0, 1);                
                
                if(events!=null){
                    if(events.length>0){
                        
                        ByteBuffer buffer = events[0].getDataBuffer();
                        
                        byte[] data = events[0].getData();
                        
                        System.out.println("---> RECEIVED EVENT LENGTH   = " + events[0].getLength());
                        System.out.println("---> DATA BYTE BUFFER LENGTH = " + data.length);
                        //System.out.println(" RECEIVED A BUFFER WITH SIZE " + buffer.capacity());
                        
                        byte[]  array = buffer.array();
                        System.out.println(" RECEIVED A BUFFER WITH SIZE " + buffer.capacity()
                        + "  ARRAY SIZE = " + array.length);
                        
                        //for(int k = 0; k < 10; k++){
                            for(int loop = 0; loop < array.length; loop++){
                                System.out.print(String.format(" %X ", array[loop]));
                                if((loop+1)%20==0) System.out.println();
                                if(loop>200) break;
                            }
                            
                        System.out.println();
                        int length = events[0].getLength();
                        
                      
                        ByteBuffer  evioBuffer = ByteBuffer.allocate(events[0].getLength());
                        evioBuffer.put(data, 8*4*2, length);
                        evioBuffer.order(buffer.order());
                        
                         System.out.println("---> ENDIAN = " + buffer.order() + "  new "
                                 + evioBuffer.order());
                         
                         byte[]  arrayE = evioBuffer.array();
                         System.out.println(" RECEIVED A BUFFER WITH SIZE " + buffer.capacity()
                                 + "  ARRAY SIZE = " + array.length);
                         
                         //for(int k = 0; k < 10; k++){
                         for(int loop = 0; loop < arrayE.length; loop++){
                             System.out.print(String.format(" %X ", arrayE[loop]));
                             if((loop+1)%20==0) System.out.println();
                             if(loop>200) break;
                         }
                         
                         System.out.println();
                        
                        EvioReader reader;
                        
                        try {
                            reader = new EvioReader(buffer);
                            
                            EvioEvent  event = reader.parseNextEvent();
                            System.out.println(" READER EVENT # = " + reader.getEventCount());
                            System.out.println(" PARSED EVENT # = " + event + "  BLOCK # " + reader.getBlockCount()
                            + "   BUFFER LIMIT = " + reader.getByteBuffer().limit()
                            + "  VERSION = " + reader.getEvioVersion());
                        } catch (EvioException ex) {
                            Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     
                        /*
                        EvioDataEvent evioEvent = new EvioDataEvent(evioBuffer,
                                EvioFactory.getDictionary());
                        return evioEvent;*/
                        return null;
                    }
                }
            } catch (EtException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EtDeadException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EtClosedException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EtEmptyException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EtBusyException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EtTimeoutException ex) {
                //Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EtWakeUpException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public DataEvent getPreviousEvent() {
        return null;
    }

    public DataEvent gotoEvent(int index) {
        return null;
    }

    public void reset() {
        
    }

    public int getCurrentIndex() {
        return 0;
    }
    
    public static void main(String[] args){
        String ethost = "129.57.76.215";
        String file   = "/tmp/myEtRing";
        Boolean isRemote = false;
        
        if(args.length>1){
            ethost = args[0];
            file   = args[1];
        } else {
            System.out.println("\n\n\nUsage : et-connect host etfile\n");
            System.exit(0);            
        }
        
        if(args.length>2){
            String flag = args[2];
            if(flag.compareTo("true")==0){
                isRemote = true;
            }
        }
        
        EvioETSource reader = new EvioETSource(ethost);
        reader.setRemote(isRemote);
        reader.open(file);
        //for(int loop = 0 ; loop < 10 ; loop++){
        int counter = 0;
        while(true){  
            counter++;
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                Logger.getLogger(EvioETSource.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Reading next event # " + counter);
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            if(event!=null) {
                System.out.println("------> received an event");
                event.show();
            }
        }
        //}
    }
}
          