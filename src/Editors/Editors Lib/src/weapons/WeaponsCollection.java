package weapons;

import frames.*;
import file.ACollection;
import game.GameInvariants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



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
public class WeaponsCollection extends ACollection<Weapon>{
    
    public ArrayList<String> frames;
    public ArrayList<String> icons;
    
	/** Constructor: an empty FramesCollection */
	public WeaponsCollection(){
        super(GameInvariants.WEAPONS_EXTENSION, GameInvariants.WEAPONS_VERSION);
        frames = new ArrayList<String>();
        icons = new ArrayList<String>();
	}	
	
	
	//-----------------------------
	// ------- LOAD / SAVE -------
	

    protected void writeHeaderV1(DataOutputStream dos) throws IOException {
        dos.writeByte(frames.size());
        for(String fileName:frames){
            dos.writeByte(fileName.length());
            dos.writeBytes(fileName);
        }
        
        // icons (v1.2)
        dos.writeByte(icons.size());
        for(String fileName:icons){
            dos.writeByte(fileName.length());
            dos.writeBytes(fileName);
        }
    }

    protected void readHeaderV1(DataInputStream dis)  throws IOException {
        int length = dis.readByte();
        frames = new ArrayList<String>(length);
        for(int i=0; i<length; i++){
            int strLen = dis.readByte();
            char[] str = new char[strLen];
            for(int j=0; j<strLen ; j++)
                str[j] = (char)dis.readByte();
        
            frames.add(new String(str));
        }
        
        // icons (v1.2)
        length = dis.readByte();
        icons = new ArrayList<String>(length);
        for(int i=0; i<length; i++){
            int strLen = dis.readByte();
            char[] str = new char[strLen];
            for(int j=0; j<strLen ; j++)
                str[j] = (char)dis.readByte();
        
            icons.add(new String(str));
        }
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
    
    
    
    
   
    @Override
    public Weapon readElement(DataInputStream dis) throws IOException {
        return new Weapon(dis, versionID);
    }
    

    
}
