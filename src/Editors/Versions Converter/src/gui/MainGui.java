/*
 * MainGui.java
 *
 * Created on 09 February 2009, 13:55
 */

package gui;

import audio.AudioEncoder;
import file.AFileResolver;
import game.GameInvariants;
import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.JTextField;
import characters.Character;
import audio.AudioFile;
import frames.FramesCollection;
import goodies.GoodiesCollection;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import language.LangEncoder;
import weapons.WeaponsCollection;
import language.LanguagePack;
import pallet.Pallet;

/**
 *
 * @author  Gil
 */
public class MainGui extends javax.swing.JFrame {
    
    
    protected static final String ABOUT =" Versions Exchanger ";
    protected static final String AUTHOR = "\n\n" +
                "     Autor: Gil Costa\n" +
                "     Evolution © 2008-2009";
    
    
    static final Color HIGHLIGHTED = Color.CYAN;
    static final Color BAD_HIGHLIGHTED = Color.ORANGE;
    static final String NONE = "none";
    
    protected Progress progress;
    
    // loading stuff
    protected int tmp1;
    protected int tmp2;
    
    protected File[] characters;
    protected File[] frames;
    protected File[] pallets;
    protected File[] goodies;
    protected File[] weapons;
    protected File[] sounds;
    protected File[] musics;
    protected File[] langs;
    
    
    
    public void locate(){
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, 
		size.height/2 - getHeight()/2);
    }
    
    /** Creates new form MainGui */
    public MainGui() {
        initComponents();
        locate();
        refresh();
    }
    
    
    protected File[] getFileList(String dirName, final String extension){
        File dir = new File(dirName);
        FilenameFilter filter = new FilenameFilter() { 
            public boolean accept(File b, String name) { 
                return name.endsWith("." + extension); 
            } 
        }; 
        return dir.listFiles(filter);
    }
    
    protected void findFileLists(){
        characters = getFileList(GameInvariants.CHARACTERS_DIR,GameInvariants.CHARACTER_EXTENSION);
        frames = getFileList(GameInvariants.ANIMATIONS_DIR,GameInvariants.FRAMES_EXTENSION);
        pallets = getFileList(GameInvariants.PALLETS_DIR,GameInvariants.PALLET_EXTENSION);
        goodies = getFileList(GameInvariants.GOODIES_DIR,GameInvariants.GOODIES_EXTENSION);
        weapons = getFileList(GameInvariants.WEAPONS_DIR,GameInvariants.WEAPONS_EXTENSION);
        sounds = getFileList(GameInvariants.AUDIO_DIR,GameInvariants.SOUND_EXTENSION);
        musics = getFileList(GameInvariants.AUDIO_DIR,GameInvariants.MUSIC_EXTENSION);
        langs = getFileList(GameInvariants.LANGUAGE_DIR,GameInvariants.LANGUAGE_EXTENSION);
    }
    
    
    int readVersion(File f){
        return new GenericFile(f).getVersion();
    }
    
    protected void initOldVersions(){
        if (characters!=null && characters.length>0) oldChar.setText(readVersion(characters[0])+"");
        else oldChar.setText(NONE);
        if (frames!=null && frames.length>0) oldFrame.setText(readVersion(frames[0])+"");
        else oldFrame.setText(NONE);
        if (pallets!=null && pallets.length>0) oldPallet.setText(readVersion(pallets[0])+"");
        else oldPallet.setText(NONE);
        if (goodies!=null && goodies.length>0) oldGoodie.setText(readVersion(goodies[0])+"");
        else oldGoodie.setText(NONE);
        if (weapons!=null && weapons.length>0) oldWeapon.setText(readVersion(weapons[0])+"");
        else oldWeapon.setText(NONE);
        if (sounds!=null && sounds.length>0) oldSound.setText(readVersion(sounds[0])+"");
        else oldSound.setText(NONE);
        if (musics!=null && musics.length>0) oldMusic.setText(readVersion(musics[0])+"");
        else oldMusic.setText(NONE);
        if (langs!=null && langs.length>0) oldLang.setText(readVersion(langs[0])+"");
        else oldLang.setText(NONE);
    }
    
    protected void initActualVersions(){
        charField.setText(GameInvariants.CHARACTER_VERSION+"");
        frameField.setText(GameInvariants.FRAMES_VERSION+"");
        palletField.setText(GameInvariants.PALLET_VERSION+"");
        goodieField.setText(GameInvariants.GOODIES_VERSION+"");
        weaponField.setText(GameInvariants.WEAPONS_VERSION+"");
        soundField.setText(GameInvariants.SOUND_VERSION+"");
        musicField.setText(GameInvariants.MUSIC_VERSION+"");
        langField.setText(GameInvariants.LANGUAGE_VERSION+"");
    }
    
    protected void highLight(JTextField f1, JTextField f2, int original){
        if (f2.getText().compareTo(NONE)==0){
            f1.setText(NONE);
            f1.setBackground(Color.WHITE);
            return;
        }
        if (f1.getText().compareTo(f2.getText())!=0){
            if (Integer.parseInt(f1.getText()) == original) f1.setBackground(HIGHLIGHTED);
            else f1.setBackground(BAD_HIGHLIGHTED);
        }else f1.setBackground(Color.WHITE);
    }
    
    protected void highLightChanges(){
        highLight(charField,oldChar, GameInvariants.CHARACTER_VERSION);
        highLight(frameField,oldFrame, GameInvariants.FRAMES_VERSION);
        highLight(palletField,oldPallet, GameInvariants.PALLET_VERSION);
        highLight(goodieField,oldGoodie, GameInvariants.GOODIES_VERSION);
        highLight(weaponField,oldWeapon, GameInvariants.WEAPONS_VERSION);
        highLight(soundField,oldSound, GameInvariants.SOUND_VERSION);
        highLight(musicField,oldMusic, GameInvariants.MUSIC_VERSION);
        highLight(langField,oldLang, GameInvariants.LANGUAGE_VERSION);
    }
    
    
    public void refresh(){
        findFileLists();
        initOldVersions();
        initActualVersions();
        highLightChanges();
    }
    
    
    protected void convertThing(File f, AFileResolver resolver, int newVersion) throws IOException{
        int oldVersion = new GenericFile(f).getVersion();
        if (oldVersion == newVersion) return;   // version already ok, nothing to do
        resolver.setFile(f);
        // else read with old version
        resolver.setVersion(oldVersion);
        resolver.load();
        // and save with the new version
        resolver.setVersion(newVersion);
        resolver.save();
    }
    
    
    
    //=====================================
    // ----------- CONVERTIONS ----------- 
    //=====================================
    
    
    protected void convertCharacter(File f){
        Character thing = new Character();
        int version = thing.getVersion();
        try{ version = Integer.parseInt(charField.getText());
        }catch(Exception e){}
        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
    protected void convertFrames(File f){
        FramesCollection thing = new FramesCollection();
        int version = thing.getVersion();
        try{ version = Integer.parseInt(frameField.getText());
        }catch(Exception e){}
        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
    protected void convertPallet(File f){
        Pallet thing = new Pallet();
        int version = thing.getVersion();
        try{ version = Integer.parseInt(palletField.getText());
        }catch(Exception e){}
        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
    protected void convertGoodies(File f){
        GoodiesCollection thing = new GoodiesCollection();
        int version = thing.getVersion();
        try{ version = Integer.parseInt(goodieField.getText());
        }catch(Exception e){}
        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
    protected void convertWeapons(File f){
        WeaponsCollection thing = new WeaponsCollection();
        int version = thing.getVersion();
        try{ version = Integer.parseInt(weaponField.getText());
        }catch(Exception e){}
        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
    protected void convertSound(File f){
        AudioEncoder thing = new AudioEncoder();
        int version = thing.getVersion();
        try{ version = Integer.parseInt(soundField.getText());
        }catch(Exception e){}
        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
    protected void convertMusic(File f){
        // TODO: NOT SUPORTED YET
//        Character thing = new Character();
//        int version = thing.getVersion();
//        try{ version = Integer.parseInt(charField.getText());
//        }catch(Exception e){}
//        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
    protected void convertLang(File f){
        LangEncoder thing = new LangEncoder();
        int version = thing.getVersion();
        try{ version = Integer.parseInt(langField.getText());
        }catch(Exception e){}
        try{convertThing(f,thing,version);}catch(IOException e){e.printStackTrace();}
    }
        
    public void convert(){
        int totalFiles = 0;
        if (characters!=null) totalFiles += characters.length;
        if (frames!=null) totalFiles += frames.length;
        if (pallets!=null) totalFiles += pallets.length;
        if (goodies!=null) totalFiles += goodies.length;
        if (weapons!=null) totalFiles += weapons.length;
        if (sounds!=null) totalFiles += sounds.length;
        if (musics!=null) totalFiles += musics.length;
        if (langs!=null) totalFiles += langs.length;
        
        // launch loader
        final int total = totalFiles;
        final MainGui yourParent = this;
        
        new Thread(){
            @Override
            public void run() {
                progress = new Progress(yourParent, total);
                progress.setVisible(true);
            }
        }.start();
        
    }
    
    // when the progress is ready, this message is catch
    protected void go(){     
        for(File f:characters){
            convertCharacter(f);
            progress.next();
        }
        for(File f:frames){
            convertFrames(f);
            progress.next();
        }
        for(File f:pallets){
            convertPallet(f);
            progress.next();
        }
        for(File f:goodies){
            convertGoodies(f);
            progress.next();
        }
        for(File f:weapons){
            convertWeapons(f);
            progress.next();
        }
        for(File f:sounds){
            convertSound(f);
            progress.next();
        }
        for(File f:musics){
            convertMusic(f);
            progress.next();
        }
        for(File f:langs){
            convertLang(f);
            progress.next();
        }

        progress.setVisible(false);
        progress = null;
        refresh();
        tmp1 = tmp2 = 0;
    }
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        label1 = new javax.swing.JLabel();
        frameField = new javax.swing.JTextField();
        label7 = new javax.swing.JLabel();
        charField = new javax.swing.JTextField();
        label5 = new javax.swing.JLabel();
        palletField = new javax.swing.JTextField();
        label4 = new javax.swing.JLabel();
        soundField = new javax.swing.JTextField();
        label3 = new javax.swing.JLabel();
        weaponField = new javax.swing.JTextField();
        label2 = new javax.swing.JLabel();
        goodieField = new javax.swing.JTextField();
        label6 = new javax.swing.JLabel();
        langField = new javax.swing.JTextField();
        label16 = new javax.swing.JLabel();
        musicField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        goBut = new javax.swing.JButton();
        exitBut = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        label8 = new javax.swing.JLabel();
        oldFrame = new javax.swing.JTextField();
        label9 = new javax.swing.JLabel();
        oldChar = new javax.swing.JTextField();
        label10 = new javax.swing.JLabel();
        oldPallet = new javax.swing.JTextField();
        label11 = new javax.swing.JLabel();
        oldSound = new javax.swing.JTextField();
        label12 = new javax.swing.JLabel();
        oldWeapon = new javax.swing.JTextField();
        label13 = new javax.swing.JLabel();
        oldGoodie = new javax.swing.JTextField();
        label14 = new javax.swing.JLabel();
        oldLang = new javax.swing.JTextField();
        label15 = new javax.swing.JLabel();
        oldMusic = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        refreshBut = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Versions Converter");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 0, 0), 2));

        label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label1.setText("Frames");

        frameField.setColumns(3);
        frameField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        frameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frameFieldActionPerformed(evt);
            }
        });
        frameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frameFieldFocusLost(evt);
            }
        });

        label7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label7.setText("Characters");

        charField.setColumns(3);
        charField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        charField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                charFieldActionPerformed(evt);
            }
        });
        charField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                charFieldFocusLost(evt);
            }
        });

        label5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label5.setText("Pallets");

        palletField.setColumns(3);
        palletField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        palletField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                palletFieldActionPerformed(evt);
            }
        });
        palletField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                palletFieldFocusLost(evt);
            }
        });

        label4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label4.setText("Sound");

        soundField.setColumns(3);
        soundField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        soundField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundFieldActionPerformed(evt);
            }
        });
        soundField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                soundFieldFocusLost(evt);
            }
        });

        label3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label3.setText("Weapons");

        weaponField.setColumns(3);
        weaponField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        weaponField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponFieldActionPerformed(evt);
            }
        });
        weaponField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                weaponFieldFocusLost(evt);
            }
        });

        label2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label2.setText("Goodies");

        goodieField.setColumns(3);
        goodieField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        goodieField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goodieFieldActionPerformed(evt);
            }
        });
        goodieField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                goodieFieldFocusLost(evt);
            }
        });

        label6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label6.setText("Language");

        langField.setColumns(3);
        langField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        langField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                langFieldActionPerformed(evt);
            }
        });
        langField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                langFieldFocusLost(evt);
            }
        });

        label16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label16.setText("Music");

        musicField.setColumns(3);
        musicField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        musicField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                musicFieldActionPerformed(evt);
            }
        });
        musicField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                musicFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(label5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(palletField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(label7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(charField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(frameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(langField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label3, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weaponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(goodieField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(musicField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(soundField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(goodieField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(weaponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(soundField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(musicField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label16)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(frameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(palletField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label5)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(charField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label7)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(langField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label6))))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14));
        jLabel1.setText("All Files will be updated to these versions:");

        goBut.setText("Go Ahead!");
        goBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButActionPerformed(evt);
            }
        });

        exitBut.setText("Exit");
        exitBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        label8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label8.setText("Frames");

        oldFrame.setColumns(3);
        oldFrame.setEditable(false);
        oldFrame.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        label9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label9.setText("Characters");

        oldChar.setColumns(3);
        oldChar.setEditable(false);
        oldChar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        label10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label10.setText("Pallets");

        oldPallet.setColumns(3);
        oldPallet.setEditable(false);
        oldPallet.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        label11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label11.setText("Sound");

        oldSound.setColumns(3);
        oldSound.setEditable(false);
        oldSound.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        label12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label12.setText("Weapons");

        oldWeapon.setColumns(3);
        oldWeapon.setEditable(false);
        oldWeapon.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        label13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label13.setText("Goodies");

        oldGoodie.setColumns(3);
        oldGoodie.setEditable(false);
        oldGoodie.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        label14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label14.setText("Language");

        oldLang.setColumns(3);
        oldLang.setEditable(false);
        oldLang.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        label15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label15.setText("Music");

        oldMusic.setColumns(3);
        oldMusic.setEditable(false);
        oldMusic.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oldLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                        .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oldMusic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(label10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oldPallet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(label9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oldChar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oldFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(label12, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oldWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(label13, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oldGoodie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oldSound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(33, 33, 33))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oldFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oldPallet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label10)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(oldChar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oldGoodie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oldWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oldSound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label11))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oldMusic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label15)
                    .addComponent(oldLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14));
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Old versions:");

        refreshBut.setText("Refresh");
        refreshBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("help");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem2.setText("About...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(refreshBut))
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(92, 92, 92)
                            .addComponent(goBut)
                            .addGap(35, 35, 35)
                            .addComponent(exitBut))
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(refreshBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitBut)
                    .addComponent(goBut))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void charFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_charFieldActionPerformed
        highLight(charField,oldChar, GameInvariants.CHARACTER_VERSION);
    }//GEN-LAST:event_charFieldActionPerformed

    private void frameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frameFieldActionPerformed
        highLight(frameField,oldFrame, GameInvariants.FRAMES_VERSION);
    }//GEN-LAST:event_frameFieldActionPerformed

    private void palletFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_palletFieldActionPerformed
         highLight(palletField,oldPallet, GameInvariants.PALLET_VERSION);
    }//GEN-LAST:event_palletFieldActionPerformed

    private void goodieFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goodieFieldActionPerformed
        highLight(goodieField,oldGoodie, GameInvariants.GOODIES_VERSION);
    }//GEN-LAST:event_goodieFieldActionPerformed

    private void weaponFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponFieldActionPerformed
        highLight(weaponField,oldWeapon, GameInvariants.WEAPONS_VERSION);
    }//GEN-LAST:event_weaponFieldActionPerformed

    private void soundFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundFieldActionPerformed
        highLight(soundField,oldSound, GameInvariants.SOUND_VERSION);
    }//GEN-LAST:event_soundFieldActionPerformed

    private void musicFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_musicFieldActionPerformed
        highLight(musicField,oldMusic, GameInvariants.MUSIC_VERSION);
    }//GEN-LAST:event_musicFieldActionPerformed

    private void langFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_langFieldActionPerformed
        highLight(langField,oldLang, GameInvariants.LANGUAGE_VERSION);
    }//GEN-LAST:event_langFieldActionPerformed

    private void refreshButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButActionPerformed
        refresh();
        repaint();
    }//GEN-LAST:event_refreshButActionPerformed

    private void exitButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButActionPerformed

    private void goButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButActionPerformed
        convert();
    }//GEN-LAST:event_goButActionPerformed

    private void charFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_charFieldFocusLost
        charFieldActionPerformed(null);
    }//GEN-LAST:event_charFieldFocusLost

    private void frameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frameFieldFocusLost
        frameFieldActionPerformed(null);
    }//GEN-LAST:event_frameFieldFocusLost

    private void palletFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_palletFieldFocusLost
        palletFieldActionPerformed(null);
    }//GEN-LAST:event_palletFieldFocusLost

    private void goodieFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_goodieFieldFocusLost
        goodieFieldActionPerformed(null);
    }//GEN-LAST:event_goodieFieldFocusLost

    private void weaponFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponFieldFocusLost
        weaponFieldActionPerformed(null);
    }//GEN-LAST:event_weaponFieldFocusLost

    private void soundFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_soundFieldFocusLost
        soundFieldActionPerformed(null);
    }//GEN-LAST:event_soundFieldFocusLost

    private void musicFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_musicFieldFocusLost
        musicFieldActionPerformed(null);
    }//GEN-LAST:event_musicFieldFocusLost

    private void langFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_langFieldFocusLost
        langFieldActionPerformed(null);
    }//GEN-LAST:event_langFieldFocusLost

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JOptionPane.showMessageDialog(null, ABOUT + AUTHOR,"About",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    
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
    private javax.swing.JTextField charField;
    private javax.swing.JButton exitBut;
    private javax.swing.JTextField frameField;
    private javax.swing.JButton goBut;
    private javax.swing.JTextField goodieField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label10;
    private javax.swing.JLabel label11;
    private javax.swing.JLabel label12;
    private javax.swing.JLabel label13;
    private javax.swing.JLabel label14;
    private javax.swing.JLabel label15;
    private javax.swing.JLabel label16;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label5;
    private javax.swing.JLabel label6;
    private javax.swing.JLabel label7;
    private javax.swing.JLabel label8;
    private javax.swing.JLabel label9;
    private javax.swing.JTextField langField;
    private javax.swing.JTextField musicField;
    private javax.swing.JTextField oldChar;
    private javax.swing.JTextField oldFrame;
    private javax.swing.JTextField oldGoodie;
    private javax.swing.JTextField oldLang;
    private javax.swing.JTextField oldMusic;
    private javax.swing.JTextField oldPallet;
    private javax.swing.JTextField oldSound;
    private javax.swing.JTextField oldWeapon;
    private javax.swing.JTextField palletField;
    private javax.swing.JButton refreshBut;
    private javax.swing.JTextField soundField;
    private javax.swing.JTextField weaponField;
    // End of variables declaration//GEN-END:variables
    
}
