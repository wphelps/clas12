/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.object;

/**
 *
 * @author gavalian
 */
public @interface EvioDataType {
    int parent() default  0;
    int tag() default 0;
    int num() default 0;
    String type() default "int";
}
