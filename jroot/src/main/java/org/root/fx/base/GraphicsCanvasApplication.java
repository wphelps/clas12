/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fx.base;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class GraphicsCanvasApplication extends Application {
    
    private static GraphicsCanvasApplication appCanvas = null;
    
     @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane  root = new BorderPane();
        //GridPane    grid = new GridPane();
        //GraphicsDataPad  canvas = new GraphicsDataPad();
        //canvas.graphicsFrame.setScaleX(0.0, 1.5);
        //canvas.graphicsFrame.setScaleY(0.2, 1.2);
        //canvas.getGraphicsFrame().setAxisZoom(false);
        FXEmbeddedCanvas canvas = new FXEmbeddedCanvas(3,3);
        root.setCenter(canvas);
        //canvas.widthProperty().bind();
        //FXEmbeddedCanvas  emb = new FXEmbeddedCanvas();        
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        //emb.getPane().setPrefHeight(600);
        //emb.getPane().setPrefWidth(600);
        //root.setCenter(emb.getPane());
        Scene scene = new Scene(root, 500, 350, Color.rgb(255,255,255));
        
        primaryStage.setScene(scene);
        primaryStage.show();
        appCanvas = this;
    }
    
    public static GraphicsCanvasApplication getInstance(){
        return appCanvas;
    }
    
    public static void main(String[] args){
        launch();
    }
}
