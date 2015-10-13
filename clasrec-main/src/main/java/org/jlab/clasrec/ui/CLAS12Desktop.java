/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jlab.clas12.calib.DetectorCalibrationFrame;
import org.jlab.clasrec.main.DetectorCalibration;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class CLAS12Desktop extends JFrame implements ActionListener {
    
    JDesktopPane  desktop = null;
    private final List<DetectorCalibrationFrame>  calibrationFrames = 
            new ArrayList<DetectorCalibrationFrame>();
            
    public CLAS12Desktop(){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200,800);
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
        JMenuItem processItem = new JMenuItem("Process File");
        processItem.addActionListener(this);
        fileMenu.add(processItem);
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
        this.calibrationFrames.add(frame);
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

    
    public void processFile(String filename){
        for(DetectorCalibrationFrame frame : this.calibrationFrames){
                frame.reset();
        }
        EvioSource reader = new EvioSource();
        reader.open(filename);
        while(reader.hasEvent()==true){
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            for(DetectorCalibrationFrame frame : this.calibrationFrames){
                frame.processEvent(event);
            }
        }
        System.out.println("--------> processing file done......");
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Load")==0){
            //DetectorCalibrationFrame frame = new DetectorCalibrationFrame();
            //desktop.add(frame);
            System.out.println("Added new plugin window");
            //desktop.add(new CLAS12MonitoringPlugin());
            //System.out.println("Adding new plugin window");            
        }
        
        if(e.getActionCommand().compareTo("Process File")==0){
            System.out.println("EXECUTING PROCESS FILE");
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileFilter(){
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".evio")
                            || f.isDirectory();
                }
                
                public String getDescription() {
                    return "EVIO CLAS data format";
                }
            });
            String currentDir = System.getenv("PWD");
            if(currentDir!=null){
                fc.setCurrentDirectory(new File(currentDir));
            }
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
               System.out.println("Opening: " + file.getAbsolutePath() + "." );
               this.processFile(file.getAbsolutePath());
            } else {
                System.out.println("Open command cancelled by user." );
            }
        }
    }
}
