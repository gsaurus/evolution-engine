package gui;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.*;
import java.net.URI;
import java.util.LinkedList;

import file.IFilesAccepter;

/**
 *  Image Drop Target Listener
 *
 *@changed    Gil Costa
 *@author     Quan Nguyen
 *@version    1.1, 23 December 2007
 *@see        http://vietpad.sourceforge.net
 */
class FileDropTargetListener extends DropTargetAdapter {
    private IFilesAccepter holder;
    private LinkedList<File> files;
    private final int myIndex;
    
    /**
     *  Constructor for the FileDropTargetListener object
     *
     *
     * @param fm  instance of VietPad
     */
    public FileDropTargetListener(IFilesAccepter holder, int index) {
        this.holder = holder;
        myIndex = index;
    }
    
    
    public int getIndex(){ return myIndex; }
    
    /**
     *  Gives visual feedback
     *
     *@param  dtde  the DropTargetDragEvent
     */
    public void dragOver(DropTargetDragEvent dtde) {
        if (files == null) {
            DataFlavor[] flavors = dtde.getCurrentDataFlavors();
            for (int i = 0; i < flavors.length; i++) {               
                if (flavors[i].isFlavorJavaFileListType()) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    return;
                }
            }
        }
        dtde.rejectDrag();
    }
    
    /**
     *  Handles dropped files
     *
     *@param  dtde  the DropTargetDropEvent
     */
    @SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent dtde) {
        Transferable transferable = dtde.getTransferable();
        DataFlavor[] flavors = transferable.getTransferDataFlavors();
        
        final boolean LINUX = System.getProperty("os.name").equals("Linux");
        files = new LinkedList<File>();
        for (int i = 0; i < flavors.length; i++) {
//            System.out.println(flavors[i].getMimeType());
            try {
                if (flavors[i].equals(DataFlavor.javaFileListFlavor) || (LINUX && flavors[i].getPrimaryType().equals("text") && flavors[i].getSubType().equals("uri-list"))) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    
                    // Missing DataFlavor.javaFileListFlavor on Linux (Bug ID: 4899516)
                    if (flavors[i].equals(DataFlavor.javaFileListFlavor)) {
                        java.util.List<Object> fileList = (java.util.List<Object>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        files.add((File) fileList.get(0));
                    } else {
                        // This workaround is for File DnD on Linux
                        String string =
                                transferable.getTransferData(DataFlavor.stringFlavor).toString().replaceAll("\r\n?", "\n");
                        URI uri = new URI(string.substring(0, string.indexOf('\n')));
                        files.add(new File(uri));
                    }
                    
                    // Note: On Windows, Java 1.4.2 can't recognize a Unicode file name
                    // (Bug ID 4896217). Fixed in Java 1.5.

                }
            } catch (Exception e) {
                e.printStackTrace();
                dtde.rejectDrop();
            }
        }
        
        if (!files.isEmpty()){
        	if (files.size()==1)
        		holder.acceptFile(files.getFirst(),myIndex);
        	else{
        		File[] fileArray = new File[files.size()];
        		files.toArray(fileArray);
        		holder.acceptFiles(fileArray,myIndex);
        	}
            files = null;
        	dtde.dropComplete(true);
        }
        
        else dtde.dropComplete(false);
    }
}
