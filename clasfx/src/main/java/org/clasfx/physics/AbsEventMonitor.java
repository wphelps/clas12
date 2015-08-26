/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.physics;

import org.clasfx.root.TEmbeddedCanvas;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.evio.clas12.EvioDataEvent;

/**
 *
 * @author gavalian
 */
public abstract class AbsEventMonitor {
    private String moduleName = "unknown";
    
    public AbsEventMonitor(){
        
    }
    
    public void setName(String name){ this.moduleName = name;}
    public String getName(){return this.moduleName;}
    
    abstract void init();
    abstract void process(EvioDataEvent event);
    abstract void draw(TEmbeddedCanvas canvas);
}
