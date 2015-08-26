/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.root;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 *
 * @author gavalian
 */
public class TStyle {
    
    static double[] red   = {0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.,0.,0.17,0.33,0.50,0.67,0.83,1.00,1.00,
                             1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,
                             1.,1.,1.,1.,1.,1.,1.};
    static double[] green = {0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.08,0.15,0.23,0.31,0.38,0.46,0.53,0.59,0.66,
                             0.73,0.80,0.87,0.72,0.58,0.43,0.29,0.14,0.00,0.08,
                             0.17,0.25,0.33,0.42,0.50,0.58,0.67,0.75,0.83,0.92,
                             1.,1.,1.,1.,1.,1.,1.};
    static double[] blue  = {0.30,0.33,0.36,0.39,0.42,0.45,0.48,0.52,0.56,0.60,
                             0.64,0.68,0.68,0.70,0.70,0.70,0.70,0.64,0.56,0.48,
                             0.40,0.33,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.17,0.33,0.50,0.67,0.83,1.};
    
    private static Integer axisLabelFontSize = 18;
    private static String  axisLabelFont     = "DejaVu Sans Mono";
    private static List<Paint>   colorMapPalette = TStyle.initColorMap(0);
    private static TreeMap<Integer,Paint>  colorPalette = TStyle.initColorPalette();
    private static Integer axisFontSize = 18;   
    
    
    public static void setAxisFontSize(int size){
        TStyle.axisLabelFontSize = size;
    }
    
    public static void setAxisFont(String fontname){
        axisLabelFont = fontname;
    }
    
    public static Integer getAxisFontSize(){
        return TStyle.axisLabelFontSize;
    }
    
    public static Font getAxisFont(){
        return new Font(TStyle.axisLabelFont,TStyle.axisLabelFontSize);
    }
    
    public static Paint getColorMap(double value, double maxvalue, boolean logarithmic){
        //System.out.println(" MAX VALUE = " + maxvalue);
        if(maxvalue==0.0) return colorMapPalette.get(0);
        double relative = value/maxvalue;
        
        int index = (int) (relative * colorMapPalette.size());
        //System.out.println("VALUE/MAX = " + value + "  " + maxvalue +
        //        " RELATIVE = " + relative + " INDEX = " + index  );
        if(index<0) return colorMapPalette.get(0);
        if(index>=colorMapPalette.size()) return colorMapPalette.get(colorMapPalette.size()-1);
        return colorMapPalette.get(index);
    }
    
    public static Paint getColor(int color){
        if(colorPalette.containsKey(color)==true){
            return colorPalette.get(color);
        }
        return Color.BLACK;
    }
    
    public static TreeMap<Integer,Paint>  initColorPalette(){
        TreeMap<Integer,Paint> colors = new TreeMap<Integer,Paint>();
        colors.put(0, Color.WHITE);
        colors.put(1, Color.BLACK);
        colors.put(2, Color.rgb(210,79,68));
        colors.put(3, Color.rgb(137,216,68));
        colors.put(4, Color.rgb(77,176,221));
        colors.put(5, Color.rgb(246,188,47));        
        colors.put(6, Color.rgb(222,82,111));
        colors.put(7, Color.rgb(230,130,58));
        colors.put(8, Color.rgb(90,207,161));
        colors.put(9, Color.rgb(106,120,203));        
        return colors;
    }
    
    public static List<Paint>  initColorMap(int mode){
        ArrayList<Paint>  palette = new ArrayList<Paint>();
        
        
        palette.add(Color.rgb(180,180,180));
        for(int loop = 0; loop < red.length; loop++){
            int pred   = (int) (255.0*red[loop]);
            int pgreen = (int) (255.0*green[loop]);
            int pblue  = (int) (255.0*blue[loop]);
                palette.add(Color.rgb(pred,pgreen,pblue));
        }
        return palette;
    }
    
    
}
