/*
 * KeyFramesPanel.java
 *
 * Created on 03 December 2008, 16:53
 */

package gui;

import characters.Animation;
import characters.FixedFrame;
import frames.Frame;
import frames.FramesCollection;
import frames.IFramesTaker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.Timer;
import pallet.Pallet;

/**
 *
 * @author  Gil Costa
 */
public class FixedFramesPanel extends javax.swing.JPanel
    implements IFramesTaker {
    
    public static final int TIMER_DELAY = 10; // miliseconds
    
    
    protected FixedActionFramePanel framePanel;
    protected CollectionLinePanel collectionPanel;
    
    protected ArrayList<FixedFrame> fixedFrames;
    
    protected Timer timer;
    protected int timeCount;
    
    
    // Sound controll
    public int sound;
    public int soundFrame;
    
    
    
    /** Creates new form KeyFramesPanel */
    public FixedFramesPanel() {
        initComponents();
        initAdvancedComponents();
        initTimer();
        soundFrame = -1;
        
        fixedFrames = new ArrayList<FixedFrame>();
        takeFrame(null);
        requestFocus();
    }
    
    
    
    /** called when changing skin */
    public void setCollection(FramesCollection collection){
        collectionPanel.setCollection(collection, null);
    }
    
    public void setHead(FramesCollection head){
        framePanel.setHead(head);
    }
    
    /** called when entering (changing animation) */
    public void setAnimation(FramesCollection original, Animation animation){
        localPrevBut.setSelected(false);
        localPreview(false); 
        if (animation==null){
            fixedFrames = new ArrayList<FixedFrame>();
            takeFrame(null);
            setCollection(new FramesCollection());
            return;
        }
        int[] indexes = new int[animation.fixedFrames.size()];
        int i=0;
        for(FixedFrame f:animation.fixedFrames)
            indexes[i++] = f.frameIndex;
        collectionPanel.setCollection(original, indexes);
        setFixedFrames(animation.fixedFrames);
        if (animation.soundFrame == null || animation.soundFrame.get() == Animation.INVALID_SOUND_FRAME){
            soundFrame = -1;
        }else{
            soundFrame = animation.soundFrame.get();
            sound = animation.sound;
        }
    }
    
    
    public void setAutoPallet(String animName, int index){
        if (index>=0){
            framePanel.setAutoPallet(animName, index);
            fixedPanel.setAutoPallet(true);
        }
        else fixedPanel.setAutoPallet(false);
    }
    
    
    public void setFixedFrames(ArrayList<FixedFrame> frames){
        fixedFrames = frames;
        setFirstFixedFrame();
    }
    
    
    
    protected void initAdvancedComponents(){
    	collectionPanel = new CollectionLinePanel(bottomScroll, this, null);
        framePanel = new FixedActionFramePanel(topScroll, fixedPanel);
        
        collectionPanel.setForeground(new java.awt.Color(255, 255, 255));
        bottomScroll.setViewportView(collectionPanel);
        bottomScroll.setWheelScrollingEnabled(false);

        framePanel.setForeground(new java.awt.Color(255, 255, 255));
        topScroll.setViewportView(framePanel);
        topScroll.setWheelScrollingEnabled(false);
        framePanel.showCM(false);
        
        fixedPanel.setUpdatable(framePanel);
    }
    
    
    
    protected void initTimer(){
         ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    timeCount++;
                    int fn = collectionPanel.getSelectedNum();
                    if (fn<0 || fn>=fixedFrames.size()) return;
                    FixedFrame fixed = fixedFrames.get(fn);
                    boolean keepOn = (loopBox.isSelected() || collectionPanel.getSelectedNum()<collectionPanel.getCollection().size()-1);
                    if(timeCount>=fixed.duration.get() && keepOn){
                        collectionPanel.selectNext();
                        timeCount = 0;
                    }
                    if (!keepOn){
                        collectionPanel.select(0);
                        localPrevBut.setSelected(false);
                        timer.stop();
                    }
                }  
         };
         timer = new Timer(TIMER_DELAY,taskPerformer);
    }
    
    
    
    public void localPreview(boolean go){
        if (go){
            timer.start();
        }else timer.stop();
        fixedPanel.previewing = go;
        fixedPanel.setFocusable(!go);
    }
    
    
    @Override
	public void takeFrame(Frame frame) {
        frameLabel.setText("Frame " + (collectionPanel.getSelectedNum()+1) + " of " + fixedFrames.size());
		framePanel.setFrame(frame);
        if (frame!=null){
            int fn = collectionPanel.getSelectedNum();
            if (fn<0 || fn>=fixedFrames.size()) return;
            FixedFrame fixed = fixedFrames.get(fn); // it must exist
            fixedPanel.setFixedFrame(fixed);
            fixedPanel.setAllEnabled(true);
            framePanel.setFixedFrame(fixed);
            
            if (localPrevBut.isSelected() && soundBox.isSelected() && fn == soundFrame)
                new PlaySound(sound);
        }else{
            fixedPanel.setAllEnabled(false);
            framePanel.setFixedFrame(null);
        }
		repaint();
	}

    public void takeFrames(Frame[] frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    protected void setFirstFixedFrame(){
        fixedPanel.reset();
        fixedPanel.setFixedFrame(fixedFrames.get(collectionPanel.getSelectedNum()));
        takeFrame(collectionPanel.getCollection().get(0));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topScroll = new gui.Scroller();
        bottomScroll = new gui.Scroller();
        frameLabel = new javax.swing.JLabel();
        fixedPanel = new gui.FixedPanel();
        localPrevBut = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        soundBox = new javax.swing.JCheckBox();
        loopBox = new javax.swing.JCheckBox();

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

        localPrevBut.setText("Local Preview");
        localPrevBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                localPrevButActionPerformed(evt);
            }
        });

        jButton1.setText("Reset zoom");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("use space to start/stop local preview");

        soundBox.setSelected(true);
        soundBox.setText("play sound (s)");
        soundBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundBoxActionPerformed(evt);
            }
        });

        loopBox.setSelected(true);
        loopBox.setText("loop (l)");
        loopBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loopBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bottomScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(topScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fixedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jButton1)
                                                .addGap(63, 63, 63)
                                                .addComponent(localPrevBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(loopBox)
                                                .addGap(29, 29, 29)))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel1))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(soundBox))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(frameLabel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(fixedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(localPrevBut, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(soundBox)
                            .addComponent(loopBox))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(bottomScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_LEFT: collectionPanel.selectPrevious(); break;
            case KeyEvent.VK_RIGHT: collectionPanel.selectNext(); break;
            case KeyEvent.VK_UP: collectionPanel.selectPageUp();break;
            case KeyEvent.VK_DOWN: collectionPanel.selectPageDown(); break;
            case KeyEvent.VK_ESCAPE: localPrevBut.setSelected(false); localPreview(false); break;
            case KeyEvent.VK_S: soundBox.setSelected(!soundBox.isSelected()); break;
            case KeyEvent.VK_L: loopBox.setSelected(!loopBox.isSelected()); break;
            case KeyEvent.VK_SPACE: localPrevBut.setSelected(!localPrevBut.isSelected()); localPreview(localPrevBut.isSelected()); break;
        }
         requestFocus();
    }//GEN-LAST:event_formKeyPressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (!fixedPanel.hasFocus())
            requestFocus();
    }//GEN-LAST:event_formMouseMoved

    private void localPrevButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_localPrevButActionPerformed
        localPreview(localPrevBut.isSelected());
    }//GEN-LAST:event_localPrevButActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        framePanel.resetScale();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void soundBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_soundBoxActionPerformed

    private void loopBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loopBoxActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_loopBoxActionPerformed

 

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.Scroller bottomScroll;
    private gui.FixedPanel fixedPanel;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToggleButton localPrevBut;
    private javax.swing.JCheckBox loopBox;
    private javax.swing.JCheckBox soundBox;
    private gui.Scroller topScroll;
    // End of variables declaration//GEN-END:variables

    
}
