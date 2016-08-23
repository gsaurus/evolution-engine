/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package audio;

import file.IReadWrite;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream; 

/**
 *
 * @author Gil
 */
public class AudioFile implements IReadWrite{
    public int versionID;
    protected byte[] data;
    protected Clip clip;
    
    AudioFile(int version){ versionID = version; }
    
    AudioFile(File file, int version) throws IOException{
        versionID = version;
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
         byte[] buffer = new byte[4096];
         int bytesRead;         
         while ((bytesRead = fis.read(buffer)) != -1)	
             os.write(buffer, 0, bytesRead);
        
        data = os.toByteArray();
    }
    
    public void play() throws Exception{
        if (clip == null) clip = AudioSystem.getClip();
        ByteArrayInputStream dis = new ByteArrayInputStream(data);
        AudioInputStream is = AudioSystem.getAudioInputStream(dis);
        clip.open(is);
        clip.start(); 
    }

    protected void writeV1(DataOutputStream dos) throws IOException {
        dos.writeInt(data.length);
        dos.write(data);
    }

    protected void readV1(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        data = new byte[size];
        dis.read(data);
    }
    
    
    
    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        switch(versionID){
            case 1: writeV1(dos); break;
            default: writeV1(dos);
        }
    }
     
    @Override
    public void readData(DataInputStream dis) throws IOException {
        switch(versionID){
            case 1: readV1(dis); break;
            default: readV1(dis);
        }
    }

}
