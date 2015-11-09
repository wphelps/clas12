/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author gavalian
 */
public class DetectorCollectionPane extends JPanel implements TreeSelectionListener {
    JTree  detectorTree = null;
    List<IDetectorCollectionListener>  listeners = new ArrayList<IDetectorCollectionListener>();
    
    public DetectorCollectionPane(){
        super();
        this.setLayout(new BorderLayout());
        this.init();
    }
    
    public void init(){
        this.detectorTree = new JTree();
        this.detectorTree.addTreeSelectionListener(this);
        JScrollPane  scrollPane = new JScrollPane(this.detectorTree);
        this.add(scrollPane);        
    }
    
    public void setCollection(DetectorCollection collection){
        this.detectorTree.setModel(collection.getTreeModel());
    }
    
    public void addListener(IDetectorCollectionListener lt){
        this.listeners.add(lt);
    }
    
    public void valueChanged(TreeSelectionEvent se) {
         JTree tree = (JTree) se.getSource();
         DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                 .getLastSelectedPathComponent();
         String selectedNodeName = selectedNode.toString();
         if (selectedNode.isLeaf()) {
             TreePath[] paths = tree.getSelectionPaths();
             System.out.println(selectedNodeName);
             if(paths.length>0){
                 if(paths[0].getPathCount()>3){
                     int sector = Integer.parseInt(paths[0].getPathComponent(1).toString());
                     int layer  = Integer.parseInt(paths[0].getPathComponent(2).toString());
                     int comp   = Integer.parseInt(paths[0].getPathComponent(3).toString());
                     for(IDetectorCollectionListener lt : this.listeners){
                         try {
                             lt.detectorAction(sector, layer, comp);
                         } catch (Exception e){
                             System.out.println("[DetectorCollectionPane] ---> (error) while executing listener");
                         }
                     }
                 }
             }
             
         }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        DetectorCollectionPane  pane = new DetectorCollectionPane();
        DetectorCollection<Double>  numbers = new DetectorCollection<Double>();
        
        for(int sector = 0; sector < 6; sector++){
            for(int region = 0; region < (sector+1)*2; region++){
                for(int c = 0; c < (region+1)*4; c++){
                    numbers.add(sector, region, c, 1.0);
                }
            }
        }
        pane.setCollection(numbers);
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
    }

  
}
