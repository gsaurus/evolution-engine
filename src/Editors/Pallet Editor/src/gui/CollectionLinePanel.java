/*
 * topPanel.java
 *
 * Created on 26 November 2008, 15:08
 */

package gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import frames.Frame;
import frames.FramesCollection;
import frames.IFramesTaker;

import java.awt.Stroke;
import java.util.ArrayList;
import util.IMarkTaker;
import util.IMarkable;

/**
 * Panel that holds a FrameCollection, shows them and allows multiple sellections
 * @author  Gil Costa
 */
public class CollectionLinePanel extends CollectionPanel implements IMarkable{
	/** default version id */
	private static final long serialVersionUID = 1L;

	//---------------------
	// ---- CONSTANTS ----
	//---------------------

    
	protected static final Color MARKED_COLOR = Color.ORANGE;
	protected static final Color MARKED_BACK_COLOR = new Color(200,150,50);

	//------------------
	// ---- FIELDS ----
	//------------------

    protected ArrayList<Integer> indexes;
    protected ArrayList<Boolean> markedOnes;
    
    protected IMarkTaker markTaker;

    

	// ----------------------------
	//  ----   CONSTRUCTORS   ----
	// ----------------------------
	/** Creates new form FramePanel */
	public CollectionLinePanel(Scroller scroller, IFramesTaker taker, IMarkTaker markTaker) {
		super(scroller, taker);
        this.markTaker = markTaker;
        indexes = new ArrayList<Integer>();
        markedOnes = new ArrayList<Boolean>();
	}


	// -----------------------
	//  ----   SETTERS   ----
	// -----------------------
	
    
    /** set frame */
	public void setCollection(FramesCollection original, int[] indexes){
        if (original != null && indexes!=null){
            this.indexes = new ArrayList<Integer>(indexes.length);
            this.markedOnes = new ArrayList<Boolean>(indexes.length);
            collection = new FramesCollection();
            for(int i:indexes){
                collection.add(original.get(i));
                this.indexes.add(i);
                this.markedOnes.add(false);
            }
        }
        else{
            this.collection = original;
            int size = 0;
            if (original!=null) size = original.size();
            this.indexes = new ArrayList<Integer>(size);
            this.markedOnes = new ArrayList<Boolean>(size);
            for(int i=0; i<size; i++){
                this.indexes.add(i);
                this.markedOnes.add(false);
            }
        }
        if (collection == null) return;
		recomputeDimensions();
        repaint();
	}
    
    
    
    
    
    
    public void addFrame(Frame original, int index){
        if (original == null) return;
        int pos = selected + 1;
        if (pos <0) pos = 0;
        if (pos > collection.size()) pos = collection.size();
        collection.add(pos,original);
        this.indexes.add(pos,index);
        this.markedOnes.add(pos,false);
        selectNext();
        recomputeDimensions();
    }
    
    
    @Override
    public void deleteSelected(){
        if (collection == null || collection.isEmpty()) return;
        collection.remove(selected);
        if (indexes.size()>=selected)
            indexes.remove(selected);
        if (markedOnes.size()>=selected)
            markedOnes.remove(selected);
        if (markTaker!=null) markTaker.takeMark(null, selected);
        if (selected>=collection.size()) selectPrevious();
        else select(selected);
        recomputeDimensions();
    }
    
    
    public void delete(int index){
        for(int i=0; i< indexes.size(); i++)
            if(index == indexes.get(i)){
                collection.remove(i);
                indexes.remove(i);
                markedOnes.remove(i);
                if (markTaker!=null) markTaker.takeMark(null, i);
                recomputeDimensions();
                if (selected==collection.size()) selected--;
                select(selected);
                return;
            }
    }
    
    public void mark(int i, boolean mark){
        if (i<0 || i>=markedOnes.size()) return;
        markedOnes.set(i, mark);
        this.repaintCell(i);
    }
    
    public void markSelected(){
        mark(selected,!markedOnes.get(selected));
    }
    public void markSelected(boolean mark){
        mark(selected,mark);
    }
    
    public boolean isMarked(int index){
        return markedOnes.get(index);
    }
    
    
	// -----------------------
	//  ----   GETTERS   ----
	// -----------------------
	
    public int getIndex(int i){
        if (i<0 || i>=indexes.size()) return 0;
        return indexes.get(i);
    }
    
    public boolean hasIndex(int index){
        for(int i:indexes)
            if (i==index) return true;
        return false;
    }
    
    public ArrayList<Integer> getIndexes(){ return indexes; }
    public ArrayList<Boolean> getMarkedOnes(){return markedOnes; }
    
    /*
    @Override
    public int columns(){
        if (dim==null) return 0;
        return collection.size();
    }
    
    @Override
    public int rows(){
        if (dim==null) return 0;
		return 1;
    }
    */



	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 600, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 200, Short.MAX_VALUE)
		);
	}// </editor-fold>//GEN-END:initComponents



	// -----------------------------------
	//  ------   MOUSE LISTENERS   ------
	// -----------------------------------

	
	// -----------------------
	//  ----   REPAINT   ----
	// -----------------------
//	public void repaintCM(){
//	int x = frame.getCM().getX();
//	int y = frame.getCM().getY();
//	repaintCursor(x,y);
//	}
//	protected void repaintCursor(int x, int y){
//	repaint(new Rectangle(x-CM_RADIUS,y-CM_RADIUS,CM_SIZE,CM_SIZE));
//	}




	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (collection == null || collection.isEmpty() || dim == null) return;

		int dw = (int)(dim.width * scaleFactor);
		int dh = (int)(dim.height * scaleFactor);
		if (dw<=0) dw = 1;
		if (dh<=0) dh = 1;
		int columns = getWidth()/dw;
		if (columns == 0) columns =1;
        
        
        g.setColor(GRID_COLOR);
		// draw grid columns
		for(int i=0; i<=columns; i++){
			g.drawLine(i*dw, 0, i*dw, getHeight());
		}
		// draw grid rows
		int rows = getHeight()/dh;
		for(int i=0; i<=rows; i++){
			g.drawLine(0, i*dh, getWidth(), i*dh);
		}
        
        
		// draw each frame
		for(int i=0; i<collection.size(); i++){
			BufferedImage img = collection.get(i).getImage();
			int imgX = (i%columns)*dw;
			int imgY = (i/columns)*dh;
            if (markedOnes.size()>i && markedOnes.get(i)){
                g.setColor(MARKED_BACK_COLOR);
				g.fillRect(imgX, imgY, dw, dh);
            }
            else if (i == selected){
				// selected frame, draw background first
				g.setColor(SELECTED_BACK_COLOR);
				g.fillRect(imgX, imgY, dw, dh);
			}
            int imgW = (int)(img.getWidth()*scaleFactor);
			int imgH = (int)(img.getHeight()*scaleFactor);
            g.drawImage(img,imgX,imgY,imgW,imgH,this);
            if (markedOnes.size()>i && markedOnes.get(i)){
                g.setColor(MARKED_COLOR);
                Graphics2D g2d = (Graphics2D)g;
                Stroke old = g2d.getStroke();
                g2d.setStroke(SELECTED_STROKE);
                g.drawRect(imgX, imgY, dw, dh);
                g2d.setStroke(old);
            }
			
		}
        
        // draw a rectangle around the selected image
        g.setColor(SELECTED_COLOR);
        Graphics2D g2d = (Graphics2D)g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(SELECTED_STROKE);
        int imgX = (selected%columns)*dw;
		int imgY = (selected/columns)*dh;
        g.drawRect(imgX, imgY, dw, dh);
        g2d.setStroke(old);
        
	}









	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables

}
