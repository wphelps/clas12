/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.utils;

import org.jlab.coda.jevio.EvioXMLDictionary;

/**
 *
 * @author gavalian
 */
public class EvioDictionaryTest {
    public static void main(String[] args){
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
 
        System.out.println(" STRING = " + dict.toString());
    }
}
