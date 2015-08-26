/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import javafx.application.Application;
import javafx.stage.Stage;
import org.root.base.IDataSet;

/**
 *
 * @author gavalian
 */
public class TCanvas {
    
    TCanvasStage canvasStage = null;

    public class CanvasApp extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception {
            canvasStage = new TCanvasStage();
            primaryStage = canvasStage.getStage();
        }
        
    }
    
    
    public TCanvas(){
        new Thread(){
            @Override
            public void run(){                                
                javafx.application.Application.launch(TEmbeddedPadApplication.class);
            }
        }.start();
        TEmbeddedPadApplication app = TEmbeddedPadApplication.waitForStartUpTest();
        app.printout();
        app.canvasCanvas.divide(2, 4);
    }
    
    public void divide(int cols, int rows){
        //app.divide(cols, rows);
        //app.canvasCanvas.divide(cols, rows);
    }
    
    public void draw(IDataSet ds){
        //this.app.canvasCanvas.draw(ds);
    }
}
