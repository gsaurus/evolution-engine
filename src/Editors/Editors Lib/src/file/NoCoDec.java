package file;


/**
 * Codification class that does not encode/decode data
 * @author Gil Costa
 */
public class NoCoDec extends ACoDec implements ICoDec{

	@Override
	public byte[] encode(byte[] data) {
		return data;
	}
	
	
	@Override
	public byte[] decode(byte[] data) {
		return data;
	}
}
