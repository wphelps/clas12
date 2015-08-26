/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import org.clasfx.root.TEmbeddedPad;
import org.root.base.IDataSet;

/**
 *
 * @author gavalian
 */
public class TEmbeddedCanvas extends BorderPane {
    
    ArrayList<TEmbeddedPad> canvasPads = new ArrayList<TEmbeddedPad>();
    private Integer currentPad = 0;
    
    public TEmbeddedCanvas(int xsize, int ysize, int cols, int rows){
        super();        
        //this.setMinSize(xsize,ysize);
        //this.prefHeight(ysize);
        this.setMinSize(100, 100);
        this.initCanvas(cols, rows);
    }
    
    
    public void cd(int pad){
        currentPad = pad;
        if(canvasPads.size()<=currentPad){
            currentPad = canvasPads.size()-1;
        }
        System.out.println("Changed pad to : " + this.currentPad);
    }
    
    public void draw(IDataSet ds){
        this.canvasPads.get(currentPad).clear();
        this.canvasPads.get(currentPad).addDataSet(ds);
        this.canvasPads.get(currentPad).redraw();
    }
    
    public void draw(IDataSet ds,String options){
        if(options.contains("same")==false){
            this.draw(ds);
        } else {
            this.canvasPads.get(currentPad).addDataSet(ds);
            this.canvasPads.get(currentPad).redraw();
        }
    }
    
    public void divide(int cols, int rows){
        this.initCanvas(cols, rows);
        this.currentPad = 0;
    }
    
    public final void initCanvas(int cols, int rows){ 
        this.currentPad = 0;
        this.canvasPads.clear();
        this.getChildren().clear();
        
        double widthFactor   = 1.0/cols;
        double heightFactor  = 1.0/rows;
        
        
        GridPane grid = new GridPane();
        
        for(int c = 0; c < cols; c++){
            for(int r = 0; r < rows; r++){
                TEmbeddedPad  pad = new TEmbeddedPad();
                this.canvasPads.add(pad);
                pad.widthProperty().bind(this.widthProperty().multiply(widthFactor));
                pad.heightProperty().bind(this.heightProperty().multiply(heightFactor));
                grid.add(pad, c, r);
            }
        }
        
        ColumnConstraints cc = new ColumnConstraints();
        //cc1.setPercentWidth(50);
        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);
        for(int c = 0; c < cols; c++){
            grid.getColumnConstraints().add(cc);
        }
        
        RowConstraints rc = new RowConstraints();
        rc.setFillHeight(true);
        rc.setVgrow(Priority.ALWAYS);
        for(int r = 0; r < rows; r++){
            grid.getRowConstraints().add(rc);
        }
        
        this.getChildren().add(grid);
        //grid.prefWidthProperty().bind(this.widthProperty());
        //grid.prefHeightProperty().bind(this.heightProperty());
        //GridPane grid = new GridPane();
                //pad.widthProperty().bind(this.widthProperty());
        //pad.heightProperty().bind(this.heightProperty());
        
    }    
}
