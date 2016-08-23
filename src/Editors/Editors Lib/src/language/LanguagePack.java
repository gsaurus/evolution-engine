/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

import file.IReadWrite;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Gil
 */
public class LanguagePack implements IReadWrite {
    public int versionID;
    public ArrayList<String> goodies;
    public ArrayList<String> weapons;
    public ArrayList<String> menus;
    public ArrayList<String> speach;

    
    LanguagePack(int version){
        versionID = version;
        goodies = new ArrayList<String>();
        weapons = new ArrayList<String>();
        menus = new ArrayList<String>();
        speach = new ArrayList<String>();
    }
    
    
    protected void writeList(DataOutputStream dos, ArrayList<String> lst, boolean int16) throws IOException{
        if (int16) dos.writeShort(lst.size());
        else dos.writeByte(lst.size());
        for(String s:lst){
            if (int16) dos.writeShort(s.length());
            else dos.writeByte(s.length());
            dos.writeChars(s);
        }
    }
    
    protected void readList(DataInputStream dis, ArrayList<String> lst, boolean int16) throws IOException{
        int size;
        if (int16) size = dis.readShort();
        else size = dis.readByte();
        lst.clear();
        for(int i=0; i<size; i++){
            int len;
            if (int16) len = dis.readShort();
            else len = dis.readByte();
            
            char[] text = new char[len];
            for (int j=0; j<len; j++)
                text[j] = dis.readChar();
            lst.add(new String(text));
        }
    }
    
    protected void writeV1(DataOutputStream dos) throws IOException {
        writeList(dos,goodies,false);
        writeList(dos,weapons,false);
        writeList(dos,menus,false);
        writeList(dos,speach,true);
    }

    protected void readV1(DataInputStream dis) throws IOException {
        readList(dis,goodies,false);
        readList(dis,weapons,false);
        readList(dis,menus,false);
        readList(dis,speach,true);
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
