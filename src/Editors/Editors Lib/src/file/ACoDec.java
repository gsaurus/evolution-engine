package file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Abstract class that performs the encoding and decoding to/from file
 * @author Gil Costa
 */
public abstract class ACoDec implements ICoDec{

	@Override
	public void encodeToFile(byte[] data, File file) throws IOException {
		byte[] encodedData = encode(data);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(encodedData);
		fos.close();
	}

	@Override
	public byte[] decodeFromFile(File file) throws IOException {
		// file stream to read data
		FileInputStream fis = new FileInputStream(file);
		// byte stream to store the readed data
		ByteArrayOutputStream os;
		os = new ByteArrayOutputStream();
		
		// read file data
		int read = 0;
		byte[] tmpData = new byte[2048];
		while((read = fis.read(tmpData)) != -1)
			os.write(tmpData,0,read);
		
		fis.close();
		os.close();
		return decode(os.toByteArray());
	}
}
