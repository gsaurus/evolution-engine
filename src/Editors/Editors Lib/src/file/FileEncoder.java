/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Gil
 */
public class FileEncoder<E extends IReadWrite> extends AFileResolver{
    E fileData;
    
    public FileEncoder(String extension, int version){
        super(extension, version);
        fileData = null;
    }
    
    public FileEncoder(E fileData){
        this.fileData = fileData;
    }
    public FileEncoder(){
        fileData = null;
    }
    
    public E getFileData(){ return fileData; }
    public void setFileData(E fileData){ this.fileData = fileData; }
    
    
    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        if (fileData == null) return;
        fileData.writeData(dos);
    }

    @Override
    public void readData(DataInputStream dis) throws IOException {
        if (fileData == null) return;
        fileData.readData(dis);
    }
    

}
