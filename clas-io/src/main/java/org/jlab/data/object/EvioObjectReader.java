/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.object;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.jlab.evio.clas12.EvioDataEvent;

/**
 *
 * @author gavalian
 */
public class EvioObjectReader {
    public static void readObject(EvioDataEvent event, Object obj){
        if (obj.getClass().isAnnotationPresent((Class<? extends Annotation>) obj.getClass())) {
            Annotation annotation = obj.getClass().getAnnotation( (Class<? extends Annotation>) obj.getClass());
            System.out.println(annotation);
        }
        
        for(Method method : obj.getClass().getDeclaredMethods()){
            System.out.println(method);
            Annotation annotation = method.getAnnotation(EvioDataType.class);
            System.out.println((EvioDataType) annotation);
        }
    }
    
    public static void main(String[] args){
        DetectorBank ftof = new DetectorBank();
        EvioObjectReader.readObject(null, ftof);
    }
}
