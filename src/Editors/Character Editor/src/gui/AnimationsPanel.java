/*
 * KeyFramesPanel.java
 *
 * Created on 03 December 2008, 16:53
 */

package gui;

import characters.Character;
import characters.Animation;
import characters.FixedFrame;
import characters.KeyFrame;
import frames.Frame;
import frames.FramesCollection;
import frames.IFramesTaker;
import game.GameInvariants;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import util.BTriple;
import util.IMarkTaker;
import util.IntTriple;
import util.UByte;

/**
 *
 * @author  Gil Costa
 */
public class AnimationsPanel extends javax.swing.JPanel implements IFramesTaker, IMarkTaker{
    
    
    protected InteractiveCollectionLinePanel collectionPanel;
    protected CollectionLinePanel animPanel;
    
    protected Animation anim;
    protected Character character;
    protected HashMap<Integer,Animation> animations;
    protected boolean hasHeads;
    
    
    /** Creates new form KeyFramesPanel */
    public AnimationsPanel() {
        initComponents();
        initAdvancedComponents();

        reset();
        requestFocus();
    }
    
    
    public void reset(){
        
        anim = new Animation(GameInvariants.CHARACTER_VERSION);
        anim.headSwap = hasHeads;
        animPanel.setCollection(new FramesCollection(), null);
        setCollection(null);
        character = null;
        animations = null;
        animationsList.setModel(new DefaultListModel());
        setFields();
    }
    
    
    public void setHeadSwaps(boolean heads){
        hasHeads = heads;
    }
    
    
    /** called when changing skin */
    public void setCollection(FramesCollection collection){
        if (collection == null)
            collectionPanel.setCollection(new FramesCollection(), null);
        else
        collectionPanel.setCollection(collection, null);
    }
    
    /** called when entering (changing animation) */
    public void setAnimation(Animation animation){
        anim = animation;
        if (animation==null){
            anim = new Animation(GameInvariants.CHARACTER_VERSION);
            anim.headSwap = hasHeads;
            animPanel.setCollection(new FramesCollection(), null);
        }else{
            int[] indexes = new int[animation.fixedFrames.size()];
            int i=0;
            for(FixedFrame f:animation.fixedFrames)
                indexes[i++] = f.frameIndex;
            animPanel.setCollection(collectionPanel.getCollection(), indexes);
        }
        setFields();
    }
    
    
    public void setFields(){
        box1.setSelected(anim.weaponPoint);
        box2.setSelected(anim.grabPoint);
        box3.setSelected(anim.velocityCtrl);
        box4.setSelected(anim.invinsible);
        
        // get the flags
        boolean h, v, flip, up, down, block;
        int val = anim.allowMov.get();
        h = (val&Animation.ALLOW_H) != 0;
        v = (val&Animation.ALLOW_V) != 0;
        flip = (val&Animation.ALLOW_FLIP) != 0;
        up = (val&Animation.ONLY_Z_UP) != 0;
        down = (val&Animation.ONLY_Z_DOWN) != 0;
        block = (val&Animation.BLOCK_MOVE) != 0;
        
        Hbox.setSelected(h);
        Vbox.setSelected(v);
        dirFlipBox.setSelected(flip);
        zUpBox.setSelected(up);
        zDownBox.setSelected(down);
        blockBox.setSelected(block);
        
        box5.setSelected(anim.attackOpt.get() != Animation.NO_ATTACK);
        this.setAttackEnabled();
        if (anim.attackOpt.get() != Animation.NO_ATTACK){
            val = anim.attackOpt.get();//%Animation.LOOP_ATTACK;
            switch(val){
                case Animation.ATTACK_WEAPON: radio1.setSelected(true); break;
                case Animation.ATTACK_POINTS: radio2.setSelected(true); break;
            }
            //box6.setSelected(anim.attackOpt.get() >= Animation.LOOP_ATTACK);
        }
        // end position
        box7.setSelected(anim.endPosition != null);
        setPositionEnabled();
        if (anim.endPosition != null){
            field1.setText(anim.endPosition.getX()+"");
            field2.setText(anim.endPosition.getY()+"");
            field3.setText(anim.endPosition.getZ()+"");
            flipBox.setSelected(anim.endFlip);
        }
        // sound
        soundBox.setSelected(anim.soundFrame.get() != Animation.INVALID_SOUND_FRAME);
        setSoundEnabled();
        if (soundBox.isSelected()){
            soundField.setText(anim.sound + "");
            soundFrameField.setText(anim.soundFrame.get() + "");
        }
        
//        // alternative animation
//        alternativeBox.setSelected(anim.usingWeapon.get() != Animation.INVALID_WEAPON);
//        setAlternativeEnabled();
//        if (alternativeBox.isSelected()){
//            alternativeField1.setText(anim.animReplaced.get() + "");
//            alternativeField2.setText(anim.usingWeapon.get() + "");
//        }
    }
    
    
    public boolean wasInitiated(){
        return animations != null && character != null;
    }
    
    
    
    
    public void setCharacter(Character character){
        animationsList.removeAll();
        this.character = character;
        if (character != null){
            this.animations = character.getAnimsSet();
            initList(character.getAnimsSet());
        }
        else{
            animations = null;
            animationsList.setModel(new DefaultListModel());
            setAnimation(null);
        }
        
        if (animations == null) return;
        if(animations.containsKey(0))
            setAnimation(animations.get(0));
        else setAnimation(null);
        anim.index = new UByte(0);
    }
    
    public Animation getAnimation(){
        int index = animationsList.getSelectedIndex();
        if (animations != null && index != -1)
            return animations.get(index);
        else return null;
    }
    
    
    protected void initAdvancedComponents(){
    	collectionPanel = new InteractiveCollectionLinePanel(bottomScroll, this, this);
        animPanel = new CollectionLinePanel(topScroll, this, this);
        
        collectionPanel.setForeground(new java.awt.Color(255, 255, 255));
        collectionPanel.setDoubleBuffered(false);
        bottomScroll.setViewportView(collectionPanel);
        bottomScroll.setWheelScrollingEnabled(false);
        bottomScroll.setDoubleBuffered(true);

        animPanel.setForeground(new java.awt.Color(255, 255, 255));
        topScroll.setViewportView(animPanel);
        topScroll.setWheelScrollingEnabled(false);
    }
    
    
    
    protected void initList(HashMap<Integer,Animation> animations){
        animationsList.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        int i=0;
        for(String s:GameInvariants.stanceTypes){
             if (animations==null || !animations.containsKey(i))
                listModel.addElement("    " + i++ + " - " + s); 
             else
                 listModel.addElement("(#) " + i++ + " - " + s);
        }
        animationsList.setModel(listModel);
        animationsList.setSelectedIndex(0);
    }
    
    
    
    @Override
	public void takeFrame(Frame frame) {
        //frameLabel.setText("Frame " + (collectionPanel.getSelectedNum()+1) + " of " + collectionPanel.getCollection().size() );
	}

    public void takeFrames(Frame[] frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
     public void takeMark(Frame f, int index) {
       if (f!=null){
           int pos = animPanel.addFrame(f, index);
           animPanel.repaint();
           FixedFrame newF = new FixedFrame();
           newF.frameIndex = index;
           anim.fixedFrames.add(pos, newF);
       }
    }
    
    
    
    
    
    
    
    
    
    
    
    public boolean componentsWithFocus(){
        return
                field1.hasFocus() || field2.hasFocus() || field3.hasFocus()
                || soundField.hasFocus() || soundFrameField.hasFocus()
            ;   // TODO
    }
    
    
    
    public void setAttackEnabled(){
        boolean b = box5.isSelected();
        radio1.setSelected(false);
        radio2.setSelected(false);
        radio1.setEnabled(b);
        radio2.setEnabled(b);
        //box6.setEnabled(b);
    }
    
     public void setPositionEnabled(){
        boolean b = box7.isSelected();
        field1.setEnabled(b);
        field2.setEnabled(b);
        field3.setEnabled(b);
        flipBox.setEnabled(b);
    }
     
    public void setSoundEnabled(){
        boolean b = soundBox.isSelected();
        soundField.setEnabled(b);
        soundFrameField.setEnabled(b);
    }
    
//    public void setAlternativeEnabled(){
//        boolean b = alternativeBox.isSelected();
//        alternativeField1.setEnabled(b);
//        alternativeField2.setEnabled(b);
//    }
    
    
     
     
     public void update(){
         if (animations==null) return;
             
         anim.weaponPoint = box1.isSelected();
         anim.grabPoint = box2.isSelected();
         anim.velocityCtrl = box3.isSelected();
         anim.invinsible = box4.isSelected();
         // fill flags
         anim.allowMov.set(0);
         if (Hbox.isSelected()) anim.allowMov.set( anim.allowMov.get() | Animation.ALLOW_H );
         if (Vbox.isSelected()) anim.allowMov.set( anim.allowMov.get() | Animation.ALLOW_V );
         if (dirFlipBox.isSelected()) anim.allowMov.set( anim.allowMov.get() | Animation.ALLOW_FLIP );
         if (zUpBox.isSelected()) anim.allowMov.set( anim.allowMov.get() | Animation.ONLY_Z_UP );
         else if (zDownBox.isSelected()) anim.allowMov.set( anim.allowMov.get() | Animation.ONLY_Z_DOWN );
         if (blockBox.isSelected()) anim.allowMov.set( anim.allowMov.get() | Animation.BLOCK_MOVE );
         
         
         if (box7.isSelected()){
             anim.endPosition = new IntTriple();
             int x,y,z;
             x = y = z = 0;
             try{
                 x = Integer.parseInt(field1.getText());
                 y = Integer.parseInt(field2.getText());
                 z = Integer.parseInt(field3.getText());
                 
             }catch(Exception e){}
             anim.endPosition.set(x, y, z);
             anim.endFlip = flipBox.isSelected();
         }else anim.endPosition = null;
         
         anim.attackOpt = new UByte();
         if (box5.isSelected()){
             if (radio1.isSelected()) anim.attackOpt.set(Animation.ATTACK_WEAPON);
             else if (radio2.isSelected()) anim.attackOpt.set(Animation.ATTACK_POINTS);
             //if (box6.isSelected())
             //    anim.attackOpt.set(anim.attackOpt.get()+Animation.LOOP_ATTACK);
         }else anim.attackOpt.set(Animation.NO_ATTACK);
         
         // sound
        if (soundBox.isSelected()){
             try{
                 anim.sound = Integer.parseInt(soundField.getText());
                 anim.soundFrame.set(Integer.parseInt(soundFrameField.getText()));
             }catch(Exception e){ anim.soundFrame.set(Animation.INVALID_SOUND_FRAME); }
         }else anim.soundFrame.set(Animation.INVALID_SOUND_FRAME);
         
         
//         // alternative animation
//        if (alternativeBox.isSelected()){
//             try{
//                 anim.animReplaced.set(Integer.parseInt(alternativeField1.getText()));
//                 anim.usingWeapon.set(Integer.parseInt(alternativeField2.getText()));
//             }catch(Exception e){ anim.usingWeapon.set(Animation.INVALID_WEAPON); }
//         }else anim.usingWeapon.set(Animation.INVALID_WEAPON);
         
         
         
//         // remove inexistent fixedFrames
//         LinkedList<FixedFrame> lst = new LinkedList<FixedFrame>();
//         for(FixedFrame f:anim.fixedFrames){
//             if (!animPanel.hasIndex(f.frameIndex))
//                 lst.add(f);
//         }
//         for(FixedFrame f :lst)
//             anim.fixedFrames.remove(f);
//         
//         // add inexistent fixedFrames
//         ArrayList<Integer> indexes = animPanel.getIndexes();
//         for(int i=0; i<indexes.size(); i++){
//             if (anim.fixedFrames.size()<=i || anim.fixedFrames.get(i).frameIndex!=indexes.get(i)){
//                 FixedFrame newF = new FixedFrame();
//                 newF.frameIndex = indexes.get(i);
//                 anim.fixedFrames.add(i,newF);
//             }
//         }
         

         
         
         
         // controll frames
         anim.framesControll();
         
         // add or remove the animation
         int index = anim.index.get();
         DefaultListModel model = (DefaultListModel)animationsList.getModel();
         if (anim.fixedFrames.isEmpty()){
             animations.remove(anim.index.get());
             if (!model.isEmpty() && ((String)model.get(index)).charAt(0)=='(' )
                 model.set(index, "    " + index + " - " + GameInvariants.stanceTypes[index]);
         }
         else if (!animations.containsKey(anim.index)){
             animations.put(anim.index.get(),anim);
             if (!model.isEmpty() && ((String)model.get(index)).charAt(0)!='(' )
                model.set(index, "(#) " + index + " - " + GameInvariants.stanceTypes[index]);
         }
         
         anim.keyFramesControl();
     }
     
     
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bottomScroll = new gui.Scroller();
        topScroll = new gui.Scroller();
        jScrollPane1 = new javax.swing.JScrollPane();
        animationsList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        box1 = new javax.swing.JCheckBox();
        box2 = new javax.swing.JCheckBox();
        box3 = new javax.swing.JCheckBox();
        box4 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        box5 = new javax.swing.JCheckBox();
        radio1 = new javax.swing.JRadioButton();
        radio2 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        box7 = new javax.swing.JCheckBox();
        field1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        field2 = new javax.swing.JTextField();
        field3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        flipBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        soundBox = new javax.swing.JCheckBox();
        soundField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        soundFrameField = new javax.swing.JTextField();
        Hbox = new javax.swing.JCheckBox();
        Vbox = new javax.swing.JCheckBox();
        dirFlipBox = new javax.swing.JCheckBox();
        zUpBox = new javax.swing.JCheckBox();
        zDownBox = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        blockBox = new javax.swing.JCheckBox();

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

        animationsList.setFont(new java.awt.Font("Courier New", 0, 12));
        animationsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        animationsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                animationsListValueChanged(evt);
            }
        });
        animationsList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                animationsListMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(animationsList);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        box1.setText("weapon hand point");

        box2.setText("grabbing");

        box3.setText("velocity control");

        box4.setText("invincible");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        box5.setText("attack");
        box5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                box5ActionPerformed(evt);
            }
        });

        radio1.setText("weapon");
        radio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio1ActionPerformed(evt);
            }
        });

        radio2.setText("action points");
        radio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(box5)
                .addContainerGap(54, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radio1)
                    .addComponent(radio2))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(box5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radio2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radio1)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        box7.setText("change position at end");
        box7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                box7ActionPerformed(evt);
            }
        });

        field1.setColumns(3);
        field1.setText("0");
        field1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field1ActionPerformed(evt);
            }
        });

        jLabel1.setText("x:");

        jLabel2.setText("y:");

        field2.setColumns(3);
        field2.setText("0");
        field2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field2ActionPerformed(evt);
            }
        });

        field3.setColumns(3);
        field3.setText("0");
        field3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field3ActionPerformed(evt);
            }
        });

        jLabel3.setText("z:");

        flipBox.setText("Flip");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(box7)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(flipBox)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(field1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(field2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(field3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(box7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(field2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(field3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(flipBox)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        soundBox.setText("play sound");
        soundBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundBoxActionPerformed(evt);
            }
        });

        soundField.setColumns(3);
        soundField.setText("0");
        soundField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundFieldActionPerformed(evt);
            }
        });

        jLabel4.setText("sound:");

        jLabel5.setText("at frame:");

        soundFrameField.setColumns(3);
        soundFrameField.setText("0");
        soundFrameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundFrameFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(soundBox)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(soundFrameField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(soundField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(soundBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(soundField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(soundFrameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        Hbox.setText("allow H movement");

        Vbox.setText("allow V movement");

        dirFlipBox.setText("Allow direction Flip");

        zUpBox.setText("only when z up");

        zDownBox.setText("only when z down");

        jLabel6.setText("only for jump attacks:");

        blockBox.setText("blocking");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dirFlipBox)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(box1)
                            .addComponent(box2)
                            .addComponent(box3)
                            .addComponent(box4)
                            .addComponent(Hbox)
                            .addComponent(Vbox)
                            .addComponent(blockBox))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zUpBox)
                            .addComponent(zDownBox)
                            .addComponent(jLabel6))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(207, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(box1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blockBox)
                        .addGap(18, 18, 18)
                        .addComponent(Hbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Vbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dirFlipBox))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(zDownBox))
                                    .addComponent(zUpBox))))))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bottomScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 875, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(topScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_LEFT: animPanel.selectPrevious(); break;
            case KeyEvent.VK_RIGHT: animPanel.selectNext(); break;
            case KeyEvent.VK_UP: animPanel.selectPageUp();break;
            case KeyEvent.VK_DOWN: animPanel.selectPageDown(); break;
            case KeyEvent.VK_DELETE:{
                int selected = animPanel.getSelectedNum();
                if (selected <0 || selected >= anim.fixedFrames.size()) break;
                anim.fixedFrames.remove(selected);
                animPanel.deleteSelected();
                update();
                break;
            }
        }
        requestFocus();
    }//GEN-LAST:event_formKeyPressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (!componentsWithFocus())
            requestFocus();
    }//GEN-LAST:event_formMouseMoved

    private void radio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio1ActionPerformed
        radio1.setSelected(true);
        radio2.setSelected(false);
    }//GEN-LAST:event_radio1ActionPerformed

    private void radio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio2ActionPerformed
        radio2.setSelected(true);
        radio1.setSelected(false);
    }//GEN-LAST:event_radio2ActionPerformed

    private void box5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_box5ActionPerformed
        setAttackEnabled();
        if (box5.isSelected() && !radio1.isSelected() && !radio2.isSelected())
            radio2.setSelected(true);
    }//GEN-LAST:event_box5ActionPerformed

    private void box7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_box7ActionPerformed
        setPositionEnabled();
    }//GEN-LAST:event_box7ActionPerformed

    private void animationsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_animationsListValueChanged
        if (animations == null) return;
        update();
        int index = animationsList.getSelectedIndex();
        if (index == anim.index.get()) return;
        if (index==-1){
            index = 0;
            animationsList.setSelectedIndex(0);
        }
        if(animations.containsKey(index))
            setAnimation(animations.get(index));
        else setAnimation(null);
        anim.index = new UByte(index);
    }//GEN-LAST:event_animationsListValueChanged

    private void animationsListMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_animationsListMouseMoved
        animationsList.requestFocus();
    }//GEN-LAST:event_animationsListMouseMoved

    private void field1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field1ActionPerformed
        field2.requestFocus();
    }//GEN-LAST:event_field1ActionPerformed

    private void field2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field2ActionPerformed
        field3.requestFocus();
    }//GEN-LAST:event_field2ActionPerformed

    private void field3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field3ActionPerformed
        requestFocus();
    }//GEN-LAST:event_field3ActionPerformed

    private void soundBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundBoxActionPerformed
        setSoundEnabled();
}//GEN-LAST:event_soundBoxActionPerformed

    private void soundFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundFieldActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_soundFieldActionPerformed

    private void soundFrameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundFrameFieldActionPerformed
        int frame = 0;
        try{
            frame = Integer.parseInt(soundFrameField.getText());
        }catch(Exception e){}
        if (frame<0 || frame >anim.fixedFrames.size())
            soundFrameField.setText("0");
}//GEN-LAST:event_soundFrameFieldActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Hbox;
    private javax.swing.JCheckBox Vbox;
    private javax.swing.JList animationsList;
    private javax.swing.JCheckBox blockBox;
    private gui.Scroller bottomScroll;
    private javax.swing.JCheckBox box1;
    private javax.swing.JCheckBox box2;
    private javax.swing.JCheckBox box3;
    private javax.swing.JCheckBox box4;
    private javax.swing.JCheckBox box5;
    private javax.swing.JCheckBox box7;
    private javax.swing.JCheckBox dirFlipBox;
    private javax.swing.JTextField field1;
    private javax.swing.JTextField field2;
    private javax.swing.JTextField field3;
    private javax.swing.JCheckBox flipBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton radio1;
    private javax.swing.JRadioButton radio2;
    private javax.swing.JCheckBox soundBox;
    private javax.swing.JTextField soundField;
    private javax.swing.JTextField soundFrameField;
    private gui.Scroller topScroll;
    private javax.swing.JCheckBox zDownBox;
    private javax.swing.JCheckBox zUpBox;
    // End of variables declaration//GEN-END:variables

    
}
