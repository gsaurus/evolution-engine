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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import util.IntPoint;
import util.IntTriple;
import util.UByte;

/**
 *
 * @author Gil
 */
public class Animation implements IReadWrite{
    public static final int NO_ATTACK = 0;
    public static final int ATTACK_WEAPON = 1;
    public static final int ATTACK_POINTS = 2;
    //public static final int LOOP_ATTACK = 10;
    
    public static final int INVALID_SOUND_FRAME = 255;
//    public static final int INVALID_WEAPON = 255;
//    public static final int ANY_WEAPON = 254;
    
//    public static final int ALLOW_NONE = 0;
//    public static final int ALLOW_H = 1;
//    public static final int ALLOW_V = 2;
//    public static final int ALLOW_BOTH = 3;
//    public static final int ALLOW_FLIP = 10;    // adds 10 to the previous options
//    public static final int ONLY_Z_UP = 100;    // adds 100 to the previous options
//    public static final int ONLY_Z_DOWN = 200;    // adds 200 to the previous options
    
    // allow none                             //           0
    public static final int ALLOW_H = 1;      //          01
    public static final int ALLOW_V = 2;      //          10
    public static final int ALLOW_FLIP = 4;   //       01 00
    public static final int ONLY_Z_UP = 8;    //       10 00
    public static final int ONLY_Z_DOWN = 16; //    01 00 00
    public static final int BLOCK_MOVE = 32; //    10 00 00
    
    
    //--------------
    // -- FIELDS --
    //--------------
    public int versionID;
    
    public UByte index;
    public ArrayList<FixedFrame> fixedFrames;
    public HashMap<Integer,KeyFrame> keyFrames;
    
    // fixedFrame controll
    public boolean grabPoint;
    public boolean weaponPoint;
    public boolean headSwap;
    
    // keyPoints controll
    public boolean velocityCtrl;
    public UByte attackOpt;
    
    // Animation controll
    public boolean invinsible;
    public IntTriple endPosition;
    public boolean endFlip;
    
    // Sound controll
    public int sound;
    public UByte soundFrame;
    
    public UByte allowMov;
    
    // extra options
    // TODO
    
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    public Animation(int version){
        versionID = version;
        fixedFrames = new ArrayList<FixedFrame>();
        keyFrames = new HashMap<Integer,KeyFrame>();
        attackOpt = new UByte();
        index = new UByte();
        endPosition = null;
        endFlip = false;
        sound = 0;
        soundFrame = new UByte(INVALID_SOUND_FRAME);
        allowMov = new UByte();
    }

    public Animation(DataInputStream dis, int version) throws IOException {
        versionID = version;
        this.readData(dis);
    }

    

    //---------------
    // -- METHODS --
    //---------------
    
    
    public void framesControll(){
        // fixed frames
        for(FixedFrame f:fixedFrames){
            if (f.head == null && headSwap) f.head = new HeadPoint();
            else if (!headSwap) f.head = null;
            if (f.weapon == null && weaponPoint) f.weapon = new WeaponPoint();
            else if (!weaponPoint) f.weapon = null;
            if (f.grab == null && grabPoint) f.grab = new GrabPoint();
            else if (!grabPoint) f.grab = null;
        }
        
        // key frames
        for(KeyFrame f:keyFrames.values()){
            f.impulsed = velocityCtrl;
            if (attackOpt != null && attackOpt.get() == Animation.ATTACK_POINTS){
                if (f.actionType == null) f.actionType = new UByte();
            }else{
                f.actionType = null;
                f.actionPoints.clear();
            }
        }
    }
    
    
    public void keyFramesControl(){
        // remove invalid keyFrames
         if (!velocityCtrl && (attackOpt == null || attackOpt.get() != Animation.ATTACK_POINTS))
             keyFrames.clear();
         else{
            LinkedList<Integer> lst2 = new LinkedList<Integer>();
             for(Entry<Integer,KeyFrame> e:keyFrames.entrySet()){
                 if (e.getValue().invalid){
                     lst2.add(e.getKey());
                 }
             }
             for(int i :lst2) keyFrames.remove(i);
         }
    }

    
    
    
    public void setVersion(int versionID){
        this.versionID = versionID;
        // fixedFrames (no need for version yet)
        for(FixedFrame f:fixedFrames)
            f.versionID = versionID;
        
        // key frames
        for(KeyFrame f:keyFrames.values()){
            f.versionID = versionID;
        }
    }
    
    protected void writeV1(DataOutputStream dos) throws IOException {
        
        if (index.get() == 18)
            System.out.println("Anim: " + index);
        
        framesControll();
        keyFramesControl();
        // index
        index.writeData(dos);
        // trivial booleans 
        dos.writeBoolean(invinsible);
        dos.writeBoolean(velocityCtrl);
        dos.writeBoolean(grabPoint);
        dos.writeBoolean(weaponPoint);
        
        // end position
        boolean endPositionChange = endPosition != null;
        dos.writeBoolean(endPositionChange);
        if (endPositionChange){
            endPosition.writeData(dos);
            dos.writeBoolean(endFlip);
        }
        
        // attack options
        attackOpt.writeData(dos);
        
        // fixed frames
        dos.writeShort(fixedFrames.size());
        for(FixedFrame f:fixedFrames)
            f.writeData(dos);
        
        // key frames
        dos.write(keyFrames.size());
        for(KeyFrame f:keyFrames.values()){
            if (!f.invalid)
            f.writeData(dos);
        }
        
        // sound
        soundFrame.writeData(dos);
        if (soundFrame.get()!=INVALID_SOUND_FRAME)
            dos.writeShort(sound);
        
        // user movement
        allowMov.writeData(dos);
    }

    protected void readV1(DataInputStream dis) throws IOException {
        // index
        index = new UByte(dis);
        if (index.get() == 18)
            System.out.println("Anim: " + index);
        // trivial booleans 
        invinsible = dis.readBoolean();
        velocityCtrl = dis.readBoolean();
        grabPoint = dis.readBoolean();
        weaponPoint = dis.readBoolean();
        
        // end position
        boolean endPositionChange = dis.readBoolean();
        if (endPositionChange){
            endPosition = new IntTriple(dis);
            endFlip = dis.readBoolean();
        }
        
        // attack options
        attackOpt = new UByte(dis);
        
        // fixed frames
        int size = dis.readShort();
        fixedFrames = new ArrayList<FixedFrame>(size);
        for(int i=0; i<size; i++){
            FixedFrame f = new FixedFrame();
            if (headSwap) f.head = new HeadPoint();
            if (weaponPoint) f.weapon = new WeaponPoint();
            if (grabPoint) f.grab = new GrabPoint();
            f.readData(dis);
            fixedFrames.add(f);
        }
        
        // key frames
        size = dis.read();
        keyFrames = new HashMap<Integer,KeyFrame>(size);
        for(int i=0; i<size; i++){
            KeyFrame f = new KeyFrame(versionID);
            int tmp = attackOpt.get()%10;
            if (tmp == ATTACK_POINTS)
                f.actionType = new UByte();
            else f.actionType = null;
            f.impulsed = velocityCtrl;
            f.readData(dis);
            keyFrames.put(f.index.get(), f);
        }
        
        // sound
        soundFrame = new UByte(dis);
        if (soundFrame.get()!=INVALID_SOUND_FRAME)
            sound = dis.readShort();
        
        // user movement (v1.2)
        allowMov = new UByte(dis);
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
