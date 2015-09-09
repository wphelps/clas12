/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.coda.jevio.EventWriter;
import org.jlab.coda.jevio.EvioCompactEventWriter;
import org.jlab.coda.jevio.EvioException;
import org.jlab.coda.jevio.EvioXMLDictionary;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioFactory;
import org.jlab.evio.decode.EvioDictionaryGenerator;

/**
 *
 * @author gavalian
 */
public class EvioDictionaryTest {
    
    public static void writeFile(boolean isDictionary){
        try {
            String  dictionary = "<xmlDict>\n" + EvioDictionaryGenerator.createDAQDictionary(
                    new String[]{"EC","PCAL","FTOF1A"}) + "</xmlDict>\n";
            System.out.println(dictionary);
            String filename = "/Users/gavalian/Work/dictTest.evio";
            /*
            EvioCompactEventWriter evioWriter = new EvioCompactEventWriter(
                    "/Users/gavalian/Work/dictTest.evio", null,
                    0, 0,
                    15*300,
                    2000,
                    8*1024*1024,
                    ByteOrder.LITTLE_ENDIAN, dictionary, true);
            */
            EventWriter  evioWriter = new EventWriter(new File(filename),10000,1024,
                    ByteOrder.LITTLE_ENDIAN,dictionary,null);
            for(int loop = 0; loop < 100; loop++){
                EvioDataEvent event = EvioFactory.createEvioEvent();
                ByteBuffer original = event.getEventBuffer();
                Long bufferSize = (long) original.capacity();            
                ByteBuffer clone = ByteBuffer.allocate(original.capacity());
                clone.order(original.order());
                original.rewind();
                clone.put(original);
                original.rewind();
                clone.flip();
                evioWriter.writeEvent(clone);
            }
            evioWriter.close();
        } catch (EvioException ex) {
            Logger.getLogger(EvioDictionaryTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EvioDictionaryTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        EvioDictionaryTest.writeFile(true);
        /*
        String xmlDict5 = "<xmlDict>" 
                //+ "<dictEntry name=\"tdc\"  tag=\"1300\" num=\"1\" type=\"int32\"/>"
                + "<leaf name='sector' tag='1300' num='1' type='int32'/>"
                + "<leaf name='layer'  tag='1302' num='2' type='int32'/>"
                + "<leaf name='adc'    tag='1304' num='3' type='int32'/>"
                + "<leaf name='DC.tdc'    tag='1300.1302' num='4.1' type='int32' description='TOF bank'/>"
                + "<bank name='SC' tag= '30' num='0' type='bank' description='time of flight bank'>"
                + "<bank name='dgtz' tag= '32' num='0' type='bank' description='time of flight bank'>"
                + "<description> Test Description </description>"
                +    "<leaf name='sector'     tag= '32' num='1' type='int32'/>"
                +    "<leaf name='layer'      tag= '32' num='2' type='char8' description='layer of scintilator'/>"
                +    "<leaf name='component'  tag= '32' num='3' type='short16'/>"
                +    "<leaf name='ADC'        tag= '32' num='4' type='int32'/>"
                + "</bank>"
                + "</bank>"
                + "</xmlDict>";
        
        EvioXMLDictionary dict = new EvioXMLDictionary(xmlDict5);
        
        System.out.println(dict.getDescription("sector"));
        
        System.out.println( dict.getTag("SC.sector") + "  "  + dict.getType("SC.layer")
        + " " + dict.getTag("SC") + "  " + dict.getDescription("SC") + " " + dict.getType("SC")
        + "  " +  dict.getType(30, 0));
 
        System.out.println(" STRING = " + dict.toString());*/
    }
}
