/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.evio;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class EvioEventViewer extends Application {
    EvioSource reader = new EvioSource();
    Stage mainStage = null;
    Label progressLabel = null;
    Integer currentEvent = 0;
    EvioDataEvent event  = null;
    @Override
    public void start(Stage stage) throws Exception {
        /*
        Group group = new Group();
        Button button = new Button("OpenFile");
        
        //button.setOnAction((event) -> { showDisplay(); });
        
        
        Button bNext = new Button("Next >");
        Button bPrev = new Button("< Previous");
        Button bShow = new Button("Show");
        
        
        button.setOnAction((buttonevent) -> { openFile(); });
        bNext.setOnAction((buttonevent) -> { actionNext(); });
        bShow.setOnAction((buttonevent) -> { showDisplay(); });
        //button.setOnAction((buttonevent) -> { openFile(); });
        HBox  vbox = new HBox();
        progressLabel = new Label("0");
        
        vbox.getChildren().add(button);
        vbox.getChildren().add(bPrev);
        vbox.getChildren().add(bNext);
        vbox.getChildren().add(bShow);
        vbox.getChildren().add(progressLabel);
        
        group.getChildren().add(vbox);
        Scene scene = new Scene(group,600,80);
        
        
        //root.widthProperty().bind(scene.);
        */
        EvioEventViewStage viewer = new EvioEventViewStage();
        stage = viewer.primaryStage;
        stage.setTitle("My JavaFX Application");
        //stage.setScene(scene);
        stage.show();
        //this.mainStage = stage;
    }
    
    
    public void openFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Evio Files", "*.evio"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            this.reader.close();
            this.reader.open(selectedFile);
            currentEvent = 1;
            this.progressLabel.setText(currentEvent.toString());
            this.event = (EvioDataEvent) reader.getNextEvent();
            //mainStage.display(selectedFile);
        }
    }
    
    public void actionNext(){
        if(reader.hasEvent()==true){
            this.event = (EvioDataEvent) reader.getNextEvent();
            this.currentEvent++;
            this.progressLabel.setText(this.currentEvent.toString());
        }
    }
    
    public void showDisplay(){
        EvioEventViewStage  viewer = new EvioEventViewStage();
        viewer.display();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
