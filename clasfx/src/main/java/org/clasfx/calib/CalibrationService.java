/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.calib;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author gavalian
 */
public class CalibrationService extends Service<Void> {
    CalibrationStore  store = new CalibrationStore();

    public CalibrationService(){
        
    }
    
    public CalibrationStore getStore(){ return store;}
    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        store.processFile();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }
            };
    }
    
}
