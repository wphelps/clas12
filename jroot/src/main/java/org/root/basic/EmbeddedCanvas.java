/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.basic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.root.base.IDataSet;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.pad.TImageCanvas;
import org.root.ui.FitPanel;
import org.root.ui.OptionsPanel;
import org.root.utils.DataFactory;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvas extends JPanel implements ActionListener {
     public   ArrayList<EmbeddedPad>  canvasPads = new  ArrayList<EmbeddedPad>();
    private  int                     canvas_COLUMNS = 1;
    private  int                     canvas_ROWS    = 1;
    private  Integer                     currentPad = 0;
    private  JPopupMenu                       popup = null;
    private  int                       popupPad     = -1;
    
    public EmbeddedCanvas(){
     super();
     this.setPreferredSize(new Dimension(500,500));
     this.createPopupMenu();
     this.divide(1, 1);
    }
    /**
     * Constructor with initial canvas size with one pad division
     * @param xsize
     * @param ysize 
     */
    public EmbeddedCanvas(int xsize, int ysize){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(1, 1);
        this.createPopupMenu();
    }
    /**
     * Constructor with initial size and divisions
     * @param xsize
     * @param ysize
     * @param rows
     * @param cols 
     */
    public EmbeddedCanvas(int xsize, int ysize, int rows, int cols){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(rows,cols);
        this.createPopupMenu();
    }
    /**
     * Change active pad on the canvas
     * @param pad 
     */
    public void cd(int pad){
        if(pad<0){
             this.currentPad = 0;
             return;
         }
         
         if(pad>=this.canvasPads.size()){
             this.currentPad = 0;
             return;
         }
         this.currentPad = pad;
    }
    /**
     * Draw data set on current canvas
     * @param ds 
     */
    public void draw(IDataSet ds){
        this.canvasPads.get(this.currentPad).draw(ds);
    }
    /**
     * Draw data set on current canvas with a options
     * @param ds
     * @param options 
     */
    public void draw(IDataSet ds, String options){
        this.canvasPads.get(this.currentPad).draw(ds,options);
    }
    /**
     * Draw Data set on given pad
     * @param pad
     * @param ds
     * @param options 
     */
    public void draw(int pad, IDataSet ds, String options){
        if(pad>=0&&pad<this.canvasPads.size()){
            this.canvasPads.get(pad).draw(ds,options);
        }
    }
    /**
     * Change Axis font size for all pads
     * @param size 
     */
    public void setAxisFontSize(int size){
        for(EmbeddedPad pad : this.canvasPads){
            pad.setAxisSize(size);
        }
    }
    /**
     * Change axis title string font size
     * @param size 
     */
    public void setAxisTitleFontSize(int size){
        for(EmbeddedPad pad : this.canvasPads){
            pad.setAxisTitleSize(size);
        }
    }
    /**
     * Change pad title font size
     * @param size 
     */
    public void setTitleFontSize(int size){
        for(EmbeddedPad pad : this.canvasPads){
            pad.setTitleSize(size);
        }
    }
    /**
     * Change font size for start box fonts
     * @param size 
     */
    public void setStatBoxFontSize(int size){
         for(EmbeddedPad pad : this.canvasPads){
             pad.setStatBoxSize(size);
             //pad.getPad().setStatBoxFontSize(size);
         }
     }
    /**
     * Divide canvas into given number of column and rows
     * @param rows
     * @param cols 
     */
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
    
    public EmbeddedPad  getPad(){
        return this.canvasPads.get(this.currentPad);
    }
    
    public EmbeddedPad  getPad(int index){
        return this.canvasPads.get(index);
    }
    
    public void setDivisionsX(int div){
        //this.getPad().setDivisionsX(div);
    }
     
    public void setDivisionsY(int div){
        //this.getPad().setDivisionsY(div);
    }
    
    public void setAxisRange(double xmin, double xmax, double ymin, double ymax){
        this.canvasPads.get(this.currentPad).setAxisRange(xmin, xmax, ymin, ymax);
    }
    
    public void setAxisRange(String axis, double min, double max){
        this.canvasPads.get(this.currentPad).setAxisRange(axis, min, max);
    }
    
    public void setGridX(boolean flag){
        this.canvasPads.get(this.currentPad).dataSetFrame.getAxisFrame().setGridX(flag);
    }
    
    public void setGridY(boolean flag){
        this.canvasPads.get(this.currentPad).dataSetFrame.getAxisFrame().setGridY(flag);
    }
    public int getCurrentPad(){
        return this.currentPad;
    }
    
    public void setLogZ(){
        this.setLogZ(true);
    }
    
    
    public void setLogX(boolean logFlag){
        this.canvasPads.get(this.currentPad).dataSetFrame.getAxisFrame().getAxisX().setLog(logFlag);
    }
    
    public void setLogY(boolean logFlag){
        this.canvasPads.get(this.currentPad).dataSetFrame.getAxisFrame().getAxisY().setLog(logFlag);
    }
    
    public void setLogZ(boolean logFlag){
        this.canvasPads.get(this.currentPad).dataSetFrame.getAxisFrame().getAxisZ().setLog(logFlag);
    }
    
    public void update(){
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
    
    private void createPopupMenu(){
        this.popup = new JPopupMenu();
        JMenuItem itemSave = new JMenuItem("Save");
        JMenuItem itemSaveAs = new JMenuItem("Save As...");
        JMenuItem itemFitPanel = new JMenuItem("Fit Panel");
        JMenuItem itemOptions = new JMenuItem("Options");
        itemSave.addActionListener(this);
        itemSaveAs.addActionListener(this);
        itemFitPanel.addActionListener(this);
        itemOptions.addActionListener(this);
        
        this.popup.add(itemSave);
        this.popup.add(itemSaveAs);
        this.popup.add(new JSeparator());
        this.popup.add(itemFitPanel);
        this.popup.add(new JSeparator());
        this.popup.add(itemOptions);
        addMouseListener(new MousePopupListener());
    }

    public int getPadNumberByXY(int x, int y){
        int w  = this.getWidth();
        int h  = this.getHeight();
        int xc = x/(w/this.canvas_COLUMNS);
        int yc = y/(h/this.canvas_ROWS);
        //int pad = yc*(this.canvas_ROWS-1) + xc;
        int pad = yc * this.canvas_COLUMNS + xc;
        //System.out.println(" pad = " + pad + " xc = " + xc + " yc = " + yc
        //+ " ROWS = " + this.canvas_ROWS + "  columns = " + this.canvas_COLUMNS);
        return pad;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action performed " + e.getActionCommand());
        if(e.getActionCommand().compareTo("Options")==0){
            this.openOptionsPane(popupPad);
        }
        if(e.getActionCommand().compareTo("Fit Panel")==0){
            this.openFitsPane(popupPad);
        }
        if(e.getActionCommand().compareTo("Save As...")==0){
            final JFileChooser fc = new JFileChooser();
//In response to a button click:
            int returnVal = fc.showSaveDialog(this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if(file.exists()==true){
                    JOptionPane.showMessageDialog(this, "Error. The file already esits....");
                } else {
                    //System.out.println("saving file : " + file.getAbsolutePath());
                    this.save(file.getAbsolutePath());
                }
            }
        }
        
    }
    
    
    class MousePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseClicked(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        private void checkPopup(MouseEvent e) {
            //System.out.println("showing");
            if (e.isPopupTrigger()) {
                popupPad = getPadNumberByXY(e.getX(),e.getY());
                //System.out.println("POP-UP coordinates = " + e.getX() + " " + e.getY() + "  pad = " + pad);
                popup.show(EmbeddedCanvas.this, e.getX(), e.getY());
            }
        }
    }

    
    public void openOptionsPane(int pad){
        //System.out.println(" Openning option panbe for pad = " + pad);
        //System.out.println(" Dataset count = " + this.getPad(pad).getDataSetCount());
        JFrame frame = new JFrame("Options");
        OptionsPanel options = new OptionsPanel(this,pad);
        frame.setLayout(new BorderLayout());
        frame.add(options, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }
    
    public void openFitsPane(int pad){
        //System.out.println(" Openning option panbe for pad = " + pad);
        //System.out.println(" Dataset count = " + this.getPad(pad).getDataSetCount());
        JFrame frame = new JFrame("Fit Panel");
        
        FitPanel options = new FitPanel(this,pad);
        frame.setLayout(new BorderLayout());
        frame.add(options, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1,3));
        
        EmbeddedCanvas canvas = new EmbeddedCanvas(500,500,1,1);
        
        frame.setSize(800, 600);
        canvas.setAxisFontSize(10);
        canvas.setTitleFontSize(10);
        canvas.setAxisTitleFontSize(10);
        canvas.setStatBoxFontSize(8);
        H1D h1a = new H1D("h1a",120,0.0,5.0);
        H1D h1b = new H1D("h1b",120,0.0,5.0);
        h1b.setFillColor(44);
        h1a.setFillColor(48);
        h1b.setTitle("Histogram 1D Random");
        h1b.setXTitle("Random Histogram");
        h1b.setYTitle("Counts");
        
        DataFactory.createSampleH1D(h1a, 2500, 2.5);
        DataFactory.createSampleH1D(h1b, 1500, 1.5);
        
        H2D  h2 = new H2D("h2","",50,0.0,14.0,50,0.0,14.0);
        
        DataFactory.createSampleH2D(h2, 40000);
        
        canvas.cd(0);
        //canvas.setLogZ();
        canvas.draw(h2);
        /*canvas.draw(h1a);
        canvas.cd(1);
        canvas.draw(h1b);
        canvas.draw(h1a, "same");
        for(int loop = 2; loop < 16; loop++){
            canvas.cd(loop);
            canvas.draw(h1b,"S");
        }*/
        //canvas.setTitleFontSize(2);
        //canvas.setAxisFontSize(12);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}
