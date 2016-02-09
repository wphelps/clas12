/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.basic;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author gavalian
 */
public class GraphicsAxisDate extends GraphicsAxis {
    
    public GraphicsAxisDate(long start, long end){
        this.setAxisRange(start, end);
    }
    
    public final void setAxisRange(long startTime, long endTime){
        long step = (endTime - startTime)/10;
        double[] d = new double[10];
        String[] s = new String[10];
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY M/d hh:mm",Locale.US);
        for(int i = 0; i < 10; i++){
            long  time = startTime + i*step;
            d[i] = time;
            s[i] = sdf.format(time);
        }
        this.setAxis(d, s);
    }
    
    public static void main(String[] args){
        //GraphicsAxisDate  axis = new GraphicsAxisDate(1455052909,1555082909);
        GraphicsAxisDate  axis = new GraphicsAxisDate(1455053278,1555053278);
        axis.show();
    }
}
