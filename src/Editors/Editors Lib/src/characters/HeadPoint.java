/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package characters;

import file.IReadWrite;
import game.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import util.UByte;

/**
 *
 * @author Gil
 */
public class HeadPoint implements IReadWrite{
    //--------------
    // -- FIELDS --
    //--------------
    public int x;
    public int y;
    public UByte frame;
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    public HeadPoint(int x, int y, UByte frame){
        this.x = x; this.y = y; this.frame = frame;
    }
    public HeadPoint(int x, int y, int frame){
        this(x,y,new UByte(frame));
    }

    public HeadPoint(DataInputStream dis) throws IOException {
        this.readData(dis);
    }
    public HeadPoint(){
        x = 0; y = 0; frame = new UByte();
    }
    public HeadPoint(HeadPoint other){
        this.x = other.x;
        this.y = other.y;
        this.frame = new UByte(other.frame.get());
    }
    

    //---------------
    // -- METHODS --
    //---------------
    
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeShort(x);
        dos.writeShort(y);
        frame.writeData(dos);
    }

    public void readData(DataInputStream dis) throws IOException {
        x = dis.readShort();
        y = dis.readShort();
        frame = new UByte(dis);
    }
    
    
}
