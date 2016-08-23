package file;

public interface ICollection<E> extends IFileResolver, Iterable<E>{
    
    /** clear all ellements */
	public void clear();
	//-------------------------
	// ------- GETTERS -------
	/** @return the frame at position i */
	public E get(int i);
	/** @return the number of frames stored */ 
	public int size();
	/** @return true if it have any frames */
	public boolean isEmpty();
    

    //-------------------------------
	// ------- ADDING ELLEMENTS -------

	/** add multiple images */
	public void add(E[] element);

	/** add a single element, if it isn't an empty element */
	public void add(E element);
    /** add a single element in the position, shifts elements to the right */
	public void add(int pos, E element);
    
    /** set the element in the specified position */
    public void set(int i, E element);
    
	//---------------------------------
	// ------- REMOVING FRAMES -------

	public void remove(int i);

}
