/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Gil
 */
public class TransparentMaker implements IFilter{
    protected Color color;
    
    public TransparentMaker(Color transparentColor){
        color = transparentColor;
    }
    
    public void applyTo(BufferedImage img) {
		int markerRGB = color.getRGB();
		for (int j=0; j<img.getHeight(); j++) {
			for (int i=0; i<img.getWidth(); i++) {
				Color oldColor = new Color(img.getRGB(i,j), true);
				if (oldColor.getRGB() == markerRGB)
					img.setRGB(i, j, 0);
	             }
	         }
	}

}
