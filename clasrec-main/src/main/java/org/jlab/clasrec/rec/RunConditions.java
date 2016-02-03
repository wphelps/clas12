/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.rec;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class RunConditions {
    boolean isInitialized = false;
    
    private TreeMap<String,String>  options = new TreeMap<String,String>();
    
    public RunConditions(){
        
    }
    
    
    public boolean isValid(){
        return this.isInitialized;
    }
    
    public void parse(String[] opt){

        if(opt==null) return;
        
        for(String key : opt){
            String[] tokens = key.split("\\s+");
            if(tokens.length>=3){
                // This part analyzes SCALE_FIELD OPTION
                if(tokens[1].startsWith("SCALE_FIELD")){
                    
                    if(tokens[2].contains("torus")){
                        options.put("TORUS_FIELD_SCALE", tokens[3].trim());
                    }
                    if(tokens[2].contains("solenoid")){
                        options.put("SOLENOID_FIELD_SCALE", tokens[3].trim());
                    }
                    
                }
                
                if(tokens[1].startsWith("RUNNO")==true){
                    options.put("RUN", tokens[2].trim());
                }
                
            }
        }
        
        this.isInitialized = true;
    }
    
    public boolean hasItem(String key){
        return this.options.containsKey(key);
    }
    
    public double getInt(String key){
        return Integer.parseInt(options.get(key));
    }
    
    public double getDouble(String key){
        return Double.parseDouble(options.get(key));
    }
    
    public void show(){
        for(Map.Entry<String,String> item : this.options.entrySet()){
            System.out.println(String.format("*%32s*%18s*", item.getKey(),item.getValue()));
        }
    }
}
