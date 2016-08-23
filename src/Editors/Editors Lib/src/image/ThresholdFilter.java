package image;

import java.awt.image.BufferedImage;


/**
 * This filter just look at the pixels value and set them to black or white
 * if value is lower or higher than a threshold value, respectively.  
 * @author Gil Costa
 *
 */
public class ThresholdFilter implements IFilter{
	// threshold
	private short threshold;

	public ThresholdFilter(short threshold){
		this.threshold = threshold;
	}
	public ThresholdFilter(){
		threshold = 128;
	}

	@Override
	public void applyTo(BufferedImage img){
		for(int x = 0 ; x < img.getWidth() ; x++){
			for(int y = 0 ; y < img.getHeight() ; y++){
				if ((img.getRGB(x, y) & 0xff) >= threshold)
					img.setRGB(x, y, 0xffffffff);
				else img.setRGB(x, y, 0x00000000);
			}
		}
	}	

}
