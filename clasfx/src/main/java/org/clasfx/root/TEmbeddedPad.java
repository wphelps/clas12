/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.root.base.AxisRegion;
import org.root.base.DataRegion;

import org.root.base.DataSetCollection;
import org.root.base.IDataSet;
import org.root.func.F1D;
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;
import org.root.histogram.H2D;

/**
 *
 * @author gavalian
 */
public class TEmbeddedPad extends Canvas {
    
    DataSetCollection   dataCollection = new DataSetCollection();
    AxisRegion          axisRegion     = new AxisRegion();
    
    ContextMenu contextMenu = null;
    
    public TEmbeddedPad(){
        super(300,300);
        this.initContextMenu();
        this.prefHeight(300);
        this.prefWidth(300);
        this.widthProperty().addListener(observable -> redraw());
        this.heightProperty().addListener(observable -> redraw());
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, (final MouseEvent mouseEvent) -> {
            //System.out.println("Mouse moved too : "
            //        + mouseEvent.getX() + "  " + mouseEvent.getY());
            mouseClickedCallBack(mouseEvent);
        });
    }
    
    public void addDataSet(IDataSet set){
        this.dataCollection.addDataSet(set);
        this.axisRegion.setDataRegion(this.dataCollection.getDataRegion());
        this.redraw();
    }
    
    public void mouseClickedCallBack(MouseEvent mouseEvent){
        if(contextMenu.isShowing()==true){
            contextMenu.hide();
            return;
        }
        double x = mouseEvent.getScreenX();
        double y = mouseEvent.getScreenY();
        if(mouseEvent.getButton()==MouseButton.SECONDARY){
            System.out.println("RIGHT BUTTON CLICKED");
            if(contextMenu.isShowing()==false){
                contextMenu.show(this, x, y);
            } else {
                contextMenu.hide();
            }
        }
        
    }
    
    
    public void initContextMenu(){
        contextMenu = new ContextMenu();
        Menu      fitMenu = new Menu("Fit");
        MenuItem  fitGaus = new MenuItem("Gaus");
        MenuItem  fitGPOL = new MenuItem("Gaus + POL2");
        fitGPOL.setOnAction((event) -> this.openFitGui());
        fitMenu.getItems().add(fitGaus);
        fitMenu.getItems().add(fitGPOL);
        //fitMenu.getItems().add(new SeparatorMenuItem());
        Menu      optionsMenu = new Menu("Options");
        contextMenu.getItems().add(fitMenu);
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().add(optionsMenu);
    }
    
    public void openFitGui(){
        DataRegion region = this.dataCollection.getDataSet(0).getDataRegion();
        F1D f1 = new F1D("gaus+p2",region.MINIMUM_X,region.MAXIMUM_X);
        f1.setParameter(0, 100.0);
        f1.setParameter(1,  0.5*(region.MAXIMUM_X-region.MINIMUM_X));
        f1.setParameter(2, 0.01*(region.MAXIMUM_X-region.MINIMUM_X));
        
        DataFitterScene stage = new DataFitterScene((H1D) this.dataCollection.getDataSet(0),
                f1);
        stage.showStage();
        this.dataCollection.addDataSet(f1);
        this.redraw();
        
    }
    
    public IDataSet getDataSet(int index){
        return this.dataCollection.getDataSet(index);
    }
    
    public void clear(){
        this.dataCollection.clear();
    }
    public final void redraw(){
        
        if(this.dataCollection.getCount()==0){
            this.axisRegion.getDataRegion().MINIMUM_X = 0.0;
            this.axisRegion.getDataRegion().MAXIMUM_X = 1.0;
            this.axisRegion.getDataRegion().MINIMUM_Y = 0.0;
            this.axisRegion.getDataRegion().MAXIMUM_Y = 1.0;
        }
        double width  = this.getWidth();
        double height = this.getHeight();
        System.out.println("REDRAWING SIZE = " + width + " x " + height);
        this.axisRegion.getFrame().x = 20;
        this.axisRegion.getFrame().y = 20;
        this.axisRegion.getFrame().width  = (int) width - 40;
        this.axisRegion.getFrame().height = (int) height - 40;
        
        GraphicsContext gc = this.getGraphicsContext2D();
        
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.rgb(255,255,255));
        gc.fillRect(0,0, width, height);
        gc.setStroke(Color.rgb(0, 0, 0));
        //gc.strokeLine(0, 0, width, height);
        AbstractDrawer.updateAxisFrameBounds(0, 0, width, height, axisRegion, gc);
        

        
        for(int loop = 0; loop < this.dataCollection.getCount(); loop++){
            System.out.println("DRAWING DATASET ID = " + loop);
            if(this.dataCollection.getDataSet(loop) instanceof GraphErrors){
                AbstractDrawer.drawGraphPoints(this.dataCollection.getDataSet(loop), axisRegion, gc);                
            }
            if(this.dataCollection.getDataSet(loop) instanceof H1D){
                AbstractDrawer.drawHistogram(this.dataCollection.getDataSet(loop), axisRegion, gc);                
            }
            if(this.dataCollection.getDataSet(loop) instanceof F1D){
                AbstractDrawer.drawFunction(this.dataCollection.getDataSet(loop), axisRegion, gc);
            }
            if(this.dataCollection.getDataSet(loop) instanceof H2D){
                AbstractDrawer.drawHistogram2D(this.dataCollection.getDataSet(loop), axisRegion, gc);
            }
        }
        AbstractDrawer.drawAxis(0, 0, width, height, axisRegion, gc);
    }
}
