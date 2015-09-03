/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.evio.decode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jlab.clas.detector.DetectorType;
import org.jlab.coda.jevio.EvioDictionaryEntry;
import org.jlab.coda.jevio.EvioXMLDictionary;
import org.jlab.evio.clas12.EvioDataDescriptor;

/**
 *
 * @author gavalian
 */
public class EvioDictionaryGenerator {
    
    
    public static EvioDataDescriptor  createDescriptor(DetectorType type){

        Integer   parent_tag = type.getDetectorId()*100;
        Integer   container_tag = parent_tag+9;
        EvioDataDescriptor desc = new EvioDataDescriptor(type.getName(),
                parent_tag.toString(), container_tag.toString());
        
        desc.addEntry("daq", "sector"   , container_tag, 1, "int8");
        desc.addEntry("daq", "layer"    , container_tag, 2, "int8");
        desc.addEntry("daq", "component", container_tag, 3, "int8");
        desc.addEntry("daq", "order"    , container_tag, 4, "int8");
        desc.addEntry("daq", "value"    , container_tag, 5, "int32");
        
        return desc;
    }
    
    public static String  createDescriptorXML(DetectorType type){

        Integer   ptag = type.getDetectorId()*100;
        Integer   ctag = ptag+9;
        String    name = type.getName();
        
        StringBuilder str = new StringBuilder();
        
        str.append(String.format("<bank name='%s' tag= '%d' num='0' type='bank'>\n",name,ptag));
        str.append(String.format("\t<bank name='%s' tag= '%d' num='0' type='bank'>\n","daq",ctag));
        str.append(String.format("\t\t<leaf name='sector'    tag='%d' num='1' type='char8'/>\n",ctag));
        str.append(String.format("\t\t<leaf name='layer'     tag='%d' num='2' type='char8'/>\n",ctag));
        str.append(String.format("\t\t<leaf name='component' tag='%d' num='3' type='char8'/>\n",ctag));

        str.append(String.format("\t\t<leaf name='order'     tag='%d' num='4' type='char8'/>\n",ctag));
        str.append(String.format("\t\t<leaf name='value'     tag='%d' num='5' type='int32'/>\n",ctag));
        str.append("\t</bank>\n");
        str.append("</bank>\n");
        
        return str.toString();
    }
    
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
    
    public static int  getDescriptorParentTag(EvioXMLDictionary dict, String system){
        Map<String, EvioDictionaryEntry> map = dict.getMap();
        int i = 0;
        for (Map.Entry<String, EvioDictionaryEntry> entry : map.entrySet()) {
           String entryName =  entry.getKey();
           EvioDictionaryEntry entryData = entry.getValue();
           if(entryName.compareTo(system)==0){
               return entryData.getTag();
           }
       }
        return -1;
    }
    
    public static int  getDescriptorContainerTag(EvioXMLDictionary dict, String system,
            String section){
        
        Map<String, EvioDictionaryEntry> map = dict.getMap();
        int i = 0;
        String bankName = system + "." + section;
        for (Map.Entry<String, EvioDictionaryEntry> entry : map.entrySet()) {
           String entryName =  entry.getKey();
           EvioDictionaryEntry entryData = entry.getValue();
           if(entryName.compareTo(bankName)==0){
               return entryData.getTag();
           }
        }
        return -1;
    }
    
    public static Set<String>  getSystems(EvioXMLDictionary dict){
        
        Set<String>  systems = new HashSet<String>();
        Map<String, EvioDictionaryEntry> map = dict.getMap();
        
        int i = 0;
        
        for (Map.Entry<String, EvioDictionaryEntry> entry : map.entrySet()) {
           String entryName =  entry.getKey();
           EvioDictionaryEntry entryData = entry.getValue();
           String[] tokens = entryName.split("\\.");
           if(tokens.length>0){
               systems.add(tokens[0]);
           }
        }
        return systems;
    }
    
    public static List<EvioDataDescriptor>  getDescriptors(EvioXMLDictionary dict){
        List<EvioDataDescriptor> descList = new ArrayList<EvioDataDescriptor>();
        Set<String> systems = EvioDictionaryGenerator.getSystems(dict);
        
        for(String system : systems){
            Set<String>  sections = EvioDictionaryGenerator.getSections(dict, system);
            for(String section : sections){
                EvioDataDescriptor  desc = EvioDictionaryGenerator.getDescriptor(
                        dict, system, section);
                descList.add(desc);
            }
        }
        return descList;
    }
    
    public static Set<String>  getSections(EvioXMLDictionary dict, String system){
        
        Set<String>  sections = new HashSet<String>();
        Map<String, EvioDictionaryEntry> map = dict.getMap();
        
        int i = 0;
        
        for (Map.Entry<String, EvioDictionaryEntry> entry : map.entrySet()) {
           String entryName =  entry.getKey();
           EvioDictionaryEntry entryData = entry.getValue();
           String[] tokens = entryName.split("\\.");
           if(tokens.length>1){
               if(tokens[0].compareTo(system)==0){
                   sections.add(tokens[1]);
               }
           }
        }
        return sections;
    }
    
    public static EvioDataDescriptor getDescriptor(EvioXMLDictionary dict, String system,
            String section){
        
        Integer ptag = EvioDictionaryGenerator.getDescriptorParentTag(dict, system);
        Integer ctag = EvioDictionaryGenerator.getDescriptorContainerTag(dict, system,section);
        
        EvioDataDescriptor desc = new EvioDataDescriptor(system,ptag.toString(),
                ctag.toString());
        
        Map<String, EvioDictionaryEntry> map = dict.getMap();
        int i = 0;
        String bankName = system + "." + section;
        for (Map.Entry<String, EvioDictionaryEntry> entry : map.entrySet()) {
           String entryName =  entry.getKey();
           EvioDictionaryEntry entryData = entry.getValue();
           String[] tokens = entryName.split("\\.");
           //System.out.println(entryName + "  size = " + tokens.length);
           if(tokens.length == 3){
               //System.out.println("  " + tokens[0] + " " + tokens[1] + " " + tokens[2]);
               if(tokens[0].compareTo(system)==0&&tokens[1].compareTo(section)==0){
                   System.out.println("---->  found entry " + tokens[2] + "  "
                   +  entryData.getType().toString());
                   desc.addEntry(section, tokens[2], ctag, entryData.getNum(), 
                           entryData.getType().name());
                   
               }
           }
       }
        return desc;
    }
    
    public static String   createDAQDictionary(String[] detectors){
        StringBuilder str = new StringBuilder();
        for(String detector : detectors){
            str.append(EvioDictionaryGenerator.createDescriptorXML(DetectorType.getType(detector)));
        }
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
        //System.out.println(EvioDictionaryGenerator.createDictionary());
        EvioDataDescriptor desc = EvioDictionaryGenerator.createDescriptor(DetectorType.PCAL);
        desc.show();
        
        
        String  dictionary = "<xmlDict>\n" 
                + EvioDictionaryGenerator.createDescriptorXML(DetectorType.PCAL)
                + EvioDictionaryGenerator.createDescriptorXML(DetectorType.CND)
                + EvioDictionaryGenerator.createDescriptorXML(DetectorType.EC)
                + EvioDictionaryGenerator.createDescriptorXML(DetectorType.FTOF1A)
                + EvioDictionaryGenerator.createDescriptorXML(DetectorType.FTOF1B)
                + EvioDictionaryGenerator.createDescriptorXML(DetectorType.FTOF2)
                + EvioDictionaryGenerator.createDescriptorXML(DetectorType.BST)
                + "</xmlDict>\n";
        
        System.out.println(dictionary);
        EvioXMLDictionary dict = new EvioXMLDictionary(dictionary);
        Map<String, EvioDictionaryEntry> map = dict.getMap();
        int i = 0;
        for (Map.Entry<String, EvioDictionaryEntry> entry : map.entrySet()) {
           String entryName =  entry.getKey();
           EvioDictionaryEntry entryData = entry.getValue();
           System.out.println("entry " + (++i) + ": name = " + entryName + ", tag = " +
                                      entryData.getTag() + ", num = " + entryData.getNum());
        }
        /*
        EvioDataDescriptor  pcalDesc = EvioDictionaryGenerator.getDescriptor(dict, "PCAL","daq");
        pcalDesc.show();
        */
        List<EvioDataDescriptor>  descList = EvioDictionaryGenerator.getDescriptors(dict);
        
        for(EvioDataDescriptor d : descList){
            d.show();
        }
    }
}
