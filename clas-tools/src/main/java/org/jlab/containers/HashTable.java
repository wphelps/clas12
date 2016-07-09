/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.containers;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gavalian
 */
public class HashTable extends DefaultTableModel {
    
    private HashCollection<TableRow>  hashCollection = null;
    private List<String>              columns        = new ArrayList<String>();
    private List<String>              types          = new ArrayList<String>();
    private Map<Integer,HashTableRowConstrain>  constrains = new TreeMap<Integer,HashTableRowConstrain>();              
    private String                              htName     = "HashTable";
     
    
    public HashTable(){
        this.hashCollection = new HashCollection(3);
    }
    
    public HashTable(int nindex){
        this.hashCollection = new HashCollection(nindex);
    }
    
    public HashTable(String name, int index){
        
    }
    
    public void setName(String name){
        this.htName = name;        
    }
    
    public String getName(){return this.htName;}
    
    public HashTable(int nindex, String... list){
        this.hashCollection = new HashCollection(nindex);
        this.initDataFormat(list);
    }
    
    private void initDataFormat(String... list){
        for(String item : list){
            if(item.contains(":")==true){
                String[] tokens = item.trim().split(":");
                this.columns.add(tokens[0]);
                this.types.add(tokens[1]);
            } else {
                this.columns.add(item);
                this.types.add("i");
            }
        }
        for(int loop = 0; loop < this.columns.size(); loop++){
           // System.out.println(loop + " " + this.columns.get(loop) + "  " + this.types.get(loop));
        }
    }
    
    public void addConstrain(int column, double min, double max){
        this.constrains.put(column, new HashTableRowConstrain(column,min,max));
    }
    
    public void addRow(String[] values){
        int ic = this.hashCollection.getIndexCount();
        int[]    index = new int[ic];
        for(int i = 0 ; i < ic; i++) index[i] = Integer.parseInt(values[i]);
        Long hashCode = HashGenerator.hashCode(index);
        TableRow  row = new TableRow();
        
        for(int i = ic ; i < values.length; i++){
            //for(int i = ic; i < values.length; i++) array[i-ic] = Integer.parseInt(values[i]);
            //System.out.println("parsing " + values[i] + "  for " + this.columns.get(i-ic) + "  type = " + this.types.get(i-ic));
            if(this.types.get(i-ic).compareTo("d")==0){
                row.add(Double.parseDouble(values[i]));
            } else {
                row.add(Integer.parseInt(values[i]));
            }
        }
        this.hashCollection.add(row, index);
        //this.addRow(array, index);
    }
    
    public void addRowAsInt(String[] values){
        int ic = this.hashCollection.getIndexCount();
        int[]    array = new int[values.length-ic];
        int[]    index = new int[ic];
        for(int i = 0 ; i < ic; i++) index[i] = Integer.parseInt(values[i]);
        for(int i = ic; i < values.length; i++) array[i-ic] = Integer.parseInt(values[i]);
        this.addRow(array, index);
    }
    
    public void setValueAtAsDouble(String name, double value, int... index){
        if(this.hasRow(index)==true){
            int idx = this.getColumnIndex(name);
            if(idx<0) return;           
            this.setValueAtAsDouble(idx,value,index);
        }
    }
    
    public void setValueAtAsInt(String name, int value, int... index){
        if(this.hasRow(index)==true){
            int idx = this.getColumnIndex(name);
            if(idx<0) return;           
            this.setValueAtAsDouble(idx,value,index);
        }
    }
    
    public void setValueAtAsDouble(int column, double value, int... index){        
        if(this.hashCollection.hasItem(index)==true){
            TableRow  row = this.getRow(index);
            row.setAt(column, value);
        }
    }
    
    public void setValueAtAsInt(int column, int value, int... index){        
        if(this.hashCollection.hasItem(index)==true){
            TableRow  row = this.getRow(index);
            row.setAt(column, value);
        }
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
    
    public boolean   hasRow(int... index){
        return this.hashCollection.hasItem(index);
    }
    
    public Number getValue(String name, int... index){
        if(this.hasRow(index)==true){
            int idx = this.getColumnIndex(name);
            if(idx<0) return 0;            
            return this.getRow(index).get(idx);
        }
        return 0;
    }
    
    public boolean hasColumn(String name){
        return (this.getColumnIndex(name)>=0);
    }
    
    private int  getColumnIndex(String name){
        for(int i = 0; i < this.columns.size(); i++){
            if(this.columns.get(i).compareTo(name)==0){
                return i;
            }
        }
        return -1;
    }
    public void show(){
        this.hashCollection.show();
    }
    
    public void describe(){
        
        for(int i = 0; i < this.columns.size();i++){
            System.out.println(String.format("* %24s  *  %5s *", this.columns.get(i),
                    this.types.get(i)));
        }
    }
    
    public int getIndexCount(){
        return this.hashCollection.getIndexCount();
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
    
    public void readFile(String filename){
        BufferedReader in = null;        
        try {
            in = new BufferedReader(new FileReader(filename));
            while (in.ready()) {
                String s = in.readLine();
                if(s.startsWith("#")==false){
                    String[] tokens = s.trim().split("\\s+");
                    this.addRowAsInt(tokens);                    
                }
            } 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HashTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HashTable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(HashTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public void writeFile(String filename){
        List<String> lines = new ArrayList<String>();
        for(Map.Entry<Long,TableRow>  rows : this.hashCollection.getMap().entrySet()){
            long hashcode = rows.getKey();
            String  keys = String.format("%4d %4d %4d ", 
                    HashGenerator.getIndex(hashcode, 0), HashGenerator.getIndex(hashcode, 1),
                    HashGenerator.getIndex(hashcode, 2));
            lines.add(keys+rows.getValue().stringLine());
        }
        try {
            File logFile=new File(filename);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            for(String line : lines){
                writer.write (line);
            }
            
            //Close writer
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
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
    
    public boolean isValid(int row, int column){
        if(this.constrains.containsKey(column)==false) return true;
        String value = (String) this.getValueAt(row, column);
        return this.constrains.get(column).isValid(Double.parseDouble(value)) != false;            
    }
    
    public class HashTableRowConstrain{
        
        public int COLUMN = 0;
        public double MIN = 0.0;
        public double MAX;
        
        public HashTableRowConstrain( int column, double min, double max){
            this.COLUMN = column;
            this.MIN    = min;
            this.MAX    = max;
        }
        
        public boolean isValid(double value){ return (value>=this.MIN&&value<=this.MAX);}
    }
    
    
    public void export(String filename){
        
    }
    
    public static void main(String[] args){
        HashTable  table = new HashTable(3,"a","b","c","d");
        table.readFile("/Users/gavalian/Work/Software/Release-8.0/COATJAVA/coatjava/etc/bankdefs/translation/EC.table");
        /*
        table.addRowAsDouble(new String[]{"21","7","1","0.5","0.1","0.6"});
        table.addRowAsDouble(new String[]{"22","8","2","0.6","0.2","0.7"});
        table.addRowAsDouble(new String[]{"23","9","3","0.7","0.3","0.8"});
        table.addRowAsDouble(new String[]{"24","10","4","0.8","0.4","0.9"});
        */
        
        table.show();
    }
}
