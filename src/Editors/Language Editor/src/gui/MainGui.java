/*
 * MainGui.java
 *
 * Created on 17 December 2008, 14:59
 */

package gui;

import file.CorruptionException;
import file.IFilesAccepter;
import game.GameInvariants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import language.LangEncoder;
import language.LanguagePack;

/**
 *
 * @author  Gil
 */
public class MainGui extends javax.swing.JFrame implements IFilesAccepter{
    
    protected static final String ABOUT =" Language Pack Editor v";
    protected static final String AUTHOR = "\n\n" +
                "     Autor: Gil Costa\n" +
                "     Evolution © 2008-2009";
    
    
    protected static final String ERROR_MESSAGE = "Woops";
    protected static final String CANT_OPEN = "Unable to open the language pack";
    protected static final String CANT_SAVE = "Unable to save the language pack";
    
    
    protected static final String SAVE_BEFORE = "Language pack had changed\nSave changes?";
    
    
    /** file chooser for images opening */
    protected JFileChooser languageChooser;
    
    protected boolean hadChanged;
    
    
    LangEncoder lang;
    
    
    
    public void locate(){
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, 
		size.height/2 - getHeight()/2);
    }
    
    /** Creates new form MainGui */
    public MainGui() {
        initComponents();
        initLanguageChooser();
        newLanguage();
        locate();
    }
    
    
    
    void setPanels(){
        LanguagePack pack = lang.getFileData();
        speach.setList(pack.speach, "Speach (characters chat, history, etc)");
        menus.setList(pack.menus, "Menu items");
        weapons.setList(pack.weapons, "Weapon names");
        goodies.setList(pack.goodies, "Goodie names");
    }
    
    
    
    
    
    
    
    
    protected void initLanguageChooser(){
        languageChooser = new JFileChooser();
        languageChooser.setDialogTitle("Choose a Language Pack");
        languageChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.LANGUAGE_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Language Pack", game.GameInvariants.LANGUAGE_EXTENSION);
        // add them
        languageChooser.addChoosableFileFilter(fs);
        languageChooser.setFileFilter(fs);
        languageChooser.setMultiSelectionEnabled(false);
    }

    
        
    protected void openLanguage(){
        int returnVal = languageChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = languageChooser.getSelectedFile();
            if (file != null)
                acceptLanguage(file);
        }
    }

    
    
    
    protected void acceptLanguage(File file){
        if (hadChanged) close();
        try {
            lang.load(file);
        } catch (IOException e) {
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN,ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        setPanels();
    }
    
    
    
    
    @Override
	public void acceptFile(File file, int index) {
        acceptLanguage(file);
	}

	@Override
	public void acceptFiles(File[] files, int index) {
        if (files!=null && files.length<=0) return;
        acceptLanguage(files[0]);
	}
    
    
    
    protected void saveLanguageAs(){
        int returnVal = languageChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = languageChooser.getSelectedFile();
            if (file != null){
                lang.setFile(file);
                saveLanguage();
            }
                
        }
    }
    
    protected void saveLanguage(){
        if (lang.getFile() == null)
            saveLanguageAs();
        else{
            speach.update();
            menus.update();
            weapons.update();
            goodies.update();
            
            try { lang.save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, CANT_SAVE, ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

        
    protected void close(){
        int res = JOptionPane.showConfirmDialog(null,SAVE_BEFORE,"save?",JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
            saveLanguage();
        hadChanged = false;
    }
    
    
    
    protected void newLanguage(){
        lang = new LangEncoder();
        setPanels();
    }
    
    
    
    
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();
        speach = new gui.ListPanel();
        menus = new gui.ListPanel();
        weapons = new gui.ListPanel();
        goodies = new gui.ListPanel();
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
        setTitle("Language Pack Editor");

        tabPane.addTab("Speach", speach);
        tabPane.addTab("Menu", menus);
        tabPane.addTab("Weapons", weapons);
        tabPane.addTab("Goodies", goodies);

        fileMenu14.setText("File");

        newMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem1.setText("New");
        newMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItem1newMenuItemActionPerformed(evt);
            }
        });
        fileMenu14.add(newMenuItem1);

        openMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem14.setText("Open");
        openMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItem14openMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu14.add(openMenuItem14);

        saveMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem14.setText("Save");
        saveMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItem14saveMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu14.add(saveMenuItem14);

        saveAsMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem14.setText("Save As ...");
        saveAsMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItem14saveAsMenuItem13ActionPerformed(evt);
            }
        });
        fileMenu14.add(saveAsMenuItem14);
        fileMenu14.add(jSeparator15);

        exitMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem14.setText("Exit");
        exitMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItem14exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu14.add(exitMenuItem14);

        menuBar14.add(fileMenu14);

        helpMenu16.setText("Help");

        aboutMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenuItem15.setText("About");
        aboutMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItem15aboutMenuItem14ActionPerformed(evt);
            }
        });
        helpMenu16.add(aboutMenuItem15);

        menuBar14.add(helpMenu16);

        setJMenuBar(menuBar14);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newMenuItem1newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItem1newMenuItemActionPerformed
        if (hadChanged) close();
        newLanguage();
    }//GEN-LAST:event_newMenuItem1newMenuItemActionPerformed

    private void openMenuItem14openMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItem14openMenuItem13ActionPerformed
        openLanguage();
    }//GEN-LAST:event_openMenuItem14openMenuItem13ActionPerformed

    private void saveMenuItem14saveMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItem14saveMenuItem13ActionPerformed
        saveLanguage();
    }//GEN-LAST:event_saveMenuItem14saveMenuItem13ActionPerformed

    private void saveAsMenuItem14saveAsMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItem14saveAsMenuItem13ActionPerformed
        saveLanguageAs();
    }//GEN-LAST:event_saveAsMenuItem14saveAsMenuItem13ActionPerformed

    private void exitMenuItem14exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItem14exitMenuItemActionPerformed
        if (hadChanged) close();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItem14exitMenuItemActionPerformed

    private void aboutMenuItem15aboutMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItem15aboutMenuItem14ActionPerformed
        JOptionPane.showMessageDialog(null, ABOUT + GameInvariants.LANGUAGE_VERSION + AUTHOR,"About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItem15aboutMenuItem14ActionPerformed
    
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
    private javax.swing.JMenuItem exitMenuItem14;
    private javax.swing.JMenu fileMenu14;
    private gui.ListPanel goodies;
    private javax.swing.JMenu helpMenu16;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JMenuBar menuBar14;
    private gui.ListPanel menus;
    private javax.swing.JMenuItem newMenuItem1;
    private javax.swing.JMenuItem openMenuItem14;
    private javax.swing.JMenuItem saveAsMenuItem14;
    private javax.swing.JMenuItem saveMenuItem14;
    private gui.ListPanel speach;
    private javax.swing.JTabbedPane tabPane;
    private gui.ListPanel weapons;
    // End of variables declaration//GEN-END:variables
    
}
