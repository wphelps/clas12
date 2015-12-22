/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.detector;

/**
 *
 * @author gavalian
 */
public class FADCConfig {
    int  fadcNSA   = 0;
    int  fadcNSB   = 0;
    int  fadcTET   = 0;
    double fadcPED = 0;
    
    public FADCConfig(double ped, int nsa, int nsb, int tet){
        this.fadcNSA = nsa;
        this.fadcNSB = nsb;
        this.fadcTET = tet;
        this.fadcPED = ped;
    }
    
    public int getNSA(){ return this.fadcNSA;}
    public int getNSB(){ return this.fadcNSB;}
    public int getTET(){ return this.fadcTET;}
    public double getPedestal(){ return this.fadcPED;}
}
