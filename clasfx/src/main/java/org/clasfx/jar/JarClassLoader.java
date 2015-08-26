/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.jar;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.clas.tools.utils.FileUtils;
import org.jlab.clas.tools.utils.ResourcesUtils;
import org.jlab.clasrec.loader.JarPluginLoader;
import org.jlab.clasrec.main.DetectorReconstruction;

/**
 *
 * @author gavalian
 */
public class JarClassLoader<T> {
    public TreeMap<String,T>  classMap = new TreeMap<String,T>();
    public ArrayList<String>  classArray = new ArrayList<String>();
    //public T  referenceClass = null;
    
    public JarClassLoader(){
        //this.referenceClass = ;
    }
    
    public void list(){
        for(Map.Entry<String,T> entry : this.classMap.entrySet()){
            System.out.println(entry.getKey());
        }
    }
    
    public List<String> getJarClassList(String jarfile){
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
            System.err.println("[JarFileLoader] ----> (warning) file is not a proper jar : " + jarfile);
            //Logger.getLogger(JarPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classList;
    }
    
    public void showList(String jarfile){
        List<String> list = this.getJarClassList(jarfile);
        for(String item : list){
            System.out.println("--->  " + item);
        }
    }
    
    public void scanDir(String dir,String classname){
        String pluginDirectory = ResourcesUtils.getResourceDir("CLAS12DIR", dir);
        if(pluginDirectory==null){
            System.err.println("[ERROR] Can not find directory : lib/plugins ");
            return;
        }
        
        List<String> pluginFiles = FileUtils.getFilesInDir(pluginDirectory);
        for(String jarFile : pluginFiles){
            this.scan(jarFile, classname);
        }
        
    }
    
    public List<String>  getList(){
        return this.classArray;
    }
    
    public void scan(String jarfile, String classString){
        List<String> classList = this.getJarClassList(jarfile);

        System.out.println("---> scanning file : " + jarfile + " for class [" + 
                classString + "]");
        
        for(String itemClass : classList){
            try {
                Class c = Class.forName(itemClass);
                
                if(c!=null){
                    if(c.getSuperclass()!=null){
                        if(c.getSuperclass().getName().compareTo(classString)==0){
                            this.classArray.add(c.getName());
                            //System.out.println("CLASS : [" + itemClass + "] superclass ["
                            //        + c.getSuperclass().getName() + "]");
                            //System.out.println("CLASS " + itemClass);
                        }
                    }
                } else {
                    System.out.println("Warning: unable to process file : " + jarfile);
                }
            }
/*
            System.out.println("Examining CLASS : " + className + " "
            + c.getSuperclass() + "  [" + c.getSuperclass().getName()
            + "]   [" +
            refclass.getName() + "]  "
            + c.getSuperclass().getName().compareTo(refclass.getClass().getName()));
            if(c.getSuperclass().getName().compareTo(refclass.getClass().getName())==0){
            //if(c.getSuperclass() == DetectorReconstruction.class){
            //System.err.println("\t ====> CLASS = " + className);
            System.out.println("FOUND ONE");
            Class  rec = (Class) c.newInstance();
            this.classMap.put(c.getClass().getName(),
            (T) rec);
            //jarGenericClasses.put(rec.getName(), rec);
            }
            }
            } catch (IOException ex) {
            System.err.println("[JarFileLoader] ----> (warning) file is not a proper jar : " + jarfile);
            //Logger.getLogger(JarPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(JarPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
            Logger.getLogger(JarPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
            Logger.getLogger(JarPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
            }*/ catch (ClassNotFoundException ex) {
                Logger.getLogger(JarClassLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }
    
    public static void main(String[] args){
        JarClassLoader<DetectorReconstruction>  loader = new JarClassLoader<DetectorReconstruction>();
        loader.showList("/Users/gavalian/Work/Software/Release-8.0/COATJAVA/coatjava/lib/plugins/clasrec-ft.jar");
        //loader.scan("/Users/gavalian/Work/Software/Release-8.0/COATJAVA/coatjava/lib/plugins/clasrec-ft.jar",
        //        "DetectorReconstruction", DetectorReconstruction.class);
        //loader.list();
    }
}
