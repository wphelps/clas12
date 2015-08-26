/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.jar;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class JarPluginLoaderDialog {
    
    private Stage  primaryStage = null;
    private String className    = "";
    private JarClassLoader   loader = new JarClassLoader();
    ObservableList<String>  tableData = null;
    TreeView<String>  treeView = null;
    String selectedPlugin      = null;
    
    public JarPluginLoaderDialog(String classname){
        this.className = classname;
        this.initClassList();
        this.initUI();
    }
    
    
    public void initClassList(){        
        this.loader.scanDir("lib/plugins", className);
        tableData = FXCollections.observableArrayList();
        tableData.add("FTOF");
        tableData.add("EC");
        tableData.add("COLS");
        tableData.add("FTOF");
        List<String> list = this.loader.getList();
        for(String item : list){
            tableData.add(item);
        }
    }
    
    public void initUI(){
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        TreeItem<String> rootNode = new TreeItem<String>("Plugins");
        rootNode.setExpanded(true);
        List<String>  list = this.loader.getList();
        for(String item : list){
            rootNode.getChildren().add(new TreeItem<String>(item));
            
        }
        treeView = new TreeView<String>(rootNode);
        /*
        TableView table = new TableView();
        TableColumn colPackage = new TableColumn("Package");
        TableColumn colClass   = new TableColumn("Class");
        table.getColumns().add(colClass);
        table.setEditable(true);
        table.setItems(tableData);
        
        BorderPane pane = new BorderPane();
        */
        //pane.getChildren().add(table);
        //BorderPane  buttonPane = new BorderPane();
        HBox  hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(5));
        hbox.setSpacing(5);
        
        Button buttonLoad = new Button(" Load ");
        buttonLoad.setOnAction((event) -> { actionLoad(); });
        hbox.getChildren().add(buttonLoad);
        
        vbox.getChildren().add(treeView);
        vbox.getChildren().add(hbox);
        
        VBox.setVgrow(treeView, Priority.ALWAYS);
        Scene scene = new Scene(vbox,400,400);
        /*
        pane.prefWidthProperty().bind(scene.widthProperty());
        pane.prefHeightProperty().bind(scene.heightProperty());
        */
        /*
        table.prefWidthProperty().bind(pane.widthProperty());
        table.prefHeightProperty().bind(pane.heightProperty());*/
        primaryStage = new Stage();
        primaryStage.setScene(scene);        
    }
    
    public String getSelectedPlugin(){
        return this.selectedPlugin;
    }
    
    public void actionLoad(){
        TreeItem<String> item = this.treeView.getSelectionModel().getSelectedItem();
        if(item!=null){
            this.selectedPlugin = item.getValue();
            if(this.selectedPlugin!=null){
                System.out.println(" SELECTED PLUGIN = " + this.selectedPlugin);
                this.primaryStage.close();
            }
        }
    }
    
    public Stage getStage(){ return this.primaryStage;}
}
