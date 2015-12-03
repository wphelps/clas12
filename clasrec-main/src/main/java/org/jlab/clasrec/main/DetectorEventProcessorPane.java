/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jlab.clas12.basic.IDetectorProcessor;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class DetectorEventProcessorPane extends JPanel implements ActionListener {
    
    List<IDetectorProcessor>  processorList = new ArrayList<IDetectorProcessor>();
    EvioSource                reader        = new EvioSource();
    JButton                   buttonPrev    = null;
    JButton                   buttonNext    = null;
    JLabel                    statusLabel   = null;
    
    public DetectorEventProcessorPane(){
        super();
        this.setLayout(new FlowLayout());
        //this.setBorder();
        buttonPrev = new JButton("<");
        buttonPrev.addActionListener(this);
        buttonNext = new JButton(">");
        buttonNext.addActionListener(this);
        
        
        buttonNext.setEnabled(false);
        buttonPrev.setEnabled(false);
        
        JButton  buttonOpen = new JButton("Open");
        buttonOpen.addActionListener(this);
        
        this.add(buttonOpen);
        this.add(Box.createHorizontalStrut(30));
        this.add(buttonPrev);
        this.add(buttonNext);
        this.add(Box.createHorizontalStrut(30));
        
        statusLabel = new JLabel("No Opened File");
        this.add(statusLabel);
    }
    
    public void addProcessor(IDetectorProcessor proc){
        this.processorList.add(proc);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Open")==0){
            String currentDir = System.getenv("PWD");
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

            if(currentDir!=null){
                fc.setCurrentDirectory(new File(currentDir));
            }
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
               System.out.println("Opening: " + file.getAbsolutePath() + "." );
               //this.processFile(file.getAbsolutePath());
               this.reader.open(file.getAbsolutePath());
               this.buttonNext.setEnabled(true);
               Integer current = this.reader.getCurrentIndex();
               Integer nevents = this.reader.getSize();
               
               this.statusLabel.setText("EVENTS IN FILE : " + nevents.toString() + "  CURRENT : " + current.toString());
               
            } else {
                System.out.println("Open command cancelled by user." );
                //this.buttonClose.setEnabled(true);
                //this.progressBar.setValue(100);
            }
        }
        if(e.getActionCommand().compareTo("<")==0){
            if(reader.hasEvent()){
                if(reader.getCurrentIndex()>=2){
                    EvioDataEvent event = (EvioDataEvent) reader.getPreviousEvent();
                    Integer current = this.reader.getCurrentIndex();
                    Integer nevents = this.reader.getSize();
                    this.statusLabel.setText("EVENTS IN FILE : " + nevents.toString() + "  CURRENT : " + current.toString());
                    
                    for(IDetectorProcessor proc : this.processorList){
                        try {
                            proc.processEvent(event);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        if(e.getActionCommand().compareTo(">")==0){
            if(reader.hasEvent()){
                EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
                Integer current = this.reader.getCurrentIndex();
                Integer nevents = this.reader.getSize();                
                this.statusLabel.setText("EVENTS IN FILE : " + nevents.toString() + "  CURRENT : " + current.toString());

                for(IDetectorProcessor proc : this.processorList){
                    try {
                        proc.processEvent(event);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        
    }
    
    public static void main(String[] args){
        JFrame fr = new JFrame();
        DetectorEventProcessorPane  pane = new DetectorEventProcessorPane();
        fr.add(pane);
        fr.pack();
        fr.setVisible(true);
    }
}
