package image;

import java.awt.image.BufferedImage;

import image.ImageUtils;

/**
 * Abstract class to be extended by filters that does convolution operations
 * @author Gil Costa
 */
public class AConvolutionFilter implements IFilter{
	protected float[] mx;		// convolution matrix as an array
	int mxH,mxW;				// matrix width and height
	
	/** get matrix value at (x,y) */
	float mxAt(int x, int y){
		return mx[y*mxH + x];
	}

	/** set matrix value at (x,y) */
	void setMxAt(int x, int y, float f){
		mx[y*mxH + x] = f;
	}

	/** normalise val in 0 to max scale*/
	int normalisedValue(int val, int max) {
		if (val < 0)  return 0;
		if (val > max) return max;
		return val;
	}


	/** convolution algorithm */
	void simpleConvolution(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		// centre of the matrix
		int cx = mxW/2;
		int cy = mxH/2;

		// a copy where to get the original pixels
		// because the original image will be directly changed
		BufferedImage tmpImg = ImageUtils.copyImage(img);
		int r, g, b;

		// for each pixel in the image
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				float sumR = 0.f, sumG = 0.f, sumB = 0.f;
				int color;
				// for each neighbour
				for(int dy = -cy; dy <= cy; dy++) {
					for(int dx = -cx; dx <= cx; dx++){
						// normalize values to repeat the border values (when in the borders)
						color = tmpImg.getRGB(normalisedValue(x+dx, width-1),normalisedValue(y+dy, height-1));
						// get RGB components
						r = ImageUtils.getRed(color);
						g = ImageUtils.getGreen(color);
						b = ImageUtils.getBlue(color);
						// get matrix weight for this pixel
						float val = mxAt(cx+dx, cy+dy);
						// and add it to the RGB accumulation values
						sumR += r*val;
						sumG += g*val;
						sumB += b*val;
					}
				}
				// the new componentes are composed and set in the image
				int newR = ImageUtils.normalised((int)sumR);
				int newG = ImageUtils.normalised((int)sumG);
				int newB = ImageUtils.normalised((int)sumB);
				color = ImageUtils.composeColor(newR, newG, newB);
				img.setRGB(x,y,color);
			}
		}
	}

	/**
	 * scales the matrix values to normalise it
	 * the sum of all matrix values must be 1
	 */
	void scale(float f){
		for(int i=0; i<9; i++){
			mx[i]*=f;
		}
	}

	// Apply method does the convolution
	@Override
	public void applyTo(BufferedImage img){
		simpleConvolution(img);
	}

}
