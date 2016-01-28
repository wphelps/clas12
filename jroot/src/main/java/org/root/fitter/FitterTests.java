/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fitter;

import org.root.func.F1D;
import org.root.histogram.GraphErrors;
import org.root.pad.TGCanvas;

/**
 *
 * @author gavalian
 */
public class FitterTests {
    
    public static void main(String[] args){
        TGCanvas c1 = new TGCanvas("c1","",500,500,1,1);
        double[]  x  = new double[]{10.0, 20.0};
        double[] ex1 = new double[]{1.0, 0.0};
        double[] y1  = new double[]{10.0, 0.0};
        double[] ey1 = new double[]{1.0, 1.0};
        
        F1D  func = new F1D("p0",9.0,22.0);
        GraphErrors  graph = new GraphErrors(x,y1,ex1,ey1);
        graph.fit(func,"RMEQ");
        c1.draw(graph);
        c1.draw(func,"same");
    }
}
