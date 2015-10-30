/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.detector;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import org.jlab.geom.geant.G4Trd;

/**
 *
 * @author gavalian
 */
public class GeantMeshView extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Group  root = new Group();
        

        
        PhongMaterial material = new PhongMaterial();
        material.setSpecularColor(Color.rgb(70, 70, 130, 0.5));
        material.setDiffuseColor(Color.rgb(50, 50, 150, 0.5));
        
        TriangleMesh  mesh = new TriangleMesh();
        G4Trd         trap = new G4Trd("pcal",30,30,10,10,60);
        
        mesh.getTexCoords().addAll(0,0);
        /*
        float h = 150;                    // Height
        float s = 300;                    // Side
        mesh.getPoints().addAll(
                0,    0,    0,            // Point 0 - Top
                0,    h,    -s/2,         // Point 1 - Front
                -s/2, h,    0,            // Point 2 - Left
                s/2,  h,    0,            // Point 3 - Back
                0,    h,    s/2           // Point 4 - Right
        );
        mesh.getFaces().addAll(
                0,0,  2,0,  1,0,          // Front left face
                0,0,  1,0,  3,0,          // Front right face
                0,0,  3,0,  4,0,          // Back right face
                0,0,  4,0,  2,0,          // Back left face
                4,0,  1,0,  2,0,          // Bottom rear face
                4,0,  3,0,  1,0           // Bottom front face
        );*/ 
        float[] points = trap.getMeshPoints();
        int[]   faces  = trap.getMeshFaces();
        
        for(int loop = 0; loop < points.length; loop++){
            System.out.print("  " + points[loop]);
        }
        System.out.println();
        
        for(int loop = 0; loop < faces.length; loop++){
            System.out.print("  " + faces[loop]);
            if((loop+1)%6==0) System.out.println();
        }
        System.out.println();
        
        mesh.getPoints().addAll(trap.getMeshPoints());
        mesh.getFaces().addAll(trap.getMeshFaces());
        
        
        
        MeshView pyramid = new MeshView(mesh);
        pyramid.setDrawMode(DrawMode.FILL);
        pyramid.setMaterial(material);
        pyramid.setTranslateX(200);
        pyramid.setTranslateY(100);
        pyramid.setTranslateZ(-600);
        root.getChildren().add(pyramid);
        
        Scene scene = new Scene(root,500,500);
        stage.setScene(scene);
        stage.setTitle("My JavaFX Application");
        //stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
