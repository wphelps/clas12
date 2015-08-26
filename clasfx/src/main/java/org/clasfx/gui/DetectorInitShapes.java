/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.gui;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.clasfx.detector.DetectorShape3D;
import org.jlab.clas.detector.DetectorType;

/**
 *
 * @author gavalian
 */
public class DetectorInitShapes {
    public static ArrayList<DetectorShape3D>  getShapesDC(){
        ArrayList<DetectorShape3D> shapes = new ArrayList<DetectorShape3D>();
        double[] diameter = new double[]{50,60, 80,90, 110, 120};
        for(int sector = 0; sector < 6; sector++){
            for(int region =0; region < 6; region++){
                DetectorShape3D shape = new DetectorShape3D(DetectorType.DC,sector,region,-1);
                shape.createPathArc(diameter[region], 8, sector*60+2, (sector+1)*60-2, 20);
                if(region%2==0){
                    shape.setColor(150,150,250);
                }
                shapes.add(shape);
            }
        }
        return shapes;
    }
    
    public static ArrayList<DetectorShape3D>  getShapesFTOF(){
        ArrayList<DetectorShape3D> shapes = new ArrayList<DetectorShape3D>();
        double[] diameter = new double[]{180,190,200};
        for(int sector = 0; sector < 6; sector++){
            for(int region =0; region < 3; region++){
                DetectorShape3D shape = new DetectorShape3D(DetectorType.FTOF,sector,region,-1);
                shape.createPathArc(diameter[region], 8, sector*60+2, (sector+1)*60-2, 20);
                if(region%2!=0){
                    shape.setColor(100, 200,100);
                } else {
                    shape.setColor(150, 250,150);
                }
                shapes.add(shape);
            }
        }
        return shapes;
    }
    
    public static ArrayList<DetectorShape3D>  getShapesFTOFPaddles(int layer){
        ArrayList<DetectorShape3D> shapes = new ArrayList<DetectorShape3D>();
        int[] padd = new int[]{24,67,5};
        
        double[] diameter = new double[]{180,190,200};
        for(int sector = 0; sector < 6; sector++){
            for(int region =0; region < padd[layer]; region++){
                double startDiameter = 50;
                double paddleWidth   = 8;
                DetectorShape3D shape = new DetectorShape3D(DetectorType.FTOF,sector,layer,region);
                shape.createPathArc(startDiameter + paddleWidth*region, 
                        paddleWidth, sector*60+2, (sector+1)*60-2, 20);
                if(region%2!=0){
                    shape.setColor(100, 200,100);
                } else {
                    shape.setColor(150, 250,150);
                }
                shapes.add(shape);
            }
        }
        return shapes;
    }
    
    public static ArrayList<DetectorShape3D>  getShapesPCAL(){
        ArrayList<DetectorShape3D> shapes = new ArrayList<DetectorShape3D>();
        Paint pcalcolor_0 = Color.rgb(180, 120, 120);
        Paint pcalcolor_1 = Color.rgb(220, 180, 180);
        double[] diameter = new double[]{250,254,258};
        for(int sector = 0; sector < 6; sector++){
            for(int region =0; region < 3; region++){
                DetectorShape3D shape = new DetectorShape3D(DetectorType.EC,sector,region,-1);
                shape.createPathArc(diameter[region], 4, sector*60+2, (sector+1)*60-2, 20);
                if(region==1){
                    shape.setColor(pcalcolor_0);
                } else {
                    shape.setColor(pcalcolor_1);
                }
                shapes.add(shape);
            }
        }
        return shapes;
    }
    
}
