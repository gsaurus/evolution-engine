package frames;

import file.ACollection;
import game.GameInvariants;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Images collection
 * @author Gil Costa
 * 
 * File Structure:
 * N				2 bytes		-> number of frames
 * I				1 byte		-> if cm are present for each frame
 *  cmx1...cmxN		N bytes		-> CM x coordinates
 *  cmy1...cmyN		N bytes		-> CM y coordinates
 *  size1...sizeN	N*4 bytes	-> size of each frame
 *  img1...imgN		4*(size1+...+sizeN) bytes	-> frames data    
 *
 */
public class FramesCollection extends ACollection<Frame> {
    
	/** if centerInformation exists or is stored */
	protected boolean cmOn;

    
	/** Constructor: an empty FramesCollection */
	public FramesCollection(){
        super(GameInvariants.FRAMES_EXTENSION, game.GameInvariants.FRAMES_VERSION);
		cmOn = true;
	}


	//-------------------------
	// ------- GETTERS -------

    public boolean hasCMOn(){ return cmOn; }


	//-------------------------
	// ------- SETTERS -------
	public void setCMOn(boolean on){
		cmOn = on;
	}
    

	

	//-------------------------------
	// ------- ADDING IMAGES -------

	public void addImages(BufferedImage[] imgs){
		List<Frame> addingFrames = new ArrayList<Frame>(imgs.length);
		for(BufferedImage img:imgs){
			Frame f = new Frame(img, versionID);
			if (f!=null) addingFrames.add(f);
		}
		elements.addAll(addingFrames);
	}

	public void addImage(BufferedImage img){
		Frame f = new Frame(img, versionID);
		if (f!=null) elements.add(f);
	}
	
	
	
	
	//-----------------------------
	// ------- LOAD / SAVE -------
	


    @Override
    public Frame readElement(DataInputStream dis) throws IOException {
        return new Frame(dis,cmOn, versionID);
    }

    
    protected void writeHeaderV1(DataOutputStream dos) throws IOException {
        for(Frame f:elements){
            f.setCMOn(cmOn);
        }
        dos.writeBoolean(cmOn);
    }
    
    
    protected void readHeaderV1(DataInputStream dis) throws IOException {
        cmOn = dis.readBoolean();
    }
    
    
    
    
    
    @Override
    public void writeHeader(DataOutputStream dos) throws IOException {
        switch(versionID){
            case 1: writeHeaderV1(dos); break;
            default: writeHeaderV1(dos);
        }      
    }
     
    @Override
    public void readHeader(DataInputStream dis) throws IOException {
        switch(versionID){
            case 1: readHeaderV1(dis); break;
            default: readHeaderV1(dis);
        }
    }   
	
}
