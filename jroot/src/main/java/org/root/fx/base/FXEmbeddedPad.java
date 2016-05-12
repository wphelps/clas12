/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fx.base;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author gavalian
 */
public class FXEmbeddedPad {
    
    private Region2D  graphicsPadRegion = new Region2D();
    private Region2D  graphicsPadFrame  = new Region2D();
    private Color     graphicsPadBackground = Color.WHITE;
    
     public FXEmbeddedPad(){
         
     }
    
    public FXEmbeddedPad(double x, double y, double width, double height){
        this.graphicsPadRegion.set(x, y, width,height);
    }
    
    public Region2D  getRegion(){ return this.graphicsPadRegion;}
    public Region2D  getFrame() { return this.graphicsPadFrame;}
    
    public void draw(GraphicsContext gc){
        
        updateFrame();
        gc.clearRect( graphicsPadRegion.x(),
                graphicsPadRegion.y(),
                graphicsPadRegion.width(),
                graphicsPadRegion.height());

        gc.setFill(graphicsPadBackground);
        gc.fillRect(graphicsPadRegion.x(),
                graphicsPadRegion.y(),
                graphicsPadRegion.width(),
                graphicsPadRegion.height());
        
        gc.setStroke(Color.BLACK);
        gc.strokeRect(graphicsPadFrame.x(),graphicsPadFrame.y(),
                graphicsPadFrame.width(),graphicsPadFrame.height());
    }
    
    public void  updateFrame(){
        this.graphicsPadFrame.set(
                graphicsPadRegion.x()+10,
                graphicsPadRegion.y()+10,
                graphicsPadRegion.width()-20,
                graphicsPadRegion.height()-20
                );        
    }
}
