/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;


import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 *
 * @author gavalian
 */
public class FunctionGroup extends StackPane {
    
    ArrayList<String>   parameterNames    = new ArrayList<String>();
    ArrayList<Spinner>  parameterSpinners = new ArrayList<Spinner>();
    ArrayList<Slider>   functionLimits    = new ArrayList<Slider> ();
            
    public FunctionGroup(String[] names, double min, double max){
        super();
        //this.setStyle("-fx-background-color: #999999;");
        this.initGroup( names, min, max);
    }
    
    public void initGroup(String[] names, double min, double max){
        
        GridPane grid = new GridPane();
        //grid.gridLinesVisibleProperty().setValue(true);
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setPadding();
        grid.setPadding(new Insets(10));
        
        for(int row = 0; row < names.length; row++){
            Spinner spinnerMin = this.createSpinner();
            Spinner spinnerValue = this.createSpinner();
            Spinner spinnerMax = this.createSpinner();
            grid.add(new Label(names[row]), 0, row);
            this.parameterSpinners.add(spinnerValue);
            this.parameterNames.add(names[row]);
            grid.add(spinnerValue, 1, row);
            grid.add(spinnerMin, 2, row);
            grid.add(spinnerMax, 3, row);
        }
        
        Slider lowLimit  = this.createSliders(min,max,min);
        Slider highLimit = this.createSliders(min,max,max);
        grid.add(lowLimit,  0, names.length,  4, 1);
        grid.add(highLimit, 0, names.length+1,  4, 1);
        
        this.functionLimits.add(lowLimit);
        this.functionLimits.add(highLimit);
        
        //grid.setStyle('border: solid 1px red');
        grid.setStyle("-fx-border-width: 1;\n"
                + "-fx-border-color: #9999CC;\n"
                + "-fx-border-insets: 5;"
                + "-fx-border-style: dashed;\n");
        this.getChildren().add(grid);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(columnConstraints);
    }
    
    public double[] getParameters(){
        int nparams = this.parameterNames.size();
        double[]  pars = new double[nparams];
        for(int loop = 0; loop < nparams; loop++){
            pars[loop] = (double) this.parameterSpinners.get(loop).valueProperty().getValue();
        }
        return pars;
    }
    
    public void setParameters(double[] pars){
        for(int loop = 0; loop < pars.length; loop++){
            this.parameterSpinners.get(loop).getValueFactory().setValue(pars[loop]);
        }
    }
    
    public double[] getLimits(){
        double[] limits = new double[2];
        limits[0] = this.functionLimits.get(0).getValue();
        limits[1] = this.functionLimits.get(1).getValue();
        return limits;
    }
    
    public Spinner createSpinner(){
        Spinner spinner = new Spinner();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                -Double.MAX_VALUE,Double.MAX_VALUE));
        spinner.setEditable(true);
        spinner.getValueFactory().setValue(0.0);
        spinner.setMaxWidth(100);
        return spinner;
    }
    
    private Slider createSliders(double min, double max, double value){
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit( 0.2*(max-min));
        slider.setMinorTickCount(5);
        slider.setBlockIncrement( 0.1*(max-min));
        slider.setValue(value);
        return slider;
    }
}
