
import audio.AudioEncoder;
import java.io.File;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gil
 */
public class Main {
    
    public static void main(String args[]){
        // current directory
        File dir = new File (".");
        // or read file from args
        if (args!=null && args.length>0)
            dir = new File(args[0]);
        
        new AudioEncoder().convertAll(dir);
    }
    
}
