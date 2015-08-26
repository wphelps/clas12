/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.physics;

import java.util.List;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.clasfx.jar.JarClassLoader;
import org.jlab.clasrec.rec.CLASReconstruction;

/**
 *
 * @author gavalian
 */
public class ReconstructionPane extends VBox {
    
    GridPane  gridPane = null;
    ListView<String>   availablePlugins = new ListView<String>();
    ListView<String>   chosenPlugins    = new ListView<String>();
    
    BorderPane  processing = new BorderPane();
    
    public ReconstructionPane(){
        //gridPane = new GridPane();
        //this.initPlugins();
        //this.setPrefSize(600, 600);
        /*
        ListView<String> list = new ListView<String>();
        ObservableList<String> data = FXCollections.observableArrayList(
            "chocolate", "salmon", "gold", "coral", "darkorchid",
            "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
            "blueviolet", "brown");
        list.setItems(data);
        */
        //this.getChildren().add(gridPane);
        //this.getChildren().add(processing);
        //list.setPrefSize(600, 600);
        //this.getChildren().add(list);
        /*processing.setStyle("-fx-border-width: 1;\n"
                + "-fx-border-color: #0000CC;\n"
                + "-fx-border-insets: 5;"
                + "-fx-border-style: dashed;\n");
        */
        //VBox.setVgrow(list, Priority.ALWAYS);
        this.initPlugins();
        HBox hbox = new HBox();
        Button run = new Button("Run");
        run.setOnAction(event -> actionRunReconstruction());
        hbox.getChildren().add(run);
        this.getChildren().add(hbox);
    }
    
    
    
    public void initPlugins(){
        
        JarClassLoader  loader = new JarClassLoader();
        loader.scanDir("lib/plugins", "org.jlab.clasrec.main.DetectorReconstruction");
        List<String>  recClasses = loader.getList();
        
        ObservableList<String> data = FXCollections.observableArrayList(recClasses);
        this.availablePlugins.setItems(data);
        
        VBox buttonBox = new VBox();
        Button  leftButton  = new Button(">");
        
        leftButton.setOnAction(event -> actionMoveItem());
        Button  rightButton = new Button("<");
        rightButton.setOnAction(event -> actionRemoveItem());
        buttonBox.setPadding(new Insets(15,15,15,15));
        buttonBox.getChildren().add(leftButton);
        buttonBox.getChildren().add(rightButton);
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15,15,15,15));
        
        hbox.getChildren().add(this.availablePlugins);
        hbox.getChildren().add(buttonBox);
        hbox.getChildren().add(this.chosenPlugins);
        this.getChildren().add(hbox);
        HBox.setHgrow(this.availablePlugins, Priority.ALWAYS);
        HBox.setHgrow(this.chosenPlugins, Priority.ALWAYS);         

        VBox.setVgrow(hbox, Priority.ALWAYS);
        //for(String name : recClasses){
        //System.out.println(" RECONSTRUCTION PLUGIN : " + name);
        //}
        /*
        //GridPane grid = new GridPane();
        //grid.gridLinesVisibleProperty().setValue(true);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        //grid.setPadding();
        gridPane.setPadding(new Insets(10));
        
        for(int loop = 0; loop < recClasses.size(); loop++){
            int x = loop%2;
            int y = loop/2;
            //gridPane.add(new Label(recClasses.get(loop)), x, y);
            Integer lbl = loop;
            gridPane.add(new Button(lbl.toString()), x, y);
        }
        
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraints);
        
        
        gridPane.prefWidthProperty().bind(this.widthProperty());
        gridPane.prefHeightProperty().bind(this.heightProperty());
        */
    }
    
    public void actionRemoveItem(){
        String item = this.chosenPlugins.getSelectionModel().selectedItemProperty().getValue();
        this.chosenPlugins.getItems().remove(item);
    }
    
    public void actionMoveItem(){
        String item = this.availablePlugins.getSelectionModel().selectedItemProperty().getValue();
        System.out.println(" SELECTED ITEM = " + item);
        this.chosenPlugins.getItems().add(item);
    }
    
    public void actionRunReconstruction(){
        System.out.println("RUNNING");
        CLASReconstruction  rec = new CLASReconstruction();
        for(String item : this.chosenPlugins.getItems()){
            rec.addDetectorToFactory(item);
        }
        
        rec.initDetectorModules();
        rec.run("input.evio", "output.evio");
        //rec.run(, /);
    }
}
