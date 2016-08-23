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
public class WeaponPoint implements IReadWrite{
    public static final int INVALID_WEAPON_POINT = -10000;
    //--------------
    // -- FIELDS --
    //--------------
    public int x;
    public int y;
    public UByte angle;
    public boolean inFront;
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    public WeaponPoint(int x, int y, int angle, boolean inFront){
        this.x = x; this.y = y;
        this.angle = UByte.degrees(angle);
        this.inFront = inFront;
    }

    public WeaponPoint(DataInputStream dis) throws IOException {
        this.readData(dis);
    }
    public WeaponPoint(){
        x = 0; y = 0; angle = new UByte(); inFront = true;
    }
    public WeaponPoint(WeaponPoint other){
        this.x = other.x;
        this.y = other.y;
        this.angle = new UByte(other.angle.get());
        this.inFront = other.inFront;
    }
    

    //---------------
    // -- METHODS --
    //---------------
    
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeShort(x);
        if (x!=INVALID_WEAPON_POINT){
            dos.writeShort(y);
            angle.writeData(dos);
            dos.writeBoolean(inFront);
        }
    }

    public void readData(DataInputStream dis) throws IOException {
        x = dis.readShort();
        if (x!=INVALID_WEAPON_POINT){
            y = dis.readShort();
            angle = new UByte(dis);
            inFront = dis.readBoolean();
        }
    }
    
    
}
