/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.evio;

import java.util.ArrayList;
import javafx.scene.control.TreeItem;
import org.jlab.clas12.raw.EvioTreeBranch;
import org.jlab.coda.jevio.DataType;
import org.jlab.coda.jevio.EvioNode;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.decode.EvioEventDecoder;

/**
 *
 * @author gavalian
 */
public class EvioDataEventTools {
    public static void getEventTree(EvioDataEvent event){
        TreeItem<String>  rootItem = new TreeItem<String>("EVENT");
        
    }
    
    public static TreeItem<String> getRawDataTree(EvioDataEvent event){
        EvioEventDecoder decoder = new EvioEventDecoder();
        TreeItem<String> rootNode = new TreeItem<String>("EVENT");
        ArrayList<EvioTreeBranch> branches = decoder.getEventBranches(event);
        for(EvioTreeBranch branch : branches){
            Integer branchTag = branch.getTag();
            TreeItem<String>  container = new TreeItem<String>(branchTag.toString());
            for(EvioNode node : branch.getNodes()){
                if(node.getDataTypeObj()==DataType.COMPOSITE){
                    Integer nodeTag = node.getTag();
                    TreeItem<String> leaf = new TreeItem<String>(nodeTag.toString());
                    container.getChildren().add(leaf);
                }
                //System.out.println("\t\t -----> NODE " + node.getTag() + " : " + node.getDataTypeObj());
            }
            rootNode.getChildren().add(container);
        }
        return rootNode;
    }
}
