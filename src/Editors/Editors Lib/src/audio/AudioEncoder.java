/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package audio;

import file.*;
import game.GameInvariants;
import java.io.DataInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 * @author Gil
 */
public class AudioEncoder extends FileEncoder<AudioFile>{
    
    public static final String ORIGINAL_AUDIO_EXTENSION = "wav";
    
    public AudioEncoder(){
        super(GameInvariants.SOUND_EXTENSION, GameInvariants.SOUND_VERSION);
    }
    
    
    public void convertAll(File dir){        
        File[] files = dir.listFiles( new FilenameFilter(){
                public boolean accept(File dir, String name) {
                    return name.endsWith(ORIGINAL_AUDIO_EXTENSION);
                }
            }
        );
        FilenameFilter filter = new FilenameFilter() { 
            public boolean accept(File b, String name) { 
                return name.endsWith("." + GameInvariants.SOUND_EXTENSION); 
            } 
        }; 
        File[] fs2 = dir.listFiles(filter);
        int n = 1;
        if (fs2!= null) n = fs2.length+1;
        for(File f: files){
            try{
                String name = n+++".";
                name += GameInvariants.SOUND_EXTENSION;
                this.setFile(new File(name));
                this.setFileData(new AudioFile(f, versionID));
                this.save();
            }catch(Exception e){ e.printStackTrace(); }
        }
    }
   
    
     @Override
    public void readData(DataInputStream dis) throws IOException {
        this.setFileData(new AudioFile(versionID));
        super.readData(dis);
    }
    

}
