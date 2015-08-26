/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.physics;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class EventMonitoringApp extends Application {
    BorderPane  mainPane = new BorderPane();
    List<Button>  actionButtons = new ArrayList<Button>();
    
    @Override
    public void start(Stage stage) throws Exception {
        
        HBox root = new HBox();
        
        
        FlowPane  pane = this.getControlPanel();
        root.getChildren().add(pane);
        root.getChildren().add(mainPane);
        HBox.setHgrow(mainPane, Priority.ALWAYS);
        mainPane.setPadding(new Insets(15, 20, 15, 20));
       
        mainPane.setStyle("-fx-border-width: 1;\n"
                + "-fx-border-color: #9999CC;\n"
                + "-fx-border-insets: 5;"
                + "-fx-border-style: dashed;\n");
        Scene  scene = new Scene(root,1000,600);
        stage.setTitle("APPLICATION");
        stage.setScene(scene);
        stage.show();
    }
    
    
    public FlowPane getControlPanel(){
        
        FlowPane pane = new FlowPane();
        Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));         
        Button button = new Button("", new ImageView(icon));        
        button.setStyle("-fx-background-color: #999999;");
        Image icon2 = new Image(getClass().getResourceAsStream("/images/icon2.png")); 
        Button button2 = new Button("",new ImageView(icon2));
        button2.setStyle("-fx-background-color: #999999;");
        
        button.setOnAction(event -> actionSystem());
        button2.setOnAction(event -> actionReconstruction());
        
        pane.getChildren().add(button);
        pane.getChildren().add(button2);
        pane.setStyle("-fx-background-color: #999999;");
        pane.setPadding(new Insets(5, 0, 5, 0));
        pane.setVgap(4);
        pane.setHgap(4);
        pane.setPrefWrapLength(70);
        this.actionButtons.add(button);
        this.actionButtons.add(button2);
        return pane;
    }
    
    
    public void actionSystem(){
        this.resetButtonsBackground();
        this.actionButtons.get(0).setStyle("-fx-background-color: #CCCCCC;");
        System.out.println("System command");
        this.mainPane.getChildren().clear();
        
        ListView<String> list = new ListView<String>();
        ObservableList<String> data = FXCollections.observableArrayList(
            "chocolate", "salmon", "gold", "coral", "darkorchid",
            "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
            "blueviolet", "brown");
        list.setItems(data);
        Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));
        
        
        VBox  hbox = new VBox();
        list.setPrefSize(600, 600);
        hbox.getChildren().add(new ImageView(icon));
        hbox.getChildren().add(new ImageView(icon));
        hbox.getChildren().add(list);
        //hbox.getChildren().add(new TreeView<String>());
        
        VBox.setVgrow(list, Priority.ALWAYS);
        this.mainPane.setCenter(list);
        //this.mainPane.getChildren().add(hbox);
        hbox.prefWidthProperty().bind(this.mainPane.widthProperty());
        hbox.prefHeightProperty().bind(this.mainPane.heightProperty());
        //this.mainPane.getChildren().add(list);
        
        //list.prefWidthProperty().bind(this.mainPane.widthProperty());
        //list.prefHeightProperty().bind(this.mainPane.heightProperty());
    }
    
    private void resetButtonsBackground(){
        for(Button button : this.actionButtons){
            button.setStyle("-fx-background-color: #999999;");
        }
    }
    
    public void actionReconstruction(){
        System.out.println("Run Reconstruction");
        ReconstructionPane pane = new ReconstructionPane();
        this.mainPane.getChildren().clear();
        this.mainPane.setCenter(pane);
        this.resetButtonsBackground();
        this.actionButtons.get(1).setStyle("-fx-background-color: #CCCCCC;");
        //pane.prefWidthProperty().bind(this.mainPane.widthProperty());
        //pane.prefHeightProperty().bind(this.mainPane.heightProperty());
    }
    
    public static void main(String[] args){
        launch();
    }
}
