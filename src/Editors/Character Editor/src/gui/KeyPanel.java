/*
 * MassCenterPanel.java
 *
 * Created on 27 November 2008, 10:11
 */

package gui;

import characters.KeyFrame;
import game.GameInvariants;
import util.BTriple;
import util.ICoordinatesTaker;
import util.IMarkable;
import util.IntPoint;
import util.UByte;


/**
 *
 * @author  Gil Costa
 * TODO: histograms to compute lines and collumns
 */
public class KeyPanel extends javax.swing.JPanel implements ICoordinatesTaker{
    
    public static final String POINT_STRING = "point";

    
    
    protected KeyFrame keyFrame;
    
    /** takes the selected point */
    ICoordinatesTaker taker;
    
    /** say that the keyFrame really exists */
    IMarkable markable;
    
    
    /** Creates new form MassCenterPanel */
    public KeyPanel() {
        initComponents();
        initTypeCombo();
        pointCombo.removeAllItems();
        keyFrame = null;
        setAllEnabled(false);
    }
    
    
    
    protected void initTypeCombo(){
        typeCombo.removeAllItems();
        for(int i=0; i<255; i++){
            String s = GameInvariants.actionType(i);
            if (s!=null)
                typeCombo.addItem(i + " - " + s);
        }
    }
    
    
    protected int getTypeFromCombo(){
        String s = (String) typeCombo.getSelectedItem();
        if (s==null) return 0;
        s = s.substring(0, s.indexOf(' '));
        int val = 0;
        try{val = Integer.parseInt(s);} catch(Exception e){}
        return val;
    }
    
    
    public void takeCoordinates(int x, int y, int index) {
        pointCombo.setSelectedIndex(index);
        updatePointFields();
    }
    
    
    //---------------------
    // ----- SETTERS -----
    //---------------------
    
    
    public void setTaker(ICoordinatesTaker taker){
        this.taker = taker;
    }
    
    public void setMarkable(IMarkable markable){
        this.markable = markable;
    }
    
    public void setAllEnabled(boolean enabled){
        boolean hasKeyFrames = enabled && (keyFrame.actionType != null || keyFrame.impulsed);
        if (enabled && (keyFrame == null || !hasKeyFrames)){
            setAllEnabled(false);
            return;
        }
        this.setEnabled(enabled);
        keyBox.setEnabled(enabled);
        
        if (enabled) keyBox.setSelected(!keyFrame.invalid);
        else keyBox.setSelected(false);
        setPointsEnabled (enabled  && keyBox.isSelected() && keyFrame.actionType != null);
        setVelocityEnabled(enabled && keyBox.isSelected() && keyFrame.impulsed);
    }
    
    protected void setPointsEnabled(boolean enabled){
        typeField.setEnabled(enabled);
        boolean knock = enabled && keyFrame.actionType.get() >= GameInvariants.AT_KNOCK;
        frontalBox.setEnabled(knock);
        backBox.setEnabled(knock);
        sparseBox.setEnabled(knock);
        newBut.setEnabled(enabled);
        typeCombo.setEnabled(enabled);
        if (!enabled || keyFrame == null || keyFrame.actionPoints.isEmpty())
            setFieldsEnabled(false);
        else setFieldsEnabled(true);
        if (!enabled || keyFrame == null || keyFrame.actionType.get() != game.GameInvariants.AT_THROW)
            setThrowFieldsEnabled(false);
        else setThrowFieldsEnabled(true);
    }
    
    protected void setVelocityEnabled(boolean enabled){
        impulseXField.setEnabled(enabled);
        impulseYField.setEnabled(enabled);
        impulseZField.setEnabled(enabled);
    }
    
    protected void setFieldsEnabled(boolean enabled){
        pointCombo.setEnabled(enabled);
        xField.setEnabled(enabled);
        yField.setEnabled(enabled);
        deleteBut.setEnabled(enabled);
    }
    
    protected void setThrowFieldsEnabled(boolean enabled){
        vxField.setEnabled(enabled);
        vyField.setEnabled(enabled);
        vzField.setEnabled(enabled);
        damageField.setEnabled(enabled);
        rotVelField.setEnabled(enabled);
    }


    public void reset(){
        update();
        keyFrame = null;
    }
    
    public void update(){
        if (this.keyFrame==null) return;
        
        keyFrame.invalid = false;
        if(keyFrame.actionType != null){
            keyFrame.actionType = new UByte(getActionType());
            keyFrame.knockFlags = new UByte(0);
            if (frontalBox.isSelected())
                keyFrame.knockFlags.set( keyFrame.knockFlags.get() | KeyFrame.FRONTAL_KNOCK );
            if (backBox.isSelected())
                keyFrame.knockFlags.set( keyFrame.knockFlags.get() | KeyFrame.BACK_KNOCK );
            if (sparseBox.isSelected())
                keyFrame.knockFlags.set( keyFrame.knockFlags.get() | KeyFrame.SPARSE_KNOCK );
            updateCurrentPoint();
            updateThrowPoint();
        } 
        if (keyFrame.impulsed) updateImpulse();
        
        boolean mark = keyBox.isSelected()
            && !((keyFrame.impulse == null || keyFrame.impulse.x == KeyFrame.INVALID_VEL || keyFrame.impulse.x == KeyFrame.INVALID_VEL) && (keyFrame.actionPoints == null || keyFrame.actionPoints.isEmpty()));
        if (markable!=null) markable.mark(keyFrame.index.get(), mark);
        
        keyFrame.invalid = !mark;
    }
    
    
    
    void updateImpulse(){
        BTriple pt = new BTriple();
        try{
           pt.x = Byte.parseByte(impulseXField.getText());
           pt.y = Byte.parseByte(impulseYField.getText());
           pt.z = Byte.parseByte(impulseZField.getText());   
       }catch (NumberFormatException e){
           pt.x = KeyFrame.INVALID_VEL;
       }
       if (!keyBox.isSelected())
           pt.x = KeyFrame.INVALID_VEL;
       keyFrame.impulse = pt;
    }
    
    public void updateCurrentPoint(){
        if (pointCombo.getItemCount()>0){
            IntPoint pt = new IntPoint();
             try{
                pt.setX(Integer.parseInt(xField.getText()));
                pt.setY(Integer.parseInt(yField.getText()));
             }catch (NumberFormatException e){ }
            keyFrame.actionPoints.set(pointCombo.getSelectedIndex(), pt);
        }
        //taker.repaint();
    }
    
      public void updateThrowPoint(){
        if (keyFrame.actionType.get() == game.GameInvariants.AT_THROW){
            BTriple pt = new BTriple();
            int damage = 0;
            byte rotVel = 0;
             try{
                pt.x = Byte.parseByte(vxField.getText());
                pt.y = Byte.parseByte(vyField.getText());
                pt.z = Byte.parseByte(vzField.getText());
                damage = Integer.parseInt(damageField.getText());
                rotVel = Byte.parseByte(rotVelField.getText());
                keyFrame.throwDamage = new UByte(damage);
                keyFrame.throwImpulse = pt;
                keyFrame.throwRotation = rotVel;
             }catch (NumberFormatException e){ }
        }
        //taker.repaint();
    }
    
    
    public void updatePointFields(){
        if (keyFrame == null) return;
        int index = pointCombo.getSelectedIndex();
        if (index < 0 || index >= keyFrame.actionPoints.size()) return;
        xField.setText(keyFrame.actionPoints.get(index).getX()+"");
        yField.setText(keyFrame.actionPoints.get(index).getY()+"");
    }
    
   public void updateThrowFields(){
        if (keyFrame == null || keyFrame.actionType == null) return;
        if (keyFrame.actionType.get() == game.GameInvariants.AT_THROW){
            vxField.setText(keyFrame.throwImpulse.x+"");
            vyField.setText(keyFrame.throwImpulse.y+"");
            vzField.setText(keyFrame.throwImpulse.z+"");
            damageField.setText(keyFrame.throwDamage.get()+"");
            rotVelField.setText(keyFrame.throwRotation+"");
        }
    }
    
    
     public void deleteCurrentPoint(){
        int index = pointCombo.getSelectedIndex();
        keyFrame.actionPoints.remove(index);
        initPointCombo();
        if (keyFrame.actionPoints.isEmpty())
            setFieldsEnabled(false);
    }
     
     
    public void addNewPoint(){
        keyFrame.actionPoints.add(new IntPoint(0,0));
        initPointCombo();
        setFieldsEnabled(true);
    }
    
    
    public void setKeyFrame(KeyFrame key){
        update();
        this.keyFrame = key;
        if (key == null || (key.actionType == null && !key.impulsed)){
            setAllEnabled(false);
        }else{
            setAllEnabled(true);
            if (key.actionType != null){
                updateThrowFields();
                typeField.setText(key.actionType.get()+"");
                boolean knock = key.actionType.get() >= GameInvariants.AT_KNOCK;
                frontalBox.setEnabled(knock);
                backBox.setEnabled(knock);
                sparseBox.setEnabled(knock);
                
                frontalBox.setSelected((key.knockFlags.get() & KeyFrame.FRONTAL_KNOCK) != 0);
                sparseBox.setSelected((key.knockFlags.get() & KeyFrame.SPARSE_KNOCK) != 0);
                backBox.setSelected((key.knockFlags.get() & KeyFrame.BACK_KNOCK) != 0);
                initPointCombo();
            }
            if (key.impulsed){
                impulseXField.setText(key.impulse.x+"");
                impulseYField.setText(key.impulse.y+"");
                impulseZField.setText(key.impulse.z+"");
            }else impulseXField.setText(KeyFrame.INVALID_VEL+"");
            
        }
        repaint();
    }
    
    protected void initFirstPoint(){
        if (!keyFrame.actionPoints.isEmpty()){
                pointCombo.setSelectedIndex(0);
                xField.setText(keyFrame.actionPoints.get(0).getX() + "");
                yField.setText(keyFrame.actionPoints.get(0).getY() + "");
            }
    }
    
    
    protected void initPointCombo(){
        pointCombo.removeAllItems();
        for(int i=0; i<keyFrame.actionPoints.size(); i++){
            pointCombo.addItem(i + " - " + POINT_STRING + " " + i);
        }
        initFirstPoint();
    }
    
    
    
    protected void sendSelectedMessage(){
        if (taker == null) return;
        int index = pointCombo.getSelectedIndex();
        if (index < 0 || index >= keyFrame.actionPoints.size())
            taker.takeCoordinates(-1, -1, -1);
        else taker.takeCoordinates(keyFrame.actionPoints.get(index).getX(), keyFrame.actionPoints.get(index).getY(), index);
    }
    
    
    
    //-------------------
    // ---- GETTERS ----
    //-------------------
    
    
     @Override
    public boolean hasFocus(){
        return
            pointCombo.hasFocus() || typeField.hasFocus()
            || frontalBox.hasFocus() || sparseBox.hasFocus() || backBox.hasFocus()
            || xField.hasFocus() || yField.hasFocus() || typeCombo.hasFocus()
            || vxField.hasFocus() || vyField.hasFocus() || vzField.hasFocus()
            || damageField.hasFocus() || rotVelField.hasFocus()
            || impulseXField.hasFocus() || impulseYField.hasFocus() || impulseZField.hasFocus()
            
        ;
    }
     
    
    
    
    
    public int getActionType(){
        int res = 0;
        try{
            res = Integer.parseInt(typeField.getText());
        }catch (NumberFormatException e){
            res = typeCombo.getSelectedIndex();
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

        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        typeField = new javax.swing.JTextField();
        typeCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        xField = new javax.swing.JTextField();
        yField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        pointCombo = new javax.swing.JComboBox();
        deleteBut = new javax.swing.JButton();
        newBut = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        vxField = new javax.swing.JTextField();
        vyField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        vzField = new javax.swing.JTextField();
        damageField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        rotVelField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        frontalBox = new javax.swing.JCheckBox();
        sparseBox = new javax.swing.JCheckBox();
        backBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        impulseXField = new javax.swing.JTextField();
        impulseYField = new javax.swing.JTextField();
        impulseZField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        keyBox = new javax.swing.JCheckBox();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("action type:");

        jLabel8.setText("or select from list");

        jLabel9.setText("put a number");

        typeField.setColumns(3);
        typeField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        typeField.setText("0");
        typeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeFieldActionPerformed(evt);
            }
        });
        typeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                typeFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                typeFieldFocusLost(evt);
            }
        });

        typeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "new point..." }));
        typeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboActionPerformed(evt);
            }
        });

        jLabel2.setText("Action Points:");

        jLabel1.setText("X:");

        xField.setColumns(3);
        xField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        xField.setText("1");
        xField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xFieldActionPerformed(evt);
            }
        });
        xField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                xFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                xFieldFocusLost(evt);
            }
        });

        yField.setColumns(3);
        yField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        yField.setText("1");
        yField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yFieldActionPerformed(evt);
            }
        });
        yField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                yFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                yFieldFocusLost(evt);
            }
        });

        jLabel4.setText("Y:");

        pointCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "new point..." }));
        pointCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pointComboActionPerformed(evt);
            }
        });
        pointCombo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pointComboFocusLost(evt);
            }
        });

        deleteBut.setText("delete");
        deleteBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pointCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(deleteBut))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pointCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteBut)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        newBut.setText("new point");
        newBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButActionPerformed(evt);
            }
        });

        jLabel6.setText("X:");

        vxField.setColumns(3);
        vxField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        vxField.setText("1");
        vxField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vxFieldActionPerformed(evt);
            }
        });
        vxField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vxFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vxFieldFocusLost(evt);
            }
        });

        vyField.setColumns(3);
        vyField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        vyField.setText("1");
        vyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vyFieldActionPerformed(evt);
            }
        });
        vyField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vyFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vyFieldFocusLost(evt);
            }
        });

        jLabel7.setText("Y:");

        jLabel10.setText("Z:");

        vzField.setColumns(3);
        vzField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        vzField.setText("1");
        vzField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vzFieldActionPerformed(evt);
            }
        });
        vzField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vzFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vzFieldFocusLost(evt);
            }
        });

        damageField.setColumns(3);
        damageField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        damageField.setText("1");
        damageField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                damageFieldActionPerformed(evt);
            }
        });
        damageField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                damageFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                damageFieldFocusLost(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Damage:");

        jLabel12.setText("Throw:");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Rot vel:");

        rotVelField.setColumns(3);
        rotVelField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        rotVelField.setText("1");
        rotVelField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotVelFieldActionPerformed(evt);
            }
        });
        rotVelField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rotVelFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                rotVelFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vzField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rotVelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(damageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(vxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(vyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(vzField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(damageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(rotVelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel16.setText("Action Points");

        frontalBox.setText("front Knock");

        sparseBox.setText("sparse Knock");

        backBox.setText("back Knock");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(frontalBox, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(typeField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sparseBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                    .addComponent(backBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(newBut)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(1, 1, 1)
                .addComponent(jLabel9)
                .addGap(1, 1, 1)
                .addComponent(jLabel8)
                .addGap(7, 7, 7)
                .addComponent(typeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(frontalBox)
                .addGap(63, 63, 63))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newBut)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(backBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sparseBox))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        impulseXField.setColumns(3);
        impulseXField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        impulseXField.setText("1");
        impulseXField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                impulseXFieldActionPerformed(evt);
            }
        });
        impulseXField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                impulseXFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                impulseXFieldFocusLost(evt);
            }
        });

        impulseYField.setColumns(3);
        impulseYField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        impulseYField.setText("1");
        impulseYField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                impulseYFieldActionPerformed(evt);
            }
        });
        impulseYField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                impulseYFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                impulseYFieldFocusLost(evt);
            }
        });

        impulseZField.setColumns(3);
        impulseZField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        impulseZField.setText("1");
        impulseZField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                impulseZFieldActionPerformed(evt);
            }
        });
        impulseZField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                impulseZFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                impulseZFieldFocusLost(evt);
            }
        });

        jLabel13.setText("vX:");

        jLabel14.setText("vY:");

        jLabel15.setText("vZ:");

        jLabel3.setText("Velocity Control");

        jLabel17.setText("set -128 on vX to invalidate velocity");

        jLabel18.setText("set -127 on components you want to be unchanged");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(227, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(impulseXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(impulseYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(impulseZField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel18))
                .addGap(27, 27, 27))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(impulseXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(impulseYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(impulseZField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        keyBox.setText("Key Frame");
        keyBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keyBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(keyBox))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(keyBox, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void pointComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointComboActionPerformed
        sendSelectedMessage();
        updatePointFields();
        
}//GEN-LAST:event_pointComboActionPerformed

    private void xFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xFieldActionPerformed
        updateCurrentPoint();
        sendSelectedMessage();
        yField.requestFocus();
}//GEN-LAST:event_xFieldActionPerformed

    private void xFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_xFieldFocusGained

}//GEN-LAST:event_xFieldFocusGained

    private void xFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_xFieldFocusLost

}//GEN-LAST:event_xFieldFocusLost

    private void pointComboFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pointComboFocusLost

}//GEN-LAST:event_pointComboFocusLost

    private void yFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yFieldActionPerformed
        updateCurrentPoint();
        sendSelectedMessage();
        requestFocus();
}//GEN-LAST:event_yFieldActionPerformed

    private void yFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_yFieldFocusGained

}//GEN-LAST:event_yFieldFocusGained

    private void yFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_yFieldFocusLost

}//GEN-LAST:event_yFieldFocusLost

    private void deleteButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButActionPerformed
        this.deleteCurrentPoint();
        sendSelectedMessage();
        repaint();
}//GEN-LAST:event_deleteButActionPerformed

    private void newButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButActionPerformed
        this.addNewPoint();
        pointCombo.setSelectedIndex(pointCombo.getItemCount()-1);
        sendSelectedMessage();
        updatePointFields();
        xField.requestFocus();
        repaint();
}//GEN-LAST:event_newButActionPerformed

    private void typeFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_typeFieldFocusLost
         try{
            setThrowFieldsEnabled(Integer.parseInt(typeField.getText()) == game.GameInvariants.AT_THROW);
        }catch (Exception e){ typeField.setText("0"); }
}//GEN-LAST:event_typeFieldFocusLost

    private void typeFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_typeFieldFocusGained

}//GEN-LAST:event_typeFieldFocusGained

    private void typeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeFieldActionPerformed
        try{
            setThrowFieldsEnabled(Integer.parseInt(typeField.getText()) == game.GameInvariants.AT_THROW);
        }catch (Exception e){ typeField.setText("0"); }
        requestFocus();
}//GEN-LAST:event_typeFieldActionPerformed

    private void typeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboActionPerformed
        typeField.setText(getTypeFromCombo()+"");
        setThrowFieldsEnabled(getTypeFromCombo() == game.GameInvariants.AT_THROW);
    }//GEN-LAST:event_typeComboActionPerformed

    private void vxFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vxFieldActionPerformed
        vyField.requestFocus();
}//GEN-LAST:event_vxFieldActionPerformed

    private void vxFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vxFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_vxFieldFocusGained

    private void vxFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vxFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_vxFieldFocusLost

    private void vyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vyFieldActionPerformed
        vzField.requestFocus();
}//GEN-LAST:event_vyFieldActionPerformed

    private void vyFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vyFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_vyFieldFocusGained

    private void vyFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vyFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_vyFieldFocusLost

    private void vzFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vzFieldActionPerformed
        damageField.requestFocus();
}//GEN-LAST:event_vzFieldActionPerformed

    private void vzFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vzFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_vzFieldFocusGained

    private void vzFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vzFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_vzFieldFocusLost

    private void damageFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_damageFieldActionPerformed
        requestFocus();
}//GEN-LAST:event_damageFieldActionPerformed

    private void damageFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_damageFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_damageFieldFocusGained

    private void damageFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_damageFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_damageFieldFocusLost

    private void impulseXFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_impulseXFieldActionPerformed
        impulseYField.requestFocus();
}//GEN-LAST:event_impulseXFieldActionPerformed

    private void impulseXFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseXFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_impulseXFieldFocusGained

    private void impulseXFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseXFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_impulseXFieldFocusLost

    private void impulseYFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_impulseYFieldActionPerformed
        impulseZField.requestFocus();
}//GEN-LAST:event_impulseYFieldActionPerformed

    private void impulseYFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseYFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_impulseYFieldFocusGained

    private void impulseYFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseYFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_impulseYFieldFocusLost

    private void impulseZFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_impulseZFieldActionPerformed
        requestFocus();
}//GEN-LAST:event_impulseZFieldActionPerformed

    private void impulseZFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseZFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_impulseZFieldFocusGained

    private void impulseZFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_impulseZFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_impulseZFieldFocusLost

    private void keyBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keyBoxActionPerformed
        if (markable != null)
            markable.mark(keyFrame.index.get(), keyBox.isSelected());
        keyFrame.invalid = !keyBox.isSelected();
        setAllEnabled(true);    // refreshEnablement
        if (keyBox.isSelected()){
            typeField.setText(getTypeFromCombo()+"");
            setThrowFieldsEnabled(getTypeFromCombo() == game.GameInvariants.AT_THROW);
            impulseXField.setText(KeyFrame.INVALID_VEL + "");
        }
}//GEN-LAST:event_keyBoxActionPerformed

    private void rotVelFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotVelFieldActionPerformed
        requestFocus();
}//GEN-LAST:event_rotVelFieldActionPerformed

    private void rotVelFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rotVelFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_rotVelFieldFocusGained

    private void rotVelFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rotVelFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_rotVelFieldFocusLost
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox backBox;
    private javax.swing.JTextField damageField;
    private javax.swing.JButton deleteBut;
    private javax.swing.JCheckBox frontalBox;
    private javax.swing.JTextField impulseXField;
    private javax.swing.JTextField impulseYField;
    private javax.swing.JTextField impulseZField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JCheckBox keyBox;
    private javax.swing.JButton newBut;
    private javax.swing.JComboBox pointCombo;
    private javax.swing.JTextField rotVelField;
    private javax.swing.JCheckBox sparseBox;
    private javax.swing.JComboBox typeCombo;
    private javax.swing.JTextField typeField;
    private javax.swing.JTextField vxField;
    private javax.swing.JTextField vyField;
    private javax.swing.JTextField vzField;
    private javax.swing.JTextField xField;
    private javax.swing.JTextField yField;
    // End of variables declaration//GEN-END:variables

    
}
