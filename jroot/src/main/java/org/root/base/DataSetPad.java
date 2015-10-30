/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import org.root.attr.TStyle;
import org.root.data.DataSetXY;
import org.root.func.F1D;
import org.root.histogram.H1D;
import org.root.histogram.H2D;

/**
 *
 * @author gavalian
 */
public class DataSetPad {
    
    private  DataSetCollection   collection = new DataSetCollection();
    private  AxisRegion        padAxisFrame = new AxisRegion();    
    private  ArrayList<LatexText>  textCollection = new ArrayList<LatexText>();
    private  PaveText              statBox        = new PaveText();
    
    public DataSetPad(){
        this.padAxisFrame.getAxisX().setMaxTicks(6);
        this.padAxisFrame.getAxisY().setMaxTicks(8);
        /*
        statBox.addText("H : h1 " );
        statBox.addText("Entries : 0.5674" );
        statBox.addText("Mean : 0.5674" );
        statBox.addText("RMS  : 0.012" );
        statBox.addText("#pi : 3.545646 +/- 56.784564" );
        */
    }
    
    public void drawOnCanvas(Graphics2D g2d, int xoffset, int yoffset, int w, int h){
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.padAxisFrame.getFrame().setBounds(120, 50, w-150, h-130);
        
        
        DataRegion  region = this.collection.getDataRegion();
        this.padAxisFrame.setDataRegion(region);
        //System.out.println(region);
        AbsDataSetDraw.drawAxisBackGround(padAxisFrame, g2d,xoffset,yoffset,w,h);
        g2d.setClip(this.padAxisFrame.getFrame().x + xoffset, 
                this.padAxisFrame.getFrame().y + yoffset,
                this.padAxisFrame.getFrame().width,
                this.padAxisFrame.getFrame().height);
        for(int loop = 0; loop < this.collection.getCount(); loop++){
            //System.out.println(" DRAWING COLLECTION ITEM # " + loop);
            IDataSet  ds = collection.getDataSet(loop);
            if(ds instanceof DataSetXY){
                AbsDataSetDraw.drawDataSetAsGraph(padAxisFrame, g2d, 
                        collection.getDataSet(loop), xoffset , yoffset, w, h);
            }
            
            if(ds instanceof F1D){
                AbsDataSetDraw.drawDataSetAsFunction(padAxisFrame, g2d, 
                        collection.getDataSet(loop), xoffset , yoffset, w, h);
            }
            
            if(ds instanceof H1D){
                AbsDataSetDraw.drawDataSetAsHistogram1D(padAxisFrame, g2d, 
                        ds, xoffset, yoffset, w, h);
            }
            
            if(ds instanceof H2D){
                AbsDataSetDraw.drawDataSetAsHistogram2D(padAxisFrame, g2d, 
                        ds, xoffset, yoffset , w, h);
            }
        }
        g2d.setClip(null);
        AbsDataSetDraw.drawAxisFrame(padAxisFrame, g2d,xoffset,yoffset,w,h);
        
        for(LatexText txt : this.textCollection){
            AbsDataSetDraw.drawText(padAxisFrame, g2d, txt, xoffset, yoffset,w,h);
        }
        /*
        Font textFont = new Font("Helvetica",Font.PLAIN,24);
        g2d.setFont(textFont);
        String latex = LatexTextTools.convertUnicode("x^2 + #pi^3 = #gamma");
        AttributedString latexString = LatexTextTools.converSuperScript(latex);
        //AttributedString latexString = new AttributedString(latex);
        latexString.addAttribute(TextAttribute.SIZE, (float)36);
        latexString.addAttribute(TextAttribute.FAMILY, "Helvetica");
        //latexString.addAttribute(TextAttribute., "Helvetica");
        //latexString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 10,11);
        //g2d.drawString(latex, 200,30);
        g2d.drawString(latexString.getIterator(), 250,30);
        */
        if(this.statBox.getTexts().size()>0){
            
            AbsDataSetDraw.drawPaveText(padAxisFrame, statBox, g2d, xoffset,yoffset,w,h);
        }
    }
    
    public void clear(){
        this.collection.clear();
    }
    
    public void add(IDataSet ds){
        this.collection.addDataSet(ds);
        if(this.collection.getCount()==1){
            try {
                this.padAxisFrame.setTitle(ds.getAttributes().getProperties().getProperty("title"));
                this.padAxisFrame.setXTitle(ds.getAttributes().getProperties().getProperty("xtitle"));
                this.padAxisFrame.setYTitle(ds.getAttributes().getProperties().getProperty("ytitle"));           
            } catch (Exception e){
                
            }
            
        }
        this.updateStatBox();
    }
    
    public void updateStatBox(){
        this.statBox.clear();
        if(this.collection.getCount()>0){
            IDataSet ds = this.collection.getDataSet(0);
            if(ds instanceof H1D){
                H1D h1 = (H1D) ds;
                this.statBox.setFont(TStyle.getStatBoxFontName(), TStyle.getStatBoxFontSize());
                this.statBox.addText( String.format("%s", h1.getName()));
                this.statBox.addText(String.format("Entries %8d", h1.getEntries()));
                this.statBox.addText(String.format("Mean    %8.4f", h1.getMean()));
                this.statBox.addText(String.format("RMS     %8.4f", h1.getRMS()));
            }
        }
        
        for(int loop = 0; loop < this.collection.getCount(); loop++){
            IDataSet ds = this.collection.getDataSet(loop);
            if(ds instanceof F1D){
                F1D func = (F1D) ds;
                for(int c = 0; c < func.getNParams(); c++){
                    this.statBox.addText(String.format("%-7s %12.4f", func.parameter(c).name(),
                            func.getParameter(c)));
                }
            }
        }
    }
    
    public void setAutoScale(Boolean flag){
        this.collection.setAutoScale(flag);
    }
    
    public void setAxisRange(double xmin, double xmax, double ymin, double ymax){
        this.collection.setDataRegion(xmin, xmax, ymin, ymax);
    }
    
    public void addText(LatexText txt){
        this.textCollection.add(txt);
    }
    
    public void setDivisionsX(int div){
        this.padAxisFrame.setDivisionsX(div);
    }
    
    public void setDivisionsY(int div){
        this.padAxisFrame.setDivisionsY(div);
    }
    
    public void setLogZ(boolean islog){
        this.padAxisFrame.getAxisZ().setAxisLog(islog);
    }
    
    public void setLogX(boolean islog){
        this.padAxisFrame.getAxisX().setAxisLog(islog);
    }
    
    public void setLogY(boolean islog){
        this.padAxisFrame.getAxisY().setAxisLog(islog);
    }
}
