package image;

import java.awt.image.BufferedImage;



/**
 * Interface for Image filtering.
 * It applies the filter directly on the input image.
 * To ensure that the original image is preserved
 * it is needed to make a copy before apply the filter.
 * 
 * @author Gil Costa
 *
 */
public interface IFilter {
	/**
	 * Apply filter to a generic image
	 * @param img the image to be filtered
	 */
	public void applyTo(BufferedImage img);
}
