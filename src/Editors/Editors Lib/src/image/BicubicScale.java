package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import image.ImageUtils;

/**
 * Class that performs a scale with bicubic interpolation  
 * @author Gil Costa
 * note: adapted from http://www.gamedev.net/community/forums/topic.asp?topic_id=336744
 * @see http://www.gamedev.net/community/forums/topic.asp?topic_id=336744
 */
public class BicubicScale implements IFilter{
	// On this version, to simplify, the inexistent neighbours 
	// of the border pixels are set to a default colour
	public static Color BORDER_COLLOUR  = Color.WHITE;

	
	@Override
	public void applyTo(BufferedImage img) {
		resample(ImageUtils.copyImage(img),img);
	}
	
	public static void resample(BufferedImage img, BufferedImage imgOut){
		
		int oldWidth = img.getWidth();
		int oldHeight = img.getHeight();
		int newWidth = imgOut.getWidth();
		int newHeight = imgOut.getHeight();
		
		// x,y coordinates for pixels in old and new images
		float ox,oy;
		int nx,ny;
		
		// t variation parameter
		int[] t = new int[4];

		// variation factors 
		float dx = 1.f*oldWidth / newWidth;
		float dy = 1.f*oldHeight / newHeight;

		for (ny = 0; ny < newHeight; ++ny){
			oy = ny * dy;
			int ioy = (int)oy;
			for (nx = 0; nx < newWidth; ++nx){
				ox = nx * dx;

				// separate integer and fractional portions of ox
				int iox = (int)ox;
				float oxfrac = frac(ox);
				
				
				int neigh[][] = new int[4][4];	// neighbourhood
				for (int i=-1; i<3; i++){
					for (int j=-1; j<3; j++){
						if (ioy+j>0 && ioy+j < oldHeight && iox + i>0 && iox+i < oldWidth)
							neigh[i+1][j+1] = img.getRGB(iox+i, ioy+j);
						else neigh[i+1][j+1] = BORDER_COLLOUR.getRGB();
					}
				}
				
				// interpolate in horizontal lines
				t[0] = interpolate(neigh[0][0], neigh[1][0], neigh[2][0], neigh[3][0], oxfrac);
				t[1] = interpolate(neigh[0][1], neigh[1][1], neigh[2][1], neigh[3][1], oxfrac);
				t[2] = interpolate(neigh[0][2], neigh[1][2], neigh[2][2], neigh[3][2], oxfrac);
				t[3] = interpolate(neigh[0][3], neigh[1][3], neigh[2][3], neigh[3][3], oxfrac);				

				// interpolate the interpolated lines in one pixel
				imgOut.setRGB(nx,ny, interpolate(t[0],t[1],t[2],t[3], frac(oy)));
			}
		}
	}

	/**
	 * interpolation function
	 * @return the interpolation of a, b, c and d in the "time" t
	 */
	private static int interpolate(int a, int b, int c, int d, float t){
		int r1 = cubic(ImageUtils.getRed(a), ImageUtils.getRed(b), ImageUtils.getRed(c), ImageUtils.getRed(d), t);
		int g1 = cubic(ImageUtils.getGreen(a), ImageUtils.getGreen(b), ImageUtils.getGreen(c), ImageUtils.getGreen(d), t);
		int b1 = cubic(ImageUtils.getBlue(a), ImageUtils.getBlue(b), ImageUtils.getBlue(c), ImageUtils.getBlue(d), t);
		r1 = ImageUtils.normalised(r1);
		g1 = ImageUtils.normalised(g1);
		b1 = ImageUtils.normalised(b1);
		return ImageUtils.composeColor(r1,g1,b1);
	}

	/** cubic value of v0, v1, v2 and v3 coefficients in the variable t */
	private static int cubic(int v0,int v1,int v2,int v3, float t) {
		int p = (v3 - v2) - (v0 - v1);
		int q = (v0 - v1) - p;
		int r = v2 - v0;
		int s = v1;
		float tSqrd = t * t;

		return (int)((p * (tSqrd * t)) + (q * tSqrd) + (r * t) + s);
	}

	/**
	 * The fractional part of the value f
	 * @return the fractional part of f
	 */
	public static final float frac(float f){
		return f-(int)f;
	}

}
