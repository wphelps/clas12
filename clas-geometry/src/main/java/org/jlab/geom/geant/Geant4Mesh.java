/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.geom.geant;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.jlab.geom.prim.Point3D;
import org.jlab.geom.prim.Transformation3D;

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
    
    public static List<MeshView> getMesh(Geant4Basic volume){
        List<MeshView> meshes = new ArrayList<MeshView>();
        Transformation3D  vT  = new Transformation3D();

        for(Geant4Basic item : volume.getChildren()){
            vT.clear();
            vT.append(item.translation());
            vT.append(item.rotation());
            vT.append(volume.translation());            
            vT.append(volume.rotation());
            vT.show();
            MeshView  mesh = Geant4Mesh.makeMeshBox(item, vT);
            meshes.add(mesh);
        }
        return meshes;
    }
    /**
     * Creates a JavaFX Mesh for a BOX object, it can be viewed in the JavaFX scene.
     * @param volume
     * @return 
     */
    public static MeshView makeMeshBox(Geant4Basic volume){        
        double[] p = volume.getParameters();
        double[] tr = volume.getPosition();        
        
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
            0,0,  3,0, 2,0,
            2,0,  1,0, 0,0,
            4,0,  5,0, 6,0,
            6,0,  7,0, 4,0,
            1,0,  2,0, 6,0,
            6,0,  5,0, 1,0,
            0,0,  1,0, 5,0,
            5,0,  4,0, 0,0,
            0,0,  4,0, 7,0,
            7,0,  3,0, 0,0,
            2,0,  3,0, 7,0,
            7,0,  6,0, 2,0                                  
        };
        
        TriangleMesh boxMesh = new TriangleMesh();
        System.out.println("CREATING MESH");
        boxMesh.getTexCoords().addAll(0, 0);
        boxMesh.getPoints().addAll(points);
        boxMesh.getFaces().addAll(faces);
        return new MeshView(boxMesh);
    }
    /**
     * Creates a JavaFX Mesh for a BOX object, it can be viewed in the JavaFX scene.
     * @param volume
     * @return 
     */
    public static MeshView makeMeshBox(Geant4Basic volume,Transformation3D trans){
        
        double[] p = volume.getParameters();
        double[] tr = volume.getPosition();        
        Point3D  point = new Point3D();
        float[]  points = new float[8*3];
        
        point.set(-p[0], -p[1], -p[2]);
        trans.apply(point);
        points[0] = (float) point.x();
        points[1] = (float) point.y();
        points[2] = (float) point.z();
        point.set(-p[0],  p[1], -p[2]);
        trans.apply(point);
        points[3] = (float) point.x();
        points[4] = (float) point.y();
        points[5] = (float) point.z();
        point.set(-p[0],  p[1],  p[2]);
        trans.apply(point);
        points[6] = (float) point.x();
        points[7] = (float) point.y();
        points[8] = (float) point.z();
        point.set(-p[0], -p[1],  p[2]);
        trans.apply(point);
        points[9] = (float) point.x();
        points[10] = (float) point.y();
        points[11] = (float) point.z();
        
        point.set(p[0], -p[1], -p[2]);
        trans.apply(point);
        points[12] = (float) point.x();
        points[13] = (float) point.y();
        points[14] = (float) point.z();
        point.set(p[0],  p[1], -p[2]);
        trans.apply(point);
        points[15] = (float) point.x();
        points[16] = (float) point.y();
        points[17] = (float) point.z();
        point.set(p[0],  p[1],  p[2]);
        trans.apply(point);
        points[18] = (float) point.x();
        points[19] = (float) point.y();
        points[20] = (float) point.z();
        point.set(p[0], -p[1],  p[2]);
        trans.apply(point);
        points[21] = (float) point.x();
        points[22] = (float) point.y();
        points[23] = (float) point.z();
        /*
        float[] points = new float[]{
            (float) -p[0], (float) -p[1], (float) -p[2],
            (float) -p[0], (float)  p[1], (float) -p[2],
            (float) -p[0], (float)  p[1], (float)  p[2],
            (float) -p[0], (float) -p[1], (float)  p[2],
            
            (float)  p[0], (float) -p[1], (float) -p[2],
            (float)  p[0], (float)  p[1], (float) -p[2],
            (float)  p[0], (float)  p[1], (float)  p[2],
            (float)  p[0], (float) -p[1], (float)  p[2]                     
        };*/
        
        int[]  faces = new int[]{
            0,0,  3,0, 2,0,
            2,0,  1,0, 0,0,
            4,0,  5,0, 6,0,
            6,0,  7,0, 4,0,
            1,0,  2,0, 6,0,
            6,0,  5,0, 1,0,
            0,0,  1,0, 5,0,
            5,0,  4,0, 0,0,
            0,0,  4,0, 7,0,
            7,0,  3,0, 0,0,
            2,0,  3,0, 7,0,
            7,0,  6,0, 2,0                                  
        };
        
        TriangleMesh boxMesh = new TriangleMesh();
        System.out.println("CREATING MESH");
        boxMesh.getTexCoords().addAll(0, 0);
        boxMesh.getPoints().addAll(points);
        boxMesh.getFaces().addAll(faces);
        return new MeshView(boxMesh);
    }
    public static void main(String[] args){
        Geant4Basic  box = new Geant4Basic("box_1","box",20,20,120);
        MeshView view = Geant4Mesh.makeMesh(box);
    }
}
