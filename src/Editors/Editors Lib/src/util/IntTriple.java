package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class for point with x,y integer coordinates 
 * @author Gil Costa
 */
public class IntTriple extends IntPoint{
	/** z coordinate */ private int z;
	
	
	// ----------------------
	//  --- CONSTRUCTORS ---
	// ----------------------
	
	/** default constructor */
	public IntTriple() {
		super();
        z = 0;
	}
	
	/** constructor by (x,y) coordinates */
	public IntTriple(int x, int y, int z) {
		super(x,y);
        this.z = z;
	}
    
    /** constructor by data input stream */
	public IntTriple(DataInputStream dis) throws IOException {
		readData(dis);
	}
	
	
	// -------------------------------
	//  ---- GETTERS and SETTERS ----
	// -------------------------------

	public final int getZ() { return z; }
	public final void setZ(int z) { this.z = z; }
	
	/** set (x,y) coordinates */
	public final void set(int x, int y, int z) {
		super.set(x,y);
        this.z = z;
	}
	
	
	
	
	
	@Override
	public IntTriple clone(){
		return new IntTriple(getX(),getY(), getZ());
	}

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
	}
    
    

    @Override
    public void writeData(DataOutputStream dos) throws IOException {
         super.writeData(dos);
         dos.writeShort(z);
    }

    @Override
    public void readData(DataInputStream dis) throws IOException {
        super.readData(dis);
        z = dis.readShort();
    }
	
}
