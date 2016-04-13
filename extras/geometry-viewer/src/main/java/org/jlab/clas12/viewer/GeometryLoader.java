/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.viewer;

import java.util.List;
import javafx.scene.shape.MeshView;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.detector.geant4.BSTGeant4Factory;
import org.jlab.detector.geant4.ECGeant4Factory;
import org.jlab.detector.geant4.FTOFGeant4Factory;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.component.ScintillatorMesh;
import org.jlab.geom.detector.ftof.FTOFDetectorMesh;
import org.jlab.geom.detector.ftof.FTOFFactory;
import org.jlab.geom.geant.Geant4Basic;

/**
 *
 * @author gavalian
 */
public class GeometryLoader {
    
    
    
    public static MeshStore  getGeometry(String name, int sector, int layer){
        MeshStore  store = new MeshStore();
        if(name.compareTo("FTOF")==0){
             ConstantProvider  cp = DataBaseLoader.getConstantsFTOF();
             FTOFGeant4Factory  factory = new FTOFGeant4Factory(cp);
             //Geant4Basic  mVolume    = factory.createSector(cp,sector,layer);
             //store.init(mVolume);
        }
        
        if(name.compareTo("BST")==0){
            ConstantProvider  cp = DataBaseLoader.getConstantsBST();
            BSTGeant4Factory  factory = new BSTGeant4Factory();
            Geant4Basic  mVolume = factory.createDetector(cp);
            store.init(mVolume);
        }
        return store;
    }
    
    public static MeshStore  getGeometry(String name){
        MeshStore  store = new MeshStore();
        if(name.compareTo("BST")==0){
            ConstantProvider  cp = DataBaseLoader.getConstantsBST();
            BSTGeant4Factory  factory = new BSTGeant4Factory();
            Geant4Basic  mVolume = factory.createDetector(cp);
            store.init(mVolume);
        }
        if(name.compareTo("EC")==0){
            ECGeant4Factory  factory = new ECGeant4Factory();
            Geant4Basic mVolume = factory.getPCAL();
            store.init(mVolume);
        }
        return store;
    }
    
    public static MeshStore  getGeometryGemc(){
        MeshStore  store = new MeshStore();
        ConstantProvider  cp = DataBaseLoader.getConstantsFTOF();
        FTOFFactory factory = new FTOFFactory();
        FTOFDetectorMesh detector = factory.getDetectorGeant4(cp);

        int counter = 1;
        for(int sector = 1; sector <= 6; sector++){
            for(int superlayer = 1; superlayer <=3; superlayer++){
                List<ScintillatorMesh>  sci = detector.getSector(sector).getSuperlayer(superlayer).getLayer(1).getAllComponents();
                for(ScintillatorMesh m : sci){
                    //m.rotateX(Math.toRadians(90.0));
                    //m.translateXYZ(20.0, 0.0, 720.0);
                    //m.translateXYZ(0.0,217.19269643788255,  654.3257910569964);
                    /*
                    if(sector==1){
                    m.translateXYZ(0.0,217.19269643788255, 654.3257910569964);
                    }
                    if(sector==2){
                    m.translateXYZ( 188.09439263164822,108.5963482189413, 654.3257910569964);
                    }
                    if(sector==3){
                    m.translateXYZ( 188.09439263164822,-108.5963482189413, 654.3257910569964);
                    }*/
                    //double zrot = -90.0+(sector-1)*60;
                    //m.rotateX(Math.toRadians(-115.0));
                    //m.rotateZ(Math.toRadians(zrot));
                    //m.rotateZ(Math.toRadians(20.0));
                    double phi = Math.toRadians((sector-1)*60.0);
                    double theta = Math.toRadians(25.0);
                    double r = 725.0;
                    double xp = Math.sin(theta)* Math.cos(phi);
                    double yp = Math.sin(theta)* Math.sin(phi);
                    double zp = Math.cos(theta);
                    //m.translateXYZ( r * xp,r*yp
                    //        , r * zp);
                    //System.out.println(" TRANSLATION  " + xp + " " + yp + " " + zp);
                    //System.out.println("   X DIR = " +  Math.sin(theta) + "  " +  Math.cos(phi));
                    MeshView mesh = m.getVolumeMesh().getMeshView();
                    //store.getMap().put("sector_" + sector + "_mesh_"+counter, mesh);
                    store.addMesh("sector_" + sector + "_mesh_"+counter, mesh,superlayer+1);
                    counter++;
                }
            }
        }
        return store;
    }
    
}
