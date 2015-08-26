/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.detector;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.component.ScintillatorPaddle;
import org.jlab.geom.detector.ftof.FTOFDetector;
import org.jlab.geom.detector.ftof.FTOFFactory;

/**
 *
 * @author gavalian
 */
public class DetectorMeshLoader {
    FTOFDetector  detector = null;
    public DetectorMeshLoader(){
        ConstantProvider      cp = DataBaseLoader.getConstantsFTOF();
            FTOFFactory      factory = new FTOFFactory();
            detector = factory.createDetectorCLAS(cp);
    }
    
    
    public TriangleMesh getMesh(int sector, int layer, int component){
        ScintillatorPaddle  paddle = (ScintillatorPaddle) this.detector.getSector(sector).
                    getSuperlayer(layer).getLayer(0).getComponent(component);
        float[] points = new float[24];
        for(int loop = 0; loop < 8; loop++){
            points[0+loop*3] = (float) (paddle.getVolumePoint(loop).x());
            points[1+loop*3] = (float) (paddle.getVolumePoint(loop).y());
            points[2+loop*3] = (float) (paddle.getVolumePoint(loop).z());
        }
        PhongMaterial matBlue = new PhongMaterial();
        matBlue.setSpecularColor(Color.rgb(70, 70, 130, 0.3));
        matBlue.setDiffuseColor(Color.rgb(50, 50, 150, 0.3));
        
        TriangleMesh mesh = new TriangleMesh();
        
        mesh.getTexCoords().addAll(0,0);
        mesh.getPoints().addAll(points);
        
        int[] faces = new int[]{
            0,0,   1,0,  2,0,
            0,0,   2,0,  3,0,
            
            0,0,   4,0,  5,0,
            0,0,   5,0,  1,0,
            
            0,0,   4,0,  7,0,
            0,0,   7,0,  3,0,
            
            3,0,   7,0,  6,0,
            3,0,   6,0,  2,0,
            
            2,0,   6,0,  5,0,
            2,0,   5,0,  1,0,
            
            4,0,   7,0,  6,0,
            4,0,   6,0,  5,0
        };
        /*
        for(float p : points){
            System.out.println(p);
        }*/
        
        mesh.getFaces().addAll(faces);
        
        
        //mesh.setDrawMode(DrawMode.FILL);
        /*
        TriangleMesh  mesh2 = new TriangleMesh();
        mesh2.getTexCoords().addAll(0,0);
        float h = 150;                    // Height
        float s = 300;                    // Side
        mesh2.getPoints().addAll(
                0,    0,    0,            // Point 0 - Top
                0,    h,    -s/2,         // Point 1 - Front
                -s/2, h,    0,            // Point 2 - Left
                s/2,  h,    0,            // Point 3 - Back
                0,    h,    s/2           // Point 4 - Right
        );
        mesh2.getFaces().addAll(
                0,0,  2,0,  1,0,          // Front left face
                0,0,  1,0,  3,0,          // Front right face
                0,0,  3,0,  4,0,          // Back right face
                0,0,  4,0,  2,0,          // Back left face
                4,0,  1,0,  2,0,          // Bottom rear face
                4,0,  3,0,  1,0           // Bottom front face
        ); 
        */
        return mesh;
    }
}
