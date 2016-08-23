package image;

import java.awt.image.BufferedImage;

import image.ImageUtils;

/**
 * This filter colorise each pixel with a specified weigh 
 * @author Gil Costa
 */
public class ColoriserFilter implements IFilter{
	private int color;		// the color to colorise images
	private double weigh;	// the weigh of the color [0.0 , 1.0]

	public ColoriserFilter(int color, double weigh){
		this.color = color;
		this.weigh = weigh;
	}

	@Override
	public void applyTo(BufferedImage img){
		for(int x = 0 ; x < img.getWidth() ; x++){
			for(int y = 0 ; y < img.getHeight() ; y++){
				// get pixel color components
				int c = img.getRGB(x, y);
				int r,g,b;
				r = ImageUtils.getRed(c);
				g = ImageUtils.getGreen(c);
				b = ImageUtils.getBlue(c);
				// get coloring components
				int newR,newG,newB;
				newR = ImageUtils.getRed(color);
				newG = ImageUtils.getGreen(color);
				newB = ImageUtils.getBlue(color);
				// colorise and normalise
				r = ImageUtils.normalised((int)(newR*weigh + r*(1-weigh)));
				g = ImageUtils.normalised((int)(newG*weigh + g*(1-weigh)));
				b = ImageUtils.normalised((int)(newB*weigh + b*(1-weigh)));
				// affect the pixel with the resulting color
				img.setRGB(x, y, ImageUtils.composeColor(r, g, b));
			}
		}
	}	

}
