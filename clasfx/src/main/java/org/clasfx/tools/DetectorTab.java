/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.tools;

import java.util.List;
import java.util.TreeMap;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.clasfx.detector.DetectorShape3D;

/**
 *
 * @author gavalian
 */
public class DetectorTab {
    TabPane  tabPane = null;
    TreeMap<String,DetectorCanvas>  tabCanvases = new TreeMap<String,DetectorCanvas>();
    
    public DetectorTab(int width, int height){
        tabPane = new TabPane();
        //tabPane.setMinSize(width, height);
        //tabPane.setMaxSize(width,height);    
    }
    
    public void addDetectorCanvas(String name){
        
        DetectorCanvas canvas = new DetectorCanvas(name,300,300);
        tabCanvases.put(name, canvas);
        Tab tab = new Tab();
        BorderPane pane = new BorderPane();
        tab.setClosable(false);
        tab.setText(name);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        //canvas.widthProperty().bind(tabPane.widthProperty());
        //canvas.heightProperty().bind(tabPane.heightProperty());
        pane.getChildren().add(canvas);
        tab.setContent(pane);
        tabPane.getTabs().add(tab);
    }
    
    public void addShapes(String name,List<DetectorShape3D> shapes){
        for(DetectorShape3D shape : shapes){
            this.tabCanvases.get(name).addShape(shape);
            this.tabCanvases.get(name).redraw();
        }
    }
    
    public TabPane getTabPane(){return this.tabPane;}
}
