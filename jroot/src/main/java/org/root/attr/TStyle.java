/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.attr;

import java.awt.Color;


/**
 *
 * @author gavalian
 */
public class TStyle {
    
    private static Integer axisFrameLineWidth    = 1;
    private static String  axisFontStringName    = "Helvetica";
    private static Integer axisFontSize          = 18;
    
    private static Color   padBackgroundColor    = Color.WHITE;
    private static Color   padFrameColor         = Color.WHITE;
    private static Color   axisFrameColor        = Color.BLACK;
    
    private static String  statBoxFontStringName = "Courier";
    private static Integer statBoxFontSize       = 18;
    private static Boolean statBoxOptions        = true;
    private static Double  statBoxTextGap        = 1.2;
            
    public  static void setAxisFont(String name, int size){
        TStyle.axisFontStringName = name;
        TStyle.axisFontSize = size;
    }
    
    public static Color getFrameBackgroundColor(){
        return TStyle.padBackgroundColor;
    }
    
    public static Color getFrameFillColor(){
        return TStyle.padFrameColor;
    }
    
    public static Color getAxisColor(){
        return TStyle.axisFrameColor;
    }
    
    public static void setFrameFillColor(int red, int green, int blue){
        padFrameColor = new Color(red,green,blue);
    }
    
    public static void setFrameBackgroundColor(int red, int green, int blue){
        padBackgroundColor = new Color(red,green,blue);
    }
    
    public static void setAxisColor(int red, int green, int blue){
        axisFrameColor = new Color(red,green,blue);
    }
    
    public static int getAxisFontSize(){
        return TStyle.axisFontSize;
    }
    
    public static String getAxisFontName(){
        return TStyle.axisFontStringName;
    }
    
    public  static void setStatBoxFont(String name, int size){
        TStyle.statBoxFontStringName = name;
        TStyle.statBoxFontSize = size;
    }
    
    public static int getStatBoxFontSize(){
        return TStyle.statBoxFontSize;
    }
    
    public static String getStatBoxFontName(){
        return TStyle.statBoxFontStringName;
    }
    
    public static void setFrameLineWidth(int width){
        TStyle.axisFrameLineWidth = width;
    }
    
    public static int getFrameLineWidth(){
        return TStyle.axisFrameLineWidth;
    }
    
    public static void setOptStat(boolean flag){
        TStyle.statBoxOptions = flag;
    }
    
    public static boolean getOptStat(){
        return TStyle.statBoxOptions;
    }
    
    public static void setStatBoxTextGap(double value){
        TStyle.statBoxTextGap = value;
    }
    
    public static double getStatBoxTextGap(){
        return TStyle.statBoxTextGap;
    }
}
