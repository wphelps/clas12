/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.tools;

import com.sun.javafx.geom.Path2D;
import java.util.ArrayList;
import javafx.event.EventHandler;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.clasfx.detector.DetectorShape3D;
import org.jlab.clas.detector.DetectorDescriptor;
import org.jlab.clas.detector.DetectorType;
import org.jlab.geom.prim.Path3D;
import org.jlab.geom.prim.Point3D;

/**
 *
 * @author gavalian
 */
public class DetectorCanvas extends Canvas {
    
    private String  detectorName = "EC";
    private final ArrayList<DetectorShape3D> detectorShapes = new ArrayList<DetectorShape3D>();
    
    private final Point3D   minCoordinates = new Point3D();
    private final Point3D   maxCoordinates = new Point3D();
    private final DetectorDescriptor  descriptor = new DetectorDescriptor();
    private final DetectorDescriptor  selectedDescriptor = new DetectorDescriptor();
    
    public DetectorCanvas(String name,int xsize, int ysize){
        super();        
        this.selectedDescriptor.setType(DetectorType.UNDEFINED);
        this.selectedDescriptor.setSectorLayerComponent(-1, -1, -1);
        this.prefWidth(xsize);
        this.prefHeight(ysize);
        this.widthProperty().addListener(observable -> redraw());
        this.heightProperty().addListener(observable -> redraw());
        this.detectorName = name;
        this.addEventFilter(MouseEvent.MOUSE_MOVED, (final MouseEvent mouseEvent) -> {
            //System.out.println("Mouse moved too : "
            //        + mouseEvent.getX() + "  " + mouseEvent.getY());
            mouseMovedCallBack(mouseEvent.getX(), mouseEvent.getY());
        });
    }
    
    public String getDetectorName(){ return this.detectorName;}
    
    public final void redraw(){
        
        
        double width  = this.getWidth();
        double height = this.getHeight();
        this.updateShapeRegion();
        //System.out.println("REDRAWING CANVAS WITH DETECTOR VIEWS " + width + "  "
        //        + height + "  " + this.detectorShapes.size());
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.rgb(148, 150, 143));
        gc.fillRect(0, 0, width, height);
        for(int loop = 0; loop < this.detectorShapes.size(); loop++){
            int npoints = this.detectorShapes.get(loop).getPath().size();
            double[] xPoints = new double[npoints];
            double[] yPoints = new double[npoints];
            for(int np = 0; np < npoints; np++){
                Point3D point = this.detectorShapes.get(loop).getPath().point(np);
                xPoints[np] = this.translateCoordinate(point.x(),
                        0, width, this.minCoordinates.x(), this.maxCoordinates.x());
                yPoints[np] = this.translateCoordinate(point.y(),
                        0, height, this.minCoordinates.y(), this.maxCoordinates.y());

            }
            /*
            if(loop%2==0){
                gc.setFill(Color.rgb(253, 136, 27));
            } else {
                gc.setFill(Color.rgb(250, 183, 10));
                //gc.setFill(Color.rgb(243, 94, 9));
            }*/
            if(this.detectorShapes.get(loop).getDescriptor().compare(this.selectedDescriptor)==true){
                gc.setFill(Color.rgb(255, 0, 0));
            } else {
                gc.setFill(this.detectorShapes.get(loop).getColor());
            }
            gc.fillPolygon(xPoints, yPoints, npoints);
        }
        /*
        System.out.println("[REDRAW] CANVAS " + this.detectorName 
                + " : " + width + " x " + height);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.rgb(218, 220, 203));
        gc.fillRect(0, 0, width, height);
        gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, width, height);*/
    }   
    
    private double translateCoordinate(double value, double cmin, double cmax,
            double rmin, double rmax){
        double xv = (value-rmin)/(rmax-rmin);
        xv = xv * (cmax-cmin);
        return (cmin+xv);
    }
    
    public void updateShapeRegion(){
        this.minCoordinates.set(0.0, 0.0, 0.0);
        this.maxCoordinates.set(0.0, 0.0, 0.0);
        
        for(DetectorShape3D  shape : this.detectorShapes){
            int npoints = shape.getPath().size();
            for(int loop = 0; loop < npoints; loop++){
                this.minPoint(minCoordinates, shape.getPath().point(loop));
                this.maxPoint(maxCoordinates, shape.getPath().point(loop));                
            }
        }
        
        double xrange = this.maxCoordinates.x() - this.minCoordinates.x();
        double yrange = this.maxCoordinates.y() - this.minCoordinates.y();
        this.minCoordinates.setX(this.minCoordinates.x()-0.1*xrange);
        this.maxCoordinates.setX(this.maxCoordinates.x()+0.1*xrange);
        
        this.minCoordinates.setY(this.minCoordinates.y()-0.1*yrange);
        this.maxCoordinates.setY(this.maxCoordinates.y()+0.1*yrange);
        /*
        System.out.println("UPDATE COORDINATES : COMPONENTS COUNT = " + this.detectorShapes.size());
        System.out.println(this.minCoordinates);
        System.out.println(this.maxCoordinates);
        */
    }
    
    private void minPoint(Point3D pcheck, Point3D ref){
        if(ref.x()<pcheck.x()) pcheck.setX(ref.x());
        if(ref.y()<pcheck.y()) pcheck.setY(ref.y());
        if(ref.z()<pcheck.z()) pcheck.setZ(ref.z());        
    }
    
    private void maxPoint(Point3D pcheck, Point3D ref){
        if(ref.x()>pcheck.x()) pcheck.setX(ref.x());
        if(ref.y()>pcheck.y()) pcheck.setY(ref.y());
        if(ref.z()>pcheck.z()) pcheck.setZ(ref.z());        
    }
    
    public void addShape(DetectorShape3D shape3d){
        this.detectorShapes.add(shape3d);
    }
    
    private boolean  pointInPath(Path3D path, double x, double y){
        int i, j;
        boolean c = false;
        int nvert = path.size();
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( (( path.point(i).y()>y) != (path.point(j).y()>y)) &&
                    (x < ( path.point(j).x()-path.point(i).x()) * 
                    (y-path.point(i).y()) / (path.point(j).y()-path.point(i).y()) +
                    path.point(i).x()))
                c = !c;
        }
        return c;
    }
    
    private void mouseMovedCallBack(Double x, Double y){
        double width  = this.getWidth();
        double height = this.getHeight();
        double xp = this.translateCoordinate(x, this.minCoordinates.x(), 
                this.maxCoordinates.x(), 0, width);
        double yp = this.translateCoordinate(y, this.minCoordinates.y(), 
                this.maxCoordinates.y(), 0, height);
        //System.out.println("LOOKING FOR COORDINATE " + xp + " " + yp 
        //        + "  [ " + x + " " + y + "]");
        int counter = 0;
        for(DetectorShape3D shape : this.detectorShapes){
            boolean pass = this.pointInPath(shape.getPath(), xp, yp);
            if(pass==true){
                if(shape.getDescriptor().compare(this.selectedDescriptor)==false){
                    this.selectedDescriptor.setType(shape.getDescriptor().getType());
                    this.selectedDescriptor.setSectorLayerComponent(
                            shape.getDescriptor().getSector(),
                            shape.getDescriptor().getLayer(),
                            shape.getDescriptor().getComponent()
                    );
                    System.out.println("STATUS " + counter + "  ->  [ " + 
                            shape.getDescriptor().getType() + " ]" + pass + 
                            "  " + shape.getDescriptor().getSector() + " / " + 
                            shape.getDescriptor().getLayer() + 
                            " / " + shape.getDescriptor().getComponent());
                    this.redraw();
                }
            }
            counter++;
        }
        //System.out.println("aloha");
    }
}
