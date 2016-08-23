package util;

import file.IReadWrite;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class for triples: x,y and z coordinates from 0 to 255 
 * @author Gil Costa
 */
public class BTriple implements IReadWrite{
	/** z coordinate */
	public byte x, y, z;

    public BTriple(byte x, byte y, byte z) {
        set(x,y,z);
    }
    public BTriple(BTriple other){
        set(other);
    }
    public BTriple() {
        this((byte)0,(byte)0,(byte)0);
    }
    public BTriple(DataInputStream dis) throws IOException{
        readData(dis);
    }

    
    
    public void set(byte x, byte y, byte z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void set(BTriple other){
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
    
    
    
    
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeByte(x); dos.writeByte(y); dos.writeByte(z);
    }

    public void readData(DataInputStream dis) throws IOException {
        x = dis.readByte(); y = dis.readByte(); z = dis.readByte();
    }
    
    
}
