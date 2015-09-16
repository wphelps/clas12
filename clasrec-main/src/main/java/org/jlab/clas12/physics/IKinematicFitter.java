/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.physics;

import java.util.Map;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.data.io.DataEvent;

/**
 *
 * @author gavalian
 */
public interface IKinematicFitter {
    
    DetectorEvent getDetectorEvent(  DataEvent event );
    PhysicsEvent  getPhysicsEvent(   DataEvent event );
    PhysicsEvent  getGeneratedEvent( DataEvent event );
    /**
     * Interface to modify or set options for kinematic fitter.
     * @param key
     * @param value 
     */
    void                setOption(String key, String value);
    /**
     * Map containing all options used in the fitter.
     * @return 
     */
    Map<String,String>  getOptionMap();
}
