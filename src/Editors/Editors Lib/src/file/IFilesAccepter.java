package file;

import java.io.File;
import java.util.Collection;

public interface IFilesAccepter {
	public void acceptFiles(File[] files, int index);
	public void acceptFile(File file, int index);
}
