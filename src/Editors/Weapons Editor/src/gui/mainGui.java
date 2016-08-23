/*
 * mainGui.java
 *
 * Created on 19 October 2008, 16:54
 */

package gui;

import file.ACollection;
import file.CorruptionException;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import frames.Frame;
import frames.FramesCollection;
import frames.IFramesTaker;
import frames.FramesCollection;

import game.GameInvariants;
import java.awt.dnd.DropTarget;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import file.IFilesAccepter;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.DefaultListModel;
import weapons.Weapon;
import weapons.WeaponsCollection;

/**
 *
 * @author  Gil Costa
 */
// TODO: SAVE CHANGES
public class mainGui extends javax.swing.JFrame
    implements IFilesAccepter, IFramesTaker
{
    /** default serial ID*/
	private static final long serialVersionUID = 1L;
    
    protected static final String ABOUT =" Weapons Editor v";
    protected static final String AUTHOR = "\n\n" +
                "     Autor: Gil Costa\n" +
                "     Evolution © 2008-2009";
    
    protected static final String ERROR_MESSAGE = "Woops";
    protected static final String CANT_OPEN_WEAPONS = "Unable to open weapons";
    protected static final String CANT_OPEN_COLLECTION = "Unable to open collection";
    protected static final String CANT_SAVE_FILE = "Unable to save weapons";
    
    public static final String ALREADY_EXISTS = "Already exists in this Weapons Set";
    
    protected static final String SAVE_BEFORE = "Weapons set had changed\nSave changes?";
    
    
    protected static final int WEAPONS_INDEX = 0;
    protected static final int COLLECTIONS_INDEX = 1;
    

	/** frame panel */
	protected gui.ActionFramePanel actionFramePanel;
    /** weapons of weapons panel */
    protected gui.CollectionPanel collectionPanel;
	
	/** the weapons weapons */
	protected WeaponsCollection weapons;
    
    /** file chooser for images opening */
    protected JFileChooser weaponsChooser;
    
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
        initGoodiesChooser();
        initCollectionChooser();
        initDropTarget();
        locate();
        
        weapons = new WeaponsCollection();
        takeFrame(null);
        requestFocus();
    }
    
    
    
    protected void initAdvancedComponents(){
    	collectionPanel = new gui.CollectionPanel(bottomScroll, this);
        actionFramePanel = new gui.ActionFramePanel(topScroll, weaponPanel);
        
        collectionPanel.setForeground(new java.awt.Color(255, 255, 255));
        bottomScroll.setViewportView(collectionPanel);
        bottomScroll.setWheelScrollingEnabled(false);

        actionFramePanel.setForeground(new java.awt.Color(255, 255, 255));
        topScroll.setViewportView(actionFramePanel);
        topScroll.setWheelScrollingEnabled(false);
        actionFramePanel.showCM(false);
        saveMenuItem14.setEnabled(false);
        saveAsMenuItem14.setEnabled(false);
        
        weaponPanel.setTaker(actionFramePanel);
    }
    
    
    
    
    protected void initGoodiesChooser(){
        weaponsChooser = new JFileChooser();
        weaponsChooser.setDialogTitle("Choose a Weapons Set");
        weaponsChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.WEAPONS_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Weapons Set", GameInvariants.WEAPONS_EXTENSION);
        // add them
        weaponsChooser.addChoosableFileFilter(fs);
        weaponsChooser.setFileFilter(fs);
        weaponsChooser.setMultiSelectionEnabled(false);
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
        collectionChooser.setMultiSelectionEnabled(true);
    }
        
        
    protected void initDropTarget(){
    	new DropTarget(actionFramePanel, new FileDropTargetListener(this,WEAPONS_INDEX));
        new DropTarget(collectionPanel, new FileDropTargetListener(this,COLLECTIONS_INDEX));
    }
        
        
    protected void openWeapons(){
        int returnVal = weaponsChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = weaponsChooser.getSelectedFile();
            if (file != null)
                acceptWeapons(file);
        }
    }
        
        
    
    
        
        
        
        
    protected String[] openCollection(){
        int returnVal = collectionChooser.showOpenDialog(this);
        String[] res = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = collectionChooser.getSelectedFiles();
            if (files != null){
                res = new String[files.length];
                for(int i=0; i<files.length; i++){
                    File file = files[i];
                    res[i] = ACollection.getName(file.getName());
                }   
            }
        }
        return res;
    }

    
    
    
    
    
    
    protected void acceptWeapons(File file){
        if (hadChanged) close();
        try {
            weapons.load(file);
        } catch (IOException e) {
            saveMenuItem14.setEnabled(false);
            saveAsMenuItem14.setEnabled(false);
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN_WEAPONS,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        saveMenuItem14.setEnabled(true);
        saveAsMenuItem14.setEnabled(true);
        setFirstWeapon();
        initAnimList();
        initIconList();
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
        saveMenuItem14.setEnabled(true);
        saveAsMenuItem14.setEnabled(true);
        setFirstWeapon();
        collectionPanel.setCollection(frames);
        actionFramePanel.repaint();
        collectionPanel.repaint();
    }
    
    
    @Override
	public void acceptFile(File file, int index) {
        switch(index){
            case WEAPONS_INDEX:
                acceptWeapons(file);
                break;
            case COLLECTIONS_INDEX:
                acceptCollection(file);
                break;
        }
	}

	@Override
	public void acceptFiles(File[] files, int index) {
        if (files!=null && files.length<=0) return;
		switch(index){
            case WEAPONS_INDEX:
                acceptWeapons(files[0]);
                break;
            case COLLECTIONS_INDEX:
                acceptCollection(files[0]);
                break;
        }
	}
    
    
    
    protected void saveGoodiesAs(){
        int returnVal = weaponsChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = weaponsChooser.getSelectedFile();
            if (file != null){
                weapons.setFile(file);
                saveGoodies();
            }
                
        }
    }
    
    protected void saveGoodies(){
        takeFrame(collectionPanel.getSelected());
        if (weapons.getFile() == null)
            saveGoodiesAs();
        else{
            try { weapons.save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, CANT_SAVE_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    
	
	
	@Override
	public void takeFrame(Frame frame) {
        frameLabel.setText("Frame " + (collectionPanel.getSelectedNum()+1) + " of " + weapons.size());
		actionFramePanel.setFrame(frame);
        if (frame!=null){
            int fn = collectionPanel.getSelectedNum();
            Weapon wp = weapons.get(fn);
            weaponPanel.setWeapon(wp);
            if (wp!=null){
                weaponPanel.setAllEnabled(true);
                actionFramePanel.setActionPoints(wp.actionPoints);
            }else{
                weaponPanel.setAllEnabled(false);
                actionFramePanel.setActionPoints(null);
            }
        }else{
            weaponPanel.setAllEnabled(false);
            actionFramePanel.setActionPoints(null);
        }
        requestFocus();
		repaint();
	}
    
    @Override
    public void takeFrames(Frame[] frames){
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
	
    
    
    protected void close(){
        int res = JOptionPane.showConfirmDialog(null,SAVE_BEFORE,"save?",JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
            saveGoodies();
        hadChanged = false;
    }
    
    
    protected void setFirstWeapon(){
        weaponPanel.reset();
        weaponPanel.setWeapon(weapons.get(collectionPanel.getSelectedNum()));
    }
    
    
    
    
    
    protected void weaponsCtrl(){
        int minSize = Integer.MAX_VALUE;
        for(String s:weapons.frames){
            FramesCollection frames;
            frames = new FramesCollection();
            try { frames.load(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" +s));
            } catch (IOException e) { break; }
            if (frames.size() < minSize) minSize = frames.size();
        }
        if (minSize == Integer.MAX_VALUE) minSize = 0;
         for(int i=weapons.size(); i<minSize; i++)
            weapons.add(new Weapon(weapons.getVersion()));
        int dif = weapons.size()-minSize;
        for(int i=0; i<dif; i++)
            weapons.remove(weapons.size()-1);
    }
    
    
    
    public void initAnimList(){
        int index = animList.getSelectedIndex();
        animList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:weapons.frames)
            listModel.addElement("[" + i++ + "] " + s);
        animList.setModel(listModel);
        if (index>=0 && index<listModel.size()){
            animList.setSelectedIndex(index);
        } else animList.setSelectedIndex(listModel.size()-1);
    }
    
    
    
    public void initIconList(){
        int index = iconList.getSelectedIndex();
        iconList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:weapons.icons)
            listModel.addElement("[" + i++ + "] " + s);
        iconList.setModel(listModel);
        if (index>=0 && index<listModel.size()){
            iconList.setSelectedIndex(index);
        } else iconList.setSelectedIndex(listModel.size()-1);
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameLabel = new javax.swing.JLabel();
        topScroll = new gui.Scroller();
        bottomScroll = new gui.Scroller();
        jButton1 = new javax.swing.JButton();
        weaponPanel = new gui.WeaponPanel();
        deleteAnim = new javax.swing.JButton();
        addAnim = new javax.swing.JButton();
        down2But = new javax.swing.JButton();
        up2But = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        animList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        menuBar14 = new javax.swing.JMenuBar();
        fileMenu14 = new javax.swing.JMenu();
        newMenuItem1 = new javax.swing.JMenuItem();
        openMenuItem14 = new javax.swing.JMenuItem();
        saveMenuItem14 = new javax.swing.JMenuItem();
        saveAsMenuItem14 = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JSeparator();
        exitMenuItem14 = new javax.swing.JMenuItem();
        helpMenu16 = new javax.swing.JMenu();
        aboutMenuItem15 = new javax.swing.JMenuItem();
        deleteAnim1 = new javax.swing.JButton();
        addAnim1 = new javax.swing.JButton();
        down2But1 = new javax.swing.JButton();
        up2But1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        iconList = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        menuBar15 = new javax.swing.JMenuBar();
        fileMenu15 = new javax.swing.JMenu();
        newMenuItem2 = new javax.swing.JMenuItem();
        openMenuItem15 = new javax.swing.JMenuItem();
        saveMenuItem15 = new javax.swing.JMenuItem();
        saveAsMenuItem15 = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JSeparator();
        exitMenuItem15 = new javax.swing.JMenuItem();
        helpMenu17 = new javax.swing.JMenu();
        aboutMenuItem16 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Weapons Editor");
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

        frameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        frameLabel.setText("Frame 0 of 0");

        jButton1.setText("Set All to Default");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        weaponPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        deleteAnim.setText("delete");
        deleteAnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAnimActionPerformed(evt);
            }
        });

        addAnim.setText("add");
        addAnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAnimActionPerformed(evt);
            }
        });

        down2But.setText("down");
        down2But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                down2ButActionPerformed(evt);
            }
        });

        up2But.setText("up");
        up2But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                up2ButActionPerformed(evt);
            }
        });

        animList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        animList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                animListValueChanged(evt);
            }
        });
        animList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                animListPropertyChange(evt);
            }
        });
        animList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                animListKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(animList);

        jLabel2.setText("Animation Collections");

        fileMenu14.setText("File");

        newMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem1.setText("New");
        newMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu14.add(newMenuItem1);

        openMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem14.setText("Open");
        openMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu14.add(openMenuItem14);

        saveMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem14.setText("Save");
        saveMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu14.add(saveMenuItem14);

        saveAsMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem14.setText("Save As ...");
        saveAsMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu14.add(saveAsMenuItem14);
        fileMenu14.add(jSeparator15);

        exitMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem14.setText("Exit");
        exitMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu14.add(exitMenuItem14);

        menuBar14.add(fileMenu14);

        helpMenu16.setText("Help");

        aboutMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenuItem15.setText("About");
        aboutMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItem14ActionPerformed(evt);
            }
        });
        helpMenu16.add(aboutMenuItem15);

        menuBar14.add(helpMenu16);

        deleteAnim1.setText("delete");
        deleteAnim1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAnim1ActionPerformed(evt);
            }
        });

        addAnim1.setText("add");
        addAnim1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAnim1ActionPerformed(evt);
            }
        });

        down2But1.setText("down");
        down2But1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                down2But1ActionPerformed(evt);
            }
        });

        up2But1.setText("   up  ");
        up2But1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                up2But1ActionPerformed(evt);
            }
        });

        iconList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        iconList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                iconListValueChanged(evt);
            }
        });
        iconList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                iconListPropertyChange(evt);
            }
        });
        iconList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                iconListKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(iconList);

        jLabel3.setText("Icons");

        fileMenu15.setText("File");

        newMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem2.setText("New");
        newMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu15.add(newMenuItem2);

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

        saveAsMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem15.setText("Save As ...");
        saveAsMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu15.add(saveAsMenuItem15);
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

        setJMenuBar(menuBar15);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bottomScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1166, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(frameLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(weaponPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel2)
                                        .addGap(74, 74, 74)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(addAnim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(deleteAnim, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(down2But)
                                                    .addComponent(up2But, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(deleteAnim1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(addAnim1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(down2But1)
                                                    .addComponent(up2But1)))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(jButton1)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(weaponPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane3))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(addAnim)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(deleteAnim))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(29, 29, 29)
                                            .addComponent(down2But))
                                        .addComponent(up2But)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(up2But1)
                                        .addComponent(addAnim1))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(deleteAnim1)
                                        .addComponent(down2But1)))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addContainerGap())
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
        if (!weaponPanel.hasFocus())
            requestFocus();
    }//GEN-LAST:event_formMouseMoved

    private void openMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItem13ActionPerformed
        openWeapons();
    }//GEN-LAST:event_openMenuItem13ActionPerformed

    private void saveAsMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItem13ActionPerformed
        saveGoodiesAs();
    }//GEN-LAST:event_saveAsMenuItem13ActionPerformed

    private void saveMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItem13ActionPerformed
        saveGoodies();
    }//GEN-LAST:event_saveMenuItem13ActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        if (hadChanged) close();
        weapons = new WeaponsCollection();
        initAnimList();
        addAnimActionPerformed(null);
}//GEN-LAST:event_newMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (hadChanged) close();
    }//GEN-LAST:event_formWindowClosing

    private void aboutMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItem14ActionPerformed
        JOptionPane.showMessageDialog(null, ABOUT + GameInvariants.WEAPONS_VERSION + AUTHOR,"About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItem14ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        weapons.clear();
        if (collectionPanel.getCollection() == null ) return;
        for(Weapon w:GameInvariants.defaultWeapons){
            if (weapons.size()>=collectionPanel.getCollection().size()) break;
            weapons.add(new Weapon(w));
        }
        weaponsCtrl();
        setFirstWeapon();
        takeFrame(collectionPanel.getSelected());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void deleteAnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAnimActionPerformed
        int index = animList.getSelectedIndex();
        if (index != -1){
            //((DefaultListModel)animList.getModel()).remove(index);
            weapons.frames.remove(index);
            weaponsCtrl();
            initAnimList();
            if (weapons.frames.isEmpty()) weapons.clear();
        }
    }//GEN-LAST:event_deleteAnimActionPerformed

    private void addAnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAnimActionPerformed
        if (weapons == null) weapons = new WeaponsCollection();
        String[] names = openCollection();
        if (names == null) return;
        boolean already = true;
        for(String name:names){
            if (name == null) return;
            if (!weapons.frames.contains(name)){
                already = false;
                weapons.frames.add(name);
            }
        }
        if (already) JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        else{
            weaponsCtrl();
            initAnimList();
        }
    }//GEN-LAST:event_addAnimActionPerformed

    private void down2ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down2ButActionPerformed
        int index = animList.getSelectedIndex();
        if (index >= 0 && index < weapons.frames.size()-1){
            // do the swap
            String tmp = weapons.frames.get(index+1);
            weapons.frames.set(index+1, weapons.frames.get(index));
            weapons.frames.set(index, tmp);
            initAnimList();
            animList.setSelectedIndex(index+1);
        }
    }//GEN-LAST:event_down2ButActionPerformed

    private void up2ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up2ButActionPerformed
        int index = animList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = weapons.frames.get(index-1);
            weapons.frames.set(index-1, weapons.frames.get(index));
            weapons.frames.set(index, tmp);
            initAnimList();
            animList.setSelectedIndex(index-1);
        }
    }//GEN-LAST:event_up2ButActionPerformed

    private void animListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_animListValueChanged
        int selected = animList.getSelectedIndex();
        if (selected == -1){
            saveMenuItem14.setEnabled(false);
            saveAsMenuItem14.setEnabled(false);
            if (collectionPanel!=null)
                collectionPanel.setCollection(null);
            if (actionFramePanel!=null)
                actionFramePanel.setFrame(null);
        }else{
            saveMenuItem14.setEnabled(true);
            saveAsMenuItem14.setEnabled(true);
            setFirstWeapon();
            acceptCollection(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" + weapons.frames.get(selected)));
        }
        if (actionFramePanel!=null) actionFramePanel.repaint();
        if (collectionPanel!=null) collectionPanel.repaint();
    }//GEN-LAST:event_animListValueChanged

    private void animListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_animListPropertyChange
        
    }//GEN-LAST:event_animListPropertyChange

    private void animListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_animListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = animList.getSelectedIndex();
            if (index != -1){
                //((DefaultListModel)animList.getModel()).remove(index);
                weapons.frames.remove(index);
                weaponsCtrl();
                initAnimList();
                if (weapons.frames.isEmpty()) weapons.clear();
            }
        }
    }//GEN-LAST:event_animListKeyPressed

    private void deleteAnim1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAnim1ActionPerformed
        int index = iconList.getSelectedIndex();
        if (index != -1){
            weapons.icons.remove(index);
            initIconList();
        }
    }//GEN-LAST:event_deleteAnim1ActionPerformed

    private void addAnim1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAnim1ActionPerformed
        String[] names = openCollection();
        if (names == null) return;
        boolean already = true;
        for(String name:names){
            if (name == null) return;
            if (!weapons.icons.contains(name)){
                already = false;
                weapons.icons.add(name);
            }
        }
        if (already) JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        else{ initIconList(); }
    }//GEN-LAST:event_addAnim1ActionPerformed

    private void down2But1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down2But1ActionPerformed
        int index = iconList.getSelectedIndex();
        if (index >= 0 && index < weapons.icons.size()-1){
            // do the swap
            String tmp = weapons.icons.get(index+1);
            weapons.icons.set(index+1, weapons.icons.get(index));
            weapons.icons.set(index, tmp);
            initAnimList();
            iconList.setSelectedIndex(index+1);
        }
    }//GEN-LAST:event_down2But1ActionPerformed

    private void up2But1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up2But1ActionPerformed
        int index = iconList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = weapons.icons.get(index-1);
            weapons.icons.set(index-1, weapons.icons.get(index));
            weapons.icons.set(index, tmp);
            initIconList();
            iconList.setSelectedIndex(index-1);
        }
    }//GEN-LAST:event_up2But1ActionPerformed

    private void iconListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_iconListValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_iconListValueChanged

    private void iconListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_iconListPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_iconListPropertyChange

    private void iconListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_iconListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = iconList.getSelectedIndex();
            if (index != -1){
                weapons.icons.remove(index);
                initIconList();
            }
        }
    }//GEN-LAST:event_iconListKeyPressed
    
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
    private javax.swing.JMenuItem aboutMenuItem15;
    private javax.swing.JMenuItem aboutMenuItem16;
    private javax.swing.JButton addAnim;
    private javax.swing.JButton addAnim1;
    private javax.swing.JList animList;
    private gui.Scroller bottomScroll;
    private javax.swing.JButton deleteAnim;
    private javax.swing.JButton deleteAnim1;
    private javax.swing.JButton down2But;
    private javax.swing.JButton down2But1;
    private javax.swing.JMenuItem exitMenuItem14;
    private javax.swing.JMenuItem exitMenuItem15;
    private javax.swing.JMenu fileMenu14;
    private javax.swing.JMenu fileMenu15;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JMenu helpMenu16;
    private javax.swing.JMenu helpMenu17;
    private javax.swing.JList iconList;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JMenuBar menuBar14;
    private javax.swing.JMenuBar menuBar15;
    private javax.swing.JMenuItem newMenuItem1;
    private javax.swing.JMenuItem newMenuItem2;
    private javax.swing.JMenuItem openMenuItem14;
    private javax.swing.JMenuItem openMenuItem15;
    private javax.swing.JMenuItem saveAsMenuItem14;
    private javax.swing.JMenuItem saveAsMenuItem15;
    private javax.swing.JMenuItem saveMenuItem14;
    private javax.swing.JMenuItem saveMenuItem15;
    private gui.Scroller topScroll;
    private javax.swing.JButton up2But;
    private javax.swing.JButton up2But1;
    private gui.WeaponPanel weaponPanel;
    // End of variables declaration//GEN-END:variables

    
}
