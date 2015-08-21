/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.evio.decode;

/**
 *
 * @author gavalian
 */
public class EvioDictionaryGenerator {
    
    public static String  dictionaryCTOF(){
        StringBuilder  str = new StringBuilder();
        str.append("<bank name='CTOF' tag= '400' num='0' type='bank'>\n");
        str.append("\t<leaf name='sector'     tag= '400' num='1' type='char8'/>\n");
        str.append("\t<leaf name='layer'      tag= '400' num='2' type='char8'/>\n");
        str.append("\t<leaf name='component'  tag= '400' num='3' type='char8'/>\n");
        str.append("\t<leaf name='TDCL'       tag= '400' num='4' type='int32'/>\n");
        str.append("\t<leaf name='TDCR'       tag= '400' num='5' type='int32'/>\n");
        str.append("\t<leaf name='ADCL'       tag= '400' num='6' type='int32'/>\n");
        str.append("\t<leaf name='ADCR'       tag= '400' num='7' type='int32'/>\n");
        str.append("</bank>");
        return str.toString();
    }
    
    public static String createDictionary(){
        StringBuilder  str = new StringBuilder();
        str.append("<xmlDict>\n");
        str.append(EvioDictionaryGenerator.createDescriptor("CTOF",  400, 4));
        str.append(EvioDictionaryGenerator.createDescriptor("FTOF", 1000, 4));
        str.append(EvioDictionaryGenerator.createDescriptor("EC",   1600, 2));
        str.append(EvioDictionaryGenerator.createDescriptor("DC",   1300, 1));
        
        str.append("</xmlDict>\n");        
        return str.toString();
    }
    
    public static String createDescriptor(String name, int tag, int mode){
        StringBuilder  str = new StringBuilder();
        str.append(String.format("<bank name='%s' tag= '%d' num='0' type='bank'>\n",name,tag));
        str.append(String.format("\t<leaf name='sector'    tag='%d' num='1' type='char8'/>\n",tag));
        str.append(String.format("\t<leaf name='layer'     tag='%d' num='2' type='char8'/>\n",tag));
        str.append(String.format("\t<leaf name='component' tag='%d' num='3' type='char8'/>\n",tag));
        if(mode==1){
            str.append(String.format("\t<leaf name='TDC' tag='%d' num='4' type='int32'/>\n",tag));
        }
        
        if(mode==2){
            str.append(String.format("\t<leaf name='TDC' tag='%d' num='4' type='int32'/>\n",tag));
            str.append(String.format("\t<leaf name='ADC' tag='%d' num='5' type='int32'/>\n",tag));
        }
        
        if(mode==4){
            str.append(String.format("\t<leaf name='TDCL' tag='%d' num='4' type='int32'/>\n",tag));
            str.append(String.format("\t<leaf name='ADCL' tag='%d' num='5' type='int32'/>\n",tag));
            str.append(String.format("\t<leaf name='TDCR' tag='%d' num='6' type='int32'/>\n",tag));
            str.append(String.format("\t<leaf name='ADCR' tag='%d' num='7' type='int32'/>\n",tag));
        }
        str.append("</bank>\n");
        return str.toString();
    }
    
    public static void main(String[] args){
        System.out.println(EvioDictionaryGenerator.createDictionary());
    }
}
