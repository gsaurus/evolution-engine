/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import file.AFileResolver;
import file.CorruptionException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Gil
 */
public class GenericFile extends AFileResolver{

    public GenericFile(File f){
        try {
            this.setExtension(f.getName().substring(f.getName().lastIndexOf('.')+1));
            load(f);
        } catch (IOException ex){ ex.printStackTrace(); }
    }
    
    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    @Override
    protected void readFileProtection(DataInputStream dis) throws IOException{
        // readFileNameProtection
        int length = dis.readByte();
        char[] str = new char[length];
        for(int i=0; i<length ; i++)
            str[i] = (char)dis.readByte();
        char[] fileName = file.getName().toCharArray();
        if (!Arrays.equals(str, fileName))
            throw new CorruptionException(CorruptionException.CORRUPTED_FILE);
        // read versionID
        versionID = dis.readByte();
    }
    
    @Override
    public void readData(DataInputStream dis) throws IOException {
        // does nothing
    }

}
