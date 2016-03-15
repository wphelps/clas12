/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.viewer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.detector.geant4.FTOFGeant4Factory;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.geant.Geant4Basic;
import org.jlab.geom.geant.Geant4Mesh;

/**
 *
 * @author gavalian
 */
public class CLAS12GeometryViewer extends Application {
    
    
    
    ContentModel  content      = null;
    TreeView<String>  treeView = null;    
    Map<String,MeshStore>  meshStores = new TreeMap<String,MeshStore>();
    
    
    Group root = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        SplitPane  splitPane = new SplitPane();
        StackPane  treePane  = new StackPane();
        
        root = new Group();
        BorderPane pane=new BorderPane();
                
        treeView = new TreeView<String>();
        
        treePane.getChildren().add(treeView);
        
        this.content = new ContentModel(800,800,200);
        this.content.setContent(root);
        
        
        content.getSubScene().heightProperty().bind(pane.heightProperty());
        content.getSubScene().widthProperty().bind(pane.widthProperty());
        
        pane.setCenter(content.getSubScene());
        
        splitPane.getItems().addAll(treePane,pane);
        splitPane.setDividerPositions(0.2);
        this.addDetector("FTOF");
        //final Scene scene = new Scene(pane, 880, 880, true);
        final Scene scene = new Scene(splitPane, 1280, 880, true);
        
        scene.setFill(Color.ALICEBLUE);
        stage.setTitle("CLAS12 Geometry Viewer - JavaFX3D");
        stage.setScene(scene);
        stage.show();
    }
    
    
    public void addDetector(String name){
        
        TreeItem<String>  clasROOT = new TreeItem<String>("CLAS12");
        
        this.treeView.setEditable(true);
        this.treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        for(int sector = 1; sector <=6 ; sector++){
            MeshStore  store = GeometryLoader.getGeometry("FTOF",sector,1);
            store.setMaterial(0.6, 0.2, 0.2, 0.4);
            this.meshStores.put(store.getName(), store);
            //treeView = new TreeView<String>();
            //this.treeView.setRoot(store.getMeshTree());
            clasROOT.getChildren().add(store.getMeshTree());
            for(Map.Entry<String,MeshView> entry : store.getMap().entrySet()){
                root.getChildren().add(entry.getValue());
            }
        }
        
        for(int sector = 1; sector <=6 ; sector++){
            MeshStore  store = GeometryLoader.getGeometry("FTOF",sector,2);
            store.setMaterial(0.1, 0.1, 0.4, 0.4);
            this.meshStores.put(store.getName(), store);
            //treeView = new TreeView<String>();
            clasROOT.getChildren().add(store.getMeshTree());
            for(Map.Entry<String,MeshView> entry : store.getMap().entrySet()){
                root.getChildren().add(entry.getValue());
            }
        }
        
        for(int sector = 1; sector <=6 ; sector++){
            MeshStore  store = GeometryLoader.getGeometry("FTOF",sector,3);
            //store.setVisible(false);
                    
            store.setMaterial(0.1, 0.4, 0.1, 0.4);
            this.meshStores.put(store.getName(), store);
            //treeView = new TreeView<String>();
            clasROOT.getChildren().add(store.getMeshTree());
            for(Map.Entry<String,MeshView> entry : store.getMap().entrySet()){
                root.getChildren().add(entry.getValue());
            }
        }
        
        this.treeView.setRoot(clasROOT);
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
