/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package file;

import java.io.IOException;

/**
 *
 * @author Gil
 */
public class InvalidVersionException extends IOException{
    public static final String INVALID_VERSION = "Incompatible file version.";
    
    public InvalidVersionException(String error){
        super(error);
    }
    
    public InvalidVersionException(int expected, int got){
        super(INVALID_VERSION + " Expected: v" + expected + ", got: v" + got);
    }
}
