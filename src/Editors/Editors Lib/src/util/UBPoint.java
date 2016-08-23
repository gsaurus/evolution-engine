package util;

import file.IReadWrite;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class for point with x,y coordinates from 0 to 255 
 * @author Gil Costa
 */
public class UBPoint implements IReadWrite{
	/** x coordinate */ private UByte x;
	/** y coordinate */ private UByte y;
	
	// ----------------------
	//  --- CONSTRUCTORS ---
	// ----------------------
	
	/** default constructor */
	public UBPoint() {
		this.x = new UByte();
		this.y = new UByte();
	}
	
	/** constructor by unsigned coordinates */
	public UBPoint(int x, int y) {
		this.x = new UByte(x);
		this.y = new UByte(y);
	}
    
    public UBPoint(DataInputStream dis)throws IOException{
        readData(dis);
    }

	
	// -------------------------------
	//  ---- GETTERS and SETTERS ----
	// -------------------------------
	
	public final int getX() { return x.get(); }
	public final int getY() { return y.get(); }
	public final void setX(int x) { this.x.set(x); }
	public final void setY(int y) { this.y.set(y); }
	
	/** set both ubyte coordinates */
	public final void set(int x, int y) {
		this.x.set(x);
		this.y.set(y);
	}

	
	
	
	
	@Override
	public UBPoint clone(){
		return new UBPoint(getX(),getY());
	}

	@Override
	public String toString() {
		return "b(" + getX() + ", " + getY() + ")";
	}
    
    
    
    public void writeData(DataOutputStream dos) throws IOException {
         x.writeData(dos);
         y.writeData(dos);
    }

    public void readData(DataInputStream dis) throws IOException {
        x = new UByte(dis);
        y = new UByte(dis);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UBPoint other = (UBPoint) obj;
        if (this.x != other.x && (this.x == null || !this.x.equals(other.x))) {
            return false;
        }
        if (this.y != other.y && (this.y == null || !this.y.equals(other.y))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.x != null ? this.x.hashCode() : 0);
        hash = 17 * hash + (this.y != null ? this.y.hashCode() : 0);
        return hash;
    }
    
    
    
	
}
