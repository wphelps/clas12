/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.jlab.clas12.basic.IDetectorProcessor;
import org.jlab.clas12.physics.IDetectorEventProcessor;
import org.jlab.data.io.DataEvent;

/**
 *
 * @author gavalian
 */
public class DetectorEventProcessorDialog extends JDialog implements ActionListener {
    
    JProgressBar  progressBar = null;
    JButton       buttonClose = null;
    DetectorEventProcessorThread  thread = null;
    
    public DetectorEventProcessorDialog(String file, IDetectorProcessor proc){
        super();
        //this.setUndecorated(true);
        this.init();        
        this.buttonClose.setEnabled(false);
        this.setVisible(true);
        this.runWithFile(file, proc);
        //this.runProcess();
    }
    
    public DetectorEventProcessorDialog(IDetectorProcessor proc){
        super();
        this.init();        
        this.buttonClose.setEnabled(false);
        //this.setUndecorated(true);
        this.setVisible(true);

        this.chooseFileAndRun(proc);
        
        //this.runProcess();
    }
    
    
   
    public final void chooseFileAndRun(IDetectorProcessor proc){
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
               this.runWithFile(file.getAbsolutePath(),proc);
            } else {
                System.out.println("Open command cancelled by user." );
                this.buttonClose.setEnabled(true);
                this.progressBar.setValue(100);
            }
    }
    
    public final void runWithFile(String file, IDetectorProcessor proc){
        System.out.println("starting processing");
        thread = new DetectorEventProcessorThread(file,proc);
        this.setVisible(true);
        this.thread.start();
        this.progressBar.setValue(0);

        while(thread.isAlive()){
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(DetectorEventProcessorDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println("progress = " + thread.getProgress());
            this.progressBar.setValue(thread.getProgress());
        }
        this.buttonClose.setEnabled(true);
                
    }
    
    public final void init(){
        this.setSize(500, 120);
        this.setLayout(new BorderLayout());
        //this.setModalityType(ModalityType.);
        this.progressBar = new JProgressBar();
        
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createTitledBorder("Processing"));
        progressPanel.add(this.progressBar,BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        buttonClose = new JButton("Close");
        buttonClose.addActionListener(this);
        buttonPanel.add(buttonClose);
        
        this.add(progressPanel);
        this.add(buttonPanel,BorderLayout.PAGE_END);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Close")==0){
            this.setVisible(false);
        }
    }
    
    
    public static void main(String[] args){
        String inputFile = "/Users/gavalian/Work/Software/Release-8.0/COATJAVA/etaPXSection_0_recon.evio";
        IDetectorProcessor processor = new IDetectorProcessor(){
            public void processEvent(DataEvent event){
                //System.out.println("------> processing data ");
            }
        };
        
        //DetectorEventProcessorDialog dialog = new DetectorEventProcessorDialog(inputFile,processor);
        DetectorEventProcessorDialog dialog = new DetectorEventProcessorDialog(processor);
        //dialog.setVisible(true);
        
        
    }
}
