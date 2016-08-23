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
import goodies.Goodie;
import goodies.GoodiesCollection;
import java.awt.dnd.DropTarget;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import file.IFilesAccepter;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.DefaultListModel;

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
    
    
    protected static final String ABOUT =" Goodies Editor v";
    protected static final String AUTHOR = "\n\n" +
                "     Autor: Gil Costa\n" +
                "     Evolution © 2008-2009";
    
    protected static final String ERROR_MESSAGE = "Woops";
    protected static final String CANT_OPEN_GOODIES = "Unable to open goodies";
    protected static final String CANT_OPEN_COLLECTION = "Unable to open collection";
    protected static final String CANT_SAVE_FILE = "Unable to save goodies";
    
    public static final String ALREADY_EXISTS = "Already exists in this Goodies Set";
    
    
    protected static final String SAVE_BEFORE = "Goodies set had changed\nSave changes?";
    
    
    protected static final int GOODIES_INDEX = 0;
    protected static final int COLLECTIONS_INDEX = 1;
    

	/** frame panel */
	protected gui.FramePanel framePanel;
    /** goodies of goodies panel */
    protected gui.CollectionPanel collectionPanel;
	
	/** the goodies goodies */
	protected GoodiesCollection goodies;
    
    /** file chooser for images opening */
    protected JFileChooser goodiesChooser;
    
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
        
        goodies = new GoodiesCollection();
        takeFrame(null);
        requestFocus();
    }
    
    
    
    protected void initAdvancedComponents(){
    	collectionPanel = new gui.CollectionPanel(bottomScroll, this);
        framePanel = new gui.FramePanel(topScroll, null, null);
        
        collectionPanel.setForeground(new java.awt.Color(255, 255, 255));
        bottomScroll.setViewportView(collectionPanel);
        bottomScroll.setWheelScrollingEnabled(false);

        framePanel.setForeground(new java.awt.Color(255, 255, 255));
        topScroll.setViewportView(framePanel);
        topScroll.setWheelScrollingEnabled(false);
        framePanel.showCM(false);
        saveMenuItem13.setEnabled(false);
        saveAsMenuItem13.setEnabled(false);
    }
    
    
    
    
    protected void initGoodiesChooser(){
        goodiesChooser = new JFileChooser();
        goodiesChooser.setDialogTitle("Choose a Goodies Set");
        goodiesChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.GOODIES_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Goodies Set", GameInvariants.GOODIES_EXTENSION);
        // add them
        goodiesChooser.addChoosableFileFilter(fs);
        goodiesChooser.setFileFilter(fs);
        goodiesChooser.setMultiSelectionEnabled(false);
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
    	new DropTarget(framePanel, new FileDropTargetListener(this,GOODIES_INDEX));
        new DropTarget(collectionPanel, new FileDropTargetListener(this,COLLECTIONS_INDEX));
    }
        
        
    protected void openGoodies(){
        int returnVal = goodiesChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = goodiesChooser.getSelectedFile();
            if (file != null)
                acceptGoodies(file);
        }
    }
    
    
    
    
    
    protected void acceptGoodies(File file){
        if (hadChanged) close();
        try {
            goodies.load(file);
        } catch (IOException e) {
            saveMenuItem13.setEnabled(false);
            saveAsMenuItem13.setEnabled(false);
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN_GOODIES,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        saveMenuItem13.setEnabled(true);
        saveAsMenuItem13.setEnabled(true);
        setFirstGoodie();
        initAnimList();
        initIconList();
        //acceptCollection(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" + goodies.getAllFrames().get(0)));
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
        if (goodies.isEmpty()){   // first time, controll goodies
            for(int i=0; i<frames.size(); i++)
                goodies.add(new Goodie(GameInvariants.GOODIES_VERSION));
        }
        int dif = goodies.size()-frames.size();
        for(int i=0; i<dif; i++)
            goodies.remove(goodies.size()-1);
        
        saveMenuItem13.setEnabled(true);
        saveAsMenuItem13.setEnabled(true);
        setFirstGoodie();
        collectionPanel.setCollection(frames);
        framePanel.repaint();
        collectionPanel.repaint();
    }
    
    
    protected void goodiesCtrl(){
        int minSize = Integer.MAX_VALUE;
        for(String s:goodies.frames){
            FramesCollection frames;
            frames = new FramesCollection();
            try { frames.load(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" +s));
            } catch (IOException e) { break; }
            if (frames.size() < minSize) minSize = frames.size();
        }
        if (minSize == Integer.MAX_VALUE) minSize = 0;
         for(int i=goodies.size(); i<minSize; i++)
                goodies.add(new Goodie(GameInvariants.GOODIES_VERSION));
            int dif = goodies.size()-minSize;
            for(int i=0; i<dif; i++)
                goodies.remove(goodies.size()-1);
    }
    
    
    @Override
	public void acceptFile(File file, int index) {
        switch(index){
            case GOODIES_INDEX:
                acceptGoodies(file);
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
            case GOODIES_INDEX:
                acceptGoodies(files[0]);
                break;
            case COLLECTIONS_INDEX:
                acceptCollection(files[0]);
                break;
        }
	}
    
    
    
    protected void saveGoodiesAs(){
        int returnVal = goodiesChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = goodiesChooser.getSelectedFile();
            if (file != null){
                goodies.setFile(file);
                saveGoodies();
            }
                
        }
    }
    
    protected void saveGoodies(){
        takeFrame(collectionPanel.getSelected());
        if (goodies.getFile() == null)
            saveGoodiesAs();
        else{
            try { goodies.save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, CANT_SAVE_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    
	
	
	@Override
	public void takeFrame(Frame frame) {
        frameLabel.setText("Frame " + (collectionPanel.getSelectedNum()+1) + " of " + goodies.size());
		framePanel.setFrame(frame);
        if (frame!=null){
            int fn = collectionPanel.getSelectedNum();
            Goodie g = goodies.get(fn); // it must exist
            goodiePanel.setGoodie(g);
            goodiePanel.setAllEnabled(true);
        }else goodiePanel.setAllEnabled(false);
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
    
    
    protected void setFirstGoodie(){
        goodiePanel.reset();
        goodiePanel.setGoodie(goodies.get(collectionPanel.getSelectedNum()));
    }
    
    
    
    
    
    public void initAnimList(){
        int index = animList.getSelectedIndex();
        animList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:goodies.frames)
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
        for(String s:goodies.icons)
            listModel.addElement("[" + i++ + "] " + s);
        iconList.setModel(listModel);
        if (index>=0 && index<listModel.size()){
            iconList.setSelectedIndex(index);
        } else iconList.setSelectedIndex(listModel.size()-1);
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
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameLabel = new javax.swing.JLabel();
        topScroll = new gui.Scroller();
        previousBut = new javax.swing.JButton();
        bottomScroll = new gui.Scroller();
        goodiePanel = new gui.GoodiePanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        animList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        addAnim = new javax.swing.JButton();
        up2But = new javax.swing.JButton();
        down2But = new javax.swing.JButton();
        deleteAnim = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        deleteAnim1 = new javax.swing.JButton();
        addAnim1 = new javax.swing.JButton();
        down2But1 = new javax.swing.JButton();
        up2But1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        iconList = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        menuBar13 = new javax.swing.JMenuBar();
        fileMenu13 = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem13 = new javax.swing.JMenuItem();
        saveMenuItem13 = new javax.swing.JMenuItem();
        saveAsMenuItem13 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JSeparator();
        exitMenuItem13 = new javax.swing.JMenuItem();
        helpMenu14 = new javax.swing.JMenu();
        aboutMenuItem14 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Goodies Editor");
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

        goodiePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton1.setText("Set All to Default");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        addAnim.setText("add");
        addAnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAnimActionPerformed(evt);
            }
        });

        up2But.setText("   up  ");
        up2But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                up2ButActionPerformed(evt);
            }
        });

        down2But.setText("down");
        down2But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                down2ButActionPerformed(evt);
            }
        });

        deleteAnim.setText("delete");
        deleteAnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAnimActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

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

        helpMenu14.setText("Help");

        aboutMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenuItem14.setText("About");
        aboutMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItem14ActionPerformed(evt);
            }
        });
        helpMenu14.add(aboutMenuItem14);

        menuBar13.add(helpMenu14);

        setJMenuBar(menuBar13);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(goodiePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteAnim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addAnim, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(down2But)
                            .addComponent(up2But)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(deleteAnim1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addAnim1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(down2But1)
                                    .addComponent(up2But1)))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jLabel3)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(frameLabel)
                .addContainerGap(542, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bottomScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(goodiePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(up2But)
                            .addComponent(addAnim))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(deleteAnim)
                            .addComponent(down2But))
                        .addGap(11, 11, 11))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(up2But1)
                            .addComponent(addAnim1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(deleteAnim1)
                            .addComponent(down2But1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGap(6, 6, 6)
                .addComponent(frameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (hadChanged) close();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void previousButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButActionPerformed
        collectionPanel.selectPrevious();
        requestFocus();
}//GEN-LAST:event_previousButActionPerformed

    private void previousButFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_previousButFocusGained

    }//GEN-LAST:event_previousButFocusGained

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
        if (!goodiePanel.hasFocus() && !animList.hasFocus() && !iconList.hasFocus())
            requestFocus();
    }//GEN-LAST:event_formMouseMoved

    private void openMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItem13ActionPerformed
        openGoodies();
    }//GEN-LAST:event_openMenuItem13ActionPerformed

    private void saveAsMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItem13ActionPerformed
        saveGoodiesAs();
    }//GEN-LAST:event_saveAsMenuItem13ActionPerformed

    private void saveMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItem13ActionPerformed
        saveGoodies();
    }//GEN-LAST:event_saveMenuItem13ActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        if (hadChanged) close();
        goodies = new GoodiesCollection();
        addAnimActionPerformed(null);
}//GEN-LAST:event_newMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (hadChanged) close();
    }//GEN-LAST:event_formWindowClosing

    private void aboutMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItem14ActionPerformed
        JOptionPane.showMessageDialog(null, ABOUT + GameInvariants.GOODIES_VERSION + AUTHOR,"About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItem14ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        goodies.clear();
        if (collectionPanel.getCollection() == null) return;
        for(Goodie g:GameInvariants.defaultGoodies){
            if(goodies.size()==collectionPanel.getCollection().size()) break;
            goodies.add(new Goodie(g));
        }
        for(int i = goodies.size(); i<collectionPanel.getCollection().size(); i++)
            goodies.add(new Goodie(GameInvariants.GOODIES_VERSION));
        goodiesCtrl();
        setFirstGoodie();
        takeFrame(collectionPanel.getSelected());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void animListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_animListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = animList.getSelectedIndex();
            if (index != -1){
                //((DefaultListModel)animList.getModel()).remove(index);
                goodies.frames.remove(index);
                goodiesCtrl();
                initAnimList();
                if (goodies.frames.isEmpty()) goodies.clear();
            }
        }
    }//GEN-LAST:event_animListKeyPressed

    private void addAnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAnimActionPerformed
        String[] names = openCollection();
        if (names == null) return;
        boolean already = true;
        for(String name:names){
            if (name == null) return;
            if (!goodies.frames.contains(name)){
                already = false;
                goodies.frames.add(name);
            }
        }
        if (already) JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        else{
            goodiesCtrl();
            initAnimList();
        }       
    }//GEN-LAST:event_addAnimActionPerformed

    private void up2ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up2ButActionPerformed
        int index = animList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = goodies.frames.get(index-1);
            goodies.frames.set(index-1, goodies.frames.get(index));
            goodies.frames.set(index, tmp);
            initAnimList();
            animList.setSelectedIndex(index-1);
        }
    }//GEN-LAST:event_up2ButActionPerformed

    private void down2ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down2ButActionPerformed
        int index = animList.getSelectedIndex();
        if (index >= 0 && index < goodies.frames.size()-1){
            // do the swap
            String tmp = goodies.frames.get(index+1);
            goodies.frames.set(index+1, goodies.frames.get(index));
            goodies.frames.set(index, tmp);
            initAnimList();
            animList.setSelectedIndex(index+1);
        }
    }//GEN-LAST:event_down2ButActionPerformed

    private void deleteAnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAnimActionPerformed
        int index = animList.getSelectedIndex();
            if (index != -1){
                //((DefaultListModel)animList.getModel()).remove(index);
                goodies.frames.remove(index);
                goodiesCtrl();
                initAnimList();
                if (goodies.frames.isEmpty()) goodies.clear();
            }
}//GEN-LAST:event_deleteAnimActionPerformed

    private void animListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_animListPropertyChange
        
    }//GEN-LAST:event_animListPropertyChange

    private void animListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_animListValueChanged
        int selected = animList.getSelectedIndex();
        if (selected == -1){
            saveMenuItem13.setEnabled(false);
            saveAsMenuItem13.setEnabled(false);
            if (collectionPanel!=null)
                collectionPanel.setCollection(null);
            if (framePanel!=null)
                framePanel.setFrame(null);
        }else{
            saveMenuItem13.setEnabled(true);
            saveAsMenuItem13.setEnabled(true);
            setFirstGoodie();
            acceptCollection(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" + goodies.frames.get(selected)));
        }
        if (framePanel!=null) framePanel.repaint();
        if (collectionPanel!=null) collectionPanel.repaint();
    }//GEN-LAST:event_animListValueChanged

    private void deleteAnim1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAnim1ActionPerformed
        int index = iconList.getSelectedIndex();
        if (index != -1){
            goodies.icons.remove(index);
            initIconList();
        }
    }//GEN-LAST:event_deleteAnim1ActionPerformed

    private void addAnim1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAnim1ActionPerformed
        String[] names = openCollection();
        if (names == null) return;
        boolean already = true;
        for(String name:names){
            if (name == null) return;
            if (!goodies.icons.contains(name)){
                already = false;
                goodies.icons.add(name);
            }
        }
        if (already) JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        else{ initIconList(); }
    }//GEN-LAST:event_addAnim1ActionPerformed

    private void down2But1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down2But1ActionPerformed
        int index = iconList.getSelectedIndex();
        if (index >= 0 && index < goodies.icons.size()-1){
            // do the swap
            String tmp = goodies.icons.get(index+1);
            goodies.icons.set(index+1, goodies.icons.get(index));
            goodies.icons.set(index, tmp);
            initAnimList();
            iconList.setSelectedIndex(index+1);
        }
    }//GEN-LAST:event_down2But1ActionPerformed

    private void up2But1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up2But1ActionPerformed
        int index = iconList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = goodies.icons.get(index-1);
            goodies.icons.set(index-1, goodies.icons.get(index));
            goodies.icons.set(index, tmp);
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
                goodies.icons.remove(index);
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
    private javax.swing.JMenuItem aboutMenuItem14;
    private javax.swing.JButton addAnim;
    private javax.swing.JButton addAnim1;
    private javax.swing.JList animList;
    private gui.Scroller bottomScroll;
    private javax.swing.JButton deleteAnim;
    private javax.swing.JButton deleteAnim1;
    private javax.swing.JButton down2But;
    private javax.swing.JButton down2But1;
    private javax.swing.JMenuItem exitMenuItem13;
    private javax.swing.JMenu fileMenu13;
    private javax.swing.JLabel frameLabel;
    private gui.GoodiePanel goodiePanel;
    private javax.swing.JMenu helpMenu14;
    private javax.swing.JList iconList;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JMenuBar menuBar13;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem13;
    private javax.swing.JButton previousBut;
    private javax.swing.JMenuItem saveAsMenuItem13;
    private javax.swing.JMenuItem saveMenuItem13;
    private gui.Scroller topScroll;
    private javax.swing.JButton up2But;
    private javax.swing.JButton up2But1;
    // End of variables declaration//GEN-END:variables

    
}
