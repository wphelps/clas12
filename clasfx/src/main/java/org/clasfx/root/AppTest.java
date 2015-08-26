/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.clasfx.detector.DetectorView3D;
import org.clasfx.jar.JarPluginLoaderDialog;
import org.root.func.F1D;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class AppTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        /*
        Group group = new Group();
        Button button = new Button("Run");
        
        button.setOnAction((event) -> { show(); });
        
        group.getChildren().add(button);
        
        Scene scene = new Scene(group,600,600);
        
        
        //root.widthProperty().bind(scene.);
        */
        //stage.setScene(scene);
        /*
        JarPluginLoaderDialog loader = new JarPluginLoaderDialog("org.jlab.clasrec.main.DetectorReconstruction");
        stage = loader.getStage();*/
        DetectorView3D viewer = new DetectorView3D();
        stage = viewer.getStage();
        stage.setTitle("My JavaFX Application");
        //stage.setScene(scene);
        stage.show();
    }
    
    public void show(){
        //System.out.println("Button Clicked");
        DataFitterScene stage = new DataFitterScene(new H1D("h1",100,0.0,1.4),
                new F1D("gaus+p1",0.0,14.0));
        stage.showStage();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
