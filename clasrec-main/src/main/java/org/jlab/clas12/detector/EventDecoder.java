/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jlab.clas.detector.BankType;
import org.jlab.clas.detector.DetectorBankEntry;
import org.jlab.clas.detector.DetectorRawData;
import org.jlab.clas.detector.DetectorType;
import org.jlab.clas.tools.benchmark.Benchmark;
import org.jlab.clas.tools.benchmark.BenchmarkTimer;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;
import org.jlab.io.decode.AbsDetectorTranslationTable;

/**
 *
 * @author gavalian
 */
public class EventDecoder {
    
    RawEventDecoder rawDecoder = null;
    
    AbsDetectorTranslationTable  table = new AbsDetectorTranslationTable();
    List<DetectorBankEntry>      dataEntries = null;
    Map<DetectorType,List<DetectorCounter> >  counters = new TreeMap<DetectorType,List<DetectorCounter>>();
    Benchmark                                benchMark = new Benchmark();
    Map<DetectorType,IFADCFitter>             pulseFitters = new TreeMap<DetectorType,IFADCFitter>();
    
    public EventDecoder(){
        
        
        rawDecoder = new RawEventDecoder();
        Set<String>     detectors = rawDecoder.getDetectorList();
        benchMark.addTimer("RAW-DECODER");
        benchMark.addTimer("RAW-TR");
        benchMark.addTimer("OBJ-CREATE");
        
        for(String detector : detectors){
            pulseFitters.put(DetectorType.getType(detector), new FADCBasicFitter(0,30,35,70));
        }
        //table.readFile("/Users/gavalian/Work/Software/Release-8.0/COATJAVA/coatjava/etc/bankdefs/translation/FTOF1A.table");
        //this.decoder.addTranslationTable(table);
    }
    
    public List<DetectorBankEntry>   getDataEntries(String type){
        return this.getDataEntries(DetectorType.getType(type));
    }
    
    public List<DetectorBankEntry>   getDataEntries(DetectorType type){
        List<DetectorBankEntry> dataList = new ArrayList<DetectorBankEntry>();
        for(DetectorBankEntry data : this.dataEntries){
            if(data.getDescriptor().getType()==type){
                dataList.add(data);
            }
        }
        return dataList;
    }
    
    public void  decode(EvioDataEvent event){
        
        this.benchMark.resume("RAW-DECODER");
        this.dataEntries = this.rawDecoder.getDataEntries(event);
        this.benchMark.pause("RAW-DECODER");
        this.benchMark.resume("RAW-TR");
        this.rawDecoder.decode(dataEntries);
        this.benchMark.pause("RAW-TR");
        
        
        counters.clear();
        
        Set<String>  detectors = rawDecoder.getDetectorList();
        Map<Integer,DetectorCounter>   counterList = new TreeMap<Integer,DetectorCounter>();
        
        this.benchMark.resume("OBJ-CREATE");
        for(String detector : detectors){
            List<DetectorBankEntry>  entries = this.getDataEntries(detector);
            /*
            DetectorType  type = DetectorType.getType(detector);
            this.counters.put(type,new ArrayList<DetectorCounter>());
            counterList.clear();
            
            for(DetectorBankEntry  entry : entries){
                int hash = entry.getDescriptor().getHashCode();
                if(counterList.containsKey(hash)==false){
                    counterList.put(hash, new DetectorCounter(entry.getDescriptor()));
                }
                
                DetectorCounter  counter = counterList.get(hash);
                int channel = entry.getDescriptor().getOrder()/2;
                if(counter.getChannels().size()<(channel+1)){
                    counter.addChannel(new DetectorChannel());
                }
                
                if(entry.getType()==BankType.TDC){
                    counter.addTDC(channel, ( (int[]) entry.getDataObject())[0]);
                }
                //System.out.println(counter);
                //System.out.println(" ORDER = " + entry.getDescriptor().getOrder() + 
                //        "  CHANNEL = " + channel);
                //System.out.println(" DETECTOR " + entry.getType() 
                //        + "  " + counter);
                //counter.addADC(channel, 10);
            }
            */
        }
        this.benchMark.pause("OBJ-CREATE");        
        
        
        
    }
    
    
    public List<DetectorCounter>   getDetectorCounters(DetectorType t){
        
        List<DetectorCounter>    counters = new ArrayList<DetectorCounter>();
        
        List<DetectorBankEntry>  entries = this.getDataEntries(t);
        Map<Integer,DetectorCounter>   counterList = new TreeMap<Integer,DetectorCounter>();
        for(DetectorBankEntry entry : entries){
            int hash = entry.getDescriptor().getHashCode();
            if(counterList.containsKey(hash)==false){
                counterList.put(hash, new DetectorCounter(entry.getDescriptor()));
            }
            DetectorCounter  counter = counterList.get(hash);
            int channel = entry.getDescriptor().getOrder()/2;
            if(counter.getChannels().size()<(channel+1)){
                counter.addChannel(new DetectorChannel());
            }
            
            if(entry.getType()==BankType.TDC){
                counter.addTDC(channel, ( (int[]) entry.getDataObject())[0]);
            }
        }
        
        for(Map.Entry<Integer,DetectorCounter> item : counterList.entrySet()){
            System.out.println(item.getValue());
        }
        return counters;
    }
    
    public void showTimer(){
        System.out.println(this.benchMark.toString());
    }
    
    public List<DetectorBankEntry>  getDataEntries(){
        return this.dataEntries;
    }
    
    public List<DetectorCounter>  getCounters(EvioDataEvent event){
        /*
        List<DetectorCounter>  counterList = new ArrayList<DetectorCounter>();
        List<DetectorRawData> rawData = decoder.getDataEntries(event);
        decoder.decode(rawData);
        List<DetectorRawData> trData  =  decoder.getDetectorData(rawData, DetectorType.FTOF1A);
        for(DetectorRawData data : trData){
            System.out.println(data);
        }
        
        Map<Integer,DetectorCounter>  counters = new TreeMap<Integer,DetectorCounter>();
        for(DetectorRawData data : trData){
            
            int hash = data.getDescriptor().getHashCode();
            if(counters.containsKey(hash)==false){
                counters.put(hash, new DetectorCounter(DetectorType.FTOF1A,
                        data.getDescriptor().getSector(),data.getDescriptor().getLayer(),
                        data.getDescriptor().getComponent()));
            }
            
            DetectorCounter counter = counters.get(hash);
            
            int tube  = data.getDescriptor().getOrder()%2;
            int value = data.getDescriptor().getOrder()/2;
            
            System.out.println(" ORDER = " + data.getDescriptor().getOrder() 
                    + "  tube = " + tube + " value = " + value);
            
            
            if(value==0){
                System.out.println("adding " + tube + " " + value + " " + counter.getChannels().size());
                counter.addADC(tube,1000);
            }
            /*
           
            //System.out.println(data);
        }
        
        for(Map.Entry<Integer,DetectorCounter>  entry : counters.entrySet()){
            counterList.add(entry.getValue());
        }
        return counterList;
        */
        return null;
    }
    
    
    public static void main(String[] args){
        
        /*
        DetectorCounter counter = new DetectorCounter(DetectorType.EC,1,1,1);
        counter.addADC(2, 150);
        counter.addADC(1, 140);
        counter.addADC(0, 100);
        counter.addTDC(0, 120);
        counter.addADC(0, 130);
        counter.addTDC(1, 140);

        
        System.out.println(counter);
        */
        
        String input = "/Users/gavalian/Work/Software/Release-8.0/COATJAVA//sector2_000211.evio.0";
        //String input = "/Users/gavalian/Work/Software/Release-8.0/COATJAVA/sector1_000139.evio";
        EvioSource  reader = new EvioSource();
        reader.open(input);
        EventDecoder decoder = new EventDecoder();
        int icounter = 0;
        //while(reader.hasEvent()){
        while(icounter<4){
            icounter++;
             EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
             System.out.println("---------------> event  # " + icounter);
             decoder.decode(event);
             List<DetectorBankEntry> counters =  decoder.getDataEntries("FTOF1A");
             //System.out.println("---------------> COUNTERS  # " + icounter);
             decoder.getDetectorCounters(DetectorType.FTOF1A);
             for(DetectorBankEntry cnt : counters){
                 System.out.println(cnt);
             }
        }
        System.out.println("done...");
        decoder.showTimer();
        
    }
}
