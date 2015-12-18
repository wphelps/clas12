/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.histogram;

import org.root.attr.Attributes;
import org.root.base.DataRegion;
import org.root.base.IDataSet;

/**
 *
 * @author gavalian
 */
public class DataBox implements IDataSet {
    String boxName = "DataBox";
    double boxX       = 0.0;
    double boxY       = 0.0;
    double boxW       = 0.0;
    double boxH       = 0.0;
    Attributes        attr = new Attributes();
    
    public DataBox(){
        
    }
    
    public DataBox(double x, double y, double w, double h){
        this.setSize(x, y, w, h);
    }
    
    public final void setSize(double x, double y, double w, double h){
        this.boxX = x;
        this.boxY = y;
        this.boxW = w;
        this.boxH = h;
    }
    
    public void setName(String name) {
        this.boxName = name;
    }

    public String getName() {
        return this.boxName;
    }

    public DataRegion getDataRegion() {
        DataRegion region = new DataRegion();
        region.MINIMUM_X = this.boxX - this.boxW*0.1;
        region.MAXIMUM_X = this.boxX + this.boxW + this.boxW*0.1;
        region.MINIMUM_Y = this.boxY - this.boxH*0.1;
        region.MAXIMUM_Y = this.boxY + this.boxH + this.boxH*0.1;
        region.MINIMUM_Z = 0.0;
        region.MAXIMUM_Z = 1.0;
        return region;
    }

    public Integer getDataSize() {
        return 2;
    }

    public Integer getDataSize(int axis) {
        return 2;
    }

    public Double getData(int x, int y) {
        return 0.0;
    }

    public Double getDataX(int index) {
        if(index==0) return this.boxX;
        return this.boxX + this.boxW;
    }

    public Double getDataY(int index) {
        if(index==0) return this.boxY;
        return this.boxY + this.boxH;
    }

    public Double getErrorX(int index) {
        return 0.0;
    }

    public Double getErrorY(int index) {
        return 0.0;
    }

    public Attributes getAttributes() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return attr;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("  %-14s : %12.6f %12.6f %12.6f %12.6f", 
                this.boxName,this.boxX,this.boxY, this.boxW,this.boxH));
        return str.toString();
    }
}
