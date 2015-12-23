/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class FXTests extends Application {
     public static void main(String[] args) {
        launch(args);
    }
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Accordion accord = new Accordion();
        
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                HDialog  dialog = new HDialog();
                dialog.showAndWait();
            }
        });
        for(int loop = 0; loop < 5; loop++){
            TitledPane tp = new TreeSelectionPane("PX"+loop);
            accord.getPanes().add(tp);
        }
        StackPane root = new StackPane();
        //root.getChildren().add(btn);
        root.getChildren().add(accord);
        
        //primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

    }
}
