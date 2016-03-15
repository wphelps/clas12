/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.viewer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import org.jlab.geom.geant.Geant4Basic;
import org.jlab.geom.geant.Geant4Mesh;

/**
 *
 * @author gavalian
 */
public class MeshStore {
    
    String    storeName  = "";
    MeshView  motherMesh = null;
    
    Map<String,MeshView>  meshStore = new TreeMap<String,MeshView>();
    private   Boolean     isVisible = true;
    
    public MeshStore(){
        
    }
    
    public String getName(){
        return this.storeName;
    }
    
    public void init(Geant4Basic volume){
        this.storeName = volume.getName();
        List<MeshView>  meshes = Geant4Mesh.getMesh(volume);
        this.meshStore.clear();
        int counter = 0;
        for(MeshView mesh : meshes){
            this.meshStore.put("mesh_" + counter, mesh);
            counter++;
        }
    }
    
    public Map<String,MeshView>  getMap(){return this.meshStore;}
    
    public void setVisible(Boolean flag){
        for(Map.Entry<String,MeshView> entry : this.meshStore.entrySet()){
            entry.getValue().setVisible(flag);
        }
    }
    
    public TreeItem  getMeshTree(){
        //TreeItem<String>  rootItem = new TreeItem<String>(this.storeName);
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<String>(this.storeName);
        rootItem.setIndependent(true);
        rootItem.setSelected(true);
        rootItem.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //System.out.println("Things change " + newValue);
                setVisible(newValue);
            }

            });
        for(Map.Entry<String,MeshView> entry : this.meshStore.entrySet()){
            
            TreeItem<String>  item = new TreeItem<String>(entry.getKey());
            rootItem.getChildren().add(item);
        }
        return rootItem;
    }
    
    
    public void setMaterial(double red, double green , double blue, double alpha){
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(new Color(red,green,blue,alpha));
        mat.setSpecularColor(new Color(red*0.5,green*0.5,blue*0.5,alpha));
        mat.setSpecularPower(0.5);
        for(Map.Entry<String,MeshView> entry : this.meshStore.entrySet()){
            entry.getValue().setMaterial(mat);
        }
    }
}
