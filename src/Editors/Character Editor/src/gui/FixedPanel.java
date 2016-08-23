/*
 * MassCenterPanel.java
 *
 * Created on 27 November 2008, 10:11
 */

package gui;

import characters.FixedFrame;
import characters.GrabPoint;
import characters.WeaponPoint;
import characters.Character;
import file.CorruptionException;
import frames.FramesCollection;
import game.GameInvariants;
import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import pallet.Pallet;
import util.ICoordinatesTaker;
import util.UByte;
import weapons.WeaponsCollection;


/**
 *
 * @author  Gil Costa
 * TODO: histograms to compute lines and collumns
 */
public class FixedPanel extends javax.swing.JPanel implements ICoordinatesTaker{
    
    //public static final String POINT_STRING = "point";

    public static final String CANT_OPEN_PALLETE = "Unable to open this pallete";
    
    protected FixedFrame fixedFrame;
    
    /** takes the selected point */
    FixedActionFramePanel taker;
    
    protected JFileChooser characterChooser;
    protected JFileChooser weaponChooser;
    protected JFileChooser palletChooser;
    
    
    protected int selectedField;
    
    
    public boolean previewing;
    
    
    /** Creates new form MassCenterPanel */
    public FixedPanel() {
        initComponents();
        initCharacterChooser();
        initWeaponsChooser();
        initPalleteChooser();
        fixedFrame = null;
        setAllEnabled(false);
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
        Character character = new Character();
        FramesCollection fc = new FramesCollection();
        try {
            character.load(file);
            File f2 = new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" + character.getFramesCollection(0));
            fc.load(f2);
        } catch (IOException e) {
            character = null; fc = null;
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,MainGui.ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, MainGui.CANT_OPEN_CHARACTER,MainGui.ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
        }
        taker.setEnemy(character,fc);
        taker.repaint();
    }
    
    
    
    
    
    
    protected void initWeaponsChooser(){
        weaponChooser = new JFileChooser();
        weaponChooser.setDialogTitle("Choose a Weapons Set");
        weaponChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.WEAPONS_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Weapons Set", game.GameInvariants.WEAPONS_EXTENSION);
        // add them
        weaponChooser.addChoosableFileFilter(fs);
        weaponChooser.setFileFilter(fs);
        weaponChooser.setMultiSelectionEnabled(false);
    }
    
    
    
    
    protected void openWeapons(){
        int returnVal = weaponChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = weaponChooser.getSelectedFile();
            if (file != null)
                acceptWeapons(file);
        }
    }

    
    
    
    protected void acceptWeapons(File file){
        WeaponsCollection weapons = new WeaponsCollection();
        FramesCollection fc = new FramesCollection();
        try {
            weapons.load(file);
            fc.load(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.ANIMATIONS_DIR + "\\" + weapons.frames.get(0)));
        } catch (IOException e) {
            fc = null;
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,MainGui.ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, "Can't open weapons set", MainGui.ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
        }
        taker.setWeapons(fc);
        taker.repaint();
    }
    
    
    
    protected void initPalleteChooser(){
        palletChooser = new JFileChooser();
        palletChooser.setDialogTitle("Choose a pallete");
        palletChooser.setCurrentDirectory(new File(GameInvariants.EDITORS_WORKING_DIR + GameInvariants.PALLETS_DIR));
        // create filters
        javax.swing.filechooser.FileFilter fs;
        fs = new FileNameExtensionFilter("Pallete", GameInvariants.PALLET_EXTENSION);
        // add them
        palletChooser.addChoosableFileFilter(fs);
        palletChooser.setFileFilter(fs);
        palletChooser.setMultiSelectionEnabled(false);
    }
    
    protected void openPallet(){
        int returnVal = palletChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = palletChooser.getSelectedFile();
            if (file != null)
                acceptPallet(file);
        }
    }
    
    public void acceptPallet(File file){
        Pallet pll = new Pallet();
        try {
            pll.load(file);
        } catch (IOException e) {
            pll = null;
            if (e instanceof CorruptionException)
                JOptionPane.showMessageDialog(null, CorruptionException.CORRUPTED_FILE,MainGui.ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, CANT_OPEN_PALLETE,MainGui.ERROR_MESSAGE,JOptionPane.ERROR_MESSAGE);
            return;
        }
        taker.setPallet(pll);
    }
    
    
    
    
    public boolean hasAutoPallet(){
        return !palletBut.isEnabled();
    }
    
    
    public void takeCoordinates(int x, int y, int index) {
        //int cx = taker.getFrame().getCM().getX();
        //int cy = taker.getFrame().getCM().getY();
        switch (index){
            case 0:
                fixedFrame.head.x = x;
                fixedFrame.head.y = y;
                break;
            case 1:
                fixedFrame.weapon.x = x;
                fixedFrame.weapon.y = y;
                break;
            case 2:
                fixedFrame.grab.baseX = x;
                fixedFrame.grab.baseY = y;
                break;
        }
        setFixedFrame(fixedFrame);
    }
    
    //---------------------
    // ----- SETTERS -----
    //---------------------
    
    
    public void setUpdatable(FixedActionFramePanel taker){
        this.taker = taker;
    }
    
    
    public void setAllEnabled(boolean enabled){
        if (enabled && fixedFrame == null )
            setAllEnabled(false);
        this.setEnabled(enabled);
        weaponBox.setEnabled(enabled && fixedFrame != null && fixedFrame.weapon!=null);
        if (!enabled || !weaponBox.isEnabled() ||!weaponBox.isSelected())
            setWeaponEnabled(false);
        else setWeaponEnabled(true);
        
        grabBox.setEnabled(enabled && fixedFrame != null && fixedFrame.grab!=null);
        if (!enabled || !grabBox.isEnabled() || !grabBox.isSelected())
            setGrabEnabled(false);
        else setGrabEnabled(true);
        setHeadEnabled(enabled && fixedFrame.head!=null);
        delayField.setEnabled(enabled);
        showBox.setEnabled(enabled);
    }
    
    
    protected void setHeadEnabled(boolean enabled){
        headXField.setEnabled(enabled);
        headYField.setEnabled(enabled);
        headFrameField.setEnabled(enabled);
    }
    
    protected void setWeaponEnabled(boolean enabled){
        weaponXField.setEnabled(enabled);
        weaponYField.setEnabled(enabled);
        weaponAngleField.setEnabled(enabled);
        weaponFrontBox.setEnabled(enabled);
        weaponField.setEnabled(enabled);
    }
    
    protected void setGrabEnabled(boolean enabled){
        grabXField.setEnabled(enabled);
        grabYField.setEnabled(enabled);
        grabAnimationField.setEnabled(enabled);
        grabAngleField.setEnabled(enabled);
        grabFlipBox.setEnabled(enabled);
    }
    
    


    public void reset(){
        fixedFrame = null;
    }
    
    public void update(){
        selectFocusedField();
        if (this.fixedFrame == null) return;
        updateDelay();
        if(fixedFrame.head != null)
            updateHead();
        if (fixedFrame.weapon != null)
            updateWeapon();
        if (fixedFrame.grab != null)
            updateGrab();
        if (taker != null)
            taker.setFixedFrame(fixedFrame);
        taker.showActionPoints(showBox.isSelected());
            
    }
    
    
    public void updateDelay(){
         try{
            fixedFrame.duration = new UByte(Integer.parseInt(delayField.getText()));
        }catch (NumberFormatException e){ }
    }
    
    public void updateHead(){
        try{
            fixedFrame.head.x = Integer.parseInt(headXField.getText());
            fixedFrame.head.y = Integer.parseInt(headYField.getText());
            fixedFrame.head.frame = new UByte(Integer.parseInt(headFrameField.getText()));
        }catch (NumberFormatException e){ }
    }
    
    public void updateWeapon(){
        if (weaponBox.isSelected()){
            try{
                fixedFrame.weapon.x = Integer.parseInt(weaponXField.getText());
                fixedFrame.weapon.y = Integer.parseInt(weaponYField.getText());
                fixedFrame.weapon.angle = new UByte((int)Math.round(255./360*(Integer.parseInt(weaponAngleField.getText())%360)));
                fixedFrame.weapon.inFront = weaponFrontBox.isSelected();
            }catch (NumberFormatException e){ }
        }else
            fixedFrame.weapon.x = WeaponPoint.INVALID_WEAPON_POINT;
    }
    
    
    public void updateGrab(){
        if (grabBox.isSelected()){
            try{
                fixedFrame.grab.baseX = Integer.parseInt(grabXField.getText());
                fixedFrame.grab.baseY = Integer.parseInt(grabYField.getText());
                fixedFrame.grab.anim = new UByte(Integer.parseInt(grabAnimationField.getText()));
                fixedFrame.grab.angle = new UByte((int)Math.round(255./360*(Integer.parseInt(grabAngleField.getText())%360)));
            }catch (NumberFormatException e){ }
        }else
            fixedFrame.grab.baseX = GrabPoint.INVALID_GRAB_POINT;
    }
    
    
    
    public void setAutoPallet(boolean auto){
        palletBut.setEnabled(!auto);
    }
    
    
    public void setFixedFrame(FixedFrame frame){
        if (this.fixedFrame != frame){
            update();
            this.fixedFrame = frame;
        }
        if (frame == null){
            setAllEnabled(false);
        }else{
            delayField.setText(frame.duration.get()+"");
            if (!previewing){
                
            }
            if (frame.head!=null){
                headXField.setText(frame.head.x+"");
                headYField.setText(frame.head.y+"");
                headFrameField.setText(frame.head.frame.get()+"");
            }
            if (frame.weapon!=null){
                if (frame.weapon.x == WeaponPoint.INVALID_WEAPON_POINT)
                    weaponBox.setSelected(false);
                else{
                    weaponBox.setSelected(true);
                    weaponXField.setText(frame.weapon.x+"");
                    weaponYField.setText(frame.weapon.y+"");
                    weaponFrontBox.setSelected(frame.weapon.inFront);
                    int angle = (int)Math.round(frame.weapon.angle.get()*360./255);
                    slider1.setValue(angle);
                    weaponAngleField.setText(angle+"");
                    
                }
            }
            if (frame.grab!=null){
                if (frame.grab.baseX == GrabPoint.INVALID_GRAB_POINT)
                    grabBox.setSelected(false);
                else{
                    grabBox.setSelected(true);
                    grabXField.setText(frame.grab.baseX+"");
                    grabYField.setText(frame.grab.baseY+"");
                    grabAnimationField.setText(frame.grab.anim.get()+"");
                    int angle = (int)Math.round(frame.grab.angle.get()*360./255);
                    slider2.setValue(angle);
                    grabAngleField.setText(angle+"");
                }
            }
            setAllEnabled(true);
        }
        focusSelectedField();
        taker.showActionPoints(showBox.isSelected());
        repaint();
    }
    
    
    
    
    //-------------------
    // ---- GETTERS ----
    //-------------------
    
    
     @Override
    public boolean hasFocus(){
        return 
            delayField.hasFocus()
            || headXField.hasFocus() || headYField.hasFocus() || headFrameField.hasFocus()
            || weaponXField.hasFocus() || weaponYField.hasFocus() || weaponAngleField.hasFocus()
            || grabXField.hasFocus() || grabYField.hasFocus() || grabAngleField.hasFocus()
            || grabAnimationField.hasFocus()   
            || weaponField.hasFocus()
        ;
    }
     
    
    
    protected void updateSlider1(){
        int angle = (int)Math.round(fixedFrame.weapon.angle.get()*360./255);
        slider1.setValue(angle);
    }
    
    protected void updateSlider2(){
        int angle = (int)Math.round(fixedFrame.grab.angle.get()*360./255);
        slider2.setValue(angle);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        headXField = new javax.swing.JTextField();
        headYField = new javax.swing.JTextField();
        headFrameField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        weaponXField = new javax.swing.JTextField();
        weaponYField = new javax.swing.JTextField();
        weaponAngleField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        weaponBox = new javax.swing.JCheckBox();
        weponBut = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        weaponField = new javax.swing.JTextField();
        slider1 = new javax.swing.JSlider();
        weaponFrontBox = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        grabXField = new javax.swing.JTextField();
        grabYField = new javax.swing.JTextField();
        grabAnimationField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        grabBox = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        grabAngleField = new javax.swing.JTextField();
        weponBut1 = new javax.swing.JButton();
        grabFlipBox = new javax.swing.JCheckBox();
        slider2 = new javax.swing.JSlider();
        jLabel23 = new javax.swing.JLabel();
        delayField = new javax.swing.JTextField();
        showBox = new javax.swing.JCheckBox();
        palletBut = new javax.swing.JButton();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        headXField.setColumns(3);
        headXField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        headXField.setText("1");
        headXField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headXFieldActionPerformed(evt);
            }
        });
        headXField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                headXFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                headXFieldFocusLost(evt);
            }
        });

        headYField.setColumns(3);
        headYField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        headYField.setText("1");
        headYField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headYFieldActionPerformed(evt);
            }
        });
        headYField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                headYFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                headYFieldFocusLost(evt);
            }
        });

        headFrameField.setColumns(3);
        headFrameField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        headFrameField.setText("1");
        headFrameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headFrameFieldActionPerformed(evt);
            }
        });
        headFrameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                headFrameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                headFrameFieldFocusLost(evt);
            }
        });

        jLabel13.setText("x:");

        jLabel14.setText("y:");

        jLabel15.setText("frame:");

        jLabel1.setText("Head Point");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(headFrameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(headXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(headYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(headXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(headYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headFrameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        weaponXField.setColumns(3);
        weaponXField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        weaponXField.setText("1");
        weaponXField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponXFieldActionPerformed(evt);
            }
        });
        weaponXField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weaponXFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weaponXFieldFocusLost(evt);
            }
        });

        weaponYField.setColumns(3);
        weaponYField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        weaponYField.setText("1");
        weaponYField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponYFieldActionPerformed(evt);
            }
        });
        weaponYField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weaponYFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weaponYFieldFocusLost(evt);
            }
        });

        weaponAngleField.setColumns(3);
        weaponAngleField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        weaponAngleField.setText("1");
        weaponAngleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponAngleFieldActionPerformed(evt);
            }
        });
        weaponAngleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weaponAngleFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weaponAngleFieldFocusLost(evt);
            }
        });

        jLabel16.setText("x:");

        jLabel17.setText("y:");

        jLabel18.setText("angle:");

        weaponBox.setText("Weapon Point");
        weaponBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponBoxActionPerformed(evt);
            }
        });

        weponBut.setText("Open Weapons");
        weponBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weponButActionPerformed(evt);
            }
        });

        jLabel2.setText("weapon #");

        weaponField.setColumns(3);
        weaponField.setText("0");
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

        slider1.setMajorTickSpacing(180);
        slider1.setMaximum(360);
        slider1.setMinorTickSpacing(90);
        slider1.setPaintLabels(true);
        slider1.setPaintTicks(true);
        slider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slider1StateChanged(evt);
            }
        });

        weaponFrontBox.setText("In front");
        weaponFrontBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponFrontBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(weaponBox)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(weaponXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(weaponYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weaponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(weponBut)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(weaponFrontBox)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(weaponAngleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(slider1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(weaponBox, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(weaponXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(weaponYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(weaponFrontBox, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(weaponAngleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(slider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weponBut)
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weaponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(6, 6, 6))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        grabXField.setColumns(3);
        grabXField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grabXField.setText("1");
        grabXField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabXFieldActionPerformed(evt);
            }
        });
        grabXField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                grabXFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                grabXFieldFocusLost(evt);
            }
        });

        grabYField.setColumns(3);
        grabYField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grabYField.setText("1");
        grabYField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabYFieldActionPerformed(evt);
            }
        });
        grabYField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                grabYFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                grabYFieldFocusLost(evt);
            }
        });

        grabAnimationField.setColumns(3);
        grabAnimationField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grabAnimationField.setText("1");
        grabAnimationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabAnimationFieldActionPerformed(evt);
            }
        });
        grabAnimationField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                grabAnimationFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                grabAnimationFieldFocusLost(evt);
            }
        });

        jLabel19.setText("x:");

        jLabel20.setText("y:");

        jLabel21.setText("animation:");

        grabBox.setText("Grabbing Point");
        grabBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabBoxActionPerformed(evt);
            }
        });

        jLabel22.setText("angle:");

        grabAngleField.setColumns(3);
        grabAngleField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grabAngleField.setText("90");
        grabAngleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabAngleFieldActionPerformed(evt);
            }
        });
        grabAngleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                grabAngleFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                grabAngleFieldFocusLost(evt);
            }
        });

        weponBut1.setText("Open Enemy");
        weponBut1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weponBut1ActionPerformed(evt);
            }
        });

        grabFlipBox.setText("Fliped");
        grabFlipBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabFlipBoxActionPerformed(evt);
            }
        });

        slider2.setMajorTickSpacing(180);
        slider2.setMaximum(360);
        slider2.setMinorTickSpacing(90);
        slider2.setPaintLabels(true);
        slider2.setPaintTicks(true);
        slider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slider2StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(grabBox)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(grabXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(grabYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(grabAnimationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(grabAngleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(weponBut1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(grabFlipBox))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(slider2, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grabBox, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(grabXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(grabYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(grabAnimationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(grabAngleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(slider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(weponBut1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(grabFlipBox)
                .addContainerGap())
        );

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel23.setText("Delay:");

        delayField.setColumns(3);
        delayField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        delayField.setText("8");
        delayField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delayFieldActionPerformed(evt);
            }
        });
        delayField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                delayFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                delayFieldFocusLost(evt);
            }
        });

        showBox.setSelected(true);
        showBox.setText("show points");
        showBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showBoxActionPerformed(evt);
            }
        });

        palletBut.setText("Open Pallet");
        palletBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                palletButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(palletBut)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(showBox)
                        .addContainerGap(312, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(delayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(palletBut))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void headXFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_impulseXFieldActionPerformed
        headYField.requestFocus();
}//GEN-LAST:event_impulseXFieldActionPerformed

    private void headXFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseXFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_impulseXFieldFocusGained

    private void headXFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseXFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_impulseXFieldFocusLost

    private void headYFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headYFieldActionPerformed
        headFrameField.requestFocus();
}//GEN-LAST:event_headYFieldActionPerformed

    private void headYFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_headYFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_headYFieldFocusGained

    private void headYFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_headYFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_headYFieldFocusLost

    private void headFrameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_impulseZFieldActionPerformed
        requestFocus();
}//GEN-LAST:event_impulseZFieldActionPerformed

    private void headFrameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_headFrameFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_headFrameFieldFocusGained

    private void headFrameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_headFrameFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_headFrameFieldFocusLost

    private void weaponXFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponXFieldActionPerformed
        weaponYField.requestFocus();
}//GEN-LAST:event_weaponXFieldActionPerformed

    private void weaponXFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponXFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_weaponXFieldFocusGained

    private void weaponXFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponXFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_weaponXFieldFocusLost

    private void weaponYFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponYFieldActionPerformed
        weaponAngleField.requestFocus();
}//GEN-LAST:event_weaponYFieldActionPerformed

    private void weaponYFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponYFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_weaponYFieldFocusGained

    private void weaponYFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponYFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_weaponYFieldFocusLost

    private void weaponAngleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponAngleFieldActionPerformed
        updateSlider1();
        requestFocus();
}//GEN-LAST:event_weaponAngleFieldActionPerformed

    private void weaponAngleFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponAngleFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_weaponAngleFieldFocusGained

    private void weaponAngleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponAngleFieldFocusLost
        update();
        updateSlider1();
        taker.repaint();
}//GEN-LAST:event_weaponAngleFieldFocusLost

    private void weaponBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponBoxActionPerformed
        update();
        taker.repaint();
}//GEN-LAST:event_weaponBoxActionPerformed

    private void grabXFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabXFieldActionPerformed
        grabYField.requestFocus();
}//GEN-LAST:event_grabXFieldActionPerformed

    private void grabXFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabXFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_grabXFieldFocusGained

    private void grabXFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabXFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_grabXFieldFocusLost

    private void grabYFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabYFieldActionPerformed
        grabAnimationField.requestFocus();
}//GEN-LAST:event_grabYFieldActionPerformed

    private void grabYFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabYFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_grabYFieldFocusGained

    private void grabYFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabYFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_grabYFieldFocusLost

    private void grabAnimationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabAnimationFieldActionPerformed
        updateGrab();
        taker.repaint();
        grabAngleField.requestFocus();
}//GEN-LAST:event_grabAnimationFieldActionPerformed

    private void grabAnimationFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabAnimationFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_grabAnimationFieldFocusGained

    private void grabAnimationFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabAnimationFieldFocusLost
        update();
        taker.repaint();
}//GEN-LAST:event_grabAnimationFieldFocusLost

    private void grabBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabBoxActionPerformed
        update();
        taker.repaint();
}//GEN-LAST:event_grabBoxActionPerformed

    private void weaponFrontBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponFrontBoxActionPerformed
        update();
        taker.repaint();
}//GEN-LAST:event_weaponFrontBoxActionPerformed

    private void grabAngleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabAngleFieldActionPerformed
        updateGrab();
        updateSlider2();
        taker.repaint();
}//GEN-LAST:event_grabAngleFieldActionPerformed

    private void grabAngleFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabAngleFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_grabAngleFieldFocusGained

    private void grabAngleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabAngleFieldFocusLost
        updateSlider2();
}//GEN-LAST:event_grabAngleFieldFocusLost

    private void delayFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delayFieldActionPerformed
        update();
        requestFocus();
}//GEN-LAST:event_delayFieldActionPerformed

    private void delayFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_delayFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_delayFieldFocusGained

    private void delayFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_delayFieldFocusLost
        update();
}//GEN-LAST:event_delayFieldFocusLost

    private void weponBut1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weponBut1ActionPerformed
        openCharacter();
    }//GEN-LAST:event_weponBut1ActionPerformed

    private void grabFlipBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabFlipBoxActionPerformed
        taker.setFlipedEnemy(grabFlipBox.isSelected());
        taker.repaint();
    }//GEN-LAST:event_grabFlipBoxActionPerformed

    private void weaponFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponFieldActionPerformed
        int index = 0;
        try{
            index = Integer.parseInt(weaponField.getText());
        }catch(Exception e){ return; }
        taker.setWeaponIndex(index);
        taker.repaint();
    }//GEN-LAST:event_weaponFieldActionPerformed

    private void weponButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weponButActionPerformed
        openWeapons();
    }//GEN-LAST:event_weponButActionPerformed

    private void weaponFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weaponFieldFocusLost
        weaponFieldActionPerformed(null);
    }//GEN-LAST:event_weaponFieldFocusLost

    private void showBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showBoxActionPerformed
        taker.showActionPoints(showBox.isSelected());
        taker.repaint();
    }//GEN-LAST:event_showBoxActionPerformed

    private void palletButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_palletButActionPerformed
        openPallet();
}//GEN-LAST:event_palletButActionPerformed

    private void slider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slider1StateChanged
        weaponAngleField.setText(slider1.getValue()+"");
        update();
        taker.repaint();
    }//GEN-LAST:event_slider1StateChanged

    private void slider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slider2StateChanged
        grabAngleField.setText(slider2.getValue()+"");
        update();
        taker.repaint();
    }//GEN-LAST:event_slider2StateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField delayField;
    private javax.swing.JTextField grabAngleField;
    private javax.swing.JTextField grabAnimationField;
    private javax.swing.JCheckBox grabBox;
    private javax.swing.JCheckBox grabFlipBox;
    private javax.swing.JTextField grabXField;
    private javax.swing.JTextField grabYField;
    private javax.swing.JTextField headFrameField;
    private javax.swing.JTextField headXField;
    private javax.swing.JTextField headYField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton palletBut;
    private javax.swing.JCheckBox showBox;
    private javax.swing.JSlider slider1;
    private javax.swing.JSlider slider2;
    private javax.swing.JTextField weaponAngleField;
    private javax.swing.JCheckBox weaponBox;
    private javax.swing.JTextField weaponField;
    private javax.swing.JCheckBox weaponFrontBox;
    private javax.swing.JTextField weaponXField;
    private javax.swing.JTextField weaponYField;
    private javax.swing.JButton weponBut;
    private javax.swing.JButton weponBut1;
    // End of variables declaration//GEN-END:variables

    
    
    
    
    
void focusSelectedField(){
    if (!this.isFocusable()) return;
    JTextField field = null;
    switch(selectedField){
        case 0: field = delayField; break;
        case 1: field = headXField; break;
        case 2: field = headYField; break;
        case 3: field = headFrameField; break;
        case 4: field = weaponXField; break;
        case 5: field = weaponYField; break;
        case 6: field = weaponAngleField; break;
        case 7: field = grabXField; break;
        case 8: field = grabYField; break;
        case 9: field = grabAnimationField; break;
        case 10: field = grabAngleField; break;                                  
    }
    if (field == null) return;
    field.requestFocus();
    field.selectAll();
} 



void selectFocusedField(){
    if (delayField.hasFocus()) selectedField = 0;
    else if (headXField.hasFocus()) selectedField = 1;
    else if (headYField.hasFocus()) selectedField = 2;
    else if (headFrameField.hasFocus()) selectedField = 3;
    else if (weaponXField.hasFocus()) selectedField = 4;
    else if (weaponYField.hasFocus()) selectedField = 5;
    else if (weaponAngleField.hasFocus()) selectedField = 6;
    else if (grabXField.hasFocus()) selectedField = 7;
    else if (grabYField.hasFocus()) selectedField = 8;
    else if (grabAnimationField.hasFocus()) selectedField = 9;
    else if (grabAngleField.hasFocus()) selectedField = 10;                  
} 
    
    
}
