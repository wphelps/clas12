/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.detector.geant4;

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
    
    
    public Geant4Basic       createSector(ConstantProvider cp, int sector, int layer){
        //Geant4Basic  mother = new Geant4Basic();
        double mother_grow_X = 10.0;
        double mother_grow_Y = 10.0;
        double mother_grow_Z =  0.5;
        
        double thtilt    = Math.toRadians(cp.getDouble(stringLayers[layer-1]+"/panel/thtilt", 0)); 
        double thmin     = Math.toRadians(cp.getDouble(stringLayers[layer-1]+"/panel/thmin", 0));
        double dist2edge = cp.getDouble(stringLayers[layer-1]+"/panel/dist2edge", 0);
        
        double perp_offset = dist2edge * Math.sin(thtilt-thmin);
        double R = Math.sqrt(dist2edge*dist2edge-perp_offset*perp_offset);
        
        //System.out.println("PANEL OFFSETG = " + perp_offset);
        
        
        List<Geant4Basic>  paddles = this.createLayer(cp, layer);
        double x_offset_first = paddles.get(0).getPosition()[0];
        double x_offset_last  = paddles.get(paddles.size()-1).getPosition()[0];
        
        double y_size_first = paddles.get(0).getParameters()[1];
        double y_size_last  = paddles.get(paddles.size()-1).getParameters()[1];
        
        double center         = x_offset_first - (x_offset_first - x_offset_last)/2.0;
        
        double y_length_first = paddles.get(0).getParameters()[1];
        double y_length_last  = paddles.get(paddles.size()-1).getParameters()[1];
        double x_size_paddle  = paddles.get(0).getParameters()[0];
        double z_size_paddle  = paddles.get(0).getParameters()[2];
        
        double[]  params = new double[5];
        
        //System.out.println(" CENTER = " + center + " " + y_offset_first + " " + y_offset_last);
        params[0]        = y_length_last  + mother_grow_X;
        params[1]        = y_length_first + mother_grow_X;
        params[2]        = Math.abs(x_offset_first - x_offset_last)/2.0 + x_size_paddle*2 + mother_grow_Y;
        params[3]        = params[2];
        params[4]        = z_size_paddle + mother_grow_Z;
        
        Geant4Basic     mother = new Geant4Basic("FTOF_S_"+sector+"_L_"+layer,"trd",params);
        mother.setPosition( -(perp_offset - center), 0.0, R);
        
        mother.setRotation("yzx", thtilt, Math.toRadians(60.0*(sector-1)), 0.0);
        for(int loop = 0; loop < paddles.size();loop++){
            double[] pos = paddles.get(loop).getPosition();
            pos[0] = pos[0] - center;
            paddles.get(loop).setName("ftof_S_"+sector+"_L_"+layer+"_P_"+(loop+1));
            paddles.get(loop).setPosition(pos[0], pos[1], pos[2]);
            mother.getChildren().add(paddles.get(loop));
        }
        return mother;
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
        
        //List<Geant4Basic>  mother = new ArrayList<Geant4Basic>();
        
        List<Geant4Basic>  lv = new ArrayList<Geant4Basic>();
        
        for(int loop = 0; loop < numPaddles; loop++){            
            int paddleId = loop + 1;
            double paddlelength = cp.getDouble(paddleLengthStr, loop);
            double xoffset = loop * (paddlewidth + gap + 2*wrapperthickness);
            String vname = String.format("sci_S%d_L%d_C%d", 1,1,paddleId);
            Geant4Basic  volume = new Geant4Basic(vname,"box",
                    paddlewidth/2.0,paddlelength/2.,paddlethickness/2.);
            volume.setId(1,layer,paddleId);
            volume.setPosition(xoffset, 0.0, 0.0);
            lv.add(volume);
        }
        return lv;
    }         
    
    
}
