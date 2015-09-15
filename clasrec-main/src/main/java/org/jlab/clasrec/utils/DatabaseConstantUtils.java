/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.utils;

import org.root.histogram.GraphErrors;

/**
 *
 * @author gavalian
 */
public class DatabaseConstantUtils {
    
    public DatabaseConstantUtils(){
        
    }
    
    public static GraphErrors getGraph(String table, String column, int run_ref, 
            String variation_ref, int run, String variation){
        GraphErrors graph = new GraphErrors();
        DatabaseConstantProvider provider1 = new DatabaseConstantProvider(run_ref,variation_ref);
        provider1.loadTable(table);
        provider1.disconnect();
        
        DatabaseConstantProvider provider2 = new DatabaseConstantProvider(run,variation);
        provider2.loadTable(table);
        provider2.disconnect();
        
        String variable = table + "/" + column;
        
        GraphErrors graph1 = provider1.getGraph(variable);
        GraphErrors graph2 = provider2.getGraph(variable);
        
        if(graph1.getDataSize().compareTo(graph2.getDataSize())==0){
            for(int loop = 0; loop < graph1.getDataSize(); loop++){
                double y1 = graph1.getDataY(loop);
                double y2 = graph1.getDataY(loop);
                double y  = 0;
                if(y2!=0) y = y1/y2;
                //System.out.println("adding point " + loop + "  " + y);
                graph.add(graph1.getDataX(loop), y);
            }
        } else {
            System.out.println("[error] ---> data size for the tables is not the same. "
            + graph1.getDataSize() + "  " + graph2.getDataSize());
        }
        
        return graph;
    }
}
