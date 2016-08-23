package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class for triples: x,y and z coordinates from 0 to 255 
 * @author Gil Costa
 */
public class UBTriple extends UBPoint{
	/** z coordinate */
	private UByte z;
	
	
	// ----------------------
	//  --- CONSTRUCTORS ---
	// ----------------------

	/** default constructor */
	public UBTriple() {
		super();
		this.z = new UByte();
	}
	
	/** constructor by ubyte coordinates x,y and z */
	public UBTriple(int x, int y, int z) {
		super(x,y);
		this.z = new UByte(z);
	}
    
    public UBTriple(DataInputStream dis) throws IOException{
        readData(dis);
    }

	
	// -------------------------------
	//  ---- GETTERS and SETTERS ----
	// -------------------------------
	
	public final int getZ() { return z.get(); }
	public final void setZ(int z) { this.z.set(z); }
	
	/** set the 3 ubyte coordinates */
	public final void set(int x, int y, int z) {
		super.set(x, y);
		this.z.set(z);
	}
	
	
	
	
	
	@Override
	public UBTriple clone(){
		return new UBTriple(getX(),getY(),getZ());
	}

	@Override
	public String toString() {
		return "b(" + getX() + ", " + getY() +", " + getZ() + ")";
	}
	
    
    @Override
    public void writeData(DataOutputStream dos) throws IOException {
         super.writeData(dos);
         z.writeData(dos);
    }

    @Override
    public void readData(DataInputStream dis) throws IOException {
        super.readData(dis);
        z = new UByte(dis);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UBTriple other = (UBTriple) obj;
        if (!super.equals(other)) return false;
        if (this.z != other.z && (this.z == null || !this.z.equals(other.z))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.z != null ? this.z.hashCode() : 0);
        return hash+super.hashCode();
    }
}
