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
public class GrabPoint implements IReadWrite{
    public static final int INVALID_GRAB_POINT = -10000;
    //--------------
    // -- FIELDS --
    //--------------
    public int baseX;
    public int baseY;
    public UByte angle;
    public UByte anim;
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    public GrabPoint(int x, int y, int angle, UByte anim, boolean flip){
        this.baseX = x; this.baseY = y;
        this.angle = UByte.degrees(angle);
        this.anim = anim;
    }
    
    public GrabPoint(int x, int y, int angle, int anim, boolean flip){
        this(x,y,angle,new UByte(anim), flip);
    }

    public GrabPoint(DataInputStream dis) throws IOException {
        this.readData(dis);
    }
    public GrabPoint(){
        baseX = 0; baseY = 0; angle = new UByte(); anim = new UByte();
    }
    public GrabPoint(GrabPoint other){
        this.baseX = other.baseX;
        this.baseY = other.baseY;
        this.angle = new UByte(other.angle.get());
        this.anim = new UByte(other.anim.get());
    }
    

    //---------------
    // -- METHODS --
    //---------------
    
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeShort(baseX);
        if (baseX!=INVALID_GRAB_POINT){
            dos.writeShort(baseY);
            angle.writeData(dos);
            anim.writeData(dos);
        }
    }

    public void readData(DataInputStream dis) throws IOException {
        baseX = dis.readShort();
        if (baseX!=INVALID_GRAB_POINT){
            baseY = dis.readShort();
            angle = new UByte(dis);
            anim = new UByte(dis);
        }
    }
    
    
}
