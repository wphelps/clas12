/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.root.func.F1D;
import org.root.func.RandomFunc;
import org.root.histogram.H1D;
import org.root.histogram.H2D;

/**
 *
 * @author gavalian
 */
public class TEmbeddedPadApplication extends Application {
    TEmbeddedPad     canvasPad = null;
    TEmbeddedCanvas  canvasCanvas = null;//new TEmbeddedCanvas(800,400,2,2);
    public static final CountDownLatch latch = new CountDownLatch(1);
    static TEmbeddedPadApplication minstance = null;
    
    public TEmbeddedPadApplication(){
        TEmbeddedPadApplication.setStartUpTest(this);
    }
    
    public static TEmbeddedPadApplication waitForStartUpTest() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return minstance;
    }
    
    public static void setStartUpTest(TEmbeddedPadApplication startUpTest0) {
        minstance = startUpTest0;
        latch.countDown();
    }
    
    public void printout(){
        System.out.println("count down printed from class");
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
       BorderPane  pane = new BorderPane();

       canvasCanvas = new TEmbeddedCanvas(800,400,2,2);
       canvasPad = this.createPad();       
       //pane.getChildren().add(canvasPad);
       //pane.getChildren().add(canvasCanvas);
       
       Scene scene = new Scene(canvasCanvas,800,600);
       //canvasPad.widthProperty().bind(pane.widthProperty());
       //canvasPad.heightProperty().bind(pane.heightProperty());
        //root.widthProperty().bind(scene.);
       //pane.prefWidthProperty().bind(scene.widthProperty());
       //pane.prefHeightProperty().bind(scene.heightProperty());
       canvasCanvas.prefWidthProperty().bind(scene.widthProperty());
       canvasCanvas.prefHeightProperty().bind(scene.heightProperty());
       stage.setScene(scene);
       stage.setTitle("My JavaFX Application");
       stage.setScene(scene);
       stage.show();
    }
    
    public void divide(int cols, int rows){
        if(this.canvasCanvas==null){
            System.out.println("--->>>>  canvas value is null");
        } else {
            this.canvasCanvas.divide(cols, rows);
        }
    }
    
    public TEmbeddedPad  createPad(){
        TEmbeddedPad pad = new TEmbeddedPad();
        F1D f1 = new F1D("gaus+p2",0.0,14.0);
        
        f1.setParameter(0,120.0);
        f1.setParameter(1,  8.2);
        f1.setParameter(2,  1.2);
        f1.setParameter(3, 24.0);
        f1.setParameter(4,  7.0);
        pad.addDataSet(f1);
        
        
        canvasCanvas.cd(0);
        canvasCanvas.draw(f1);
        canvasCanvas.cd(2);
        canvasCanvas.draw(f1);
        
        H2D h2 = new H2D("h2",30,0.0,14.0,30,0.0,14.0);
        h2.setTitle("Two Random Gaussians");
        h2.setXTitle("Random Generated Function");
        h2.setYTitle("Counts");
        
        F1D f2 = new F1D("gaus+p2",0.0,14.0);
        f2.setParameter(0,120.0);
        f2.setParameter(1,  8.2);
        f2.setParameter(2,  1.2);
        f2.setParameter(3, 24.0);
        f2.setParameter(4,  7.0);
        
        RandomFunc rndm = new RandomFunc(f2);
        
        for(int i = 0; i < 2400000; i++){
            h2.fill(rndm.random(),rndm.random());
        }
        canvasCanvas.cd(3);
        canvasCanvas.draw(h2);
        
        canvasCanvas.cd(2);
        canvasCanvas.draw(h2);
        
        canvasCanvas.cd(1);
        
        H1D h1d = h2.projectionX();
        h1d.setFillColor(3);
        canvasCanvas.draw(h1d);
        return pad;
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
