/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package goodies;

import file.IReadWrite;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import util.UByte;

/**
 *
 * @author Gil Costa
 */
public class Goodie implements IReadWrite{
    public int versionID;
    //--------------
    // -- FIELDS --
    //--------------
    public UByte name;
    public UByte type;
    public short amount;
    
    
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    
    public Goodie(UByte name, UByte type, short amount, int version){
        versionID = version;
        this.name = name;
        this.type = type;
        this.amount = amount;
    }
    
    public Goodie(int name, int type, int ammount, int version){
        this(new UByte(name),new UByte(type),(short)ammount, version);
    }

    public Goodie(DataInputStream dis, int version) throws IOException{
        versionID = version;
        this.readData(dis);
    }
    
    public Goodie(Goodie other){
        this(new UByte(other.name.get()),new UByte(other.type.get()), other.amount, other.versionID);
    }

    public Goodie(int version) {
        versionID = version;
        name = new UByte(255);
        type = new UByte();
        amount = 0;
    }
    
    
    
    
    //---------------
    // -- METHODS --
    //---------------
    
    protected void writeV1(DataOutputStream dos) throws IOException{
        name.writeData(dos);
        type.writeData(dos);
        dos.writeShort(amount);
    }
    
      
    
    protected void readV1(DataInputStream dis) throws IOException{
       // old v 1.0
//        byte n = dis.readByte();
//        char[] txt= new char[n];
//        for(int i=0; i<n; i++)
//            txt[i] = dis.readChar();   
        
        name = new UByte(dis);  // old v 1.1    
        type = new UByte(dis);
        amount = dis.readShort();
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
