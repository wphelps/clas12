/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.detector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class DetectorViewApplication extends Application  {

    @Override
    public void start(Stage stage) throws Exception {
        
        DetectorView3D viewer = new DetectorView3D();
        BorderPane  pane = new BorderPane();
        pane.setCenter(viewer.getSubScene());
        
        viewer.getSubScene().widthProperty().bind(pane.widthProperty());
        viewer.getSubScene().heightProperty().bind(pane.heightProperty());
        Scene scene = new Scene(pane,500,500);
        pane.prefWidthProperty().bind(scene.widthProperty());
        pane.prefHeightProperty().bind(scene.heightProperty());
        
        DetectorMeshLoader  loader = new DetectorMeshLoader();
        int[] layerpads = new int[]{23,60,5};
        PhongMaterial[]  material = new PhongMaterial[3];
        
        material[0] = new PhongMaterial();
        material[0].setSpecularColor(Color.rgb(70, 70, 130, 0.5));
        material[0].setDiffuseColor(Color.rgb(50, 50, 150, 0.5));
        
        material[1] = new PhongMaterial();
        material[1].setSpecularColor(Color.rgb(130, 70, 70, 0.5));
        material[1].setDiffuseColor(Color.rgb(150, 50, 50, 0.5));
        
        material[2] = new PhongMaterial();
        material[2].setSpecularColor(Color.rgb(70, 130, 70, 0.5));
        material[2].setDiffuseColor(Color.rgb(50, 150, 50, 0.5));
        
        for(int layer = 0; layer < 3; layer++)
            for(int sector = 0; sector < 6; sector++)
                for(int loop = 0; loop < layerpads[layer]; loop++ ){
                    TriangleMesh   mesh = loader.getMesh(sector, layer, loop);
                    //System.out.println(mesh);
                    viewer.addMesh(mesh, material[layer]);
                }
        
        //TriangleMesh   mesh = loader.getMesh(0,0,20);
                    //System.out.println(mesh);
        //viewer.addMesh(mesh, material[0]);
        
        //viewer.initMesh();
        stage.setScene(scene);
        stage.setTitle("My JavaFX Application");
        //stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
