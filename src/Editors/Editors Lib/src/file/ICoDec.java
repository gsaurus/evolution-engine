package file;

import java.io.File;
import java.io.IOException;

/**
 * Interface for codeing/decoding data
 * @author Gil Costa
 */
public interface ICoDec {
	
	/**
	 * encode method
	 * @param data the byteArray to encode
	 * @return the result of encoding the given byteArray
	 */
	public byte[] encode(byte[] data);
	/**
	 * decode method
	 * @param data the byteArray to decode
	 * @return the result of decoding the given byteArray
	 */
	public byte[] decode(byte[] data);
	
	
	/** encodes and saves data directly to the given file */
	public void encodeToFile(byte[] data, File file) throws IOException;
	
	/** reads the given file and decodes it's information */
	public byte[] decodeFromFile(File file) throws IOException;
	
}
