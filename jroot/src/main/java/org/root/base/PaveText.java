/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.root.attr.TStyle;


/**
 *
 * @author gavalian
 */
public class PaveText {
    
    private ArrayList<LatexText>  latexTexts = new ArrayList<LatexText>();
    public PaveText(){
        
    }
    
    public void addText(String text){
        LatexText lt = new LatexText(text);
        this.latexTexts.add(lt);
    }
    
    public void setFont(String name, int size){
        for(LatexText t : this.latexTexts){
            t.setFont(name);
            t.setFontSize(size);
        }
    }
    
    public Rectangle2D  getBounds(FontMetrics  fm, Graphics2D g2d){
        Rectangle2D bounds = this.latexTexts.get(0).getBounds(fm, g2d);
        int counter = 0;
        double  maxWidth  = 0.0;
        double  maxHeight = 0.0;
        
        for(LatexText text : this.latexTexts){
            Rectangle2D r = text.getBounds(fm, g2d);
            if(r.getWidth()>maxWidth) maxWidth = r.getWidth();
            maxHeight += r.getHeight()*TStyle.getStatBoxTextGap();
            //if(r.getHeight()>maxHeight) maxHeight = r.getHeight();
            //System.out.println("STRING TEXT # " + counter + "  " + r.getWidth() + "  " + r.getHeight());
            //counter++;
            //r            
        }
        bounds.setRect(0, 0, maxWidth, maxHeight);
        return bounds;
    }
    
    public void clear(){
        this.latexTexts.clear();
    }
    
    public List<LatexText>  getTexts(){
        return this.latexTexts;
    }
    
    
}
