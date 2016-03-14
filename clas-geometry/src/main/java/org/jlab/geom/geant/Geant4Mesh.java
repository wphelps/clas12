/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.geom.geant;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author gavalian
 */
public class Geant4Mesh {
    String meshName = "G4Mesh";
    MeshView volumeMesh = null;
    
    public Geant4Mesh(){
        
    }
    
    
    public final void createMesh(Geant4Basic volume){
        this.meshName = volume.getName();
        this.volumeMesh = Geant4Mesh.makeMesh(volume);
    }
    /**
     * Creates a JavaFX Mesh object from given volume, they type string
     * determines which function should be called.
     * @param volume
     * @return 
     */
    public static MeshView makeMesh(Geant4Basic volume){
        if(volume.getType().compareTo("box")==0)
            return Geant4Mesh.makeMeshBox(volume);
        return null;
    }
    /**
     * Creates a JavaFX Mesh for a BOX object, it can be viewed in the JavaFX scene.
     * @param volume
     * @return 
     */
    public static MeshView makeMeshBox(Geant4Basic volume){
        double[] p = volume.getParameters();
            float[] points = new float[]{
                (float) -p[0], (float) -p[1], (float) -p[2],
                (float) -p[0], (float)  p[1], (float) -p[2],
                (float) -p[0], (float)  p[1], (float)  p[2],
                (float) -p[0], (float) -p[1], (float)  p[2],
                
                (float)  p[0], (float) -p[1], (float) -p[2],
                (float)  p[0], (float)  p[1], (float) -p[2],
                (float)  p[0], (float)  p[1], (float)  p[2],
                (float)  p[0], (float) -p[1], (float)  p[2]                     
            };            
            int[]  faces = new int[]{
                0,0,  1,0, 2,0,
                2,0,  3,0, 0,0,
                4,0,  5,0, 6,0,
                6,0,  7,0, 4,0,
                0,0,  1,0, 5,0,
                5,0,  4,0, 0,0,
                1,0,  2,0, 6,0,
                6,0,  5,0, 1,0,
                2,0,  3,0, 7,0,
                7,0,  6,0, 2,0,
                0,0,  3,0, 7,0,
                7,0,  4,0, 0,0
            };
            TriangleMesh boxMesh = new TriangleMesh();
            return new MeshView(boxMesh);
    }
}
