/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.io.hippo;

import org.jlab.data.io.DataEvent;
import org.jlab.data.io.DataSync;
import org.jlab.evio.clas12.EvioDataDictionary;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioFactory;
import org.jlab.hipo.io.HipoWriter;

/**
 *
 * @author gavalian
 */
public class HipoDataSync implements DataSync {
    
    HipoWriter writer = null;
        
    public HipoDataSync(){
        this.writer = new HipoWriter();
    }
    
    public void open(String file) {
        this.writer.open(file);
        EvioDataDictionary  dict = EvioFactory.getDictionary();
        String[] descList = dict.getDescriptorList();
        for(String desc : descList){
            String descString = dict.getDescriptor(desc).toString();
            this.writer.addHeader(descString);
        }
    }

    public void writeEvent(DataEvent event) {
        EvioDataEvent  evioEvent = (EvioDataEvent) event;
        this.writer.writeEvent(evioEvent.getEventBuffer().array());
    }

    public void close() {
        this.writer.close();
    }
    
}
