/*
 * mainGui.java
 *
 * Created on 19 October 2008, 16:54
 */

package gui;

import file.CorruptionException;
import image.ImageUtils;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import frames.Frame;
import frames.FramesCollection;
import frames.IFramesTaker;
import frames.FramesCollection;

import game.GameInvariants;
import java.awt.dnd.DropTarget;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import util.ICoordinatesTaker;
import file.IFilesAccepter;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author  Gil Costa
 */
public class mainGui extends javax.swing.JFrame
    implements IFilesAccepter, IFramesTaker,
                ICoordinatesTaker
{
    /** default serial ID*/
	private static final long serialVersionUID = 1L;
    
    
    protected static final String ABOUT =" Image Collections Editor v";
    protected static final String AUTHOR = "\n\n" +
                "     Autor: Gil Costa\n" +
                "     Evolution © 2008-2009";
    
    protected static final String ERROR_MESSAGE = "Woops";
    protected static final String CANT_OPEN_IMAGE = "Unable to open image";
    protected static final String CANT_OPEN_IMAGES = "Unable to open images";
    protected static final String CANT_OPEN_FILE = "Unable to open collection";
    protected static final String CANT_SAVE_FILE = "Unable to save collection";
    
    
    protected static final String SAVE_BEFORE = "Collection had changed\nSave changes?";
    
  
    
    protected static final int IMAGES_INDEX = 0;
    protected static final int COLLECTIONS_INDEX = 1;
    

	/** frame panel */
	protected gui.FramePanel framePanel;
    /** collection of collection panel */
    protected gui.CollectionPanel collectionPanel;
	
	/** the collection collection */
	protected FramesCollection collection;
    
    /** file chooser for images opening */
    protected JFileChooser imageChooser;
    
    /** file chooser for internal images opening */
    protected JFileChooser collectionChooser;
    
    protected boolean hadChanged;
	
    
    public void locate(){
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, 
		size.height/2 - getHeight()/2);
    }
    
    
	/** Creates new form mainGui */
    public mainGui() {
        initComponents();
        initAdvancedComponents();
        initImageChooser();
        initCollectionChooser();
        initDropTarget();
        locate();
        
        collection = new FramesCollection();
        hotSpotPanel.setFramePanel(framePanel);
        takeFrame(null);
        takeFrames(null);
        
        requestFocus();
    }
    
    
    
    protected void initAdvancedComponents(){
    	collectionPanel = new gui.CollectionPanel(bottomScroll, this);
        framePanel = new gui.FramePanel(topScroll, this, null);
        
        collectionPanel.setForeground(new java.awt.Color(255, 255, 255));
        bottomScroll.setViewportView(collectionPanel);
        bottomScroll.setWheelScrollingEnabled(false);

        framePanel.setForeground(new java.awt.Color(255, 255, 255));
        topScroll.setViewportView(framePanel);
        topScroll.setWheelScrollingEnabled(false);
    }
    
    
    
    
    protected void initImageChooser(){
        imageChooser = new JFileChooser();
        imageChooser.setDialogTitle("Open Image Files");
        // create filters
        javax.swing.filechooser.FileFilter[] fs = new javax.swing.filechooser.FileFilter[5];
        fs[0] = new FileNameExtensionFilter("all image types", "bmp","wbmp", "jpg", "jpeg", "png", "gif");
        fs[1] = new FileNameExtensionFilter("Bitmap", "bmp","wbmp");
        fs[2] = new FileNameExtensionFilter("JPEG", "jpg","jpeg");
        fs[3] = new FileNameExtensionFilter("PNG", "png");
        fs[4] = new FileNameExtensionFilter("GIF", "gif","gif");
        // add them
        for(int i=0; i<5; i++) imageChooser.addChoosableFileFilter(fs[i]);
        imageChooser.setFileFilter(fs[0]);
        imageChooser.setMultiSelectionEnabled(true);
    }


    protected void initCollectionChooser(){
        collectionChooser = new JFileChooser();
        collectionChooser.setDialogTitle("Choose Image Collection");
        collectionChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Image Collection", GameInvariants.FRAMES_EXTENSION);
        // add them
        collectionChooser.addChoosableFileFilter(fs);
        collectionChooser.setFileFilter(fs);
        collectionChooser.setMultiSelectionEnabled(false);
    }
        
        
    protected void initDropTarget(){
    	new DropTarget(framePanel, new FileDropTargetListener(this,IMAGES_INDEX));
        new DropTarget(collectionPanel, new FileDropTargetListener(this,COLLECTIONS_INDEX));
    }
        
        
    protected void openImages(){
        int returnVal = imageChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = imageChooser.getSelectedFiles();
            if (files != null){
                if (files.length==1) acceptImage(files[0]);
                else acceptImages(files);
            }
        }
    }
        
        
    
    
        
        
        
        
    protected void openCollection(){
        if (hadChanged) close();
        int returnVal = collectionChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = collectionChooser.getSelectedFile();
            if (file != null){
                acceptCollection(file);
            }   
        }
    }

    
    
    
    
    
    
    protected void acceptImage(File file){
        BufferedImage img;
        try {
            img = ImageIO.read(file);
            if (img == null) throw new IOException();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, CANT_OPEN_IMAGE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.setEnabled(false);
		new OpenImgs(this,ImageUtils.copyImage(img)).setVisible(true);
    }
    
    
    public void acceptImages(File[] files) {
		LinkedList<BufferedImage> imgs = new LinkedList<BufferedImage>();
        for(File file:files){
            BufferedImage tmpImg = null;
            try { tmpImg = ImageIO.read(file);
            } catch (IOException ex) {}
            if (tmpImg!=null)
                imgs.add(tmpImg);
        }
        if (!imgs.isEmpty()){
            BufferedImage[] imgsArray = new BufferedImage[imgs.size()];
            imgs.toArray(imgsArray);
            imgs = null;
            for(int i=0 ; i<imgsArray.length ; i++)
                imgsArray[i] = ImageUtils.copyImage(imgsArray[i]);
            this.setEnabled(false);
            new OpenImgs(this,imgsArray).setVisible(true);
        }else JOptionPane.showMessageDialog(null, CANT_OPEN_IMAGES,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
	}
    
    
    protected void acceptCollection(File file){
        if (hadChanged) close();
        collection = new FramesCollection();
        try { collection.load(file);
        } catch (IOException e) {
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        collectionPanel.setCollection(collection);
        hotSpotPanel.checkCM(collection.hasCMOn());
    }
    
    
    @Override
	public void acceptFile(File file, int index) {
        switch(index){
            case IMAGES_INDEX:
                acceptImage(file);
                break;
            case COLLECTIONS_INDEX:
                acceptCollection(file);
                break;
        }
	}

	@Override
	public void acceptFiles(File[] files, int index) {
		switch(index){
            case IMAGES_INDEX:
                acceptImages(files);
                break;
            case COLLECTIONS_INDEX:
                if (files!=null && files.length>=1)
                    acceptCollection(files[0]);
                break;
        }
	}
    
    
    
    protected void saveCollectionAs(){
        int returnVal = collectionChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = collectionChooser.getSelectedFile();
            if (file != null){
                collection.setFile(file);
                saveCollection();
            }
                
        }
    }
    
    protected void saveCollection(){
        if (collection.getFile() == null)
            saveCollectionAs();
        else{
            collection.setCMOn(hotSpotPanel.isCMChecked());
            try { collection.save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, CANT_SAVE_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        hadChanged = false;
    }
    
	
	
	@Override
	public void takeFrame(Frame frame) {
        frameLabel.setText("Frame " + collectionPanel.getSelectedNum() + " of " + collection.size());
		framePanel.setFrame(frame);
        if (frame!=null){
            hotSpotPanel.takeCoordinates(-1,frame.getCM().getX(), frame.getCM().getY());
            hotSpotPanel.setAllEnabled(true);
        }else hotSpotPanel.setAllEnabled(false);
//		framePanel.resetScale();
        requestFocus();
		repaint();
	}
    
    @Override
    public void takeFrames(Frame[] frames){
        this.setEnabled(true);
        this.requestFocus();
        if (frames!=null && frames.length!=0)
            hadChanged = true;
        
        collection.add(frames);
        if (collection.isEmpty()){
            framePanel.setFrame(null);
            collectionPanel.setCollection(null);
        }else{
            collectionPanel.setCollection(collection);
            collectionPanel.select(0);
        }
        requestFocus();
        repaint();
    }
	
	@Override
	public void takeCoordinates(int index, int x, int y){
        //if (x!=framePanel.getFrame().getCM().getX() || y!=framePanel.getFrame().getCM().getY())
            hadChanged = true;
		hotSpotPanel.takeCoordinates(index, x, y);
	}
    
    
    protected void close(){
        int res = JOptionPane.showConfirmDialog(null,SAVE_BEFORE,"save?",JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
            saveCollection();
        hadChanged = false;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        butImgs = new javax.swing.JButton();
        frameLabel = new javax.swing.JLabel();
        topScroll = new gui.Scroller();
        previousBut = new javax.swing.JButton();
        bottomScroll = new gui.Scroller();
        hotSpotPanel = new gui.HotSpotPanel();
        menuBar13 = new javax.swing.JMenuBar();
        fileMenu13 = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem13 = new javax.swing.JMenuItem();
        saveMenuItem13 = new javax.swing.JMenuItem();
        saveAsMenuItem13 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JSeparator();
        exitMenuItem13 = new javax.swing.JMenuItem();
        helpMenu13 = new javax.swing.JMenu();
        aboutMenuItem13 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Images Loader");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        butImgs.setText("Add Images");
        butImgs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butImgsActionPerformed(evt);
            }
        });
        butImgs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                butImgsFocusGained(evt);
            }
        });

        frameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        frameLabel.setText("Frame 0 of 0");

        previousBut.setText("previous");
        previousBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButActionPerformed(evt);
            }
        });
        previousBut.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                previousButFocusGained(evt);
            }
        });
        topScroll.setViewportView(previousBut);

        hotSpotPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        fileMenu13.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu13.add(newMenuItem);

        openMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem13.setText("Open");
        openMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu13.add(openMenuItem13);

        saveMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem13.setText("Save");
        saveMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu13.add(saveMenuItem13);

        saveAsMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem13.setText("Save As ...");
        saveAsMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu13.add(saveAsMenuItem13);
        fileMenu13.add(jSeparator14);

        exitMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem13.setText("Exit");
        exitMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu13.add(exitMenuItem13);

        menuBar13.add(fileMenu13);

        helpMenu13.setText("Help");

        aboutMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenuItem13.setText("About");
        aboutMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItem13ActionPerformed(evt);
            }
        });
        helpMenu13.add(aboutMenuItem13);

        menuBar13.add(helpMenu13);

        setJMenuBar(menuBar13);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(butImgs))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(frameLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(hotSpotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(butImgs, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hotSpotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bottomScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (hadChanged) close();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void butImgsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butImgsActionPerformed
        openImages();
}//GEN-LAST:event_butImgsActionPerformed

    private void previousButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButActionPerformed
        collectionPanel.selectPrevious();
        requestFocus();
}//GEN-LAST:event_previousButActionPerformed

    private void butImgsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_butImgsFocusGained

    }//GEN-LAST:event_butImgsFocusGained

    private void previousButFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_previousButFocusGained

    }//GEN-LAST:event_previousButFocusGained

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
         switch(evt.getKeyCode()){
            case KeyEvent.VK_LEFT: collectionPanel.selectPrevious(); break;
            case KeyEvent.VK_RIGHT: collectionPanel.selectNext(); break;
            case KeyEvent.VK_UP: collectionPanel.selectPageUp();break;
            case KeyEvent.VK_DOWN: collectionPanel.selectPageDown(); break;
            case KeyEvent.VK_DELETE: collectionPanel.deleteSelected(); break;
        }
         requestFocus();
    }//GEN-LAST:event_formKeyPressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (!hotSpotPanel.hasFocus())
            requestFocus();
    }//GEN-LAST:event_formMouseMoved

    private void openMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItem13ActionPerformed
        openCollection();
    }//GEN-LAST:event_openMenuItem13ActionPerformed

    private void saveAsMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItem13ActionPerformed
        saveCollectionAs();
    }//GEN-LAST:event_saveAsMenuItem13ActionPerformed

    private void saveMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItem13ActionPerformed
        saveCollection();
    }//GEN-LAST:event_saveMenuItem13ActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        if (hadChanged) close();
        collection = new FramesCollection();
        framePanel.setFrame(null);
        framePanel.repaint();
        collectionPanel.setCollection(collection);
        collectionPanel.repaint();
}//GEN-LAST:event_newMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (hadChanged) close();
    }//GEN-LAST:event_formWindowClosing

    private void aboutMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItem13ActionPerformed
        JOptionPane.showMessageDialog(null, ABOUT + GameInvariants.FRAMES_VERSION + AUTHOR,"About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItem13ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainGui().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem13;
    private gui.Scroller bottomScroll;
    private javax.swing.JButton butImgs;
    private javax.swing.JMenuItem exitMenuItem13;
    private javax.swing.JMenu fileMenu13;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JMenu helpMenu13;
    private gui.HotSpotPanel hotSpotPanel;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JMenuBar menuBar13;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem13;
    private javax.swing.JButton previousBut;
    private javax.swing.JMenuItem saveAsMenuItem13;
    private javax.swing.JMenuItem saveMenuItem13;
    private gui.Scroller topScroll;
    // End of variables declaration//GEN-END:variables

    
}
