/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gavalian
 */
public class ConstantsTablePanel extends JPanel {
    private JScrollPane  scrollPane = null;
    private JTable       guiTable     = null;
    List<IConstantsTableListener>  listeners = new ArrayList<IConstantsTableListener>();
    ConstantsTable       constantsTable      = null;
    
    
    final Color[] rowColors = new Color[] {
                randomColor(), randomColor(), randomColor(),
        randomColor(),randomColor(),randomColor(),randomColor(),randomColor(),randomColor()
        };
    private static Color randomColor() {
        Random rnd = new Random();
        return new Color(rnd.nextInt(256),
                rnd.nextInt(256), rnd.nextInt(256));
    }
    
    public ConstantsTablePanel(ConstantsTable table){
        super();
        this.setLayout(new BorderLayout());
        //this.setPreferredSize(new Dimension(1200,800));
        this.guiTable = new JTable(table);

        //scrollPane.add(table);
        scrollPane = new JScrollPane(this.guiTable);
        //this.table.setFillsViewportHeight(true);
        this.add(scrollPane,BorderLayout.CENTER);
        
        this.guiTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent e) {
                //System.out.println("Selection has changed " + getSelected().toString());
                if(listeners.size()>0){
                    DetectorDescriptor desc = getSelected();
                    for(IConstantsTableListener lt : listeners){
                        lt.entrySelected(desc.getSector(), desc.getLayer(), desc.getComponent());
                        //System.out.println("SELECTED ");
                        //lt.entrySelected();
                    }
                }
            }
        });
        
        this.guiTable.setDefaultRenderer(Object.class,new ConstantsCellRenderer());
        this.guiTable.setAutoCreateRowSorter(true);
        /*
        this.guiTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                //JLabel l = (JLabel) getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            //Get the status for the current row.
                
            //l.setBackground(Color.GREEN);
                JPanel pane = new JPanel();
                if(row%2==0){
                    pane.setBackground(new Color(255,140,140,255));
                } else {
                    pane.setBackground(new Color(180,255,180,255));
                }
                return pane;
            }
        });*/
    }
    
    public void addListener(IConstantsTableListener lt){
        this.listeners.add(lt);
    }
    
    public DetectorDescriptor  getSelected(){
        int row   =  this.guiTable.getSelectedRow();
        String   v1 = (String) this.guiTable.getModel().getValueAt(row, 1);
        String   v2 = (String) this.guiTable.getModel().getValueAt(row, 2);
        String   v3 = (String) this.guiTable.getModel().getValueAt(row, 3);
        DetectorDescriptor desc = new DetectorDescriptor();
        desc.setSectorLayerComponent(
                Integer.parseInt(v1),
                Integer.parseInt(v2),
                Integer.parseInt(v3));
        //this.guiTable.getModel()
        return desc;
                
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(900, 700);
        ConstantsTable      table = new ConstantsTable(DetectorType.FTOF,new String[]{"geoMean","error","Quality"});
        
        table.addEntry(1, 1, 1);
        table.addEntry(1, 1, 2);
        table.addEntry(1, 1, 3);
        table.addEntry(1, 1, 4);
        table.getEntry(1, 1, 1).setData(0, 0.5);
        ConstantsTablePanel panel = new ConstantsTablePanel(table);
        
        panel.addListener(new IConstantsTableListener(){
            public void entrySelected(int sector, int layer, int component){
                System.out.println(" SELECTED COMPONENT   ====> " + sector + "  " + layer + " " + component);
            }
        });
        
        
        frame.add(panel,BorderLayout.CENTER);
        //frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
