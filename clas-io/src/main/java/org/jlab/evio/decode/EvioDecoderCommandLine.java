/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.evio.decode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.jlab.clas.detector.DetectorRawData;
import org.jlab.clas.detector.DetectorType;
import org.jlab.clas.tools.utils.FileUtils;
import org.jlab.clas.tools.utils.ResourcesUtils;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;
import org.jlab.io.decode.AbsDetectorTranslationTable;

/**
 *
 * @author gavalian
 */
public class EvioDecoderCommandLine {
    public static void main(String[] args){
        
        String directory = ResourcesUtils.getResourceDir("CLAS12DIR", "etc/bankdefs/translation");        
        List<String> tables = FileUtils.getFilesInDir(directory);
        
        EvioEventDecoder  decoder = new EvioEventDecoder();
        
        for(int loop = 0; loop < tables.size(); loop++){
            
            AbsDetectorTranslationTable  trTable = new AbsDetectorTranslationTable();
            Path tablePath = Paths.get(tables.get(loop));
            

            String filename    = tablePath.getFileName().toString();
            String system_name = filename.substring(0, filename.indexOf("."));
            System.out.println("LOADING FILE : " + tablePath.getFileName() 
                    + " SYSTEM = " + system_name);
            trTable.readFile(tables.get(loop));
            trTable.setName(system_name);            
            decoder.addTranslationTable(trTable);
            
            if(system_name.compareTo("FTOF1A")==0){
                trTable.setDetectorType(DetectorType.FTOF);
            }
            
            if(system_name.compareTo("FTOF1B")==0){
                trTable.setDetectorType(DetectorType.FTOF);
            }
            
            if(system_name.compareTo("ECAL")==0){
                trTable.setDetectorType(DetectorType.EC);
            }
            if(system_name.compareTo("PCAL")==0){
                trTable.setDetectorType(DetectorType.EC);
            }
            //Path filepath = 
            //trTable.setName(directory);
            //trTable.readFile(directory);
        }
        
        int inputindex = 0;
        int debug      = 0;
        if(args[0].compareTo("-debug")==0){
            debug = 1;
            inputindex = 1;
        }
        
        String inputFileName  = args[inputindex];
        String outputFileName = inputFileName + "_decoded.evio";
        
        EvioSource reader = new EvioSource();
        reader.open(inputFileName);
        
        while(reader.hasEvent()==true){
            
            System.out.println("***************************** EVIO DATA EVENT");
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            List<DetectorRawData>  rawdata = decoder.getDataEntries(event);
            System.out.println(" >>>>>>>>>>>>> RAWDATA AS IT COMES");
            
            for(DetectorRawData data : rawdata){
                System.out.println(data);
                /*
                System.out.println(data.getDataSize() 
                        + "  " + data.getIntegral( 0, 10)
                        + "  " + data.getIntegral(10, 20)
                        + "  " + data.getIntegral(20,  30)
                        + "  " + data.getSignal(0, 30, 35, 70)
                );*/
                //short[] array = (short[]) data.getDataObject(0);
                //System.out.println(array.length);
                    
            }
            
            decoder.decode(rawdata);
            
            System.out.println(" >>>>>>>>>>>>> DECODED AS IT COMES");
            for(DetectorRawData data : rawdata){
                System.out.println(data);
            }
            
            
            List<DetectorRawData>  selectionFTOF = decoder.getDetectorData(rawdata, 
                    DetectorType.FTOF);
            System.out.println(" >>>>>>>>>>>>> SELECTION ");
            for(DetectorRawData data : selectionFTOF){
                System.out.println(data);
            }

        }
    }
}
