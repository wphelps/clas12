/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.bio;

/**
 *
 * @author gavalian
 */
public class BioHeaderConstants {
    
    /**
     * All constants for record bit definitions
     */
    public static int  RECORD_HEADER_SIZE          = 12;
    public static int  LOWBYTE_RECORD_SIZE         = 0;
    public static int  HIGHBYTE_RECORD_SIZE        = 23;
    public static int  LOWBYTE_RECORD_EVENTCOUNT   = 0;
    public static int  HIGHBYTE_RECORD_EVENTCOUNT  = 23;
    public static int  LOWBYTE_RECORD_COMPRESSION  = 24;
    public static int  HIGHBYTE_RECORD_COMPRESSION = 24;
    
    public static int  LOWBYTE_RECORD_STRINGID = 0;
    public static int  HIGHBYTE_RECORD_STRINGID = 0;
    

    
}
