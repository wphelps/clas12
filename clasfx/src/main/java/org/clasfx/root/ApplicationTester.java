/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.root.base.IDataSet;
import org.root.func.F1D;
import org.root.func.RandomFunc;
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class ApplicationTester extends Application {
    FunctionGroup  funcGroup = null;
    TEmbeddedPad pad = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        VBox  vbox = new VBox();
        funcGroup = new FunctionGroup(new String[]{"a","b","c","d","e"},0.0,14.0);
        
        BorderPane  pane = new BorderPane();
        pane.setBorder(Border.EMPTY);
        /*
        TEmbeddedCanvas canvas = new TEmbeddedCanvas(500,500);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        pane.setMinSize(300, 300);*/
        pad = new TEmbeddedPad();
        double[] xdata = new double[]{1.0,2.0,3.0,4.0};
        double[] ydata = new double[]{1.0,2.0,3.0,4.0};
        GraphErrors graph = new GraphErrors(xdata,ydata);
        
        H1D h1 = new H1D("h1","Random Function (Gaus + POL2)",200,0.0,14.0);


        h1.setXTitle("Random Generated Function");
        h1.setYTitle("Counts");

        F1D f1 = new F1D("gaus+p2",0.0,14.0);
        
        f1.setParameter(0,120.0);
        f1.setParameter(1,  8.2);
        f1.setParameter(2,  1.2);
        f1.setParameter(3, 24.0);
        f1.setParameter(4,  7.0);

        RandomFunc rndm = new RandomFunc(f1);

        for(int i = 0; i < 34000; i++){
            h1.fill(rndm.random());
        }
        
        pad.addDataSet(h1);
        pad.widthProperty().bind(pane.widthProperty());
        pad.heightProperty().bind(pane.heightProperty());
        pane.setMinSize(300, 300);
        
        pane.getChildren().add(pad);

        vbox.getChildren().add(this.createControls());
        vbox.getChildren().add(pane);   
        vbox.getChildren().add(funcGroup);

        VBox.setVgrow(pane, Priority.ALWAYS);
        
        //vbox.getChildren().addAll(canvas,func);
        //vbox.getChildren().add(canvas);
//Group  root = new Group(func);
        //func.setPrefSize(400, 400);
        
        Scene scene = new Scene(vbox);
        
        //root.widthProperty().bind(scene.);
        
        stage.setScene(scene);
        stage.setTitle("My JavaFX Application");
        stage.setScene(scene);
        stage.show();
        
        //this.showDialog();
    }
    
    public void showDialog(){
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.DECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(new Group(new Text(25, 25, "Hello World!")));
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.showAndWait();
    }
    
    public void fitDataSet(){
        double[] params = this.funcGroup.getParameters();
        double[] limits = this.funcGroup.getLimits();
        //System.out.println(params);
        for(double t : limits)
            System.out.println(t);
        for(double l : params)
            System.out.println(l);
        
        F1D func = new F1D("gaus+p1",limits[0],limits[1]);
        for(int loop = 0; loop < params.length; loop++){
            func.setParameter(loop, params[loop]);
        }
        H1D h1 = (H1D) this.pad.getDataSet(0);
        h1.fit(func);
        
        double[] result = new double[func.getNParams()];
        for(int loop = 0; loop < result.length; loop++){
            result[loop] = func.getParameter(loop);
        }
        this.pad.clear();
        this.pad.addDataSet(h1);
        this.pad.addDataSet(func);
        this.funcGroup.setParameters(result);
    }
    
    private HBox createControls(){
        HBox hbox = new HBox(5.0);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        //hbox.setStyle("-fx-background-color: #336699;");
        hbox.setStyle("-fx-background-color: #999999;");
        Button fitButton = new Button("   Fit   ");
        fitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        System.out.println("Mouse clicked");
                        //button3.setEffect(shadow);
                        //showDialog();
                        fitDataSet();
                    }
                }
        );
        
        hbox.getChildren().add(fitButton);
        return hbox;
    }
    
    public static void main(String[] args){
        launch(args);
    }   
}
