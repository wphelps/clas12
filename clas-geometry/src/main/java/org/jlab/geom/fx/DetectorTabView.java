/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.geom.fx;


import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

/**
 *
 * @author gavalian
 */
public class DetectorTabView extends BorderPane {
    
    TabPane tabPane = new TabPane();
    
    Map<String,DetectorMesh2DCanvas>  canvases = new HashMap<String,DetectorMesh2DCanvas>();
    
    public DetectorTabView(double x, double y){
        //this.prefWidth(x);
        //this.prefHeight(y);
        this.setPadding(new Insets(10,10,10,10));
        this.setCenter(tabPane);
        this.tabPane.prefHeight(y);
        this.tabPane.prefWidth(x);
    }
    
    public void update(){
        for(Map.Entry<String,DetectorMesh2DCanvas> can : this.canvases.entrySet()){
            can.getValue().update();
        }
    }
    
    public void addView(DetectorMesh2DCanvas canvas){
        this.canvases.put(canvas.getName(), canvas);
        Tab tab1 = new Tab();
        
        tab1.setText(canvas.getName());
        tab1.setClosable(false);
        
        BorderPane  pane = new BorderPane();
        pane.setPadding(new Insets(5,5,5,5));
        pane.setTop(canvas.getToolBar());
        pane.setCenter(canvas);
        tab1.setContent(pane);
        
        //canvas.widthProperty().bind(pane.widthProperty());
        //canvas.heightProperty().bind(pane.heightProperty());
        canvas.widthProperty().bind(tabPane.widthProperty());
        canvas.heightProperty().bind(tabPane.heightProperty().multiply(0.90));
        tabPane.getTabs().add(tab1);
    }
    
}
