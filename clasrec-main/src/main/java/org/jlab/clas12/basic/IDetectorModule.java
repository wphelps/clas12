/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.basic;

import org.jlab.clas.detector.DetectorType;

/**
 *
 * @author gavalian
 */
public interface IDetectorModule {
    String  getName();
    String  getAuthor();
    DetectorType getType();
}
