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
import java.util.List;
import util.BTriple;
import util.IntPoint;
import util.UBPoint;
import util.UByte;

/**
 *
 * @author Gil
 */
public class KeyFrame implements IReadWrite{
    public static final byte INVALID_VEL = Byte.MIN_VALUE;
    
    public static final int FRONTAL_KNOCK = 1;
    public static final int SPARSE_KNOCK = 2;
    public static final int BACK_KNOCK = 4;
    //--------------
    // -- FIELDS --
    //--------------
    public int versionID;
    
    public UByte index;
    public UByte actionType;
    public List<IntPoint> actionPoints;
    public BTriple impulse;
    public BTriple throwImpulse;
    public UByte throwDamage;
    public byte throwRotation;
    public boolean impulsed;
    public boolean invalid;
    
    // Knocking flags
    public UByte knockFlags;
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------

    public KeyFrame(DataInputStream dis, int version) throws IOException {
        versionID = version;
        this.readData(dis);
    }
    public KeyFrame(int version){
        versionID = version;
        this.actionPoints = new ArrayList<IntPoint>();
        index = new UByte();
        actionType = new UByte();
        impulse = new BTriple();
        knockFlags = new UByte();
    }
    public KeyFrame(KeyFrame other){
        this.versionID = other.versionID;
        this.impulsed = other.impulsed;
        this.index = other.index;
        this.actionType = other.actionType;
        this.knockFlags = other.knockFlags;
        this.impulse = new BTriple(other.impulse.x, other.impulse.y, other.impulse.z);
        this.actionPoints = new ArrayList<IntPoint>(other.actionPoints.size());
        for(IntPoint p:other.actionPoints)
            actionPoints.add(p.clone());
    }
    

    //---------------
    // -- METHODS --
    //---------------
    
    protected void writeVs1_2_3(DataOutputStream dos) throws IOException {
        index.writeData(dos);
        // action
        if (actionType!=null){
            actionType.writeData(dos);
            if (actionType.get()>=GameInvariants.AT_KNOCK && versionID>=3)
                knockFlags.writeData(dos);
            // points
            dos.write(actionPoints.size());
            for(IntPoint p:actionPoints){
                p.writeData(dos);
            }
            // throw stuff
            if (actionType.get() == game.GameInvariants.AT_THROW && throwImpulse != null){
                throwImpulse.writeData(dos);
                throwDamage.writeData(dos);
                if (versionID>=2) dos.writeByte(throwRotation);
            }
        }
        // movement impulse
        if (impulsed){
            int toWrite = impulse.x;
            if (impulse.x == KeyFrame.INVALID_VEL)
                toWrite = INVALID_VEL;
            dos.writeByte(toWrite);
            if (toWrite!=INVALID_VEL){
                dos.writeByte(impulse.y);
                dos.writeByte(impulse.z);
            }
        }
    }

    protected void readVs1_2_3(DataInputStream dis) throws IOException {
        index = new UByte(dis);
        // action
        if (actionType !=null){
            actionType.readData(dis);
            if (actionType.get()>=GameInvariants.AT_KNOCK && versionID>=3)
                knockFlags = new UByte(dis);
            // poins
            int n = dis.readByte();
            actionPoints = new ArrayList<IntPoint>(n);
            for(int i=0; i<n; i++)
                actionPoints.add(new IntPoint(dis));
            // throw stuff
            if (actionType.get() == game.GameInvariants.AT_THROW){
                throwImpulse = new BTriple(dis);
                throwDamage = new UByte(dis);
                if (versionID>=2) throwRotation = dis.readByte();
            }
        }
        // movement impulse
        if (impulsed){
            impulse.x = dis.readByte();
            if (impulse.x!=INVALID_VEL){
                impulse.y = dis.readByte();
                impulse.z = dis.readByte();
            }
            else impulse.y = impulse.z = 0;
        }
    }
    
    
    
    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        switch(versionID){
            case 1: writeVs1_2_3(dos); break;
            default: writeVs1_2_3(dos);
        }
    }
     
    @Override
    public void readData(DataInputStream dis) throws IOException {
        switch(versionID){
            case 1: readVs1_2_3(dis); break;
            default: readVs1_2_3(dis);
        }
    }
    
    
}
