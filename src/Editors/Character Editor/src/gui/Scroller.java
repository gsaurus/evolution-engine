package gui;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class Scroller extends JScrollPane{
	/** default id */
	private static final long serialVersionUID = 1L;

	
	
	// --------------------------
	//  ------   SCROLL   ------
	// --------------------------

	public void setPosition(double horizontal, double vertical){
		JScrollBar hb = getHorizontalScrollBar();
		JScrollBar vb = getVerticalScrollBar();
		int vx,vy;
		vx = hb.getMinimum() + (int)(hb.getMaximum()*horizontal);
		vy = vb.getMinimum() + (int)(vb.getMaximum()*vertical);
		hb.setValue(vx);
		vb.setValue(vy);
	}
   
    // TODO:
    /*
    public void centerAt(double horizontal, double vertical){
        JScrollBar hb = getHorizontalScrollBar();
		JScrollBar vb = getVerticalScrollBar();
		int vx,vy;
		vx = hb.getMinimum() + (int)(hb.getMaximum()*horizontal)-?;
		vy = vb.getMinimum() + (int)(vb.getMaximum()*vertical)-?;
		hb.setValue(vx);
		vb.setValue(vy);
    }
    */
	
	public double getHPosition(){
		JScrollBar hb = getHorizontalScrollBar();
		return hb.getValue()*1./(hb.getMaximum()-hb.getMinimum());
	}
	public double getVPosition(){
		JScrollBar vb = getVerticalScrollBar();
		return vb.getValue()*1./(vb.getMaximum()-vb.getMinimum());
	}
	
	public double getHIncrement(){
		JScrollBar hb = getHorizontalScrollBar();
		return hb.getVisibleAmount()*1./(hb.getMaximum()-hb.getMinimum());
	}
	public double getVIncrement(){
		JScrollBar vb = getVerticalScrollBar();
		return vb.getVisibleAmount()*1./(vb.getMaximum()-vb.getMinimum());
	}
	
}
