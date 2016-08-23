/*
 * CharacterPanel.java
 *
 * Created on 05 December 2008, 10:08
 */

package gui;

import characters.Character;
import file.ACollection;
import frames.FramesCollection;
import game.GameInvariants;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author  Gil Costa
 */
public class CharacterPanel extends javax.swing.JPanel {
    
    public static final String ADD_NEW_NAME = "Write a name";
    public static final String ALREADY_EXISTS = "Already exists in this character";
    public static final String INVALID_VALUE = "Invalid value";
    
    public static final String COLLECTION = "Images Collection";
    public static final String CHOOSE_COLLECTION = "Choose an Image Collection";
    
    public static final String ADD_ORIGINAL = "Original animation number";
    public static final String ADD_REPLACEMENT = "Replacement animation number";
    public static final String ADD_WEAPON = "Weapon number";
    
    protected Character character;
    
    /** file chooser for internal images opening */
    protected JFileChooser collectionChooser;
    
    
    /** Creates new form CharacterPanel */
    public CharacterPanel() {
        initComponents();
        initCollectionChooser();
    }
    
    
    
    
    protected void initCollectionChooser(){
        collectionChooser = new JFileChooser();
        collectionChooser.setDialogTitle(CHOOSE_COLLECTION);
        collectionChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Image Collection", GameInvariants.FRAMES_EXTENSION);
        // add them
        collectionChooser.addChoosableFileFilter(fs);
        collectionChooser.setFileFilter(fs);
        collectionChooser.setMultiSelectionEnabled(true);
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

    
    
    
    
    
    
    public void setCharacter(Character c){
        if (character != null) update();
        if (c == null) character = new Character();
        else this.character = c;
        updateFields();
    }
    
    public Character getCharacter(){ return character; }
    
    public int getHeadSwapIndex(){
        return headList.getSelectedIndex();
    }
    
    public int getIconIndex(){
        return iconList.getSelectedIndex();
    }
    
    protected static String getName(String s){
        if (s==null) return null;
        return s.substring(s.indexOf(' ')+1);
    }
    
    
    protected void updateNames(){
        ListModel model = nameList.getModel();
        character.names = new ArrayList<String>(model.getSize());
        for(int i=0; i<model.getSize(); i++){
            character.names.add(getName((String)model.getElementAt(i)));
        }
    }
    
    protected void updateAnims(){
        ListModel model = animList.getModel();
        character.anims = new ArrayList<String>(model.getSize());
        for(int i=0; i<model.getSize(); i++){
            character.anims.add(getName((String)model.getElementAt(i)));
        }
    }
    
    protected void updateHeads(){
        ListModel model = headList.getModel();
        character.heads = new ArrayList<String>(model.getSize());
        for(int i=0; i<model.getSize(); i++){
            character.heads.add(getName((String)model.getElementAt(i)));
        }
    }
    
    protected void updateIcons(){
        ListModel model = iconList.getModel();
        character.icons = new ArrayList<String>(model.getSize());
        for(int i=0; i<model.getSize(); i++){
            character.icons.add(getName((String)model.getElementAt(i)));
        }
    }
    
    protected void updateReplaces(){
        ListModel model = replaceList.getModel();
        character.replacements = new ArrayList<String>(model.getSize());
        for(int i=0; i<model.getSize(); i++){
            character.replacements.add((String)model.getElementAt(i));
        }
    }
    
    
    protected void updateOptions(){
        try{ character.walkVel.set(Integer.parseInt(walkField.getText())); }
        catch (Exception e){}
        try{ character.jumpVel.set(Integer.parseInt(jumpField.getText())); }
        catch (Exception e){}
        try{ character.runJumpVel.set(Integer.parseInt(runJumpField.getText())); }
        catch (Exception e){}
        //try{ character.gravity.set((int)(Double.parseDouble(gravityField.getText())*100)); }
        //catch (Exception e){}
        character.autoPallete = autoPalleteBox.isSelected();
    }
    
    
    public void update(){
        updateNames();
        updateAnims();
        updateHeads();
        updateIcons();
        updateReplaces();
        updateOptions();
    }
    
 
    public void initNameList(){
        int index = nameList.getSelectedIndex();
        nameList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:character.names)
            listModel.addElement("[" + i++ + "] " + s);
        nameList.setModel(listModel);
        if (index>=0 && index<=listModel.size()){
            nameList.setSelectedIndex(index);
        } else nameList.setSelectedIndex(0);
    }
    
    
    public void initAnimList(){
        int index = animList.getSelectedIndex();
        animList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:character.anims)
            listModel.addElement("[" + i++ + "] " + s);
        animList.setModel(listModel);
        if (index>=0 && index<=listModel.size()){
            animList.setSelectedIndex(index);
        } else animList.setSelectedIndex(0);
    }
    
    
    public void initHeadList(){
        int index = headList.getSelectedIndex();
        headList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:character.heads)
            listModel.addElement("[" + i++ + "] " + s);
        headList.setModel(listModel);
        if (index>=0 && index<=listModel.size()){
            headList.setSelectedIndex(index);
        } else headList.setSelectedIndex(0);
    }
    
    
    public void initIconList(){
        int index = iconList.getSelectedIndex();
        iconList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:character.icons)
            listModel.addElement("[" + i++ + "] " + s);
        iconList.setModel(listModel);
        if (index>=0 && index<=listModel.size()){
            iconList.setSelectedIndex(index);
        } else iconList.setSelectedIndex(0);
    }
    
    public void initReplaceList(){
        int index = replaceList.getSelectedIndex();
        replaceList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:character.replacements)
            listModel.addElement(s);
        replaceList.setModel(listModel);
        if (index>=0 && index<=listModel.size()){
            replaceList.setSelectedIndex(index);
        } else replaceList.setSelectedIndex(0);
    }
    
    
    protected void initOptions(){
        autoPalleteBox.setSelected(character.autoPallete);
        walkField.setText(character.walkVel.get() + "");
        jumpField.setText(character.jumpVel.get() + "");
        runJumpField.setText(character.runJumpVel.get() + "");
        //gravityField.setText((character.gravity.get()/100.) + "");
    }
    
    public void updateFields(){
        initNameList();
        initAnimList();
        initHeadList();
        initIconList();
        initReplaceList();
        initOptions();
    }
    
    
    
    
    public String getSelectedName(){
        if (nameList.getModel().getSize()!=0 && nameList.getSelectedIndex()==-1)
            nameList.setSelectedIndex(0);
        return getName((String) nameList.getSelectedValue());
    }
    public String getSelectedAnim(){
        if (animList.getModel().getSize()!=0 && animList.getSelectedIndex()==-1)
            animList.setSelectedIndex(0);
        return getName((String) animList.getSelectedValue());
    }
    public String getSelectedHead(){
        if (headList.getModel().getSize()!=0 && headList.getSelectedIndex()==-1)
            headList.setSelectedIndex(0);
        return getName((String) headList.getSelectedValue());
    }
    
    public String getSelectedIcon(){
        if (iconList.getModel().getSize()!=0 && iconList.getSelectedIndex()==-1)
            iconList.setSelectedIndex(0);
        return getName((String) iconList.getSelectedValue());
    }
    public String getSelectedReplacement(){
        if (replaceList.getModel().getSize()!=0 && replaceList.getSelectedIndex()==-1)
            replaceList.setSelectedIndex(0);
        return (String) replaceList.getSelectedValue();
    }
    
    
    public boolean hasHeads(){
        return character.hasHeadSwaps();
    }
    
    public boolean hasIcons(){
        return character.hasIcons();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        headList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        animList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        addHead = new javax.swing.JButton();
        addAnim = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        nameList = new javax.swing.JList();
        addName = new javax.swing.JButton();
        autoPalleteBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        up1But = new javax.swing.JButton();
        down1But = new javax.swing.JButton();
        up2But = new javax.swing.JButton();
        down2But = new javax.swing.JButton();
        up3But = new javax.swing.JButton();
        down3But = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        iconList = new javax.swing.JList();
        addIcon = new javax.swing.JButton();
        up4But = new javax.swing.JButton();
        down4But = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        replaceList = new javax.swing.JList();
        addReplace = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        walkField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jumpField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        runJumpField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();

        headList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        headList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                headListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(headList);

        jLabel1.setText("Head Swaps");

        animList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        animList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                animListKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(animList);

        jLabel2.setText("Bodies");

        addHead.setText(" add ");
        addHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHeadActionPerformed(evt);
            }
        });

        addAnim.setText(" add ");
        addAnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAnimActionPerformed(evt);
            }
        });

        jLabel4.setText("Names");

        nameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        nameList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameListMouseClicked(evt);
            }
        });
        nameList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameListKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameListKeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(nameList);

        addName.setText(" add ");
        addName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNameActionPerformed(evt);
            }
        });

        autoPalleteBox.setText("force pallete-head swap");
        autoPalleteBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoPalleteBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("with the pallete #N");

        jLabel5.setText("head #N is associated");

        up1But.setText("   up  ");
        up1But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                up1ButActionPerformed(evt);
            }
        });

        down1But.setText("down");
        down1But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                down1ButActionPerformed(evt);
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

        up3But.setText("   up  ");
        up3But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                up3ButActionPerformed(evt);
            }
        });

        down3But.setText("down");
        down3But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                down3ButActionPerformed(evt);
            }
        });

        jLabel6.setText("Icons");

        iconList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        iconList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                iconListKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(iconList);

        addIcon.setText(" add ");
        addIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addIconActionPerformed(evt);
            }
        });

        up4But.setText("   up  ");
        up4But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                up4ButActionPerformed(evt);
            }
        });

        down4But.setText("down");
        down4But.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                down4ButActionPerformed(evt);
            }
        });

        jLabel7.setText("Icons are associated directly with the bodies");

        jLabel8.setText("or, if no direct related body, associated with heads");

        jLabel9.setText("Animation replacements by using weapons");

        replaceList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        replaceList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                replaceListMouseClicked(evt);
            }
        });
        replaceList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                replaceListKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                replaceListKeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(replaceList);

        addReplace.setText(" add ");
        addReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addReplaceActionPerformed(evt);
            }
        });

        jLabel10.setText("Format:");

        jLabel11.setText("Original   Replacement   Weapon");

        jLabel12.setText("walk velocity");

        walkField.setColumns(3);
        walkField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        walkField.setText("0");

        jLabel13.setText("jump velocity");

        jumpField.setColumns(3);
        jumpField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jumpField.setText("0");

        jLabel16.setText("standard: 48");

        jLabel17.setText("default: 5");

        jLabel18.setText("default: 4");

        jLabel19.setText("run jump velocity");

        runJumpField.setColumns(3);
        runJumpField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        runJumpField.setText("0");

        jLabel20.setText("default: 8");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(down1But)
                                            .addComponent(up1But)
                                            .addComponent(addName))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(59, 59, 59)
                                        .addComponent(jLabel2))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(up2But)
                                                .addComponent(down2But))
                                            .addComponent(addAnim))))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel1)
                                        .addGap(57, 57, 57)
                                        .addComponent(jLabel6))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(addHead)
                                                    .addComponent(up3But)
                                                    .addComponent(down3But))))
                                        .addGap(15, 15, 15)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(addIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(up4But, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(down4But))))
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel11))
                                            .addComponent(jLabel10))))
                                .addGap(755, 755, 755)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(addReplace))
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(walkField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jumpField)
                                    .addComponent(runJumpField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel20))))
                        .addGap(128, 128, 128)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autoPalleteBox)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel6)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(40, 40, 40)
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(7, 7, 7)
                                    .addComponent(addHead)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(up3But)
                                    .addGap(1, 1, 1)
                                    .addComponent(down3But)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(down4But, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(up4But, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(addName)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(up1But)
                                        .addGap(2, 2, 2)
                                        .addComponent(down1But))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(addAnim)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(up2But)
                                        .addGap(1, 1, 1)
                                        .addComponent(down2But))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(addReplace)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(walkField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jumpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(runJumpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(autoPalleteBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNameActionPerformed
        String name = JOptionPane.showInputDialog(ADD_NEW_NAME);
        if (name == null) return;
        if (character.nameExists(name)){
            JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        }else{
            character.addName(name);
            initNameList();
        }
}//GEN-LAST:event_addNameActionPerformed

    private void addAnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAnimActionPerformed
        String[] names = openCollection();
        if (names == null) return;
        boolean already = true;
        for(String name:names){
            if (name == null) return;
            if (!character.animExists(name)){
                already = false;
                character.addAnimation(name);
            }
        }
        if (already) JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        else initAnimList();
    }//GEN-LAST:event_addAnimActionPerformed

    private void addHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addHeadActionPerformed
        String[] names = openCollection();
        if (names == null) return;
        boolean already = true;
        for(String name:names){
            if (name == null) return;
            if (!character.headExists(name)){
                already = false;
                character.addHeadSwap(name);
            }
        }
        if (already) JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        else initHeadList();
    }//GEN-LAST:event_addHeadActionPerformed

    private void nameListKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameListKeyTyped
        
    }//GEN-LAST:event_nameListKeyTyped

    private void nameListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = nameList.getSelectedIndex();
            if (index != -1)
                //((DefaultListModel)nameList.getModel()).remove(index);
                character.names.remove(index);
                initNameList();
        }
    }//GEN-LAST:event_nameListKeyPressed

    private void animListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_animListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = animList.getSelectedIndex();
            if (index != -1){
                //((DefaultListModel)animList.getModel()).remove(index);
                character.anims.remove(index);
                initAnimList();
            }
        }
    }//GEN-LAST:event_animListKeyPressed

    private void headListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_headListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = headList.getSelectedIndex();
            if (index != -1){
                //((DefaultListModel)headList.getModel()).remove(index);
                character.heads.remove(index);
                initHeadList();
            }
        }
    }//GEN-LAST:event_headListKeyPressed

    private void nameListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameListMouseClicked
        if (evt.getClickCount() == 2){
            String name = JOptionPane.showInputDialog(ADD_NEW_NAME,getSelectedName());
            if (name == null) return;
            if (character.nameExists(name) ){
                JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
            }else{
                character.setName(nameList.getSelectedIndex(), name);
                initNameList();
            }
        }
    }//GEN-LAST:event_nameListMouseClicked

    private void autoPalleteBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoPalleteBoxActionPerformed
        character.autoPallete = autoPalleteBox.isSelected();
}//GEN-LAST:event_autoPalleteBoxActionPerformed

    private void up1ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up1ButActionPerformed
        int index = nameList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = character.getName(index-1);
            character.setName(index-1, character.getName(index));
            character.setName(index, tmp);
            initNameList();
            nameList.setSelectedIndex(index-1);
        }
    }//GEN-LAST:event_up1ButActionPerformed

    private void up2ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up2ButActionPerformed
        int index = animList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = character.getAnimName(index-1);
            character.setAnimName(index-1, character.getAnimName(index));
            character.setAnimName(index, tmp);
            initAnimList();
            animList.setSelectedIndex(index-1);
        }
    }//GEN-LAST:event_up2ButActionPerformed

    private void up3ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up3ButActionPerformed
        int index = headList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = character.getHeadName(index-1);
            character.setHeadName(index-1, character.getHeadName(index));
            character.setHeadName(index, tmp);
            initHeadList();
            headList.setSelectedIndex(index-1);
        }
    }//GEN-LAST:event_up3ButActionPerformed

    private void down1ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down1ButActionPerformed
        int index = nameList.getSelectedIndex();
        if (index >= 0 && index < character.names.size()-1){
            // do the swap
            String tmp = character.getName(index+1);
            character.setName(index+1, character.getName(index));
            character.setName(index, tmp);
            initNameList();
            nameList.setSelectedIndex(index+1);
        }
    }//GEN-LAST:event_down1ButActionPerformed

    private void down2ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down2ButActionPerformed
        int index = animList.getSelectedIndex();
        if (index >= 0 && index < character.anims.size()-1){
            // do the swap
            String tmp = character.getAnimName(index+1);
            character.setAnimName(index+1, character.getAnimName(index));
            character.setAnimName(index, tmp);
            initAnimList();
            animList.setSelectedIndex(index+1);
        }
    }//GEN-LAST:event_down2ButActionPerformed

    private void down3ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down3ButActionPerformed
        int index = headList.getSelectedIndex();
        if (index >= 0 && index < character.heads.size()-1){
            // do the swap
            String tmp = character.getHeadName(index+1);
            character.setHeadName(index+1, character.getHeadName(index));
            character.setHeadName(index, tmp);
            initHeadList();
            headList.setSelectedIndex(index+1);
        }
    }//GEN-LAST:event_down3ButActionPerformed

    private void iconListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_iconListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = iconList.getSelectedIndex();
            if (index != -1){
                //((DefaultListModel)headList.getModel()).remove(index);
                character.icons.remove(index);
                initIconList();
            }
        }
}//GEN-LAST:event_iconListKeyPressed

    private void addIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIconActionPerformed
        String[] names = openCollection();
        if (names == null) return;
        boolean already = true;
        for(String name:names){
            if (name == null) return;
            if (!character.iconExists(name)){
                already = false;
                character.addIcon(name);
            }
        }
        if (already) JOptionPane.showMessageDialog(this, ALREADY_EXISTS);
        else initIconList();
}//GEN-LAST:event_addIconActionPerformed

    private void up4ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_up4ButActionPerformed
        int index = iconList.getSelectedIndex();
        if (index >0){
            // do the swap
            String tmp = character.getIconName(index-1);
            character.setIconName(index-1, character.getIconName(index));
            character.setIconName(index, tmp);
            initIconList();
            iconList.setSelectedIndex(index-1);
        }
}//GEN-LAST:event_up4ButActionPerformed

    private void down4ButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_down4ButActionPerformed
        int index = iconList.getSelectedIndex();
        if (index >= 0 && index < character.icons.size()-1){
            // do the swap
            String tmp = character.getIconName(index+1);
            character.setIconName(index+1, character.getIconName(index));
            character.setIconName(index, tmp);
            initIconList();
            iconList.setSelectedIndex(index+1);
        }
}//GEN-LAST:event_down4ButActionPerformed

    private void replaceListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_replaceListMouseClicked
        if (evt.getClickCount() == 2){
            try{
                int ori = Integer.parseInt(JOptionPane.showInputDialog(ADD_ORIGINAL));
                int rep = Integer.parseInt(JOptionPane.showInputDialog(ADD_REPLACEMENT));
                int weap = Integer.parseInt(JOptionPane.showInputDialog(ADD_WEAPON));
                if (ori<0 || rep<0 || weap<0 || ori>255 || rep>255 || weap>255){
                    JOptionPane.showMessageDialog(this, INVALID_VALUE);
                    return;
                }

                String s = ori + "  " + rep + "  " + weap;
                int index = replaceList.getSelectedIndex();
                if (index>=0)
                    character.setReplacement(index, s);
                else character.addReplacement(s);
                initReplaceList();
            }catch(Exception e){ JOptionPane.showMessageDialog(this, INVALID_VALUE); }
        }
}//GEN-LAST:event_replaceListMouseClicked

    private void replaceListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_replaceListKeyPressed
        if( evt.getKeyCode() == KeyEvent.VK_DELETE){
            int index = replaceList.getSelectedIndex();
            if (index != -1){
                //((DefaultListModel)nameList.getModel()).remove(index);
                character.replacements.remove(index);
                initReplaceList();
            }
        }
}//GEN-LAST:event_replaceListKeyPressed

    private void replaceListKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_replaceListKeyTyped
        // TODO add your handling code here:
}//GEN-LAST:event_replaceListKeyTyped

    private void addReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addReplaceActionPerformed
        try{
            int ori = Integer.parseInt(JOptionPane.showInputDialog(ADD_ORIGINAL));
            int rep = Integer.parseInt(JOptionPane.showInputDialog(ADD_REPLACEMENT));
            int weap = Integer.parseInt(JOptionPane.showInputDialog(ADD_WEAPON));
            if (ori<0 || rep<0 || weap<0 || ori>255 || rep>255 || weap>255){
                JOptionPane.showMessageDialog(this, INVALID_VALUE);
                return;
            }
            
            String s = ori + "  " + rep + "  " + weap;
            character.addReplacement(s);
            initReplaceList();
        }catch(Exception e){ JOptionPane.showMessageDialog(this, INVALID_VALUE); }
}//GEN-LAST:event_addReplaceActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAnim;
    private javax.swing.JButton addHead;
    private javax.swing.JButton addIcon;
    private javax.swing.JButton addName;
    private javax.swing.JButton addReplace;
    private javax.swing.JList animList;
    private javax.swing.JCheckBox autoPalleteBox;
    private javax.swing.JButton down1But;
    private javax.swing.JButton down2But;
    private javax.swing.JButton down3But;
    private javax.swing.JButton down4But;
    private javax.swing.JList headList;
    private javax.swing.JList iconList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jumpField;
    private javax.swing.JList nameList;
    private javax.swing.JList replaceList;
    private javax.swing.JTextField runJumpField;
    private javax.swing.JButton up1But;
    private javax.swing.JButton up2But;
    private javax.swing.JButton up3But;
    private javax.swing.JButton up4But;
    private javax.swing.JTextField walkField;
    // End of variables declaration//GEN-END:variables
    
}
