/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.jlab.data.io.DataEvent;

/**
 *
 * @author gavalian
 */
public class DataEventProcessorDialog extends JDialog {
    
    JProgressBar  progressBar = null;
    JButton buttonClose = null;
    
    public DataEventProcessorDialog(){
        super();
        this.setSize(500, 120);
        this.setLayout(new BorderLayout());
        this.init();
        this.setModalityType(ModalityType.APPLICATION_MODAL);
    }
    
    public void init(){
        
        this.progressBar = new JProgressBar();
        
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createTitledBorder("Processing"));
        progressPanel.add(this.progressBar,BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        buttonClose = new JButton("Close");
        buttonPanel.add(buttonClose);

        this.add(progressPanel);
        this.add(buttonPanel,BorderLayout.PAGE_END);
    }
    
    
    public  void  startProcess(IDataEventProcessor ep){
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
               this.startProcess(file.getAbsolutePath(),ep);
            } else {
                System.out.println("Open command cancelled by user." );
            }
    }
    
    private void startProcess(String filename, IDataEventProcessor ep){
        DataEventProcessorThread thread = new DataEventProcessorThread();
        thread.setFileName(filename);
        thread.addProcessor(ep);
        thread.setPriority(Thread.currentThread().getPriority()-1);
        thread.start();
        buttonClose.setEnabled(false);
        while(thread.isAlive()){
            
            try {
                Thread.sleep(30);                
                int progress = (int) thread.getProgress();
                System.out.println("set progress value = " + progress);
                this.progressBar.setValue(progress);
            } catch (InterruptedException ex) {
                Logger.getLogger(DataEventProcessorDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        this.progressBar.setValue(100);
        this.buttonClose.setEnabled(true);
    }
    
    public void selectFile(){
        
    }
    
    public static void runProcess(IDataEventProcessor processor){
        DataEventProcessorDialog dialog = new DataEventProcessorDialog();
        dialog.setVisible(true);
        dialog.startProcess(processor);
    }
    
    public static void main(String[] args){
        DataEventProcessorDialog dialog = new DataEventProcessorDialog();
        dialog.setVisible(true);
        dialog.startProcess( 
                new IDataEventProcessor(){
                    public void process(DataEvent event){
                        System.out.println("yeah right");
                    }                    
                });
    }
}
