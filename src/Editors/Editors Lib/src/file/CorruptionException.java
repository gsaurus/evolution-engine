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
public class CorruptionException extends IOException{
    public static final String CORRUPTED_FILE = "Corrupted file!";
    
    public CorruptionException(String error){
        super(error);
    }
}
