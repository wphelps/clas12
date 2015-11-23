/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author gavalian
 * @param <T>
 */
public class DetectorList<T extends IDetectorUnit> extends ArrayList<T> {
        
    public DetectorList(){
        
    }
    
    public List<T> getList(DetectorType type){
        List<T> collection = new ArrayList<T>();
        for(T item : this){
            if(item.getDescriptor().getType()==type){
                collection.add(item);
            }
        }
        return collection;
    }
    
    public List<T> getList(DetectorType type, int sector){
        List<T> collection = new ArrayList<T>();
        for(T item : this){
            if(item.getDescriptor().getType()==type&&
                    item.getDescriptor().getSector()==sector){
                collection.add(item);
            }
        }
        return collection;
    }
    
    public List<T> getList(DetectorType type, int sector, int layer){
        List<T> collection = new ArrayList<T>();
        for(T item : this){
            if(item.getDescriptor().getType()==type&&
                    item.getDescriptor().getSector()==sector&&
                    item.getDescriptor().getLayer()==layer){
                collection.add(item);
            }
        }
        return collection;
    }
}
