/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.clasfx.tools.DetectorCanvas;
import org.clasfx.tools.DetectorTab;
import org.clasfx.root.TEmbeddedCanvas;

/**
 *
 * @author gavalian
 */

public class CLASApp extends Application {

    public void start(Stage stage) {
        //Circle circ = new Circle(40, 40, 30);
        //Group root = new Group(circ);
       /*
       DetectorCanvas canvas = new DetectorCanvas("EC",300,300);
       Group root = new Group(canvas);
       canvas.redraw();
*/
       DetectorTab root = new DetectorTab(600,600);
       root.addDetectorCanvas("DC");
       root.addDetectorCanvas("FTOF");
       //VBox box = new VBox(root);       
       
       DetectorCanvas detcanvas = new DetectorCanvas("DC",300,300);
       BorderPane borderPane = new BorderPane();
    
       borderPane.setCenter(root.getTabPane());
       
       //root.prefHeight(300);
       //root.prefWidth(400);
       /*HBox hbox = new HBox();
       hbox.getChildren().add(borderPane);
       */
       TEmbeddedCanvas embcanvas = new TEmbeddedCanvas(500,500,2,3);
       //hbox.getChildren().add(embcanvas);
       //embcanvas.redraw();
       //embcanvas.widthProperty().bind(hbox.widthProperty().multiply(0.5));
       //embcanvas.heightProperty().bind(hbox.heightProperty());
       //TEmbeddedPad embpad = new TEmbeddedPad();
       //SplitPane  splitpane = new SplitPane();
       //Group rootG = new Group(embcanvas);
       HBox hbox = new HBox();
       hbox.getChildren().add(detcanvas);
       HBox.setHgrow(root.getTabPane(), Priority.ALWAYS);
       hbox.getChildren().add(embcanvas);
       Scene scene = new Scene(hbox, 800, 700);
       detcanvas.widthProperty().bind(scene.widthProperty().multiply(0.5));
       detcanvas.heightProperty().bind(scene.heightProperty());
       root.getTabPane().prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
       embcanvas.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
       embcanvas.prefHeightProperty().bind(scene.heightProperty());
       //embpad.widthProperty().bind(scene.widthProperty());
       //embpad.heightProperty().bind(scene.heightProperty());
       //borderPane.prefHeightProperty().bind(scene.heightProperty());
       //borderPane.prefWidthProperty().bind(scene.widthProperty());

       /*
       canvas.widthProperty().bind(scene.widthProperty());
       canvas.heightProperty().bind(scene.heightProperty());
               */
       stage.setTitle("My JavaFX Application");
       stage.setScene(scene);
       stage.show();
   }
   
   public static void main(String[] args){
       launch(args);
   }
}
