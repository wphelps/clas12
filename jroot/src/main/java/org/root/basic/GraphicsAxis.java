/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.basic;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import org.root.base.LatexText;

/**
 *
 * @author gavalian
 */
public class GraphicsAxis {
    private List<Double>  axisMarks       = new ArrayList<Double>();
    private List<String>  axisMarksString = new ArrayList<String>();
    private int           axisLength      = 100;
    private LatexText     axisTitle       = new LatexText("",0.5,0.0);
    private Font          axisFont        = new Font("Avenier",Font.PLAIN,12);
    
    public GraphicsAxis(){
        
    }
    
    public final void setAxis(double[] values, String[] markers){
        if(values.length!=markers.length){
            return;
        }
        this.axisMarks.clear();
        this.axisMarksString.clear();
        for(int i = 0; i < values.length; i++){
            this.axisMarks.add(values[i]);
            this.axisMarksString.add(markers[i]);
        }
    }
    
    public void setTitleFont(String fontname){
        this.axisTitle.setFont(fontname);
    }
    
    public void setTitleSize(int size){
        this.axisTitle.setFontSize(size);
    }
    
    public void show(){
        for(int i = 0; i < axisMarks.size(); i++){
            System.out.println(String.format("%4d : %9.4f %25s", i,
                    this.axisMarks.get(i),this.axisMarksString.get(i)));
        }
    }
    
}
