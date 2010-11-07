/*
 * StringUtils
 *
 * Confidential and proprietary.
 */

package com.appspot.smartshop.utils;

import java.util.Vector;

/**
 * Class StringSpliter is used to split a String into a String array 
 * depends on the token character.
 */
 
public class StringUtils {
    
    
    // Constructor
    public static int arraySize;
    public StringUtils() {    
        
    }
    
    /**
     * Split a given String into new String array bases on the token character.
     * @param token
     *      The token character.
     * @param aStr
     *      The given String     
     * @return
     *      The String array has been splited by token character.
     */      
    public static String[] split(char token, String aStr){
        int startPos = 0;
        int strArrayLength = 0;
        for(int i = 0 ; i < aStr.length() ; i++){            
            if(aStr.charAt(i) == token){
                strArrayLength++;
            }
        }
        
        strArrayLength = strArrayLength + 1;
        
        String[] strArray = new String[strArrayLength];
        int index = 0;
        for(int i = 0 ; i < aStr.length() ; i++){            
            if(aStr.charAt(i) == token){
                int tokenPos = aStr.indexOf(token,startPos+1);
                strArray[index] = aStr.substring(startPos,tokenPos);
                startPos = tokenPos+1;
                index++;
            }
        }
        arraySize = index;
        strArray[index] = aStr.substring(startPos,aStr.length());
        return strArray;
    }
    
    /**
     * Split a given String into new String Vector bases on the token character.
     * 
     * @param token
     *      The token character.
     * @param aStr
     *      The given String     
     * @return
     *      The String Vector.
     */ 
    public Vector splitToVector(char token, String aStr){
        Vector _elements = new Vector();
        try{            
            if(aStr != null && aStr.length() != 0){
                int startPos = 0;
                int strArrayLength = 0;
                for(int i = 0 ; i < aStr.length() ; i++){            
                    if(aStr.charAt(i) == token){
                        strArrayLength++;
                    }
                }
                
                strArrayLength = strArrayLength + 1;
                
                String[] strArray = new String[strArrayLength];
                int index = 0;
                for(int i = 0 ; i < aStr.length() ; i++){            
                    if(aStr.charAt(i) == token){
                        int tokenPos = aStr.indexOf(token,startPos+1);
                        strArray[index] = aStr.substring(startPos,tokenPos);
                        _elements.addElement(strArray[index]);
                        startPos = tokenPos+1;
                        index++;
                    }
                }
                arraySize = index;
                strArray[index] = aStr.substring(startPos,aStr.length());
                _elements.addElement(strArray[index]);
                
            }
            
        }catch(Exception e){
            
        }
        return _elements;
    }
    
    public static boolean isEmptyOrNull(String txt) {
		return (txt == null || txt.equals("")) ? true : false;
	}
    
} 

