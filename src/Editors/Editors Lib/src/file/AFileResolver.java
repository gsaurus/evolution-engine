package file;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;



/**
 * File resolver
 * @author Gil Costa
 *
 */
public abstract class AFileResolver implements IFileResolver{
    protected String extension;
    protected int versionID = -1;
	/** the file associated to this collection */
	protected File file;
	
	/** A codec to save and load files */
	protected ICoDec codec;

	/** Constructor: an empty FramesCollection */
	public AFileResolver(String extension, int versionID){
        this.extension = extension;
        this.versionID = versionID;
		file = null;
		codec = new NoCoDec();
	}
    /** Constructor: an empty FramesCollection */
	public AFileResolver(){
        this("",0);
	}
    
    
    public void setExtension(String extension){ this.extension = extension; }
    public String getExtension(){ return extension; }


	//-------------------------
	// ------- GETTERS -------

    public String getFileName(){
        if (file == null) return null;
        return file.getName();
    }
    public File getFile(){ return file; }
    public int getVersion(){ return versionID; }
    public void setVersion(int versionID){ this.versionID = versionID; }


	//-------------------------
	// ------- SETTERS -------
	public void setFileName(String fileName){
		this.file = new File(fileName);
	}
	public void setFile(File file){
		this.file = file;
	}

	
   
    
    //---------------------------------
	// ------- FILE STUFF -------
     
    
    protected boolean fileNameOk(File file){
        String fileName = file.getName();
        String currentExtension = fileName.substring(fileName.lastIndexOf('.')+1);
        return currentExtension.compareTo(extension) == 0;
    }
    
    protected void resolveFile(){
        if (!fileNameOk(file)){
            String fileName = file.getPath();
            fileName+= "." + extension;
            file = new File(fileName);
        }
    }
    
    
    
    protected void writeFileProtection(DataOutputStream dos) throws IOException{
        String fileName = file.getName();
        dos.writeByte(fileName.length());
        dos.writeBytes(fileName);
        dos.writeByte(versionID);
    }
    
    
    protected void readFileProtection(DataInputStream dis) throws IOException{
        // readFileNameProtection
        int length = dis.readByte();
        char[] str = new char[length];
        for(int i=0; i<length ; i++){
            //str[i] = dis.readChar();      // v 1.0
            str[i] = (char)dis.readByte();  // v 1.1
        }
        char[] fileName = file.getName().toCharArray();
        if (!Arrays.equals(str, fileName))
            throw new CorruptionException(CorruptionException.CORRUPTED_FILE);
        int v = dis.readByte();
        if (versionID!=-1 && v!=versionID) throw new InvalidVersionException(versionID,v);
    }
    
    
    
    
    //-----------------------------
	// ------- LOAD / SAVE -------
	
 
    
    public abstract void writeData(DataOutputStream dos)  throws IOException;
    public abstract void readData(DataInputStream dis)  throws IOException;
    
    
	public void save() throws IOException{
        // fileName validation
        resolveFile();
        
		// create a ByteArrayOutputStream to stream everything into a byte array
		// to encode it first and finally save into file
		ByteArrayOutputStream os;
		os = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
        
        
        // read file protection
        writeFileProtection(dos);
        
        writeData(dos);
        
		dos.close();

		//------------------------------------
		// --- Encode information to file ---
		codec.encodeToFile(os.toByteArray(), file);
	}
	
    
	
	public void load(File file) throws IOException{
		if (!fileNameOk(file))
            throw new IOException("Invalid file type");
        this.file = file;
		// decode file information
		byte[] data = codec.decodeFromFile(file);
		// create InputStream and DataInputStream to read the data
		ByteArrayInputStream is;
		is = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(is);
		
        
        // readFileNameProtection
        readFileProtection(dis);
        
        readData(dis);
		
        dis.close();
        is.close();
	}
    
    @Override
	public void load() throws IOException{
		load(file);
	}
    
	
}
