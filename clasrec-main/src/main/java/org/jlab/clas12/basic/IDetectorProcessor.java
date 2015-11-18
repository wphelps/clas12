/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.basic;

import org.jlab.data.io.DataEvent;

/**
 *
 * @author gavalian
 */
public interface IDetectorProcessor {
    void processEvent(DataEvent event);    
}
