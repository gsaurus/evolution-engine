package util;

import file.IReadWrite;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class for unsigned byte representation
 * @author Gil Costa
 */
public class UByte implements IReadWrite, Comparable<UByte>{
	/** the byte */
	private byte b;
	
	
	// ----------------------
	//  --- CONSTRUCTORS ---
	// ----------------------
	
	/** default constructor: b = zero */
	public UByte(){ b = 0; }
	/** constructor by valye */
	public UByte(int b){
		set(b);
	}
    /** constructor by byte value */
	public UByte(byte b){
		this.b = b;
	}
    
     public UByte(DataInputStream dis) throws IOException {
        readData(dis);
    }
	
	
	// -------------------------------
	//  ---- GETTERS and SETTERS ----
	// -------------------------------
	
	/** set the byte to the given unsigned value */
	public void set(int b){
		int val = b%256;
		if (val < 128) this.b = (byte)b;
		else this.b = (byte)(b-256);
	}
	
	/** get the current byte unsigned value */
	public int get(){
		if (b>=0) return b;
		else return b+256;
	}
	
	
	//--------------------------------
	// ---- Special BYTE getters ----
	/** get the byte value correspondent to the given ubyte value */
	public static byte value(int val){
		UByte tmpByte = new UByte(val);
		return tmpByte.b;
	}
	
	/** get the byte value correspondent to the given UByte */
	public static byte value(UByte b){
		return b.b;
	}
	
	
    //-----------------------------
    // ---- STATIC Operations ----
    
    
    public static UByte degrees(int d){
        d = d%360;
        d = d*255/360;
        return new UByte(d);
    }
    
    public static int getDegrees(UByte b){
        return b.get()*360/255;
    }
    
	
	//------------------------------
	// ---- Clone and ToString ----
	
	@Override
	protected UByte clone(){
		return new UByte(get());
	}

	@Override
	public String toString() {
		return "b" + get();
	}
    
    
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeByte(b);
    }

    public void readData(DataInputStream dis) throws IOException {
        b = dis.readByte();
    }

    public int compareTo(UByte o) {
        return this.b - o.b;
    }
}
