/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.containers;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author gavalian
 */
public class HashTableViewer extends JPanel {
    JTable  table = new JTable();
    
    public HashTableViewer(HashTable ht){
        super();
        this.setPreferredSize(new Dimension(600,600));
        this.setLayout(new BorderLayout());
        table.setModel(ht);
        
        JScrollPane  scroll = new JScrollPane(table);
        //scroll.add(table);

        this.add(scroll,BorderLayout.CENTER);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        HashTable  table = new HashTable(3,"a","b","c");
        table.addRowAsDouble(new String[]{"21","7","1","0.5","0.1","0.6"});
        table.addRowAsDouble(new String[]{"22","8","2","0.6","0.2","0.7"});
        table.addRowAsDouble(new String[]{"23","9","3","0.7","0.3","0.8"});
        table.addRowAsDouble(new String[]{"24","10","4","0.8","0.4","0.9"});
        table.show();
        HashTableViewer canvas = new HashTableViewer(table);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}
