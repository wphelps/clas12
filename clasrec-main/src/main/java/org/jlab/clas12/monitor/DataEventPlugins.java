/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.monitor;

import java.util.TreeMap;
import org.jlab.data.io.DataEventStore;

/**
 *
 * @author gavalian
 */
public class DataEventPlugins {
    
    private TreeMap<String,IDataEventPlugin>  plugins = new TreeMap<String,IDataEventPlugin>();
    
    public DataEventPlugins(){
        
    }    
    
    public void add(IDataEventPlugin pl){
        this.plugins.put(pl.getName(), pl);
    }
    
    public void process(DataEventStore store){
        
    }
}
