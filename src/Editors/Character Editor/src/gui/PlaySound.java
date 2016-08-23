/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import audio.AudioEncoder;
import audio.AudioFile;
import game.GameInvariants;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gil
 */
public class PlaySound {
    //protected static TreeMap<Integer,AudioFile> sounds = new TreeMap<Integer,AudioFile>();
    
    public PlaySound(int index){
        AudioFile audio;
        //if (sounds.containsKey(index))
        //    audio = sounds.get(index);
        //else{
            String fileName = GameInvariants.EDITORS_WORKING_DIR + GameInvariants.AUDIO_DIR + "\\";
            fileName += index + "." + GameInvariants.SOUND_EXTENSION;
            File file = new File(fileName);
            AudioEncoder encoder = new AudioEncoder();
            encoder.setFile(file);
            try {
                encoder.load();
                audio = encoder.getFileData();
                audio.play();
            } catch (Exception e) { return; }
            //sounds.put(index, audio);
        //}
        
        //try {
        //    audio.play();
        //} catch (Exception e) { return; }
            
    }
}
