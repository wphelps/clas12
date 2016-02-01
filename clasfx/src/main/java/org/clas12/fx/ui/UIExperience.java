/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clas12.fx.ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class UIExperience extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        // Tests On Particle Pane
        ParticlePane pane = new ParticlePane();
        root.getChildren().add(pane.getPane());
        //--------------------------------
        Scene scene = new Scene(root, 800, 700);
        stage.setTitle("My JavaFX Application");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args){
       launch(args);
   }
}
