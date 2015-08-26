/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.physics;

import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class EventMonitorLibrary {
    
    private static TreeMap<String,AbsEventMonitor>  library = initLibrary();
            
    public static TreeMap<String,AbsEventMonitor>  initLibrary(){
        TreeMap<String,AbsEventMonitor> map = new TreeMap<String,AbsEventMonitor>();
        map.put("DVCS", new PhysicsMonitorDVCS());
        return map;
    }
    
    public static TreeMap<String,AbsEventMonitor> getLibrary(){
        return EventMonitorLibrary.library;
    }
}
