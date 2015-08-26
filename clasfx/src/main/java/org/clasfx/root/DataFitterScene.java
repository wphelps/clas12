/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.root.func.F1D;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class DataFitterScene  {
    
    private H1D  histogram = null;
    private F1D  function  = null;
    FunctionGroup  funcGroup = null;
    TEmbeddedPad pad = null;
    Scene        fitterScene = null;
    
    public DataFitterScene(H1D h, F1D f){
        
        //super();
        this.histogram = h;
        this.function  = f;
        BorderPane  pane = new BorderPane();
        pad = new TEmbeddedPad();
        
        pad.addDataSet(this.histogram);
        pad.widthProperty().bind(pane.widthProperty());
        pad.heightProperty().bind(pane.heightProperty());
        pane.setMinSize(300, 300);
        
        pane.getChildren().add(pad);
        
        VBox  vbox = new VBox();        
        //vbox.getChildren().add(this.createControls());
        this.funcGroup = this.createFunctionGroup();
        
        
        HBox toolBox = this.createToolBar();
        
        vbox.getChildren().add(toolBox);
        vbox.getChildren().add(pane);
        vbox.getChildren().add(funcGroup);

        VBox.setVgrow(pane, Priority.ALWAYS);
        
        this.fitterScene = new Scene(vbox);
    }
    
    public HBox createToolBar(){
        HBox hbox = new HBox(5.0);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setStyle("-fx-background-color: #999999;");
        
        Button fitButton = new Button("   Fit   ");
        fitButton.setOnAction((event) -> { fitData(); } );
        hbox.getChildren().add(fitButton);
        
        return hbox;
    }
    
    public Scene getScene(){
        return this.fitterScene;
    }
    /**
     * Fitting the HISTOGRAM with function, then updating the GUI 
     * with fit results.
     */
    private void fitData(){
        double[] params = this.funcGroup.getParameters();
        double[] limits = this.funcGroup.getLimits();
        
        for(int loop = 0; loop < params.length; loop++){
            this.function.setParameter(loop, params[loop]);
        }        
        this.function.setRange(limits[0], limits[1]);
        H1D h1 = (H1D) this.pad.getDataSet(0);
        h1.fit(function);
        double[] result = new double[function.getNParams()];
        for(int loop = 0; loop < result.length; loop++){
            result[loop] = function.getParameter(loop);
        }
        this.pad.clear();
        this.pad.addDataSet(h1);
        this.pad.addDataSet(function);
        this.funcGroup.setParameters(result);
    }
    
    private FunctionGroup  createFunctionGroup(){
        String[] names  = new String[this.function.getNParams()];
        double[] params = new double[this.function.getNParams()];
        
        for(int loop = 0; loop < names.length; loop++){
            names[loop] = this.function.parameter(loop).name();
            params[loop] = this.function.getParameter(loop);
        }

        FunctionGroup group = new FunctionGroup(names, this.function.getMin(),
                this.function.getMax());
        group.setParameters(params);
        return group;
    }
    
    public void showStage(){
        
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.DECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        dialog.setScene(this.fitterScene);
        //dialog.setResizable(false);
        dialog.showAndWait();
    }
}
