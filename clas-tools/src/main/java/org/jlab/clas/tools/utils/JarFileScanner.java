/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.tools.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gavalian
 */
public class JarFileScanner {
    public JarFileScanner(){
        
    }
    
    public List<String>  getClassList(String jarfile){
        ArrayList<String>  classList = new ArrayList<String>();
        try {            
            JarFile jarFile = new JarFile(jarfile);
            Enumeration e = jarFile.entries();
            
            //URL[] urls = { new URL("jar:file:" + jarfile+"!/") };
            //URLClassLoader cl = URLClassLoader.newInstance(urls);
            URL[] urls = {new URL("jar:file:" + jarfile + "!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);
             
            while (e.hasMoreElements()) {
                JarEntry je = (JarEntry) e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                String className = je.getName().substring(0,je.getName().length()-6);
                className = className.replace('/', '.');
                //System.err.println("CLASS = " + className);
                //Class c = cl.loadClass(className);
                classList.add(className);

            }
        } catch (IOException ex) {
            System.err.println("Warning: file is not a proper jar : " + jarfile);
            //Logger.getLogger(JarPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classList;
    }
    
    
    
    public List<String>  scanJarFile(String jarfile, String superclass){
        ArrayList<String>  scanclasses = new ArrayList<String>();
        List<String> classList = this.getClassList(jarfile);

        System.out.println("---> scanning file : " + jarfile + " for class [" + 
                superclass + "]");
        //Class interface = Class.forName(superclass);
        try {
            Class ci = Class.forName(superclass);
            for(String itemClass : classList){
                try {
                    Class c = Class.forName(itemClass);
                    
                    if(c!=null){
                        //if(c.isAssignableFrom())
                        boolean  flag = ci.isAssignableFrom(c);
                        //System.out.println("class : " + itemClass + "  flag = " + flag);
                        if(flag==true){
                                scanclasses.add(c.getName());
                                //System.out.println("CLASS : [" + itemClass + "] superclass ["
                                //        + c.getSuperclass().getName() + "]");
                                //System.out.println("CLASS " + itemClass);
                        }
                    } else {
                        System.out.println("Warning: unable to process file : " + jarfile);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(JarFileScanner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ClassNotFoundException exh){
            
        }
        return scanclasses;
    }
    
    public List<String>  scanDir(String dir, String superclass){
        ArrayList<String>  matched = new ArrayList<String> ();
        String pluginDirectory = ResourcesUtils.getResourceDir("CLAS12DIR", dir);
        if(pluginDirectory==null){
            System.err.println("Warning:  can not find directory : lib/plugins ");
            return matched;
        }
        
        List<String> pluginFiles = FileUtils.getFilesInDir(pluginDirectory);
        for(String jarFile : pluginFiles){
            List<String> jarList = this.scanJarFile(jarFile, superclass);
            matched.addAll(jarList);
        }
        return matched;
    }
}
