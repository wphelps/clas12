/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.jlab.ccdb.Assignment;
import org.jlab.ccdb.CcdbPackage;
import org.jlab.ccdb.JDBCProvider;
import org.jlab.ccdb.TypeTable;
import org.jlab.ccdb.TypeTableColumn;
import org.jlab.clas.tools.utils.StringTable;
import org.jlab.geom.base.ConstantProvider;
import org.root.histogram.GraphErrors;


/**
 *
 * @author gavalian
 */
public class DatabaseConstantProvider implements ConstantProvider {
    
    private HashMap<String,String[]> constantContainer = new HashMap<String,String[]>();
    private boolean PRINT_ALL = true;
    private String variation  = "default";
    private Integer runNumber = 10;
    private Integer loadTimeErrors = 0;
    
    private JDBCProvider provider;
    
    
    public DatabaseConstantProvider(){
        this.loadTimeErrors = 0;
        this.runNumber = 10;
        this.variation = "default";
        
        String address = "mysql://clas12reader@clasdb.jlab.org/clas12";
        
        String envAddress = this.getEnvironment();        
        if(envAddress!=null) address = envAddress;
        this.initialize(address);
    }
    
    public DatabaseConstantProvider(int run, String var){
        
        this.loadTimeErrors = 0;
        this.runNumber = run;
        this.variation = var;
        
        String address = "mysql://clas12reader@clasdb.jlab.org/clas12";
        
        String envAddress = this.getEnvironment();        
        if(envAddress!=null) address = envAddress;
        this.initialize(address);
    }
    
    public DatabaseConstantProvider(String address){
        this.initialize(address);
    }
    
    public DatabaseConstantProvider(String address, String var){
        this.variation = var;
        this.initialize(address);
    }
    
    private String getEnvironment(){
        
        String envCCDB   = System.getenv("CCDB_DATABASE");
        String envCLAS12 = System.getenv("CLAS12DIR");
        
        String propCLAS12 = System.getProperty("CLAS12DIR");
        String propCCDB   = System.getProperty("CCDB_DATABASE");
        
        //System.out.println("ENVIRONMENT : " + envCLAS12 + " " + envCCDB + " " + propCLAS12 + " " + propCCDB);
        
        if(envCCDB!=null&&envCLAS12!=null){
            StringBuilder str = new StringBuilder();
            str.append("sqlite:///");
            if(envCLAS12.charAt(0)!='/') str.append("/");
            str.append(envCLAS12);
            if(envCCDB.charAt(0)!='/' && envCLAS12.charAt(envCLAS12.length()-1)!='/'){
                str.append("/");
            }
            str.append(envCCDB);
            return str.toString();
        }
        
        if(propCCDB!=null&&propCLAS12!=null){
            StringBuilder str = new StringBuilder();
            str.append("sqlite:///");
            if(propCLAS12.charAt(0)!='/') str.append("/");
            str.append(propCLAS12);
            if(propCCDB.charAt(0)!='/' && propCLAS12.charAt(propCLAS12.length()-1)!='/'){
                str.append("/");
            }
            str.append(propCCDB);
            return str.toString();
        }
        
        return null;
    }
    
    private void initialize(String address){
        provider = CcdbPackage.createProvider(address);
        System.out.println("[DB] --->  open connection with : " + address);
        System.out.println("[DB] --->  database variation   : " + this.variation);
        System.out.println("[DB] --->  database run number  : " + this.runNumber);
        
        provider.connect();
        if(provider.getIsConnected()==true){
            System.out.println("[DB] --->  database connection  : success");
        } else {
            System.out.println("[DB] --->  database connection  : failed");
        }
        
        provider.setDefaultVariation(variation);
        provider.setDefaultRun(this.runNumber);

        //Assignment asgmt = provider.getData("/test/test_vars/test_table");
    }
    
    public void loadTable(String table_name){
        try {
            Assignment asgmt = provider.getData(table_name);
            
            int ncolumns = asgmt.getColumnCount();
            TypeTable  table = asgmt.getTypeTable();
            Vector<TypeTableColumn> typecolumn = asgmt.getTypeTable().getColumns();
            System.out.println("[DB LOAD] ---> loading data table : " + table_name);
            System.out.println("[DB LOAD] ---> number of columns  : " + typecolumn.size());
            System.out.println();
            for(int loop = 0; loop < ncolumns; loop++){
                //System.out.println("Reading column number " + loop 
                //+ "  " + typecolumn.elementAt(loop).getCellType()
                //+ "  " + typecolumn.elementAt(loop).getName());
                Vector<String> row = asgmt.getColumnValuesString(loop);
                String[] values = new String[row.size()];
                for(int el = 0; el < row.size(); el++){
                    values[el] = row.elementAt(el);
                    //for(String cell: row){
                    //System.out.print(cell + " ");
                }
                StringBuilder str = new StringBuilder();
                str.append(table_name);
                str.append("/");
                str.append(typecolumn.elementAt(loop).getName());
                constantContainer.put(str.toString(), values);
                //System.out.println(); //next line after a row
            }
            //provider.close();
        } catch (Exception e){
            System.out.println("[DB LOAD] --->  error loading table : " + table_name);
            this.loadTimeErrors++;
        }
    }
    
    public void loadTables(String... tbl){
        for(String table : tbl){
            
        }
    }
    
    @Override
    public boolean hasConstant(String string) {
        return constantContainer.containsKey(string);
    }

    @Override
    public int length(String string) {
        if(this.hasConstant(string)) return constantContainer.get(string).length;
        return 0;
    }

    @Override
    public double getDouble(String string, int i) {
        if(this.hasConstant(string)==true && i < this.length(string)){
            return Double.parseDouble(constantContainer.get(string)[i]);
        } else {
            
        }
        return 0.0;
    }

    @Override
    public int getInteger(String string, int i) {
        if(this.hasConstant(string)==true && i < this.length(string)){
            return Integer.parseInt(constantContainer.get(string)[i]);
        } else {
            
        }
        return 0;
    }
    
    public void disconnect(){
        System.out.println("[DB] --->  database disconnect  : success");
        this.provider.close();
    }
    /**
     * prints out table with loaded values.
     */
    public void show(){
        System.out.println("\n\n");
        StringTable table = new StringTable();
        System.out.println("\t" + StringTable.getCharacterString("*", 70));
        System.out.println(String.format("\t*  %-52s : %8s   *", "Item Name","Length"));
        System.out.println("\t" + StringTable.getCharacterString("*", 70));
        System.out.print(this.showString());
        System.out.println("\t" + StringTable.getCharacterString("*", 70));
    }
    /**
     * returns a string representing a table printout of the constants
     * 
     * @return 
     */
    public String showString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,String[]> item : this.constantContainer.entrySet()){
            str.append(String.format("\t*  %-52s : %8d   *\n", item.getKey(),item.getValue().length));
        }
        return str.toString();
    }
    
    @Override
    public String toString(){
        System.err.println("Database Constat Provider: ");
        StringBuilder str = new StringBuilder();
        if(PRINT_ALL==true){
            for(Map.Entry<String,String[]> entry : constantContainer.entrySet()){
                str.append(String.format("%24s : %d\n", entry.getKey(),entry.getValue().length));
                for(int loop = 0; loop < entry.getValue().length; loop++){
                    //str.append("\n");
                    str.append(String.format("%18s ", entry.getValue()[loop]));
                }
                str.append("\n");
            }
        } else {
            for(Map.Entry<String,String[]> entry : constantContainer.entrySet()){
                str.append(String.format("%24s : %d\n", entry.getKey(),entry.getValue().length));
            }
        }
        return str.toString();
    }
    
    public int getSize(){
        return this.constantContainer.size();
    }
    
    public GraphErrors getGraph(String constant){
        GraphErrors graph = new GraphErrors();
        if(this.constantContainer.containsKey(constant)==true){
            String[]  values = this.constantContainer.get(constant);
            int counter = 0;
            for(String item : values){
                counter++;
                graph.add((double) counter, Double.parseDouble(item) );
            }
        }
        return graph;
    }

    public static void main(String[] args){
        DatabaseConstantProvider provider = new DatabaseConstantProvider();
        provider.disconnect();
    }

}
