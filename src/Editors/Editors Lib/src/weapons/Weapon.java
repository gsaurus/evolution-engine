/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package weapons;

import file.IReadWrite;
import game.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.BTriple;
import util.UBPoint;
import util.UByte;

/**
 *
 * @author Gil
 */
public class Weapon implements IReadWrite{
    public int versionID;
    //--------------
    // -- FIELDS --
    //--------------
    public UByte name;
    public UByte actionType;
    public BTriple throwImpulse;
    public UByte throwDamage;
    public byte throwRotation;
    public boolean rotation;
    public List<UBPoint> actionPoints;
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    public Weapon(UByte name, UByte actionType, UBPoint[] actionPoints, boolean rotation, int version){
        versionID = version;
        this.name = name;
        this.actionType = actionType;
        this.rotation = rotation;
        this.actionPoints = new ArrayList<UBPoint>();
        for(UBPoint b:actionPoints)
            this.actionPoints.add(b);
    }
    public Weapon(int name, int actionType, UBPoint[] actionPoints, boolean rotation, int version){
        this(new UByte(name),new UByte(actionType),actionPoints,rotation, version);
    }

    public Weapon(DataInputStream dis, int version) throws IOException {
        versionID = version;
        this.readData(dis);
    }
    public Weapon(int version){
        versionID = version;
        this.name = new UByte(255);
        actionType = new UByte();
        this.actionPoints = new ArrayList<UBPoint>();
    }
    public Weapon(Weapon other){
        this.versionID = other.versionID;
        this.name = new UByte(other.name.get());
        this.actionType = new UByte(other.actionType.get());
        this.rotation = other.rotation;
        this.actionPoints = new ArrayList<UBPoint>(other.actionPoints.size());
        for(UBPoint p:other.actionPoints)
            actionPoints.add(p.clone());
    }
    

    //---------------
    // -- METHODS --
    //---------------
    
    protected void writeVs1_2(DataOutputStream dos) throws IOException {
        name.writeData(dos);
        actionType.writeData(dos);
        if (actionType.get() == game.GameInvariants.AT_THROW && throwImpulse != null){
            throwImpulse.writeData(dos);
            throwDamage.writeData(dos);
            if (versionID>=2) dos.writeByte(throwRotation);
        }
            
        dos.writeBoolean(rotation);
        dos.write(actionPoints.size());
        for(UBPoint p:actionPoints){
            p.writeData(dos);
        }
    }

    protected void readVs1_2(DataInputStream dis) throws IOException {
        // v 1.0
//        byte n = dis.readByte();
//        char[] txt= new char[n];
//        for(int i=0; i<n; i++)
//            txt[i] = dis.readChar();
        name = new UByte(dis);  // v 1.1
        
        actionType = new UByte(dis);
        if (actionType.get() == game.GameInvariants.AT_THROW){
            throwImpulse = new BTriple(dis);
            throwDamage = new UByte(dis);
            if (versionID>=2) throwRotation = dis.readByte();
        }
        rotation = dis.readBoolean();
        byte n = dis.readByte();
        actionPoints = new ArrayList<UBPoint>(n);
        for(int i=0; i<n; i++)
            actionPoints.add(new UBPoint(dis));
    }
    
    
    
    
    
    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        switch(versionID){
            case 1: case 2: writeVs1_2(dos); break;
            default: writeVs1_2(dos);
        }
    }
     
    @Override
    public void readData(DataInputStream dis) throws IOException {
        switch(versionID){
            case 1: case 2: readVs1_2(dis); break;
            default: readVs1_2(dis);
        }
    }
    
    
}
