/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.evio;

import java.io.File;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clasfx.root.DataFitterScene;
import org.clasfx.root.TEmbeddedPad;
import org.jlab.clas.detector.DetectorRawData;
import org.jlab.clas12.raw.EvioTreeBranch;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;
import org.jlab.evio.decode.EvioEventDecoder;
import org.root.func.F1D;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class EvioEventViewStage {
    
    Stage primaryStage = null;
    TreeView<String> treeView = null;
    TEmbeddedPad     pad = null;
    EvioSource       reader = new EvioSource();
    EvioDataEvent    dataEvent = null;
    EvioEventDecoder decoder = new EvioEventDecoder();
    ArrayList<DetectorRawData> raw = null;
    Integer currentEvent  = 0;
    H1D    currentHistogram = null;
    
    public EvioEventViewStage(){
                
        //this.dataEvent = event;
        VBox      vbox = new VBox();
        HBox buttonPane = new HBox();
        buttonPane.setPadding(new Insets(5, 5, 5, 5));
        buttonPane.setSpacing(10);
        //buttonPane.setStyle("-fx-background-color: #999999;");
        buttonPane.setStyle("-fx-background-color: #33669A;");
        
        Button bOpen = new Button("Open File");
        Button bPrev = new Button("Previous");
        Button bNext = new Button("Next");
        
        Button bFit = new Button("Fit");
        
        bOpen.setOnAction((buttonevent) -> { actionOpen(); });
        bNext.setOnAction((buttonevent) -> { actionNext(); });
        bFit.setOnAction((buttonevent) -> { actionFit(); });
        
        buttonPane.getChildren().add(bOpen);
        buttonPane.getChildren().add(bPrev);
        buttonPane.getChildren().add(bNext);
        buttonPane.getChildren().add(bFit);
        
        SplitPane splitPane = new SplitPane();
        Group     root      = new Group();
        
        
        //TreeItem<String>  rootNode = this.getTreeNodes();//EvioDataEventTools.getRawDataTree(event);
        TreeItem<String> rootNode = new TreeItem<String>("Event");
        splitPane.setDividerPositions(0.3);
        BorderPane  pane = new BorderPane();
        
        treeView = new TreeView<String>(rootNode);
        
        //raw = this.decoder.getDataEntries(this.dataEvent);
        
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if(mouseEvent.getClickCount() == 2)
                {
                    TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
                    System.out.println("Selected Text : " + item.getValue()); 
                    
                    drawBank(item.getValue());
                    // Create New Tab
                }
            }
        });
                
        pad = new TEmbeddedPad();
        
        pane.getChildren().add(pad);
        pad.widthProperty().bind(pane.widthProperty());
        pad.heightProperty().bind(pane.heightProperty());
        splitPane.getItems().add(treeView);
        splitPane.getItems().add(pane);
        
        root.getChildren().add(splitPane);
        
        vbox.getChildren().add(buttonPane);
        vbox.getChildren().add(splitPane);
        
        
        Scene scene = new Scene(vbox,1000,800);
        
        splitPane.prefWidthProperty().bind(scene.widthProperty());
        splitPane.prefHeightProperty().bind(scene.heightProperty());
        
        this.primaryStage = new Stage();
        this.primaryStage.setScene(scene);
        this.primaryStage.initStyle(StageStyle.DECORATED);
        this.primaryStage.initModality(Modality.APPLICATION_MODAL);
        //primary
    }
    
    public void actionFit(){
        F1D f1 = new F1D("landau+p1",
                        this.currentHistogram.getXaxis().min(),
                        this.currentHistogram.getXaxis().max());
        f1.setParameter(0, 100.0);
        f1.setParameter(1, (f1.getMax()-f1.getMin())/2.0);
        f1.setParameter(2, 1.5);
        DataFitterScene stage = new DataFitterScene(this.currentHistogram,
                f1);
        stage.showStage();
    }
    
    public void actionOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Evio Files", "*.evio"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if (selectedFile != null) {
            this.openFile(selectedFile.getAbsolutePath());
            //mainStage.display(selectedFile);
        }
    }
    
    public void actionNext(){
        if(this.reader.hasEvent()==true){
            this.dataEvent = (EvioDataEvent) this.reader.getNextEvent();
            this.currentEvent++;
            this.updateTreeNodes();
        }
    }
    
    public void openFile(String filename){
        reader.close();
        reader.open(filename);
        this.currentEvent = 1;
        this.dataEvent = (EvioDataEvent) reader.getNextEvent();
        this.updateTreeNodes();
    }
    
    public void updateTreeNodes(){
        TreeItem<String> root = new TreeItem<String>("Event");
        root.setExpanded(true);
        this.raw = new ArrayList<DetectorRawData> ();
        ArrayList<EvioTreeBranch> branches = decoder.getEventBranches(this.dataEvent);
        System.out.println("UPDATING TREE NODES");
        for(EvioTreeBranch branch : branches){
            System.out.println(" Adding branch " + branch.getTag());
            ArrayList<DetectorRawData> r = decoder.getDataEntries(dataEvent, branch.getTag());
            if(r!=null)
                raw.addAll(r);
        }
        
        for(DetectorRawData data : raw){
            TreeItem<String> item = new TreeItem(String.format("%3s/%3s/%3s", 
                    data.getDescriptor().getCrate(),
                    data.getDescriptor().getSlot(),
                    data.getDescriptor().getChannel())

            );
            item.setExpanded(true);
            root.getChildren().add(item);
        }
        this.treeView.rootProperty().set(root);
    }
    
    public TreeItem<String>  getTreeNodes(){

        TreeItem<String> root = new TreeItem<String>("Event");
        this.raw = new ArrayList<DetectorRawData> ();
        ArrayList<EvioTreeBranch> branches = decoder.getEventBranches(this.dataEvent);
        for(EvioTreeBranch branch : branches){
            ArrayList<DetectorRawData> r = decoder.getDataEntries(dataEvent, branch.getTag());
            if(r!=null)
                raw.addAll(r);
        }
        
        for(DetectorRawData data : raw){
            TreeItem<String> item = new TreeItem(String.format("%3s/%3s/%3s", 
                    data.getDescriptor().getCrate(),
                    data.getDescriptor().getSlot(),
                    data.getDescriptor().getChannel())
            );
            root.getChildren().add(item);
        }
        return root;
    }
    
    public void drawBank(String bank){
        String[] tokens = bank.split("/");
        if(tokens.length<3) return;
        Integer crate = Integer.parseInt(tokens[0].trim());
        Integer slot  = Integer.parseInt(tokens[1].trim());
        Integer chan  = Integer.parseInt(tokens[2].trim());
        System.out.println(" LOOKING FOR " + crate + " " + slot + " " + chan);
        for(int loop = 0; loop < this.raw.size();loop++){
            if(
                    this.raw.get(loop).getDescriptor().getCrate()==crate&&
                    this.raw.get(loop).getDescriptor().getSlot()==slot&&
                    this.raw.get(loop).getDescriptor().getChannel()==chan
                    ){
                if(raw.get(loop).getMode()==1){
                    short[] pulse = (short[]) raw.get(loop).getDataObject(0);
                    H1D h1 = new H1D("H",pulse.length,0,pulse.length);
                    for(int bin = 0; bin < pulse.length; bin++){
                        h1.setBinContent(bin, pulse[bin]);
                    }
                    this.pad.clear();
                    this.pad.addDataSet(h1);
                    this.pad.redraw();
                    this.currentHistogram = h1;
                    return;
                }
            }
        }
    }
    
    public void display(){
        this.primaryStage.showAndWait();
    }
}
