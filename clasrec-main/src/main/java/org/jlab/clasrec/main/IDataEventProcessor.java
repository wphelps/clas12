/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import org.jlab.data.io.DataEvent;

/**
 *
 * @author gavalian
 */
public interface IDataEventProcessor {
    void process(DataEvent event);    
}
