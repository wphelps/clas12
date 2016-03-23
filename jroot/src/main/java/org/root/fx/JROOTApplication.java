/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fx;

import java.awt.Dimension;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.root.basic.EmbeddedCanvas;
import org.root.data.NTuple;

/**
 *
 * @author gavalian
 */
public class JROOTApplication extends Application {
    StackPane  canvasPane = new StackPane();
    SplitPane  mainSplitPane = null;
    SplitPane  consoleSplitPane = null;
    ToolBar    toolBar          = null;
    NTuple     ntuple           = null;
    TreeView   treeView   = new TreeView();
    Image      leafImage  = null;
    final SwingNode swingNode = new SwingNode();
    EmbeddedCanvas  embeddedCanvas = new EmbeddedCanvas(800,650);
    
    JROOTInterpreter jrootInterpreter = new JROOTInterpreter();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        this.leafImage = new Image(getClass().getResourceAsStream("/icons/tea-plant-leaf-icon.png"));
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10,10,10,10));
        
        this.mainSplitPane = new SplitPane();
                
        JROOTConsole console = new JROOTConsole();
        console.setInterpreter(jrootInterpreter);
        
        this.consoleSplitPane = new SplitPane();
        this.consoleSplitPane.setOrientation(Orientation.VERTICAL);
        this.embeddedCanvas.setPreferredSize(new Dimension(800,600));
        VBox  container = new VBox();
        this.swingNode.setContent(embeddedCanvas);
        
        container.getChildren().add(console.getPane());
        container.getChildren().add(treeView);
        
        this.mainSplitPane.setDividerPositions( 0.2f);
        this.consoleSplitPane.setDividerPositions( 0.7f);
        
        this.consoleSplitPane.getItems().addAll(treeView,console.getPane());
        swingNode.resize(800, 600);
        this.mainSplitPane.getItems().addAll(this.consoleSplitPane,this.swingNode);

        //root.setLeft(container);
        root.setCenter(this.mainSplitPane);
        this.initToolBar();
        root.setTop(toolBar);
        this.open();
        Scene scene = new Scene(root, 1100, 850);
        
        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void initToolBar(){
        this.toolBar = new ToolBar();
        ComboBox  canvasZone = new ComboBox();
        canvasZone.getItems().addAll((Object[]) new String[]{"1x1","1x2","1x3","2x2"});
                
        toolBar.getItems().addAll(
                new Separator(),
                new Button("Open"),
                new Separator(),
                canvasZone
        );
    }
    
    public void open(){
        this.ntuple = new NTuple("T",12);
        this.ntuple.getVariables();
        TreeItem<String> rootItem = new TreeItem("T");
        for(String item : this.ntuple.getVariables()){
            TreeItem leaf = new TreeItem(item,new ImageView(this.leafImage));
            rootItem.getChildren().add(leaf);
        }
        this.treeView.setRoot(rootItem);
        this.treeView.getRoot().setExpanded(true);
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
