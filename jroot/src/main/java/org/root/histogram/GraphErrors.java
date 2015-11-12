/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.histogram;

import org.root.attr.AttributesLine;
import org.root.attr.AttributesMarker;
import org.root.data.DataSetXY;
import org.root.fitter.DataFitter;
import org.root.func.F1D;



/**
 *
 * @author gavalian
 */
public class GraphErrors extends DataSetXY {
    
    public AttributesLine    graphLineAttributes   = new AttributesLine();
    public AttributesMarker  graphMarkerAttributes = new AttributesMarker();
    
    public GraphErrors(){
        setName("GraphErrors");        
    } 
    
    public GraphErrors(double[] x, double[] y){
        super(x,y);
        setName("GraphErrors");        
    } 
    
    public GraphErrors(String name,double[] x, double[] y){
        super(x,y);
        setName(name);        
    } 
    
    
    public GraphErrors(double[] x, double[] y, double[] ex, double[] ey){
        super(x,y,ex,ey);
        setName("GraphErrors");        
    } 
    
    public GraphErrors(String name, double[] x, double[] y, double[] ex, double[] ey){
        super(x,y,ex,ey);
        setName(name);
    }
    
    public void fit(F1D func){
        DataFitter.fit(this, func);
    }
    
    public void setMarkerStyle(int style){ 
        this.graphMarkerAttributes.MARKER_STYLE = style; 
    }
    
    public void setMarkerColor(int color){ 
        this.graphMarkerAttributes.MARKER_COLOR = color; 
    }
    
    public void setMarkerSize(int size){ 
        this.graphMarkerAttributes.MARKER_SIZE = size; 
    }
     
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int loop = 0; loop < this.getDataSize(); loop++){
            str.append(String.format("%-7d %12.5f %12.5f\n", loop,
                    this.getDataX().getValue(loop),
                    this.getDataY().getValue(loop)
                    ));
        }
        return str.toString();
    }
}
