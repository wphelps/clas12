/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.geant.detector.ftof;

import java.util.ArrayList;
import java.util.List;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.geant.Geant4Basic;

/**
 *
 * @author gavalian
 */
public class FTOFGeant4Factory {
    
    
    private List<Geant4Basic>  volumes = new ArrayList<Geant4Basic>();
    private String[] stringLayers = new String[]{
        "/geometry/ftof/panel1a",
        "/geometry/ftof/panel1b",
        "/geometry/ftof/panel2"};
    
    public FTOFGeant4Factory(ConstantProvider provider){
        
    }
    
    public List<Geant4Basic> createLayer(ConstantProvider cp, int layer){
        
        int    numPaddles       =    cp.length(stringLayers[layer-1]+"/paddles/paddle");
        double paddlewidth      = cp.getDouble(stringLayers[layer-1]+"/panel/paddlewidth", 0); 
        double paddlethickness  = cp.getDouble(stringLayers[layer-1]+"/panel/paddlethickness", 0); 
        double gap              = cp.getDouble(stringLayers[layer-1]+"/panel/gap", 0);
        double wrapperthickness = cp.getDouble(stringLayers[layer-1]+"/panel/wrapperthickness", 0);
        double thtilt    = Math.toRadians(cp.getDouble(stringLayers[layer-1]+"/panel/thtilt", 0)); 
        double thmin     = Math.toRadians(cp.getDouble(stringLayers[layer-1]+"/panel/thmin", 0)); 
        
        String paddleLengthStr = stringLayers[layer-1]+"/paddles/Length";
        
        List<Geant4Basic>  lv = new ArrayList<Geant4Basic>();
        
        for(int loop = 0; loop < numPaddles; loop++){            
            int paddleId = loop + 1;
            double paddlelength = cp.getDouble(paddleLengthStr, loop);
            double xoffset = loop * (paddlewidth + gap + 2*wrapperthickness);
            String vname = String.format("sci_S%d_L%d_C%d", 1,1,paddleId);
            Geant4Basic  volume = new Geant4Basic(vname,"box",
                    paddlethickness/2.0,paddlelength/2.,paddlewidth/2.);
            volume.setId(1,layer,paddleId);
            volume.setPosition(xoffset, 0.0, 0.0);
            lv.add(volume);
        }
        
        return lv;
    }
    
}
