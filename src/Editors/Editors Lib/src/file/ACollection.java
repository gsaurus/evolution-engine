package file;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
public abstract class ACollection<E extends IReadWrite> extends AFileResolver implements ICollection<E>{
	/** elements */
	protected List<E> elements;


	/** Constructor: an empty FramesCollection */
	public ACollection(String extension, int version){
        super(extension, version);
		clear();
	
	}

	@Override
	public void clear(){ elements = new ArrayList<E>(); }


	//-------------------------
	// ------- GETTERS -------
	@Override public Iterator<E> iterator(){ return elements.iterator(); }
	@Override public int size(){ return elements.size(); }
	@Override public boolean isEmpty(){ return elements.isEmpty(); }


	
    
    
    //-------------------------------
	// ------- ADDING FRAMES -------

	@Override
	public void add(E[] elements){
        if (elements!=null)
            for(E f: elements)
                add(f);
    }

	@Override
	public void add(E element){
        if (element!=null) elements.add(element);
    }
    
    @Override
	public void add(int pos, E element){
        if (element!=null) elements.add(pos, element);
    }
    
    @Override
	public void set(int i, E element){
        if (i>=0 && i<elements.size())
            elements.set(i, element);
    }
    
    @Override
    public E get(int index){
        if (elements == null || index<0 || index>=elements.size()) return null;
        return elements.get(index);
    }
    
    

	//---------------------------------
	// ------- REMOVING FRAMES -------

	@Override 
	public void remove(int i){
		elements.remove(i);
	}
    
    
    
    
    //-----------------------------
	// ------- LOAD / SAVE -------
	
    
    
    public static final String getName(String s){
        int lastDash = s.lastIndexOf('\\');
        if (lastDash>=0 && lastDash<=s.length())
            return s.substring(lastDash);
        else return s;
    }
    
    
    
    
    public abstract E readElement(DataInputStream dis)  throws IOException;
    
    public abstract void writeHeader(DataOutputStream dos)  throws IOException;
    public abstract void readHeader(DataInputStream dis)  throws IOException;
    // does nothing by default
    public void writeFoother(DataOutputStream dos)  throws IOException{}
    public void readFoother(DataInputStream dis)  throws IOException{}
    

	@Override
	public void writeData(DataOutputStream dos) throws IOException{        
        writeHeader(dos);
        
		int N = size();
		// write number of elements
		dos.writeShort((short)N);
		
        // write all elements
        for(E g:elements){
            g.writeData(dos);
        }
        
        writeFoother(dos);
	}
	
	
    
	@Override
	public void readData(DataInputStream dis) throws IOException{
        readHeader(dis);
        
		// read number of frames
		int N = dis.readShort();
		elements = new ArrayList<E>(N);
        // write all elements
        for(int i=0; i< N; i++){
            elements.add(readElement(dis));
        }
        
        readFoother(dis);
	}
    
	
}
