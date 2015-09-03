/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jlab.clas12.calib.DetectorCalibrationFrame;
import org.jlab.clasrec.main.DetectorCalibration;

/**
 *
 * @author gavalian
 */
public class CLAS12Desktop extends JFrame implements ActionListener {
    JDesktopPane  desktop = null;
    public CLAS12Desktop(){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        this.initDesktop();
        this.initMenuBar();
        this.setVisible(true);
        
    }
    
    
    void initDesktop(){
        desktop = new JDesktopPane();
        this.add(desktop);
    }
    
    void initMenuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        bar.add(fileMenu);
        
        JMenu pluginMenu = new JMenu("Plugins");
        JMenuItem loadPlugin = new JMenuItem("Load");
        loadPlugin.addActionListener(this);
        pluginMenu.add(loadPlugin);
        
        bar.add(pluginMenu);
        
        this.setJMenuBar(bar);
    }
    
    public void addCalibrationModule(DetectorCalibration calib) {
        DetectorCalibrationFrame frame = new DetectorCalibrationFrame(calib);
        this.desktop.add(frame);
    }
    
    public void addCalibrationModule(String calibname) {
        try {
            Class c = Class.forName(calibname);
            if(c.isAssignableFrom(DetectorCalibration.class)){
                DetectorCalibration calib = (DetectorCalibration) c.newInstance();
                this.addCalibrationModule(calib);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CLAS12Desktop.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CLAS12Desktop.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CLAS12Desktop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        CLAS12Desktop desktop = new CLAS12Desktop();
        //desktop.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Load")==0){
            //DetectorCalibrationFrame frame = new DetectorCalibrationFrame();
            //desktop.add(frame);
            System.out.println("Added new plugin window");
            //desktop.add(new CLAS12MonitoringPlugin());
            //System.out.println("Adding new plugin window");
            
        }
    }
}
