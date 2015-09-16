/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.physics;

import org.jlab.clas.detector.DetectorType;
import org.jlab.clas.pdg.PDGDatabase;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;

/**
 *
 * @author gavalian
 */
public class CLAS6DetectorEventProcess implements IDetectorEventProcessor {    
    
    public boolean processDetectorEvent(DetectorEvent event) {
        /**
         * getResponse(DetectoType.EC, layer) returns hit for given layer.
         * layer = 0 : ECIN
         * layer = 1 : ECOUT
         * layer = 2 : ECTOT
         */
        DetectorResponse  ecin  = event.getParticles().get(0).getResponse(DetectorType.EC,0);
        DetectorResponse  ecout = event.getParticles().get(0).getResponse(DetectorType.EC,1);
        DetectorResponse  ectot = event.getParticles().get(0).getResponse(DetectorType.EC,2);
         if(ecin!=null&&ecout!=null&&ectot!=null){
             double momentum = event.getParticles().get(0).vector().mag();
             double samplingFraction = ectot.getEnergy()/momentum;
             if(ecin.getEnergy()>0.05&&ecout.getEnergy()>0.05&&
                     samplingFraction>0.27){
                 event.getParticles().get(0).setPid(11);
             } else {
                 event.getParticles().get(0).setPid(0);
             }
         }
         return true;
    }
    
}
