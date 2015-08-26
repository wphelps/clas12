/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class TCanvasStage {
    
    Stage  stage = null;
    TEmbeddedCanvas  canvasCanvas = null;

    public TCanvasStage(){
        BorderPane  pane = new BorderPane();
        
        canvasCanvas = new TEmbeddedCanvas(800,400,2,2);
        
        Scene scene = new Scene(canvasCanvas,800,600);
        canvasCanvas.prefWidthProperty().bind(scene.widthProperty());
        canvasCanvas.prefHeightProperty().bind(scene.heightProperty());
        stage.setScene(scene);
        stage.setTitle("My JavaFX Application");
        stage.setScene(scene);
    }
    
    public Stage getStage(){
        return this.stage;
    }
}
