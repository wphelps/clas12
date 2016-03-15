/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.viewer;

import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.detector.geant4.FTOFGeant4Factory;
import org.jlab.geom.base.ConstantProvider;
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
             Geant4Basic  mVolume    = factory.createSector(cp,sector,layer);
             store.init(mVolume);
        }
        return store;
    }
}
