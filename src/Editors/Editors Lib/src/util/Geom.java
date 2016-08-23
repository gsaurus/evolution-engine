/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author Gil Costa
 */
public class Geom {

    	// -----------------------------------
	//      Geometric Transformations
	// -----------------------------------
	
	// Translate
	public IntPoint translate(int x, int y, int dx, int dy){
		x+=dx; y+=dy;
        return new IntPoint(x,y);
	}
	
	// Rotate
	public IntPoint rotate(int x, int y, double angle) {
		int oldX = x;
		x = (int)(x*Math.cos(angle) - y*Math.sin(angle));
		y = (int)(oldX*Math.sin(angle) + y*Math.cos(angle));
        return new IntPoint(x,y);
	}

	// RotateAround
	public IntPoint rotateAround(int x, int y, int cx, int cy, double angle) {
        IntPoint tmp;
		tmp = translate(x,y,-x,-y);
		tmp = rotate(tmp.getX(), tmp.getY(), angle);
		tmp = translate(tmp.getX(), tmp.getY(), x,y);
        return tmp;
	}
	
	//Scale
	public IntPoint scale(int x, int y, double fx, double fy){
		x*=fx; y*=fy;
        return new IntPoint(x,y);
	}
	
	// Centered Scale
	public IntPoint centeredScale(int x, int y, double fx, double fy){
		IntPoint tmp;
		tmp = translate(x,y,-x,-y);
		tmp = scale(tmp.getX(), tmp.getY(), fx,fy);
		tmp = translate(tmp.getX(), tmp.getY(), x,y);
        return tmp;
	}
    
    
    
    
	//-------------------------
	//    STATIC UTILITIES
	//-------------------------
	/**
	 * distance between two points
	 * @param x1 first point x coordinate
	 * @param y1 first point y coordinate
	 * @param x2 second point x coordinate
	 * @param y2 second point y coordinate
	 * @return the distance between the two points
	 */
	public static double distanceBetween(int x1, int y1, int x2, int y2){
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)); 
	}
        
}
