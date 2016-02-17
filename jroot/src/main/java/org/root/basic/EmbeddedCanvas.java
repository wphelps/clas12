/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.basic;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.root.base.DataSetPad;
import org.root.base.IDataSet;
import org.root.histogram.H1D;
import org.root.pad.TImageCanvas;
import org.root.utils.DataFactory;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvas extends JPanel {
     public   ArrayList<EmbeddedPad>  canvasPads = new  ArrayList<EmbeddedPad>();
    private  int                     canvas_COLUMNS = 1;
    private  int                     canvas_ROWS    = 1;
    private  Integer                     currentPad = 0;
    
    public EmbeddedCanvas(){
     super();
     this.setPreferredSize(new Dimension(500,500));
     this.divide(1, 1);
    }
    
    public EmbeddedCanvas(int xsize, int ysize){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(1, 1);
    }
    
    public EmbeddedCanvas(int xsize, int ysize, int rows, int cols){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(rows,cols);
    }
     
    public void cd(int pad){
        if(pad<0){
             this.currentPad = 0;
             return;
         }
         
         if(pad>=this.canvasPads.size()){
             this.currentPad = 0;
         }
         this.currentPad = pad;
    }
    
    public void draw(IDataSet ds){
        this.canvasPads.get(this.currentPad).draw(ds);
    }
    
    public void draw(IDataSet ds, String options){
        this.canvasPads.get(this.currentPad).draw(ds,options);
    }
    
    public void setAxisFontSize(int size){
        for(EmbeddedPad pad : this.canvasPads){
            pad.setAxisSize(size);
        }
    }
    
    public void setAxisTitleFontSize(int size){
        for(EmbeddedPad pad : this.canvasPads){
            pad.setAxisTitleSize(size);
        }
    }
    
    public void setTitleFontSize(int size){
        for(EmbeddedPad pad : this.canvasPads){
            pad.setTitleSize(size);
        }
    }
    public void setStatBoxFontSize(int size){
         for(EmbeddedPad pad : this.canvasPads){
             //pad.getPad().setStatBoxFontSize(size);
         }
     }
    public final void divide(int rows, int cols){
        this.canvasPads.clear();
        this.removeAll();
        this.revalidate();
        this.canvas_COLUMNS = rows;
        this.canvas_ROWS    = cols;
        this.setLayout(new GridLayout(cols,rows));
        int xsize = this.getWidth()/cols;
        int ysize = this.getHeight()/rows;
        for(int loop = 0; loop < cols*rows; loop++){
            EmbeddedPad pad = new EmbeddedPad(xsize,ysize);
            canvasPads.add(pad);
            this.add(pad);
        }
        this.revalidate();
        //this.update();
        this.repaint();
    }
    
    
    public void save(String filename){
        int w = this.getSize().width;
        int h = this.getSize().height;
        try {
            List<DataSetFrame>  pads = new ArrayList<DataSetFrame>();
            for(EmbeddedPad  pad : this.canvasPads){
                pads.add(pad.getPad());
            }
            //TImageCanvas.save(filename, w,h,this.canvas_COLUMNS,this.canvas_ROWS,
            //        pads);
            System.out.println(
                    String.format("[TGCanvas::save] ----> size ( %d x %d )  dim (%dx%d)  FILE : %s",
                            w,h,this.canvas_COLUMNS,this.canvas_ROWS,filename));
        } catch (Exception e){
            
        }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1,3));
        
        EmbeddedCanvas canvas = new EmbeddedCanvas(500,500,4,4);
        frame.setSize(800, 600);
        canvas.setAxisFontSize(10);
        canvas.setTitleFontSize(10);
        canvas.setAxisTitleFontSize(10);
        H1D h1a = new H1D("h1a",120,0.0,5.0);
        H1D h1b = new H1D("h1b",120,0.0,5.0);
        h1b.setFillColor(44);
        h1a.setFillColor(48);
        h1b.setTitle("Histogram 1D Random");
        h1b.setXTitle("Random Histogram");
        h1b.setYTitle("Counts");
        
        DataFactory.createSampleH1D(h1a, 2500, 2.5);
        DataFactory.createSampleH1D(h1b, 1500, 1.5);
        canvas.cd(0);
        canvas.draw(h1a);
        canvas.cd(1);
        canvas.draw(h1b);
        canvas.draw(h1a, "same");
        for(int loop = 2; loop < 16; loop++){
            canvas.cd(loop);
            canvas.draw(h1b);
        }
        //canvas.setTitleFontSize(2);
        //canvas.setAxisFontSize(12);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}
