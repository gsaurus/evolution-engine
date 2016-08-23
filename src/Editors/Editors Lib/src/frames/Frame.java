package frames;

import file.IReadWrite;
import java.awt.image.BufferedImage;

import java.io.DataOutputStream;
import java.io.IOException;
import util.IntPoint;
import image.ImageUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import util.Pair;

/**
 * A single image frame, with shift and mass center information
 * @author Gil Costa
 */
public class Frame implements IReadWrite{
    public int versionID;
    /** the format of the saved images */
	public static final String IMG_FORMAT = "png";
    
    
	/** the image*/
	protected BufferedImage img;
	/** hot spot */
	protected IntPoint cm;
    /** action points */
	//protected List<IntPoint> pts;
    
    protected boolean cmOn;
    
	
	
	/** default constructor: everything uninitialised */
	public Frame(int version) {
        versionID = version;
		this.img = null;
		cm = null;
        //pts = new ArrayList<IntPoint>();
	}
	
	/** constructor by giving the image */
	public Frame(BufferedImage img, int version) {
        versionID = version;
		this.img = img;
		cm = new IntPoint();
        //pts = new ArrayList<IntPoint>();
	}
	
	/** constructor by fields */
	public Frame(BufferedImage img, IntPoint cm, int version) {
        versionID = version;
		this.img = img;
		this.cm = cm;
        //pts = new ArrayList<IntPoint>();
	}
    
    public Frame(DataInputStream dis, boolean cmOn, int version) throws IOException{
        versionID = version;
        this.cmOn = cmOn;
        readData(dis);
    }
	
	//-------------------------
	// ------- GETTERS -------
	
	public BufferedImage getImage(){ return img; }
	/** get the mass center */
	public IntPoint getCM(){ return cm; }
    /** get the mass center */
	//public List<IntPoint> getActionPoints(){ return pts; }
    
    public boolean isCMOn(){ return cmOn; }
	
	//-------------------------
	// ------- SETTERS -------
	
    public void setCMOn(boolean on){ cmOn=on; }
    
//    public void addActionPoint(int x, int y){
//        pts.add(new IntPoint(x,y));
//    }
//    
//    public void setActionPoint(int i, int x, int y){
//        if (i>=0 && i < pts.size())
//            pts.set(i, new IntPoint(x,y));
//    }
    
    protected IntPoint cropedPoint(IntPoint pt, IntPoint delta){
        IntPoint newPt = new IntPoint();
        newPt.setX(cm.getX()-delta.getX());
        newPt.setY(cm.getY()-delta.getY());
        return newPt;
    }
    
	/** set the mass center */
	public void setCM(IntPoint pt){ cm = pt; }
	public void setImg(BufferedImage img){ this.img = img; }
    public boolean cropFrame(){
        Pair<IntPoint,BufferedImage> pt = ImageUtils.crop(this.img);
        if (pt==null) return false;
        img = pt.second;
        
        // crop cm
        cm = cropedPoint(cm,pt.first);
        // crop action points
//        for(IntPoint p:pts)
//            p = cropedPoint(p,pt.first);
        return true;
    }

    
    
    protected void writeV1(DataOutputStream dos) throws IOException {
        // hot spot
        if (cmOn)
            cm.writeData(dos);
        // action points
        /*
        if (actionOn){
            dos.write(pts.size());
            for(IntPoint p:pts)
                p.writeData(dos);
        }
        */
        
        //--------------------------------
		//  --- write the images data ---
		// write image into byteArray, to know its size first
        ByteArrayOutputStream imgsData = new ByteArrayOutputStream();
		
		// write the image into the temporary imgsData stream
		ImageIO.write(img, IMG_FORMAT, imgsData);
		
		// write the sizes into the data output stream
        dos.writeInt(imgsData.size());
		
		// finally write the image
		dos.write(imgsData.toByteArray());
    }
    
    
     
    
    protected void readV1(DataInputStream dis) throws IOException {
        // hot spot
        if (cmOn)
            cm = new IntPoint(dis);
        // action points
        /*
        if (actionOn){
            int nActions = dis.readByte();
            pts = new ArrayList<IntPoint>(nActions);
            for(int i=0; i< nActions; i++){
                pts.add(new IntPoint(dis));
            }
        }
        */
        
        
        //------------------------------
		//  --- read the image data ---
		// read size
		int size = dis.readInt();
		
		// read image
		byte[] tmp = new byte[size];
		dis.read(tmp);
		img = ImageIO.read(new ByteArrayInputStream(tmp));
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
