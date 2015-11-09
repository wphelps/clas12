/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.geom.geant;

import java.util.ArrayList;
import java.util.List;
import org.jlab.geom.prim.Face3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.geom.prim.Shape3D;
import org.jlab.geom.prim.Triangle3D;

/**
 *
 * @author gavalian
 */
public class G4Trd implements IG4Volume {
    
    G4Material volumeMaterial = new G4Material();
    
    String   volumeName   = "g4trd";
    String   volumeParent = "mother_volume";
    Shape3D  volumeShape  = new Shape3D();
    
    double  g4trd_dx1 = 0.0;
    double  g4trd_dx2 = 0.0;
    double  g4trd_dy1 = 0.0;
    double  g4trd_dy2 = 0.0;
    double  g4trd_dz  = 0.0;
    
    public G4Trd(String name, double dx1, double dx2, double dy1, double dy2, double dz){
        this.volumeName = name;
        this.g4trd_dx1 = dx1;
        this.g4trd_dx2 = dx2;
        this.g4trd_dy1 = dy1;
        this.g4trd_dy2 = dy2;
        this.g4trd_dz = dz;
    }
    
    public String getName() {
        return volumeName;
    }

    public String getParent() {
        return this.volumeParent;
    }

    public String gemcString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Initializes the shape for the G4Trap volume.
     */
    public void initShape(){         
        List<Point3D>  trapPoints = this.getPoints();
        
        int[] faces = this.getFaces();
        for(int loop = 0; loop < faces.length; loop += 3){
            this.volumeShape.addFace(new Triangle3D(
                    trapPoints.get(loop),
                    trapPoints.get(loop+1),
                    trapPoints.get(loop+2)
            ));
            
        }        
    }
    
    public Shape3D getShape() {
        return this.volumeShape;
    }

    public List<Point3D> getPoints() {
        ArrayList<Point3D>  points = new ArrayList<Point3D>();
        
        points.add(new Point3D( -g4trd_dx1, -g4trd_dy1, -this.g4trd_dz));
        points.add(new Point3D(  g4trd_dx1, -g4trd_dy1, -this.g4trd_dz));
        points.add(new Point3D(  g4trd_dx1,  g4trd_dy1, -this.g4trd_dz));
        points.add(new Point3D( -g4trd_dx1,  g4trd_dy1, -this.g4trd_dz));
        
        points.add(new Point3D( -g4trd_dx2, -g4trd_dy2,  this.g4trd_dz));
        points.add(new Point3D(  g4trd_dx2, -g4trd_dy2,  this.g4trd_dz));
        points.add(new Point3D(  g4trd_dx2,  g4trd_dy2,  this.g4trd_dz));
        points.add(new Point3D( -g4trd_dx2,  g4trd_dy2,  this.g4trd_dz));
        
        return points;
    }
    
    /**
     * returns array with the faces based on the points.
     * @return 
     */
    public int[] getFaces() {
        int[] faces = new int[]{
            0, 1, 2,
            2, 3, 0,            
            0, 4, 5,
            0, 5, 1,
            
            1, 5, 6,
            1, 6, 2,
            2, 6, 7,
            2, 7, 3,
            
            3, 7, 4,
            3, 4, 0,
            4, 5, 6,
            4, 6, 7
        };
        return faces;
    }
    /**
     * Returns array of faces with texture map.
     * @return 
     */
    public int[] getMeshFaces() {
        int[] faces = this.getFaces();
        int[] meshfaces = new int[faces.length*2];
        for(int loop = 0; loop < faces.length; loop++){
            meshfaces[loop*2] = faces[loop];
        }
        return meshfaces;
    }

    public float[] getMeshPoints() {
        
        float[]  points = new float[]{
            (float) -g4trd_dx1, (float) -g4trd_dy1, (float) -this.g4trd_dz,
            (float) g4trd_dx1, (float) -g4trd_dy1, (float) -this.g4trd_dz,
                
            (float) g4trd_dx1, (float) g4trd_dy1, (float) -this.g4trd_dz,
            (float) -g4trd_dx1, (float) g4trd_dy1, (float) -this.g4trd_dz,
            
            (float) -g4trd_dx2, (float) -g4trd_dy2, (float) this.g4trd_dz,
            (float)  g4trd_dx2, (float) -g4trd_dy2,  (float) this.g4trd_dz,
            (float)  g4trd_dx2, (float)  g4trd_dy2, (float) this.g4trd_dz,
            (float) -g4trd_dx2, (float)  g4trd_dy2,  (float) this.g4trd_dz
        };
        return points;
    }    

    public G4Material getMaterial() {
        return this.volumeMaterial;
    }

    public void setMaterial(G4Material mat) {
        this.volumeMaterial = mat;
    }
    
}
