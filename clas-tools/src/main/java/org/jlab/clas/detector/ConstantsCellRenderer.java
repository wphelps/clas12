/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gavalian
 */
public class ConstantsCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent
        (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent
                                  (table, value, isSelected, hasFocus, row, column);
            
            if(isSelected==true){
                c.setBackground(new Color(20,20,255));                
                return c;
            }
            
            if(row%2==0){
                c.setBackground(new Color(220,255,220));
            } else {
                c.setBackground(new Color(220,220,255));
            }
            
            return c;
        }
}
