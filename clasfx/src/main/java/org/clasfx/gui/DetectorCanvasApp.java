/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.clasfx.calib.CalibrationService;
import org.clasfx.calib.CalibrationStore;
import org.clasfx.detector.DetectorShape3D;
import org.clasfx.jar.JarPluginLoaderDialog;
import org.clasfx.tools.DetectorCanvas;
import org.clasfx.tools.DetectorTab;
import org.clasfx.root.TEmbeddedCanvas;
import org.clasfx.root.TEmbeddedPad;
import org.jlab.clas.detector.DetectorType;
import org.jlab.clasrec.utils.CLASGeometryLoader;
import org.jlab.evio.clas12.EvioDataEvent;



/**
 *
 * @author gavalian
 */
public class DetectorCanvasApp extends Application {
    
    CalibrationStore  store = new CalibrationStore();
    Stage mainStage = null;
    CalibrationService serviceTask = new CalibrationService();
    
    @Override
    public void start(Stage stage) {
        
        VBox  vbox = new VBox();
        
        vbox.getChildren().add(this.initMenuBar());
        
        /*
        DetectorCanvas canvas = new DetectorCanvas("EC",300,300);
        Group root = new Group(canvas);
        canvas.redraw();
        */
        
        
        /*
        DetectorCanvas root = new DetectorCanvas("FTOF",600,600);
        CLASGeometryLoader loader = new CLASGeometryLoader();
        loader.loadGeometry("FTOF");
        
        ArrayList<DetectorShape3D> shapes = DetectorInitShapes.getShapesDC();
        for(DetectorShape3D shape : shapes) root.addShape(shape);
        */
        
        DetectorTab  tab = new DetectorTab(400,400);
        
        tab.addDetectorCanvas("DC");
        tab.addDetectorCanvas("FTOF 1A");    
        tab.addDetectorCanvas("FTOF 1B");    
        tab.addDetectorCanvas("FTOF 2");    
        tab.addDetectorCanvas("EC");
        
        
        tab.addDetectorCanvas("CLAS12");
        ArrayList<DetectorShape3D> shapes = DetectorInitShapes.getShapesDC();
        tab.addShapes("DC", shapes);
        
        ArrayList<DetectorShape3D> shapesFTOF_1A = DetectorInitShapes.getShapesFTOFPaddles(0);
        tab.addShapes("FTOF 1A", shapesFTOF_1A);
        
        ArrayList<DetectorShape3D> shapesFTOF_1B = DetectorInitShapes.getShapesFTOFPaddles(1);
        tab.addShapes("FTOF 1B", shapesFTOF_1B);
        
        ArrayList<DetectorShape3D> shapesFTOF_2 = DetectorInitShapes.getShapesFTOFPaddles(2);
        tab.addShapes("FTOF 2", shapesFTOF_2);
        
        ArrayList<DetectorShape3D> shapesPCAL = DetectorInitShapes.getShapesPCAL();
        tab.addShapes("EC", shapesPCAL);
        
       /* 
        ArrayList<DetectorShape3D> shapesFTOF = DetectorInitShapes.getShapesFTOF();
        for(DetectorShape3D shape : shapesFTOF) root.addShape(shape);
        
        ArrayList<DetectorShape3D> shapesPCAL = DetectorInitShapes.getShapesPCAL();
        for(DetectorShape3D shape : shapesPCAL) root.addShape(shape);
        */
        /*
        for(int lx = 0; lx<5; lx++){
            for(int ly = 0; ly < 5; ly++){
                DetectorShape3D   shape = new DetectorShape3D();        
                shape.SECTOR = 1;
                shape.LAYER  = 1;
                shape.COMPONENT = lx*ly;
                double xs = -40 + lx*10.0;
                double ys = -40 + ly*10.0;
                shape.addPathPoint(  xs,  ys);
                shape.addPathPoint(  xs,  ys+10.0);
                shape.addPathPoint(  xs+10.0, ys+10.0);
                shape.addPathPoint(  xs+10.0,  ys);
                shape.addPathPoint(  xs,  ys);
                
                root.addShape(shape);
            }
        }*/
        //root.updateShapeRegion();
        
        
        BorderPane borderPane = new BorderPane();
        
        //TEmbeddedCanvas canvas = new TEmbeddedCanvas(500,500,1,1);
        TEmbeddedCanvas canvas = new TEmbeddedCanvas(300,300,2,2);
        
        BorderPane borderPaneCanvas = new BorderPane();
        borderPaneCanvas.setCenter(canvas);
        borderPane.setCenter(tab.getTabPane());
        //root.widthProperty().bind(borderPane.widthProperty());
        //root.heightProperty().bind(borderPane.heightProperty());
        //root.prefHeight(300);
        //root.prefWidth(400);
        //canvas.prefWidthProperty().bind(borderPaneCanvas.widthProperty());
        //canvas.prefHeightProperty().bind(borderPaneCanvas.heightProperty());
        
        canvas.prefWidthProperty().bind(borderPaneCanvas.widthProperty());
        canvas.prefHeightProperty().bind(borderPaneCanvas.heightProperty());
        
        SplitPane  pane = new SplitPane();
        pane.getItems().add(borderPane);
        pane.getItems().add(borderPaneCanvas);
        
        vbox.getChildren().add(pane);
        Scene scene = new Scene(vbox, 1200, 600);
        
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
        //((VBox) scene.getRoot()).getChildren().add(this.initMenuBar());
        /*
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        */
        this.mainStage = stage;
        stage.setTitle("My JavaFX Application");
        stage.setScene(scene);
        stage.show();
    }
    
    
    public MenuBar initMenuBar(){
        MenuBar bar = new MenuBar();
        
        Menu menuFile = new Menu("File");
        MenuItem itemOpenFile = new MenuItem("Open..");
        itemOpenFile.setOnAction((event) -> actionOpenFile());
        menuFile.getItems().add(itemOpenFile);
        
        Menu menuPlugins = new Menu("Plugins");
        MenuItem  itemLoadPlugin = new MenuItem("Load");
        itemLoadPlugin.setOnAction((event) -> actionLoadPlugin());
        menuPlugins.getItems().add(itemLoadPlugin);
        
        
        bar.getMenus().add(menuFile);
        bar.getMenus().add(menuPlugins);
        return bar;
    }
    
    public void actionOpenFile(){
        System.out.println("Openning file....");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Evio Files", "*.evio"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            this.store.setFile(selectedFile.getAbsolutePath());
            this.serviceTask.getStore().setFile(selectedFile.getAbsolutePath());
            this.serviceTask.start();
            //mainStage.display(selectedFile);
        }
    }
    
    public void actionProcessFile(){
        
    }
    
    public void actionLoadPlugin(){
        System.out.println("LOADING PLUGIN..");
        JarPluginLoaderDialog  dialog = new 
        JarPluginLoaderDialog("org.clasfx.calib.DetectorCalibration");
        Stage stage = dialog.getStage();
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
        
        String pluginName = dialog.getSelectedPlugin();
        this.serviceTask.getStore().addClass(pluginName);
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
