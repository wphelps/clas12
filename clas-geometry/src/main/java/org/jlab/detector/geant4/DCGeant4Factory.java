/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.detector.geant4;

import org.jlab.geom.geant.Geant4Basic;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Plane3D;
import org.jlab.geom.prim.Point3D;

/**
 *
 * @author gavalian
 */
public class DCGeant4Factory {
    /**
     * This will return the geant4 volume (Trapezoid) which
     * describes the one layer of Drift Chambers.
     * @param xstart
     * @param step
     * @param count
     * @param th_endplate
     * @param tilt
     * @return 
     */
    public Geant4Basic  getLayer(double x_wire, double  x_edge,
            double width,
            double th_endplate, double tilt){
        
        double pDz     = x_wire;
        double pTheta  = tilt;
        
        Plane3D  leftPlane  = new Plane3D(-x_edge,0.0,0.0, 
                1.0*Math.sin(th_endplate),-Math.cos(th_endplate),0.0);
        Plane3D  rightPlane = new Plane3D(-x_edge,0.0,0.0, 
                Math.sin(th_endplate),Math.cos(th_endplate),0.0);
        
        Line3D wire = new Line3D(-pDz, -1000.0, 0.0, -pDz, 1000.0,0.0);
        wire.rotateZ(tilt);
        
        Point3D  leftPoint = new Point3D();
        Point3D  rightPoint = new Point3D();
        
        leftPlane.intersection(wire, leftPoint);
        rightPlane.intersection(wire, rightPoint);
        
        Line3D  wireB = new Line3D(leftPoint,rightPoint);
        
        double pDx1 = leftPoint.distance(rightPoint);
        double pDx2 = pDx1;
        
        wire.set(pDz, -1000.0, 0.0, pDz, 1000.0,0.0);
        wire.rotateZ(tilt);
        
        leftPlane.intersection(wire, leftPoint);
        rightPlane.intersection(wire, rightPoint);
        Line3D  wireT = new Line3D(leftPoint,rightPoint);
        
        double pDx3 = leftPoint.distance(rightPoint);
        double pDx4 = pDx3;
        double pAlp1 = Math.toRadians(0.0);
        double pAlp2 = Math.toRadians(0.0);
        pDz = pDz*Math.cos(tilt);
        
        double pDy1 = width;
        double pDy2 = width;
        double pPhi   = Math.toRadians(90.0);
        
        Geant4Basic  layer = new Geant4Basic("Layer_","trap",
                pDz, pTheta, pPhi,
                pDy1, pDx1, pDx2, pAlp1,
                pDy2, pDx3, pDx4, pAlp2
        );
        layer.setRotation("yxz", tilt, Math.toRadians(90.0),0.0);
        return layer;
    }
    
    public Geant4Basic  getRegion(int region ){
        Geant4Basic  regionMother = new Geant4Basic("REGION_"+region,"box",200,200,200);
        for(int layer = 0; layer < 6; layer++){
            Geant4Basic  dcL = this.getLayer(
                    100, 120, 1.0,
                    Math.toRadians(12.5), Math.toRadians(6.0));
            dcL.setPosition(0.0, 0.0, layer*12.0);
            regionMother.getChildren().add(dcL);
        }
        for(int layer = 0; layer < 6; layer++){
            Geant4Basic  dcL = this.getLayer(
                    100, 120, 1.0,
                    Math.toRadians(12.5), Math.toRadians(-6.0));
            dcL.setPosition(0.0, 0.0, 72+layer*12.0);
            regionMother.getChildren().add(dcL);
        }
        
        return regionMother;
    }
}
