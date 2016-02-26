/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.object;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class DetectorBank {
    private List<Integer>  adcL = new ArrayList<Integer>();
    public DetectorBank(){
        
    }
    
    @EvioDataType(parent=1200,tag=1202,num=3,type="int32")
    public List getADCL(){
        return this.adcL;
    }
    
}
