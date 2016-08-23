package util;

import file.IReadWrite;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class for point with x,y integer coordinates 
 * @author Gil Costa
 */
public class IntPoint implements IReadWrite{
	/** x coordinate */ private int x;
	/** y coordinate */ private int y;
	
	
	// ----------------------
	//  --- CONSTRUCTORS ---
	// ----------------------
	
	/** default constructor */
	public IntPoint() {
		this.x = 0;
		this.y = 0;
	}
	
	/** constructor by (x,y) coordinates */
	public IntPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
    
    /** constructor by data input stream */
	public IntPoint(DataInputStream dis) throws IOException {
		readData(dis);
	}
	
	
	// -------------------------------
	//  ---- GETTERS and SETTERS ----
	// -------------------------------

	public final int getX() { return x; }
	public final int getY() { return y; }
	public final void setX(int x) { this.x = x; }
	public final void setY(int y) { this.y = y; }
	
	/** set (x,y) coordinates */
	public final void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	
	
	@Override
	public IntPoint clone(){
		return new IntPoint(getX(),getY());
	}

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
    
    

    public void writeData(DataOutputStream dos) throws IOException {
         dos.writeShort(x);
         dos.writeShort(y);
    }

    public void readData(DataInputStream dis) throws IOException {
        x = dis.readShort();
        y = dis.readShort();
    }
    
    
    public void rotateAround(int cx, int cy, double rads){
        x-=cx; y-=cy;
        int tmpx = (int)(x*Math.cos(rads) - y*Math.sin(rads));
        int tmpy = (int)(x*Math.sin(rads) + y*Math.cos(rads));
        x = tmpx; y = tmpy;
        x+=cx; y+=cy;
    }
	
}
