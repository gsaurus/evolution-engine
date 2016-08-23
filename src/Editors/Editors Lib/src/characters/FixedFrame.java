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
 * @author Gil Costa
 */
public class FixedFrame implements IReadWrite{
    //--------------
    // -- FIELDS --
    //--------------
    
    public int frameIndex;
    public UByte duration;
    // Note: use x = invalid to invalidade weapon and grab options
    // null means it is disabled on the animation
    public HeadPoint head;
    public WeaponPoint weapon;
    public GrabPoint grab;

    public int versionID;   // Not used yet
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    
    public FixedFrame(int frameIndex, int duration, HeadPoint head, WeaponPoint weapon, GrabPoint grab) {
        this.frameIndex = frameIndex;
        this.duration = new UByte(duration);
        this.head = head;
        this.weapon = weapon;
        this.grab = grab;
    }

    public FixedFrame(DataInputStream dis) throws IOException {
        this.readData(dis);
    }
    
    public FixedFrame(){
        frameIndex = 0;
        duration = new UByte(20);
        head = null; weapon = null; grab = null;
    }
    public FixedFrame(FixedFrame other){
        this.frameIndex = other.frameIndex;
        this.duration = other.duration;
        this.head = new HeadPoint(other.head);
        this.weapon = new WeaponPoint(other.weapon);
        this.grab = new GrabPoint(other.grab);
    }
    

    //---------------
    // -- METHODS --
    //---------------
    
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeShort(frameIndex);
        duration.writeData(dos);
        if(head!=null) head.writeData(dos);
        if(weapon!=null) weapon.writeData(dos);
        if(grab!=null) grab.writeData(dos);
    }

    public void readData(DataInputStream dis) throws IOException {
        frameIndex = dis.readShort();
        duration = new UByte(dis);
        if(head!=null) head.readData(dis);
        if(weapon!=null) weapon.readData(dis);
        if(grab!=null) grab.readData(dis);
    }
    
    
}
