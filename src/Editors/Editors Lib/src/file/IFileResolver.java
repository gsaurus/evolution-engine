package file;


import java.io.File;
import java.io.IOException;

public interface IFileResolver{

	//-------------------------
	// ------- GETTERS -------
	/** @return the file name of this ImagesCollection */
	public String getFileName();
    /** @return the file of this ImagesCollection */
	public File getFile();
    public int getVersion();
	


	//-------------------------
	// ------- SETTERS -------
	/** sets the fileName of the images collection */
	public void setFileName(String fileName);
    /** sets the file of the images collection */
	public void setFile(File file);


	//-----------------------------
	// ------- OPEN / SAVE -------

	/** Open an existing ImagesCollection, by giving a File */
	public void load(File file) throws IOException;
	/** Load data from file, accordingly to its internal file name */
	public void load() throws IOException;


	/**
	 * Save the AnimationCollection
	 * @throws IOException if the save operation can't be performed
	 */
	public void save() throws IOException;


}
