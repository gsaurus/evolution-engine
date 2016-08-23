/*
 * mainGui.java
 *
 * Created on 19 October 2008, 16:54
 */

package gui;

import file.CorruptionException;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import frames.Frame;
import frames.FramesCollection;
import frames.IFramesTaker;
import frames.FramesCollection;
import characters.Character;

import file.ACollection;
import game.GameInvariants;
import java.awt.dnd.DropTarget;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import file.IFilesAccepter;
import image.ImageUtils;
import image.TransparentMaker;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeListener;
import pallet.Pallet;
import util.IColorTaker;
import util.ICoordinatesTaker;
import util.UBTriple;
import weapons.Weapon;
import weapons.WeaponsCollection;

/**
 *
 * @author  Gil Costa
 */
// TODO: SAVE CHANGES
public class MainGui extends javax.swing.JFrame
    implements IFilesAccepter, IFramesTaker, IColorTaker, ICoordinatesTaker, ChangeListener
{
    /** default serial ID*/
	private static final long serialVersionUID = 1L;
    
    
    protected static final String ABOUT =" Pallet Editor v";
    protected static final String AUTHOR = "\n\n" +
                "     Autor: Gil Costa\n" +
                "     Evolution © 2008-2009";
    
    protected static final String ERROR_MESSAGE = "Woops";
    protected static final String CANT_COMPUTE_PALLET = "Unsuitable Image";
    protected static final String CANT_OPEN_IMAGE = "Unable to open image";
    protected static final String CANT_OPEN_PALLET = "Unable to open pallet";
    protected static final String CANT_OPEN_CHARACTER = "Unable to open character";
    protected static final String CANT_OPEN_COLLECTION = "Unable to open collection";
    protected static final String CANT_SAVE_FILE = "Unable to save pallet";
    
    
    protected static final String SAVE_BEFORE = "Weapons set had changed\nSave changes?";
    
    
    protected static final int PALLET_INDEX = 0;
    protected static final int COLLECTION_INDEX = 1;
    
    protected static final int FIRST_PALLET = 1;
    

	protected FramePanel framePanel;
    
    protected CollectionPanel collectionPanel;
	
	/** the pallet pallet */
	protected Pallet pallet;
    protected Map.Entry<Color,Color> currentColor;
    
    protected JFileChooser palletChooser;
    protected JFileChooser collectionChooser;
    protected JFileChooser imageChooser;
    
    protected boolean hadChanged;
	
    
    
    public void locate(){
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, 
		size.height/2 - getHeight()/2);
    }
    
	/** Creates new form mainGui */
    public MainGui() {
        initComponents();
        initAdvancedComponents();

        initImageChooser();
        initPalletChooser();
        initCollectionChooser();
        
        initDropTarget();
        locate();
        
        currentColor = new  HashMap.SimpleEntry<Color,Color>(Color.BLACK,Color.BLACK);
        pallet = new Pallet();
        takeFrame(null);
        requestFocus();
    }
    
    
    
    protected void initAdvancedComponents(){
    	collectionPanel = new CollectionPanel(bottomScroll, this);
        framePanel = new FramePanel(frameScroll, this, this);
        
        bottomScroll.setViewportView(collectionPanel);
        bottomScroll.setWheelScrollingEnabled(false);

        frameScroll.setViewportView(framePanel);
        frameScroll.setWheelScrollingEnabled(false);
        
        framePanel.pickColor(true);
        framePanel.showCM(false);
        
        colorChooser.getSelectionModel().addChangeListener(this);
        AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
        colorChooser.removeChooserPanel(oldPanels[0]);
        colorChooser.addChooserPanel(oldPanels[0]);
        colorChooser.setPreviewPanel(new JPanel());
        
        saveMenu.setEnabled(false);
        saveAsMenu.setEnabled(false);
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
        imageChooser.setMultiSelectionEnabled(false);
    }
    
    
    
    protected void initPalletChooser(){
        palletChooser = new JFileChooser();
        palletChooser.setDialogTitle("Choose a pallet");
        palletChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.PALLETS_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Pallet", GameInvariants.PALLET_EXTENSION);
        // add them
        palletChooser.addChoosableFileFilter(fs);
        palletChooser.setFileFilter(fs);
        palletChooser.setMultiSelectionEnabled(false);
    }
 

    protected void initCollectionChooser(){
        collectionChooser = new JFileChooser();
        collectionChooser.setDialogTitle("Choose an Image Collection");
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
    	new DropTarget(framePanel, new FileDropTargetListener(this,PALLET_INDEX));
        new DropTarget(collectionPanel, new FileDropTargetListener(this,COLLECTION_INDEX));
    }
     
    
    
    protected void openPallet(){
        if (hadChanged) close();
        int returnVal = palletChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = palletChooser.getSelectedFile();
            if (file != null)
                acceptPallet(file);
        }
    }
    
        
        
        
    protected void openCollection(){
        int returnVal = collectionChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = collectionChooser.getSelectedFile();
            if (file != null){
                acceptCollection(file);
            }   
        }
    }
    
    protected BufferedImage openImage(){
        int returnVal = imageChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = imageChooser.getSelectedFile();
            if (file != null){
                try{
                    return ImageIO.read(file);
                }catch(IOException e){
                    JOptionPane.showMessageDialog(null, CANT_OPEN_IMAGE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
                }
            }   
        }
        return null;
    }
    
    
    
    protected void acceptPallet(File file){
        if (hadChanged) close();
        Pallet pll = new Pallet();
        try {
            pll.load(file);
        } catch (IOException e) {
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN_PALLET,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        pallet = pll;
        takeColor(new Color(0));
        String collName = ACollection.getName(file.getName());
        collName = collName.substring(0,collName.lastIndexOf('_'));
        collName += "." + GameInvariants.FRAMES_EXTENSION;
        acceptCollection(new File(GameInvariants.EDITORS_WORKING_DIR+GameInvariants.ANIMATIONS_DIR +"\\" + collName));
        saveMenu.setEnabled(true);
        saveAsMenu.setEnabled(true);
    }
    
   
    
    
    protected void acceptCollection(File file){
        FramesCollection frames;
        frames = new FramesCollection();
        try { frames.load(file);
        } catch (IOException e) {
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN_COLLECTION,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        collectionPanel.setCollection(frames);
        collectionPanel.repaint();
        //Color c = pallet.colors.get(currentColor.getKey());
        //if (c!=null) refreshMiniPanel(c);
        //else this.setFirstColor();
        setFirstColor();
    }
    
    
    @Override
	public void acceptFile(File file, int index) {
        switch(index){
            case PALLET_INDEX:
                acceptPallet(file);
                break;
            case COLLECTION_INDEX:
                acceptCollection(file);
                if (collectionPanel.getCollection()!=null && !collectionPanel.getCollection().isEmpty()){
                    resetPallet();
                }
                break;
        }
	}

	@Override
	public void acceptFiles(File[] files, int index) {
        if (files!=null && files.length<=0) return;
		acceptFile(files[0],index);
	}
    
    
    
    protected File inducePalletFile(){
        final String dirName = GameInvariants.EDITORS_WORKING_DIR + GameInvariants.PALLETS_DIR;
        File dir = new File(dirName);
        String tmp = collectionPanel.getCollection().getFileName();
        final String prefix = tmp.substring(0, tmp.lastIndexOf("."));
        FilenameFilter filter = new FilenameFilter() { 
            public boolean accept(File b, String name) { 
                return name.startsWith(prefix) &&
                       name.endsWith("." + GameInvariants.PALLET_EXTENSION); 
            } 
        }; 
        File[] files = dir.listFiles(filter);
        String fileName = dirName + "\\" + prefix + "_";
        if (files == null || files.length == 0) fileName+=FIRST_PALLET+"";
        else fileName += (files.length+FIRST_PALLET) + "";
        fileName += "." + GameInvariants.PALLET_EXTENSION;
        return new File(fileName);
    }
    
    
    protected void savePalletAs(){
        if (pallet.getFile() == null)
            palletChooser.setSelectedFile(inducePalletFile());
       int returnVal = palletChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = palletChooser.getSelectedFile();
            if (file != null){
                pallet.setFile(file);
                savePallet();
            }
                
        }
    }
    
    
    
    protected void savePallet(){
        //takeFrame(collectionPanel.getSelected());
        if (pallet.getFile() == null){
            savePalletAs();
        }else{
            try { pallet.save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, CANT_SAVE_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    
	
	
	@Override
	public void takeFrame(Frame frame) {
        updateColor();
        if (collectionPanel.getCollection() != null)
            frameLabel.setText("Frame " + (collectionPanel.getSelectedNum()+1) + " of " + collectionPanel.getCollection().size());
        else frameLabel.setText("-");
        applyPallet();
	}
    
    @Override
    public void takeFrames(Frame[] frames){
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
	
    
    
    protected void close(){
        int res = JOptionPane.showConfirmDialog(null,SAVE_BEFORE,"save?",JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
            savePallet();
        hadChanged = false;
    }


    
    protected void applyPallet(){
        Frame original = collectionPanel.getSelected();
        if (original == null) return;
        framePanel.takeFrame(new Frame(pallet.applyToImage(original.getImage()),GameInvariants.FRAMES_VERSION));
    }
    
    
    protected void setFirstColor(){
        currentColor.setValue(currentColor.getKey());
        Frame f = collectionPanel.getSelected();
        Color color = null;
        BufferedImage img = null;
        if (f != null)
            img = f.getImage();
        if (img!=null){
            for(int x=0; x<img.getWidth(); x++){
                for(int y=0; y<img.getHeight(); y++){
                    if (img.getRGB(x, y)!=0){
                        color = new Color(img.getRGB(x, y));
                        break;
                    }
                }
            }
        }
        //refreshMiniPanel(color);
        takeColor(color);
        applyPallet();
    }
    
    
    public void updateColor(){
        currentColor.setValue(miniPanel.getBackground());
        // if no changes, nothing to do
        Color before = pallet.colors.get(currentColor.getKey());
        if (currentColor.getValue().equals(before) || (before == null && currentColor.getValue().equals(currentColor.getKey())))
            return;
        // else make changes
        if (!currentColor.getKey().equals(currentColor.getValue())){
            pallet.colors.put(currentColor.getKey(), currentColor.getValue());
            
        }else{
            pallet.colors.remove(currentColor.getKey());
        }
        // apply and show changes
        applyPallet();
    }
    
    public void takeColor(Color c) {
        if (c == null) return;
        Color pc = pallet.colors.get(c);
        if (pc == null) pc = new Color(c.getRGB());
        currentColor = new HashMap.SimpleEntry<Color,Color>(c,pc);
        
        refreshMiniPanel(pc);
    }
    
    public void setColor(Color c) {
        updateColor();
        takeColor(c);
        
    }
    
    public void takeCoordinates(int x, int y, int data) {
        BufferedImage img = collectionPanel.getSelected().getImage();
        int rgb = img.getRGB(x, y);
        if ((rgb&0xff000000) == 0) return;
        setColor(new Color(rgb));
    }
    
    protected void refreshMiniPanel(Color c){
        colorChooser.setColor(c);
        miniPanel.setBackground(c);
        miniPanel.repaint();
    }
    
    
    public boolean hasGeneralFocus(){
        return false;
    }
    
    
    public void stateChanged(ChangeEvent e) {
        Color c = colorChooser.getColor();
        refreshMiniPanel(c);
        updateColor();
    }
    
    
    public boolean computePallet(BufferedImage original, BufferedImage changes){
        // make transparent and crop the given image
        changes = ImageUtils.copyImage(changes);
        Color c = ImageUtils.borderPredominance(changes);
        TransparentMaker f = new TransparentMaker(c);
        f.applyTo(changes);
        changes = ImageUtils.crop(changes).second;
        
        // now, prepare the computation
        int width = Math.min(original.getWidth(), changes.getWidth());
        int deltaY = changes.getHeight()-original.getHeight();
        int lower = Math.max(0, deltaY);
        
        // finally compute
        pallet.colors.clear();
        for(int y=changes.getHeight()-1; y>=lower; y--){
            for(int x = 0; x<width; x++){
                int rgb1 = original.getRGB(x, y-deltaY);
                int rgb2 = changes.getRGB(x, y);
                if ((rgb1&0xff000000) == 0 || (rgb2&0xff000000) == 0) continue;
                Color c1 = new Color(rgb1);
                Color c2 = new Color(rgb2);
                if (c1.equals(c2) || pallet.colors.containsKey(c1)) continue;
                pallet.colors.put(c1, c2);
            }
        }
        applyPallet();
        return !pallet.colors.isEmpty();
    }
    
    
    protected void resetPallet(){
        pallet.setFile(null);
        pallet.colors.clear();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bottomScroll = new gui.Scroller();
        frameScroll = new gui.Scroller();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        miniPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        frameLabel = new javax.swing.JLabel();
        colorChooser = new javax.swing.JColorChooser();
        jLabel1 = new javax.swing.JLabel();
        menuBar15 = new javax.swing.JMenuBar();
        fileMenu15 = new javax.swing.JMenu();
        newMenuItem4 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        openMenuItem15 = new javax.swing.JMenuItem();
        saveMenuItem15 = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JSeparator();
        exitMenuItem15 = new javax.swing.JMenuItem();
        helpMenu17 = new javax.swing.JMenu();
        aboutMenuItem16 = new javax.swing.JMenuItem();
        menuBar16 = new javax.swing.JMenuBar();
        fileMenu16 = new javax.swing.JMenu();
        newMenuItem5 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        openMenuItem16 = new javax.swing.JMenuItem();
        saveMenu = new javax.swing.JMenuItem();
        saveAsMenu = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JSeparator();
        exitMenuItem16 = new javax.swing.JMenuItem();
        helpMenu18 = new javax.swing.JMenu();
        aboutMenuItem17 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pallet Editor");
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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton2.setText("restore");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        miniPanel.setBackground(new java.awt.Color(0, 0, 0));
        miniPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                miniPanelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout miniPanelLayout = new javax.swing.GroupLayout(miniPanel);
        miniPanel.setLayout(miniPanelLayout);
        miniPanelLayout.setHorizontalGroup(
            miniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        miniPanelLayout.setVerticalGroup(
            miniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(miniPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(miniPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jButton3.setText("auto detect");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        frameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        frameLabel.setText("Frame 0 of 0");

        jLabel1.setText("Auto detect from an image similar to the frame");

        fileMenu15.setText("File");

        newMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem4.setText("New from Images");
        newMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItem2newMenuItemActionPerformed(evt);
            }
        });
        fileMenu15.add(newMenuItem4);
        fileMenu15.add(jSeparator2);

        openMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem15.setText("Open");
        openMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu15.add(openMenuItem15);

        saveMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem15.setText("Save");
        saveMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu15.add(saveMenuItem15);
        fileMenu15.add(jSeparator16);

        exitMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem15.setText("Exit");
        exitMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu15.add(exitMenuItem15);

        menuBar15.add(fileMenu15);

        helpMenu17.setText("Help");

        aboutMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenuItem16.setText("About");
        aboutMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItem14ActionPerformed(evt);
            }
        });
        helpMenu17.add(aboutMenuItem16);

        menuBar15.add(helpMenu17);

        fileMenu16.setText("File");

        newMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem5.setText("New from Images");
        newMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItem2newMenuItemActionPerformed(evt);
            }
        });
        fileMenu16.add(newMenuItem5);
        fileMenu16.add(jSeparator3);

        openMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem16.setText("Open");
        openMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu16.add(openMenuItem16);

        saveMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenu.setText("Save");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu16.add(saveMenu);

        saveAsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenu.setText("Save As");
        saveAsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuActionPerformed(evt);
            }
        });
        fileMenu16.add(saveAsMenu);
        fileMenu16.add(jSeparator17);

        exitMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem16.setText("Exit");
        exitMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu16.add(exitMenuItem16);

        menuBar16.add(fileMenu16);

        helpMenu18.setText("Help");

        aboutMenuItem17.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenuItem17.setText("About");
        aboutMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItem14ActionPerformed(evt);
            }
        });
        helpMenu18.add(aboutMenuItem17);

        menuBar16.add(helpMenu18);

        setJMenuBar(menuBar16);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(frameLabel)
                        .addGap(108, 108, 108)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bottomScroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(frameScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(colorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jButton3)))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(frameScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(colorChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 283, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (hadChanged) close();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
         switch(evt.getKeyCode()){
            case KeyEvent.VK_LEFT: collectionPanel.selectPrevious(); break;
            case KeyEvent.VK_RIGHT: collectionPanel.selectNext(); break;
            case KeyEvent.VK_UP: collectionPanel.selectPageUp();break;
            case KeyEvent.VK_DOWN: collectionPanel.selectPageDown(); break;
        }
         requestFocus();
    }//GEN-LAST:event_formKeyPressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (!hasGeneralFocus())
            requestFocus();
    }//GEN-LAST:event_formMouseMoved

    private void openMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItem13ActionPerformed
        openPallet();
    }//GEN-LAST:event_openMenuItem13ActionPerformed

    private void saveMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItem13ActionPerformed
        savePallet();
    }//GEN-LAST:event_saveMenuItem13ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (hadChanged) close();
    }//GEN-LAST:event_formWindowClosing

    private void aboutMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItem14ActionPerformed
        JOptionPane.showMessageDialog(null, ABOUT + GameInvariants.PALLET_VERSION + AUTHOR,"About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItem14ActionPerformed

    private void newMenuItem2newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItem2newMenuItemActionPerformed
        if (hadChanged) close();
        collectionPanel.setCollection(null);
        framePanel.takeFrame(null);
        pallet = new Pallet();
        saveMenu.setEnabled(false);
        saveAsMenu.setEnabled(false);
        openCollection();
        if (collectionPanel.getCollection()!=null && !collectionPanel.getCollection().isEmpty()){
            resetPallet();
            saveMenu.setEnabled(true);
            saveAsMenu.setEnabled(true);
        }
        framePanel.repaint();
        collectionPanel.repaint();
    }//GEN-LAST:event_newMenuItem2newMenuItemActionPerformed

    private void miniPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_miniPanelMousePressed

}//GEN-LAST:event_miniPanelMousePressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        refreshMiniPanel(currentColor.getKey());
        updateColor();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (collectionPanel.getSelected() == null) return;
        BufferedImage img = openImage();
        if (img == null) return;
        boolean result = computePallet(collectionPanel.getSelected().getImage(), img);
        if (!result)
            JOptionPane.showMessageDialog(null, CANT_COMPUTE_PALLET,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
        else{
            takeColor(new Color(0));
            //Color c = pallet.colors.get(currentColor.getKey());
            //if (c!=null) refreshMiniPanel(c);
            //else this.setFirstColor();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void saveAsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuActionPerformed
        savePalletAs();
}//GEN-LAST:event_saveAsMenuActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGui().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem16;
    private javax.swing.JMenuItem aboutMenuItem17;
    private gui.Scroller bottomScroll;
    private javax.swing.JColorChooser colorChooser;
    private javax.swing.JMenuItem exitMenuItem15;
    private javax.swing.JMenuItem exitMenuItem16;
    private javax.swing.JMenu fileMenu15;
    private javax.swing.JMenu fileMenu16;
    private javax.swing.JLabel frameLabel;
    private gui.Scroller frameScroll;
    private javax.swing.JMenu helpMenu17;
    private javax.swing.JMenu helpMenu18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuBar menuBar15;
    private javax.swing.JMenuBar menuBar16;
    private javax.swing.JPanel miniPanel;
    private javax.swing.JMenuItem newMenuItem4;
    private javax.swing.JMenuItem newMenuItem5;
    private javax.swing.JMenuItem openMenuItem15;
    private javax.swing.JMenuItem openMenuItem16;
    private javax.swing.JMenuItem saveAsMenu;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JMenuItem saveMenuItem15;
    // End of variables declaration//GEN-END:variables




    
}
