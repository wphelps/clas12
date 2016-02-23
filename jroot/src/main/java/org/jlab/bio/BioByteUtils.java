/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.bio;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class BioByteUtils {
    
    public static TreeMap<Integer,Integer> bitMap = BioByteUtils.createBitMap();
    
    public static TreeMap<Integer,Integer>  createBitMap(){
        TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();
        for(int loop = 0; loop < 32; loop++){
            int  integer_value = 0;
            for(int hb = 0 ; hb < loop; hb++){
                integer_value = integer_value | (1<<hb);
            }
            map.put(loop, integer_value);
        }
        return map;
    }
    
    public static byte[] generateByteArray(int count){
        byte[] array = new byte[count];
        byte data = 0;
        for(int i = 0; i < count; i++){
            array[i] = data;
            data++;
            if(data>125) data = 0;
        }
        return array;
    }
    
    public static void printBitMap(){
        for(Map.Entry<Integer,Integer> entry : bitMap.entrySet()){
            System.out.println(String.format("%4d : ", entry.getKey()) 
                    + String.format("%32s", Integer.toBinaryString(entry.getValue())).replace(' ', '0'));
        }
    }
    
    public static int getInteger(int data, int bitstart, int bitend){
        int length = bitend - bitstart + 1;
        if(BioByteUtils.bitMap.containsKey(length)==true){
            int value = ((data>>bitstart)&BioByteUtils.bitMap.get(length));
            return value;
        } else {
            System.out.println("[DataUtilities] : ERROR length = " + length);
        }
        return 0;
    }
    
    public static short getShortFromByte(byte data){
        short short_data = 0;
        return (short) ((short_data|data)&0x00FF);
    }
    
    public static int  getIntFromShort(short data){
        int int_data = 0;
        return (int) ( (int_data|data)&0x0000FFFF);
    }
    
    public static int  getIntFromByte(byte data){
        int int_data = 0;
        return (int) ( (int_data|data)&0x0000FFFF);
    }
    
    public static short  getShortFromInt(int data){
        short int_data = 0;
        return (short) ( (int_data|data)&0xFFFF);
    }
    
    public static byte   getByteFromInt(int data){
        byte byte_data = 0;
        return (byte) ((byte_data|data)&0xFF);
    }
    
    public static byte   getByteFromShort(short data){
        byte byte_data = 0;
        return (byte) ((byte_data|data)&0xFF);
    }
    
    public static String getByteString(int word){
        String strL = String.format("%32s", Integer.toBinaryString(word)).replace(' ', '0');
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < 32; i++){
            str.append(strL.charAt(i));
            if((i+1)%8==0) str.append(" ");
        }
        return str.toString();
    }
    
    public static int write(int word, int number, int start, int end){
        int index = end - start + 1;
        int result = (word & ~(BioByteUtils.bitMap.get(index)<<(start)));
        result = result|( (number&BioByteUtils.bitMap.get(index))<<(start)) ;
        return result;
    }
    
    public static int read(int word, int start, int end){
        int index = end-start+1;
        return (word>>(start))&BioByteUtils.bitMap.get(index);
    }
    
    public static void main(String[] args){
        //BioByteUtils.printBitMap();
        //System.out.println(BioByteUtils.getByteString(1245678));
        int header  = 0x00FFFFFF;
        int headerM = BioByteUtils.write(header, 4, 10, 12);
        int headerR = BioByteUtils.read(headerM, 10,12);
        
        System.out.println(BioByteUtils.getByteString(header));
        System.out.println(BioByteUtils.getByteString(headerM));
        System.out.println(BioByteUtils.getByteString(headerR));
    }
}
