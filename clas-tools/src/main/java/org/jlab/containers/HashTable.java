/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.containers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gavalian
 */
public class HashTable extends DefaultTableModel {
    
    private HashCollection<TableRow>  hashCollection = null;
    private List<String>              columns        = new ArrayList<String>();
    
    public HashTable(){
        this.hashCollection = new HashCollection(3);
    }
    
    public HashTable(int nindex){
        this.hashCollection = new HashCollection(nindex);
    }
    
    public HashTable(int nindex, String... list){
        this.hashCollection = new HashCollection(nindex);
        for(String item : list){
            columns.add(item);
        }
    }
    
    public void addRowAsInt(String[] values){
        int ic = this.hashCollection.getIndexCount();
        int[]    array = new int[values.length-ic];
        int[]    index = new int[ic];
        for(int i = 0 ; i < ic; i++) index[i] = Integer.parseInt(values[i]);
        for(int i = ic; i < values.length; i++) array[i-ic] = Integer.parseInt(values[i]);
        this.addRow(array, index);
    }
    
    public void addRowAsDouble(String[] values){
        int ic = this.hashCollection.getIndexCount();
        double[] array = new double[values.length-ic];
        int[]    index = new int[ic];
        for(int i = 0 ; i < ic; i++) index[i] = Integer.parseInt(values[i]);
        for(int i = ic; i < values.length; i++) array[i-ic] = Double.parseDouble(values[i]);
        this.addRow(array, index);
    }
    
    public void addRowAsDouble(String[] values, int... index){
        TableRow  row = new TableRow();
        double[] array = new double[values.length];
        for(int i=0;i<array.length;i++) array[i] = Double.parseDouble(values[i]);
        this.addRow(array, index);
    }
    
    public void addRow(int[] values, int... index){
        TableRow  row = new TableRow();
        row.set(values);
        this.hashCollection.add(row, index);
    }
    
    public void addRow(double[] values, int... index){
        TableRow  row = new TableRow();
        row.set(values);
        this.hashCollection.add(row, index);
    }
    
    public void addRow(String[] array, int... index){
        
        for(int i = 0; i < array.length;i++){
            
        }
    }
    
    public void addRow(TableRow row, int... index){
        this.hashCollection.add(row, index);
    }
    
    public TableRow  getRow(int... index){
        return this.hashCollection.getItem(index);
    }
    
    public void show(){
        this.hashCollection.show();
    }
    
    
    @Override
    public String getColumnName(int col) {
        
        if(col>2){
            return this.columns.get(col-3);
        }
        return "A";
    }
    
     @Override
    public int getColumnCount(){
        int ncolumns = 0;
        try{
            ncolumns = this.hashCollection.getIndexCount() + this.columns.size();
        } catch(Exception e){
            
        }
        return ncolumns;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
       //all cells false
       return false;
    }
    
    @Override
    public int getRowCount(){
        //System.out.println("RAW COUNT is " + this.arrayEntries.size());
        //return this.arrayEntries.size();
        //return 2;
        int nrows = 0;
        try {
            nrows = this.hashCollection.getMap().size();
        } catch (Exception e){
            
        }
        return nrows;
    }
    
    @Override
    public Object getValueAt(int row, int column) { 
        Set<Long>  keys = this.hashCollection.getMap().keySet();
        int   ic = this.hashCollection.getIndexCount();
        Iterator   iter = keys.iterator();
        Long       value = (Long) iter.next();
        for(int i = 0; i < row; i++){
            value = (Long) iter.next();
        }
        //System.out.println();
        if(column<this.hashCollection.getIndexCount()){
            Integer index = HashGenerator.getIndex(value, column);
            return index.toString();
        }
        TableRow  trow = this.hashCollection.getMap().get(value);
        return trow.get(column-ic).toString();
    }
    
    public static void main(String[] args){
        HashTable  table = new HashTable(3,"a","b","c");
        table.addRowAsDouble(new String[]{"21","7","1","0.5","0.1","0.6"});
        table.addRowAsDouble(new String[]{"22","8","2","0.6","0.2","0.7"});
        table.addRowAsDouble(new String[]{"23","9","3","0.7","0.3","0.8"});
        table.addRowAsDouble(new String[]{"24","10","4","0.8","0.4","0.9"});
        table.show();
    }
}
