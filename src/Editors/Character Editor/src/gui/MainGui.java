/*
 * MainGui.java
 *
 * Created on 19 October 2008, 16:54
 */

package gui;

import file.CorruptionException;

import java.io.File;
import java.io.IOException;

import frames.FramesCollection;

import game.GameInvariants;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import file.IFilesAccepter;
import characters.Character;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author  Gil Costa
 * 
 * BUG LIST
 *       - AnimationPanel: deleting frames equal to other existing frames isn't working
 * 
 */
public class MainGui extends javax.swing.JFrame
    implements IFilesAccepter
{
    /** default serial ID*/
	private static final long serialVersionUID = 1L;
    
    protected static final String ABOUT =" Character Editor v";
    protected static final String AUTHOR = "\n\n" +
                "     Autor: Gil Costa\n" +
                "     Evolution © 2008-2009";
    
    protected static final String ERROR_MESSAGE = "Woops";
    protected static final String CANT_OPEN_ANIMATION = "Unable to open frames collection";
    protected static final String CANT_OPEN_CHARACTER = "Unable to open character";
    protected static final String CANT_SAVE_FILE = "Unable to save the character";
    
    
    protected static final String SAVE_BEFORE = "Character had changed\nSave changes?";
    
	
	/** the character character */
	protected Character character;
    
    /** file chooser for images opening */
    protected JFileChooser characterChooser;
    
    protected boolean hadChanged;
    
    
    FramesCollection collection;
    FramesCollection head;
    
    protected int previousIndex;
	
    
    
    
    public void locate(){
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, 
		size.height/2 - getHeight()/2);
    }
    
    
	/** Creates new form MainGui */
    public MainGui() {
        initComponents();
        initAdvancedComponents();
        initCharacterChooser();
        //initDropTarget();
        locate();
        
        collection = null;
        head = null;
        
        newCharacter();
        requestFocus();
    }
    
    
    protected void newCharacter(){
        character = new Character();
        characterPanel.setCharacter(character);
        tabbedPane.setSelectedComponent(characterPanel);
        previousIndex = 0;
    }
    
    
    
    protected void initAdvancedComponents(){
        animsPanel.initList(null);
    }
    
    
    
    
    protected void initCharacterChooser(){
        characterChooser = new JFileChooser();
        characterChooser.setDialogTitle("Choose a Character");
        characterChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.CHARACTERS_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Claracter", game.GameInvariants.CHARACTER_EXTENSION);
        // add them
        characterChooser.addChoosableFileFilter(fs);
        characterChooser.setFileFilter(fs);
        characterChooser.setMultiSelectionEnabled(false);
    }

    
        
    protected void openCharacter(){
        int returnVal = characterChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = characterChooser.getSelectedFile();
            if (file != null)
                acceptCharacter(file);
        }
    }

    
    
    
    protected void acceptCharacter(File file){
        characterPanel.setCharacter(null);  // update previous character
        animsPanel.reset();
        if (hadChanged) close();
        try {
            character.load(file);
        } catch (IOException e) {
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN_CHARACTER,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        characterPanel.setCharacter(character);
        tabbedPane.setSelectedComponent(characterPanel);
        previousIndex = 0;
    }
    
    
    
    
    @Override
	public void acceptFile(File file, int index) {
        acceptCharacter(file);
	}

	@Override
	public void acceptFiles(File[] files, int index) {
        if (files!=null && files.length<=0) return;
        acceptCharacter(files[0]);
	}
    
    
    
    protected void saveCharacterAs(){
        int returnVal = characterChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = characterChooser.getSelectedFile();
            if (file != null){
                character.setFile(file);
                saveCharacter();
            }
                
        }
    }
    
    protected void saveCharacter(){
        if (character.getFile() == null)
            saveCharacterAs();
        else{
//            update(previousIndex,0);
//            tabbedPane.setSelectedIndex(0);
//            character.synchronizeIn();
            if (tabbedPane.getSelectedIndex() == 0) update(0,0);
            tabbedPane.setSelectedIndex(0);
            try { character.save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, CANT_SAVE_FILE, ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

        
    protected void close(){
        int res = JOptionPane.showConfirmDialog(null,SAVE_BEFORE,"save?",JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
            saveCharacter();
        hadChanged = false;
    }

    
    
    protected FramesCollection loadCollection(String name, String canNot){
        String path = GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" + name;
        try{
            FramesCollection fr = new FramesCollection();
            fr.load(new File(path));
            return fr;
        }catch(IOException e){
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, canNot,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return null;   
        }
    }
    
    
    
    
    
    protected void update(int prevIndex, int nextIndex){
        switch(prevIndex){
            case 0:
                characterPanel.update();
                String clStr;
                clStr = characterPanel.getSelectedAnim();
                if (clStr!=null){
                    collection = loadCollection(clStr, "Can't open body animation");
                    clStr = characterPanel.getSelectedHead();
                    if (clStr!=null)
                        head = loadCollection(clStr, "Can't open head animation");
                    else head = null;
                }else collection = null;
                break;
            case 1:
                if (animsPanel.wasInitiated()){
                    animsPanel.update();
                }
                break;
            case 2: fixedPanel.setAnimation(null, null); break;
            case 3: keysPanel.setAnimation(null, null); break;    
        }
        switch(nextIndex){
            case 1:
                animsPanel.setHeadSwaps(characterPanel.hasHeads());
                if (!animsPanel.wasInitiated() && collection!=null){
                    animsPanel.setCollection(collection);
                    animsPanel.setCharacter(character);
                }else if (collection==null){
                    animsPanel.setCollection(null);
                    animsPanel.setCharacter(null);
                }
                break;
            case 2:
                if(collection!=null){
                    fixedPanel.setAnimation(collection, animsPanel.getAnimation());
                    fixedPanel.setHead(head);
                    if (character.autoPallete)
                        fixedPanel.setAutoPallet(collection.getFileName(), characterPanel.getHeadSwapIndex());
                    else fixedPanel.setAutoPallet("",-1);
                    fixedPanel.repaint();
                }
                break;
            case 3:
                if(collection!=null){
                    keysPanel.setAnimation(collection, animsPanel.getAnimation());
                }
                
        }
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        characterPanel = new gui.CharacterPanel();
        animsPanel = new gui.AnimationsPanel();
        fixedPanel = new gui.FixedFramesPanel();
        keysPanel = new gui.KeyFramesPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Character Editor");
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

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });
        tabbedPane.addTab("Character", characterPanel);
        tabbedPane.addTab("Animations", animsPanel);
        tabbedPane.addTab("Frames", fixedPanel);
        tabbedPane.addTab("Key Frames", keysPanel);

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

        setJMenuBar(menuBar14);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 904, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (hadChanged) close();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        //tabbedPane.getSelectedComponent().requestFocus();
    }//GEN-LAST:event_formMouseMoved

    private void openMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItem13ActionPerformed
        openCharacter();
    }//GEN-LAST:event_openMenuItem13ActionPerformed

    private void saveAsMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItem13ActionPerformed
        saveCharacterAs();
    }//GEN-LAST:event_saveAsMenuItem13ActionPerformed

    private void saveMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItem13ActionPerformed
        saveCharacter();
    }//GEN-LAST:event_saveMenuItem13ActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        if (hadChanged) close();
        newCharacter();
}//GEN-LAST:event_newMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (hadChanged) close();
    }//GEN-LAST:event_formWindowClosing

    private void aboutMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItem14ActionPerformed
        JOptionPane.showMessageDialog(null, ABOUT + GameInvariants.CHARACTER_VERSION + AUTHOR,"About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItem14ActionPerformed

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        int index = tabbedPane.getSelectedIndex();
        if (index != previousIndex){
            update(previousIndex,index);
            previousIndex = index;
        }
    }//GEN-LAST:event_tabbedPaneStateChanged
    
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
    private javax.swing.JMenuItem aboutMenuItem15;
    private gui.AnimationsPanel animsPanel;
    private gui.CharacterPanel characterPanel;
    private javax.swing.JMenuItem exitMenuItem14;
    private javax.swing.JMenu fileMenu14;
    private gui.FixedFramesPanel fixedPanel;
    private javax.swing.JMenu helpMenu16;
    private javax.swing.JSeparator jSeparator15;
    private gui.KeyFramesPanel keysPanel;
    private javax.swing.JMenuBar menuBar14;
    private javax.swing.JMenuItem newMenuItem1;
    private javax.swing.JMenuItem openMenuItem14;
    private javax.swing.JMenuItem saveAsMenuItem14;
    private javax.swing.JMenuItem saveMenuItem14;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    
}
