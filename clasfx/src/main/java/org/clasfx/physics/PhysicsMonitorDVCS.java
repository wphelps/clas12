/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.physics;

import org.clasfx.root.TEmbeddedCanvas;
import org.jlab.clas.physics.EventFilter;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.clas12.physics.GenericKinematicFitter;
import org.jlab.evio.clas12.EvioDataEvent;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class PhysicsMonitorDVCS extends AbsEventMonitor {
    
    H1D  electron_Q2 = null;
    H1D  electron_W2 = null;
    H1D  missing_Gamma = null;
    
    EventFilter   filter = new EventFilter("11:2212");
    GenericKinematicFitter fitter = new GenericKinematicFitter(11.0,"11:X+:X-:Xn");
    
    public PhysicsMonitorDVCS(){
        this.setName("DVCS");
    }
    
    @Override
    void init() {
        electron_Q2 = new H1D("Q2",100,0.0,5.0);
        electron_Q2.setFillColor(4);
        electron_W2 = new H1D("W2",100,0.0,5.0);
        electron_W2.setFillColor(3);
        missing_Gamma = new H1D("MxEP",100,-0.2,0.4);
        missing_Gamma.setFillColor(5);
    }

    @Override
    void process(EvioDataEvent event) {
        PhysicsEvent   physEvent = fitter.getPhysicsEvent(event);
        if(filter.isValid(physEvent)){
            Particle q2 = physEvent.getParticle("[b]-[11]");
            electron_Q2.fill(-q2.vector().mass2());
            Particle w2 = physEvent.getParticle("[b]+[t]-[11]");
            electron_W2.fill(w2.vector().mass2());            
            missing_Gamma.fill(physEvent.getParticle("[b]+[t]-[11]-[2212]").vector().mass2());
        }
    }

    @Override
    void draw(TEmbeddedCanvas canvas) {
        canvas.divide(2, 2);
        canvas.cd(0);
        canvas.draw(electron_Q2);
        canvas.cd(1);
        canvas.draw(electron_W2);
        canvas.cd(2);
        canvas.draw(missing_Gamma);
    }
    
}
