/*
 * MassCenterPanel.java
 *
 * Created on 27 November 2008, 10:11
 */

package gui;

import game.GameInvariants;
import javax.swing.JComponent;
import util.BTriple;
import util.UBPoint;
import util.ICoordinatesTaker;
import util.UByte;
import weapons.Weapon;


/**
 *
 * @author  Gil Costa
 * TODO: histograms to compute lines and collumns
 */
public class WeaponPanel extends javax.swing.JPanel implements ICoordinatesTaker{
    
    public static final String POINT_STRING = "point";

    
    
    protected Weapon weapon;
    
    /** takes the selected point */
    ICoordinatesTaker taker;
    
    
    /** Creates new form MassCenterPanel */
    public WeaponPanel() {
        initComponents();
        initTypeCombo();
        pointCombo.removeAllItems();
        weapon = null;
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
    
    public void setAllEnabled(boolean enabled){
        this.setEnabled(enabled);
        nameField.setEnabled(enabled);
        typeField.setEditable(enabled);
        newBut.setEnabled(enabled);
        rotationBox.setEnabled(enabled);
        typeCombo.setEnabled(enabled);
        if (!enabled || weapon.actionPoints.isEmpty())
            setFieldsEnabled(false);
        else setFieldsEnabled(true);
        if (!enabled || weapon.actionType.get() != game.GameInvariants.AT_THROW)
            setThrowFieldsEnabled(false);
        else setThrowFieldsEnabled(true);
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
        rotField.setEnabled(enabled);
    }


    public void reset(){
        weapon = null;
    }
    
    public void update(){
        if (this.weapon==null) return;
        weapon.name = new UByte(getWeaponName());
        weapon.actionType = new UByte(getActionType());
        weapon.rotation = rotationBox.isSelected();
        updateCurrentPoint();
        updateThrowPoint();
    }
    
    public void updateCurrentPoint(){
        if (pointCombo.getItemCount()>0){
            UBPoint pt = new UBPoint();
             try{
                pt.setX(Integer.parseInt(xField.getText()));
                pt.setY(Integer.parseInt(yField.getText()));
             }catch (NumberFormatException e){ }
            weapon.actionPoints.set(pointCombo.getSelectedIndex(), pt);
        }
        //taker.repaint();
    }
    
      public void updateThrowPoint(){
        if (weapon.actionType.get() == game.GameInvariants.AT_THROW){
            BTriple pt = new BTriple();
            int damage = 0;
            byte rot = 0;
             try{
                pt.x = Byte.parseByte(vxField.getText());
                pt.y = Byte.parseByte(vyField.getText());
                pt.z = Byte.parseByte(vyField.getText());
                damage = Integer.parseInt(damageField.getText());
                rot = Byte.parseByte(rotField.getText());
             }catch (NumberFormatException e){ }
            weapon.throwDamage = new UByte(damage);
            weapon.throwImpulse = pt;
        }
        //taker.repaint();
    }
    
    
    public void updatePointFields(){
        if (weapon == null) return;
        int index = pointCombo.getSelectedIndex();
        if (index < 0 || index >= weapon.actionPoints.size()) return;
        xField.setText(weapon.actionPoints.get(index).getX()+"");
        yField.setText(weapon.actionPoints.get(index).getY()+"");
    }
    
   public void updateThrowFields(){
        if (weapon == null) return;
        if (weapon.actionType.get() == game.GameInvariants.AT_THROW){
            vxField.setText(weapon.throwImpulse.x+"");
            vyField.setText(weapon.throwImpulse.y+"");
            vzField.setText(weapon.throwImpulse.z+"");
            damageField.setText(weapon.throwDamage.get()+"");
            rotField.setText(weapon.throwRotation+"");
        }
    }
    
    
     public void deleteCurrentPoint(){
        int index = pointCombo.getSelectedIndex();
        weapon.actionPoints.remove(index);
        initPointCombo();
        if (weapon.actionPoints.isEmpty())
            setFieldsEnabled(false);
    }
     
     
    public void addNewPoint(){
        weapon.actionPoints.add(new UBPoint(0,0));
        initPointCombo();
        setFieldsEnabled(true);
    }
    
    
    public void setWeapon(Weapon wp){
        update();
        this.weapon = wp;
        updateThrowFields();
        if (wp == null){
            setAllEnabled(false);
        }else{
            setAllEnabled(true);
            nameField.setText(wp.name.get()+"");
            typeField.setText(wp.actionType.get()+"");
            rotationBox.setSelected(wp.rotation);
            initPointCombo();
        }
        repaint();
    }
    
    protected void initFirstPoint(){
        if (!weapon.actionPoints.isEmpty()){
                pointCombo.setSelectedIndex(0);
                xField.setText(weapon.actionPoints.get(0).getX() + "");
                yField.setText(weapon.actionPoints.get(0).getY() + "");
            }
    }
    
    
    protected void initPointCombo(){
        pointCombo.removeAllItems();
        for(int i=0; i<weapon.actionPoints.size(); i++){
            pointCombo.addItem(i + " - " + POINT_STRING + " " + i);
        }
        initFirstPoint();
    }
    
    
    
    protected void sendSelectedMessage(){
        if (taker == null) return;
        int index = pointCombo.getSelectedIndex();
        if (index < 0 || index >= weapon.actionPoints.size())
            taker.takeCoordinates(-1, -1, -1);
        else taker.takeCoordinates(weapon.actionPoints.get(index).getX(), weapon.actionPoints.get(index).getY(), index);
    }
    
    
    
    //-------------------
    // ---- GETTERS ----
    //-------------------
    
    
     @Override
    public boolean hasFocus(){
        return
            pointCombo.hasFocus() || nameField.hasFocus() || typeField.hasFocus()
            || xField.hasFocus() || yField.hasFocus() || typeCombo.hasFocus()
            || vxField.hasFocus() || vyField.hasFocus() || vzField.hasFocus()
            || damageField.hasFocus() || rotField.hasFocus()
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
    
    public int getWeaponName(){
         int res = 0;
        try{
            res = Integer.parseInt(nameField.getText());
        }catch (NumberFormatException e){}
         if (res>255) res = 255;
        return res;
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        xField = new javax.swing.JTextField();
        yField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        pointCombo = new javax.swing.JComboBox();
        deleteBut = new javax.swing.JButton();
        newBut = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        typeField = new javax.swing.JTextField();
        typeCombo = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        rotationBox = new javax.swing.JCheckBox();
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
        jLabel13 = new javax.swing.JLabel();
        rotField = new javax.swing.JTextField();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jLabel3.setText("Name index:");

        nameField.setColumns(3);
        nameField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nameField.setText("0");
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
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

        jLabel5.setText("action type:");

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

        jLabel8.setText("or select from list");

        jLabel9.setText("put a number");

        rotationBox.setSelected(true);
        rotationBox.setText("Center rotation when thrown");
        rotationBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotationBoxActionPerformed(evt);
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

        jLabel11.setText("Damage:");

        jLabel12.setText("Throw:");

        jLabel13.setText("rot vel:");

        rotField.setColumns(3);
        rotField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        rotField.setText("1");
        rotField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotFieldActionPerformed(evt);
            }
        });
        rotField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rotFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                rotFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(vzField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(damageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rotField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(damageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rotField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel8)))
                                .addGap(13, 13, 13))
                            .addComponent(typeField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(typeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(newBut)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(rotationBox)))
                .addGap(196, 196, 196))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(newBut)
                            .addComponent(jLabel3)
                            .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel9)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel8)
                        .addGap(7, 7, 7)
                        .addComponent(typeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rotationBox)
                .addContainerGap(69, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        requestFocus();
}//GEN-LAST:event_nameFieldActionPerformed

    private void nameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusGained
        nameField.selectAll();
}//GEN-LAST:event_nameFieldFocusGained

    private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusLost

}//GEN-LAST:event_nameFieldFocusLost

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

    private void rotationBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotationBoxActionPerformed
        weapon.rotation = rotationBox.isSelected();
}//GEN-LAST:event_rotationBoxActionPerformed

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

    private void rotFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotFieldActionPerformed
        requestFocus();
}//GEN-LAST:event_rotFieldActionPerformed

    private void rotFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rotFieldFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_rotFieldFocusGained

    private void rotFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rotFieldFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_rotFieldFocusLost
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField damageField;
    private javax.swing.JButton deleteBut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JTextField nameField;
    private javax.swing.JButton newBut;
    private javax.swing.JComboBox pointCombo;
    private javax.swing.JTextField rotField;
    private javax.swing.JCheckBox rotationBox;
    private javax.swing.JComboBox typeCombo;
    private javax.swing.JTextField typeField;
    private javax.swing.JTextField vxField;
    private javax.swing.JTextField vyField;
    private javax.swing.JTextField vzField;
    private javax.swing.JTextField xField;
    private javax.swing.JTextField yField;
    // End of variables declaration//GEN-END:variables

    
}
