/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clas12.fx.ui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author gavalian
 */
public class ParticlePane {
    //Pane  scenePane = new Pane();
    
    GridPane  scenePane = null;
    
    public ParticlePane(){
        
        this.scenePane = new GridPane();
        scenePane.setHgap(10);
        scenePane.setVgap(10);
        scenePane.setPadding(new Insets(0, 10, 0, 10));
        
        Text category = new Text("Particle ID:");
        //category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scenePane.add(category, 1, 0); 
        ComboBox  pidBox = new ComboBox();
        pidBox.getItems().addAll("11","22","2212","211","321","-321","-211","-11");
        pidBox.getSelectionModel().select(0);
        //this.scenePane.getChildren().add(new Button("Process"));
        scenePane.add(pidBox, 2, 0);
        
        Text txtMomenta = new Text("Momenta:");
        //category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scenePane.add(txtMomenta, 1, 1); 
        
        Spinner<Double> spinner = new Spinner<Double>(0.0,10.0,1.5,0.1);
        //spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
        //0.0,20.0));
        spinner.setEditable(true);
        
        //spinner.getValueFactory().setValue(1.5);

        spinner.setMaxWidth(100);
        scenePane.add(spinner, 2, 1);
    }
    
    public Parent getPane(){
        return this.scenePane;
    }
}
