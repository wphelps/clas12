/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author gavalian
 */
public class ConstantsTable extends DefaultTableModel {
    
    TreeMap<Integer,ConstantsTableEntry> tableEntries = new TreeMap<Integer,ConstantsTableEntry>();
    List<ConstantsTableEntry>            arrayEntries = new ArrayList<ConstantsTableEntry>();
    
    String[]  tableColumns = null;
    DetectorType  type     = DetectorType.UNDEFINED;
        
    public ConstantsTable(DetectorType t, String[] columns){
    
        this.tableColumns = columns;
        //this.tableColumns = new String[3];
        this.type = t;
        this.addColumn("Detector");
        this.addColumn("Sector");
        this.addColumn("Layer");
        this.addColumn("Component");
        
        for(String c : columns){
            this.addColumn(c);
        }    
    }
    
    public ConstantsTableEntry getEntry(int sector, int layer, int component){
        int key = DetectorDescriptor.generateHashCode(sector, layer, component);
        if(this.tableEntries.containsKey(key)==true) return this.tableEntries.get(key);
        return null;
    }
    
    public void addEntry(int s, int l, int c){
        ConstantsTableEntry entry = new ConstantsTableEntry(s,l,c,tableColumns.length);
        this.tableEntries.put(entry.getDescriptor().getHashCode(), entry);
        this.arrayEntries.add(entry);
    }
    
    public double getValue(int sector, int layer, int component, String name){
        int index = this.getIndexForName(name);
        return this.getValue(sector, layer, component, index);
    }
    
    public double getValue(int sector, int layer, int component, int index){
        int key = DetectorDescriptor.generateHashCode(sector, layer, component);
        if(this.tableEntries.containsKey(key)==true) return this.tableEntries.get(key).getData(index);
        return 0.0;
    }
    
    public int getIndexForName(String name){
        for(int loop = 0; loop < this.tableColumns.length;loop++){
            if(name.compareTo(this.tableColumns[loop])==0) return loop;
        }
        return -1;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        for(Map.Entry<Integer,ConstantsTableEntry> entry : this.tableEntries.entrySet()){
            str.append(entry.getValue().toString());              
        }
        
        return str.toString();
    }    
    
    
    
    @Override
    public int getColumnCount(){
        int ncolumns = 0;
        try{
            ncolumns = 4 + tableColumns.length;
        } catch(Exception e){
            
        }
        return ncolumns;
    }

    
    @Override
    public int getRowCount(){
        //System.out.println("RAW COUNT is " + this.arrayEntries.size());
        //return this.arrayEntries.size();
        //return 2;
        int nrows = 0;
        try {
            nrows = arrayEntries.size();
        } catch (Exception e){
            
        }
        return nrows;
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        if(column==0){
            return this.type.getName();            
        }
        if(column==1){
            return ((Integer) this.arrayEntries.get(row).getDescriptor().getSector()).toString();
        }
        
        if(column==2){
            return ((Integer) this.arrayEntries.get(row).getDescriptor().getLayer()).toString();
        }
        if(column==3){
            return ((Integer) this.arrayEntries.get(row).getDescriptor().getComponent()).toString();
        }
                
        return ((Double) this.arrayEntries.get(row).getData(column-4));
    }
    
    public static void main(String[] args){
        
        ConstantsTable table = new ConstantsTable(DetectorType.FTOF,new String[]{"geoMean","error","c","d"});
        table.addEntry(1, 1, 0);
        table.addEntry(1, 1, 1);
        table.addEntry(1, 1, 2);
        
        table.getEntry(1, 1, 0).setData(0, 0.5);
        table.getEntry(1, 1, 0).setData(1, 0.6);
        table.getEntry(1, 1, 0).setData(2, 0.7);
        
        
        
        System.out.println(table.toString());
        
        System.out.println("VALUE = " + table.getValue(1, 1, 0, "error"));
    }
}
