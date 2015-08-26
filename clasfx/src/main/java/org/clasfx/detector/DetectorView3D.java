/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.detector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 *
 * @author gavalian
 */
public class DetectorView3D {
    
    Group rootGroup  = null; //new Group();
    Group lightGroup = null; //new Group();
    Group meshGroup  = null; 
    Stage primaryStage = null;
    Camera camera = null;
    DoubleProperty  mousePressedX = new SimpleDoubleProperty();
    DoubleProperty  mousePressedY = new SimpleDoubleProperty();
    Scene scene = null;
    
    Double rotationX = 0.0;
    Double rotationY = 0.0;
    Double rotationZ = 0.0;
    
    SubScene  subScene = null;
    
    public DetectorView3D(){
        rootGroup = new Group();
        this.initSubScene();
        
        subScene.widthProperty().addListener(observable -> actionResize());
        subScene.heightProperty().addListener(observable -> actionResize());
        //this.moveMesh();
    }
    
    public void moveMesh(){
        meshGroup.setTranslateX( 0.5 * this.subScene.getWidth());
        meshGroup.setTranslateY( 0.5 * this.subScene.getHeight());
        //for(Node node : this.meshGroup.getChildren()){
        //    node.setTranslateX( 0.25 * this.subScene.getWidth());
        //    node.setTranslateY( 0.25 * this.subScene.getHeight());
        //}
    }
    
    public void actionResize(){
        System.out.println("RESIZE + " + this.subScene.getWidth() + " "
        + this.subScene.getHeight());
        this.moveMesh();
    }
    
    public void initSubScene(){
        rootGroup  = new Group();
        meshGroup  = new Group();
        lightGroup = new Group();
        
        this.addLight();
        //this.initMesh();
        this.initAxis();
        rootGroup.getChildren().add(meshGroup);
        rootGroup.getChildren().add(lightGroup);
        subScene = new SubScene(rootGroup,500,500,true,SceneAntialiasing.BALANCED);
        
        meshGroup.setTranslateX(250);
        meshGroup.setTranslateY(250);
        meshGroup.setRotationAxis(new Point3D(250.0,250.0,0.0));
        camera = new PerspectiveCamera();
        camera.setTranslateZ(-800);
        camera.setTranslateX(0.0);
        camera.setTranslateY(0.0);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        //camera.setRotationAxis(Point3D.ZERO);
        //camera.setRotate(25.0);
        subScene.setCamera(camera);
        
        
        subScene.setOnMouseDragged((event) -> { mouseDragged( event);});
        subScene.setOnMousePressed( (event) -> { mousePressed(event);});
        subScene.setFill(Color.rgb(250, 250, 240));
        
    }
    
    public SubScene getSubScene(){
        return subScene;
    }
    
    public void addMesh(TriangleMesh mesh, PhongMaterial mat){
        MeshView meshview = new MeshView(mesh);
        meshview.setMaterial(mat);
        meshview.setDrawMode(DrawMode.FILL);
        meshGroup.getChildren().add(meshview);
    }
    
    public void initMesh(){
        PhongMaterial matBlue = new PhongMaterial();
        matBlue.setSpecularColor(Color.rgb(70, 70, 130, 0.3));
        matBlue.setDiffuseColor(Color.rgb(50, 50, 150, 0.3));
        
        PhongMaterial matRed = new PhongMaterial();
        matRed.setSpecularColor( Color.rgb(130, 70, 70, 0.3));
        matRed.setDiffuseColor(  Color.rgb(150, 50, 50, 0.3));
        
        for(int xc = 0; xc < 10; xc++){
            Box box = new Box(80,80,500+xc*50);
            if(xc%2==0){
                box.setMaterial(matBlue);
            } else {
                box.setMaterial(matRed);
            }
            box.setTranslateX(10+xc*100);
            box.setTranslateY(200);
            this.meshGroup.getChildren().add(box);
        }
        
                
    }
    
    public void initAxis(){
        
        PhongMaterial axisMaterialX = new PhongMaterial();
        axisMaterialX.setDiffuseColor( Color.RED);
        axisMaterialX.setSpecularColor(Color.RED);
        Cylinder  axisX = new Cylinder(1.0,500.0);
        axisX.setMaterial(axisMaterialX);
        
        PhongMaterial axisMaterialY = new PhongMaterial();
        axisMaterialY.setDiffuseColor(  Color.GREEN );
        axisMaterialY.setSpecularColor( Color.GREEN );
        
        Cylinder  axisY = new Cylinder(1.0,500.0);
        axisY.setMaterial(axisMaterialY);
        
        PhongMaterial axisMaterialZ = new PhongMaterial();
        axisMaterialZ.setDiffuseColor(  Color.BLUE );
        axisMaterialZ.setSpecularColor( Color.BLUE );
        
        Cylinder  axisZ = new Cylinder(1.0,500.0);
        axisZ.setMaterial(axisMaterialZ);
        
        axisY.setRotationAxis(Rotate.Z_AXIS);
        axisY.setRotate(90.0);
        
        axisZ.setRotationAxis(Rotate.X_AXIS);
        axisZ.setRotate(90.0);
        
        this.meshGroup.getChildren().add(axisX);
        this.meshGroup.getChildren().add(axisY);
        this.meshGroup.getChildren().add(axisZ);
    }
    
    public void addLight(){
        AmbientLight light = new AmbientLight(Color.rgb(150,150,150));
        PointLight   point = new PointLight(Color.rgb(250, 250, 250));
        PointLight   point2 = new PointLight(Color.rgb(250, 250, 250));
        
        point.setVisible(true);
        point2.setVisible(true);
        lightGroup.getChildren().add(light);
        point.setTranslateZ(-400);
        point.setTranslateX(-100);
        point.setTranslateY(-100);
        
        point2.setTranslateZ(800);
        point2.setTranslateX(300);
        point2.setTranslateY(300);
        
        lightGroup.getChildren().add(point);
        lightGroup.getChildren().add(point2);
    }
    
    public void addShape(){
        
        Sphere sphere=new Sphere(100);
        sphere.setTranslateX(200);
        sphere.setTranslateY(200);
        
        PhongMaterial mat = new PhongMaterial();
        //mat.setSpecularColor(Color.);
        mat.setDiffuseColor(Color.rgb(100,100,240,0.3));
        
        sphere.setMaterial(mat);
        
        Cylinder cube = new Cylinder(100,50,50);
        PhongMaterial mat2 = new PhongMaterial();
        mat2.setSpecularColor(Color.LIGHTCORAL);
        mat2.setDiffuseColor(Color.CORAL);
        cube.setMaterial(mat2);
        cube.setTranslateX(200);
        cube.setTranslateY(200);
        cube.setTranslateZ(400);
        this.rootGroup.getChildren().add(sphere);
        this.rootGroup.getChildren().add(cube);
        
    }
    
    
    
    public void mouseDragged(MouseEvent event){
        //double x = event.
        double x = event.getSceneX();
        double y = event.getSceneY();
        double dx = this.mousePressedX.getValue() - x;
        double dy = this.mousePressedY.getValue() - y;
        System.out.println("MOUSE DRUGGED " + dx + " " + dy );
        //        + "  FROM = " + this.mousePressedX.getValue() + "  "
        //        + this.mousePressedY.getValue());
        double xc = camera.getTranslateX();
        double yc = camera.getTranslateY();
        double zc = camera.getTranslateZ();
        double r  = Math.sqrt(xc*xc + yc*yc + zc*zc);
        double theta = Math.sqrt(xc*xc + yc*yc)/zc;
        double phi   = Math.atan2(yc, xc);
        double newphi = phi + dx;
        double newtheta = theta + dy;
        System.out.println(" THETA = " + theta + " " + newtheta
                + " PHI = " + phi + " " + newphi);
        //camera.setTranslateX(500);
        //camera.setTranslateZ(500);
        //double rot = camera.getRotate();
        //camera.setRotationAxis(Rotate.Z_AXIS);
        
        
        if(Math.abs(dx)>Math.abs(dy)){
            //double rot = meshGroup.getRotate();
            //meshGroup.setRotate(rot + 2.0);
            rotationY += 2.0;
            //meshGroup.setRotate(rotationY);
        } else {
            //meshGroup.setRotationAxis(Rotate.X_AXIS);
            rotationX += 2.0;
            //meshGroup.setRotate(rotationX);
        }
        System.out.println("X/Y/Z = " + rotationX + " " + rotationY);
        
        
        meshGroup.setRotationAxis(Rotate.X_AXIS);        
        meshGroup.setRotate(rotationX);
        
        meshGroup.setRotationAxis(Rotate.Y_AXIS); 
        meshGroup.setRotate(rotationY);
        /*        
        if(dx>0){
            camera.setRotate(rot + 1.0);
        } else {
            camera.setRotate(rot - 1.0);
        }
        */

        //camera.setRotationAxis(Point3D.ZERO);
        //camera.set
        
    }
    
    public Stage getStage(){
        return this.primaryStage;
    }

    private void mousePressed(MouseEvent event) {
        this.mousePressedX.setValue(event.getSceneX());
        this.mousePressedY.setValue(event.getSceneY());
        System.out.println("Mouse pressed");
    }
}
