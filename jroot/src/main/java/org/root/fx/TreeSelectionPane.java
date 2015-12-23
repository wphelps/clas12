/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fx;


import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
/**
 *
 * @author gavalian
 */
public class TreeSelectionPane extends TitledPane {
    
    String[]  choices   = new String[]{"px","py","pz"};
    ComboBox  choiceBox = null;
    
    public TreeSelectionPane(String variable){
        this.setText(variable);
        GridPane  grid = new GridPane(); 
        grid.setVgap(4);
        grid.setHgap(15);
        grid.setPadding(new Insets(5, 5, 5, 5));
        choiceBox = new ComboBox();
        choiceBox.getItems().addAll(choices);
        grid.add(new Label("Variable X"), 1, 1);
        grid.add(choiceBox,2,1);
        this.setContent(grid);
    }
    
}
