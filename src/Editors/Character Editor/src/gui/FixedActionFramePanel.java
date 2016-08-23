/*
 * ActionFramePanel.java
 *
 * Created on 26 November 2008, 15:08
 */

package gui;

import characters.Animation;
import characters.FixedFrame;
import characters.GrabPoint;
import characters.WeaponPoint;
import frames.Frame;
import frames.FramesCollection;
import game.GameInvariants;
import image.ImageUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


import java.io.IOException;
import java.util.ArrayList;
import pallet.Pallet;
import util.ICoordinatesTaker;
import util.IntPoint;
import util.Pair;


/**
 * Panel that holds a frame, shows it and allows it's mass center manipulation
 * @author  Gil Costa
 */
public class FixedActionFramePanel extends ActionFramePanel{
    /** default serial ID*/
	private static final long serialVersionUID = 1L;
	
	//---------------------
	// ---- CONSTANTS ----
	//---------------------
	
    // TODO BufferedImages for head, weapon and grabed
    
    protected static final Color[] ACTION = {new Color(0,50,255),
                                             new Color(255,0,0),
                                             new Color(0,230,00)
                                        };
    protected static final Color[] SELECTED_ACTION = 
                                        {
                                             new Color(50,150,255),
                                             new Color(255,100,50),
                                             new Color(50,255,50)
                                        };
    
	
	
	//------------------
	// ---- FIELDS ----
	//------------------
	
	protected boolean[] show = {true,true,true};
    FixedFrame fixedFrame;
    // things to show
    protected FramesCollection head;
    protected FramesCollection weapons;
    protected int weaponIndex;
    protected FramesCollection enemy;
    protected characters.Character enemyChar;
    protected boolean flipedEnemy;
    protected Pallet pallet;
    
	
	
	// ----------------------------
	//  ----   CONSTRUCTORS   ----
	// ----------------------------
	/** Creates new form ActionFramePanel */
    public FixedActionFramePanel(Scroller scroller, ICoordinatesTaker coordinatesTaker) {
    	super(scroller, coordinatesTaker);
    }
    
    public void setHead(FramesCollection head){
        this.head = head;
        repaint();
    }
    
    public void setWeapons(FramesCollection f){
        this.weapons = f;
    }
    public void setWeaponIndex(int index){
        weaponIndex = index;
    }
    
    public void setEnemy(characters.Character enemyChar, FramesCollection f){
        this.enemyChar = enemyChar;
        this.enemy = f;
    }
    
    public void setFlipedEnemy(boolean flip){
        flipedEnemy = flip;
    }
    
    public void setPallet(Pallet p){
        pallet = p;
        setFrame(frame);
    }
    
    public void setAutoPallet(String animName, int num){        
        pallet = new Pallet();
        String fileName = GameInvariants.EDITORS_WORKING_DIR + GameInvariants.PALLETS_DIR + "\\";
        fileName += animName.substring(0,animName.lastIndexOf('.'));
        fileName += "_" + num + "." + GameInvariants.PALLET_EXTENSION;
        pallet.setFileName(fileName);
        try { pallet.load();
        } catch (IOException ex) {
            pallet = null;
        }
        setFrame(frame);
    }
    
    
    @Override
    public void setFrame(Frame f){
        Frame newFrame;
        if (f!= null && pallet != null){
            newFrame = new Frame(GameInvariants.FRAMES_VERSION);
            newFrame.setCM(f.getCM());
            newFrame.setCMOn(f.isCMOn());
            newFrame.setImg(pallet.applyToImage(f.getImage()));
        }else newFrame = f;
        super.setFrame(newFrame);
    }
    
    public void setFixedFrame(FixedFrame fixedFrame){
        selected = -1;
        this.fixedFrame = fixedFrame;
        if(fixedFrame == null || getFrame() == null){
            show[0] = false;
            show[1] = false;
            show[2] = false;
            actionPoints = new ArrayList();
        }
        else{
//            int cx = getFrame().getCM().getX();
//            int cy = getFrame().getCM().getY();
            show[0] = true;
            show[1] = fixedFrame.weapon!=null && fixedFrame.weapon.x!=WeaponPoint.INVALID_WEAPON_POINT;
            show[2] = fixedFrame.grab != null && fixedFrame.grab.baseX!=GrabPoint.INVALID_GRAB_POINT;
            actionPoints = new ArrayList(3);
            if (fixedFrame.head != null)
                actionPoints.add(new IntPoint(fixedFrame.head.x,fixedFrame.head.y));
            else actionPoints.add(null);
            if (fixedFrame.weapon!=null)
                actionPoints.add(new IntPoint(fixedFrame.weapon.x,fixedFrame.weapon.y));
            else actionPoints.add(null);
            if (fixedFrame.grab != null)
                actionPoints.add(new IntPoint(fixedFrame.grab.baseX,fixedFrame.grab.baseY));
            else actionPoints.add(null);
        }
    }
    
    
    @Override
    protected void repaintCursor(int x, int y){
		repaint();  // force repaint all the frame
    }
    
    
    
    protected void drawHead(Graphics g, int deltaX, int deltaY){
        if (head == null) return;
        int headIndex = fixedFrame.head.frame.get();
        Frame headFrame = head.get(headIndex);
        if (headFrame == null)
            headFrame = head.get(0);
        if (headFrame!= null){
            BufferedImage img = headFrame.getImage();
            if (headFrame.getCM() != null){
                deltaX -=headFrame.getCM().getX();
                deltaY -=headFrame.getCM().getY();
            }
            deltaX*=scaleFactor;
            deltaY*=scaleFactor;

            int headW = (int)(img.getWidth()*scaleFactor);
            int headH = (int)(img.getHeight()*scaleFactor);
            //g.setColor(Color.BLACK);
            //g.drawRect(deltaX,deltaY, headW, headH);
            g.drawImage(img, DX+deltaX,DY+deltaY,headW, headH,this);
        }
    }
    
    
    protected void drawEnemy(Graphics g, int deltaX, int deltaY){
        if (fixedFrame.grab == null || enemyChar == null || enemy == null) return;
        int frameIndex = fixedFrame.grab.anim.get();
        final Animation anim = enemyChar.getAnim(frameIndex);
        if (anim == null) return;
        FixedFrame fixed = anim.fixedFrames.get(0);
        if (fixed!= null && fixed.frameIndex>=0 && fixed.frameIndex<enemy.size()){
            // get frame
            Frame tmpFrame = enemy.get(fixed.frameIndex);
            // get radians
            double rads = Math.toRadians(fixedFrame.grab.angle.get()*360./255);
            // get image
            BufferedImage img = tmpFrame.getImage();
            // flip
            if (flipedEnemy) img = ImageUtils.getFlipedX(img);
            
            // rotate
            Pair<BufferedImage,Pair<Integer,Integer>> square = ImageUtils.square(img);
            img = square.first;
            img = ImageUtils.getRotated(img, rads);
            
            // CM rotation
            if (tmpFrame.getCM() != null){
                IntPoint tmpPoint = new IntPoint(tmpFrame.getCM().getX()+square.second.first/2, tmpFrame.getCM().getY()+square.second.second/2);
                if (flipedEnemy) tmpPoint.setX(img.getWidth()-tmpPoint.getX());
                tmpPoint.rotateAround(img.getWidth()/2, img.getHeight()/2, -rads);
                //tmpPoint.rotateAround(tmpFrame.getCM().getX(), tmpFrame.getCM().getY(), rads);
                deltaX -=tmpPoint.getX();
                deltaY -=tmpPoint.getY();
            }
            deltaX*=scaleFactor;
            deltaY*=scaleFactor;

            // draw
            int w = (int)(img.getWidth()*scaleFactor);
            int h = (int)(img.getHeight()*scaleFactor);
            //g.setColor(Color.BLACK);
            //g.drawRect(deltaX,deltaY, w, h);
            g.drawImage(img, DX+deltaX,DY+deltaY,w, h,this);
        }
    }
    
    
    protected void drawWeapon(Graphics g, int deltaX, int deltaY){
        if (fixedFrame.weapon == null || weapons == null) return;
        if (weaponIndex <0 || weaponIndex > weapons.size()) return;
        
        // get frame
        Frame tmpFrame = weapons.get(weaponIndex);
        // get radians
        double rads = Math.toRadians(fixedFrame.weapon.angle.get()*360./255);
        // get image
        BufferedImage img = tmpFrame.getImage();

        // rotate
        Pair<BufferedImage,Pair<Integer,Integer>> square = ImageUtils.square(img);
        img = square.first;
        img = ImageUtils.getRotated(img, rads);

        // CM rotation
        if (tmpFrame.getCM() != null){
            IntPoint tmpPoint = new IntPoint(tmpFrame.getCM().getX()+square.second.first/2, tmpFrame.getCM().getY()+square.second.second/2);
            tmpPoint.rotateAround(img.getWidth()/2, img.getHeight()/2, -rads);
            //tmpPoint.rotateAround(tmpFrame.getCM().getX(), tmpFrame.getCM().getY(), rads);
            deltaX -=tmpPoint.getX();
            deltaY -=tmpPoint.getY();
        }
        deltaX*=scaleFactor;
        deltaY*=scaleFactor;

        // draw
        int w = (int)(img.getWidth()*scaleFactor);
        int h = (int)(img.getHeight()*scaleFactor);
        //g.setColor(Color.BLACK);
        //g.drawRect(deltaX,deltaY, w, h);
        g.drawImage(img, DX+deltaX,DY+deltaY,w, h,this);
    }
    
    
    
    
    protected void drawImage(Graphics g, BufferedImage img, int imgW, int imgH){
        boolean drawWeapon = actionPoints.size()>1 && actionPoints.get(1)!=null && fixedFrame.weapon!=null;
        int fcx = frame.getCM().getX();
        int fcy = frame.getCM().getY();
        if (drawWeapon && !fixedFrame.weapon.inFront)
            drawWeapon(g, fcx+actionPoints.get(1).getX(), fcy + actionPoints.get(1).getY());
        g.drawImage(img, DX,DY,imgW, imgH,this);
        if (drawWeapon && fixedFrame.weapon.inFront)
            drawWeapon(g,fcx + actionPoints.get(1).getX(), fcy + actionPoints.get(1).getY());
    }
    

	@Override
	public void paintComponent(Graphics g) {
		clearScr(g);
        if (frame == null || frame.getCM()==null) return;
		BufferedImage img = frame.getImage();
        int fcx = frame.getCM().getX();
        int fcy = frame.getCM().getY();
		if (img!=null){
			int imgW = (int)(img.getWidth()*scaleFactor);
			int imgH = (int)(img.getHeight()*scaleFactor);
            
            //DX = Math.max((this.getWidth()-imgW)/2,0);
            //DY = Math.max((this.getHeight()-imgH)/2,0);
            DX = Math.max((int)(this.getWidth()/2-frame.getCM().getX()*scaleFactor),0);
            DY = Math.max((int)(this.getHeight()*0.8-frame.getCM().getY()*scaleFactor),0);
            
            // draw black rectangle arround
            if (showActionPoints){
                g.setColor(Color.BLACK);
                g.drawRect(DX, DY, imgW, imgH);
            }
            
            // draw enemy and image
            if (fixedFrame != null && fixedFrame.grab != null && fixedFrame.grab.baseY>frame.getCM().getY()){
                drawImage(g,img, imgW, imgH);
                if (actionPoints != null && actionPoints.size()>2 && actionPoints.get(2)!=null)
                    drawEnemy(g,fcx + actionPoints.get(2).getX(), fcy + actionPoints.get(2).getY());
            }
            else{
                if (actionPoints != null && actionPoints.size()>2 && actionPoints.get(2)!=null)
                    drawEnemy(g,fcx + actionPoints.get(2).getX(), fcy + actionPoints.get(2).getY());
                drawImage(g,img, imgW, imgH);
            }
            
            if (showCM){
                int cx = (int)(frame.getCM().getX()*scaleFactor+(0.5*scaleFactor));
                int cy = (int)(frame.getCM().getY()*scaleFactor+(0.5*scaleFactor));
                // draw CM
                if (showActionPoints)
                    drawCross(g,cx,cy, Color.WHITE);
            }
            if (actionPoints!=null){
                int size = Math.min(actionPoints.size(),ACTION.length);
                for(int i=0; i<size; i++){
                    if (show[i]){
                        IntPoint p = actionPoints.get(i);
                        if (p==null) continue;
                        Color color;
                        if (i == selected) color = SELECTED_ACTION[i];
                        else color = ACTION[i];
                        int cx = (int)((fcx + p.getX())*scaleFactor+(0.5*scaleFactor));
                        int cy = (int)((fcy + p.getY())*scaleFactor+(0.5*scaleFactor));
                        
                        // draw head
                        if (i == 0) drawHead(g,fcx + p.getX(),fcy + p.getY());
                        
                        // draw cross point
                        if (showActionPoints)
                            drawCross(g,cx,cy, color);
                    }
                }
            }
		}
		
	}


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
