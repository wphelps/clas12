/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fx;


import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class HDialog extends Dialog<H1D>{
    Group group = new Group();
    public HDialog(){
        
        GridPane grid = new GridPane();
        grid.add(new Label("abomination"), 1, 1);
        grid.add(new Label("b"), 1, 2);
        grid.add(new Button("Stop"), 2, 1);
        grid.add(new Label("d"), 2, 2);
        group.getChildren().add(grid);
        this.getDialogPane().setContent(group);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);
        
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(buttonTypeCancel);

    }
}
