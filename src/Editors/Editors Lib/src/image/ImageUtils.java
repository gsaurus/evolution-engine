package image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.HashMap;
import javax.imageio.ImageIO;

import util.UBPoint;
import util.IntPoint;
import util.Pair;

/**
 * Utilities to manipulate BufferedImages and colors
 * @author Gil Costa
 */
public class ImageUtils {
	//graph configuration
	private static final GraphicsConfiguration
		gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
								.getDefaultScreenDevice()
								.getDefaultConfiguration();
	
	//-------------------------
	// ------- GETTERS -------
	
	/** @return the local graphics configuration  */
	public static final GraphicsConfiguration getGC(){
	    return gc;
	}
	
	
	//--------------------------------
	// ------- LOADING IMAGES -------
	
	/**
	 * Reads an image from a URL
	 * @param fileURL the url to the image file
	 * @return the read BufferedImage, or null if can't read
	 * @throws IOException if the file can't be loaded
	 */
	public static final BufferedImage loadImageFile(URL fileURL) throws IOException{
		BufferedImage img;
		img = ImageIO.read(fileURL);
		return img;
	}
	
	
	/**
	 * Reads an image from a file
	 * @param file the image file
	 * @return the read BufferedImage, or null if can't read
	 * @throws IOException if the file can't be loaded
	 */
	public static final BufferedImage loadImageFile(File file) throws IOException{
		BufferedImage img;
		img = ImageIO.read(file);
		return img;
	}
	
	/**
	 * Reads an image from a pathname
	 * @param fileName the path for the image file
	 * @return the read BufferedImage, or null if can't read
	 * @throws IOException if the file can't be loaded
	 */
	public static final BufferedImage loadImageFile(String fileName) throws IOException{
		return loadImageFile(new File(fileName));
	}
	
	
	
	//-----------------------------
	// ------- SAVE IMAGES -------
	
	public static final void saveImage(BufferedImage img, String format, File file) throws IOException{
		ImageIO.write(img, format, file);
	}
	
	//----------------------
	// ------- COPY -------
	
	/**
	 * copies an image, the resulting image have Alpha chanel
	 * @param img the image to be copied
	 * @return a copy of the provided image
	 */
	public static final BufferedImage copyImage(BufferedImage image){
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return newImage;
	}

	
	
	//-----------------------
	// ------- SPLIT -------
	
	/**
	 * split an image into cols x rows sub-images
	 * @param fullImg
	 * @param cols
	 * @param rows
	 * @return BufferedImage array containing the generated sub-images 
	 */
	public static final BufferedImage[] split(BufferedImage fullImg, int cols, int rows){
        
		int width = fullImg.getWidth()/cols;
		int height = fullImg.getHeight()/rows;
		// how many images contained in it
		int nX = fullImg.getWidth() / width;
		int nY = fullImg.getHeight()/ height;
		
		BufferedImage[] imgs = new BufferedImage[nX*nY];
		
		int transparency = fullImg.getColorModel().getTransparency();
		Graphics2D stripGC;
		
		for (int i=0; i < imgs.length; i++) {
			imgs[i] = ImageUtils.getGC().createCompatibleImage(width, height, transparency);
			   
			// create a graphics context
			stripGC = imgs[i].createGraphics();
			
			// copy image
			int row = i/nX;
			int col = i%nX;
			stripGC.drawImage(fullImg, 0,0, width,height,
								col*width,row*height,
								(col*width)+width,row*height+height,
							null);
			stripGC.dispose();
		}
		return imgs;
	}
	
	
	
	//----------------------
	// ------- CROP -------
	
	/**
	 * Crop rectangularly an image, removing it's extra transparent pixels 
	 * @param img the image to be processed
	 * @return a UBPoint indicating the first point
	 * of the resulting cropping in the original image 
	 */
	public static final Pair<IntPoint,BufferedImage> crop(BufferedImage img){
		if (img == null) return null;
		int cTop, cBottom, cLeft, cRight;
		int width = img.getWidth();
		int height = img.getHeight();
		boolean cropNow;
		
		
		// -- VERTICAL CROPPING --
		// Top
		cropNow = false;
		for(cTop = 0 ; cTop < height ; cTop++){
			for(int x=0; x<width ; x++){
                int alpha = img.getRGB(x,cTop)&0xff000000;
				cropNow = (alpha) != 0;
                if (cropNow) break;
            }
            if (cropNow) break;
        }
        
		
		// -- TEST EMPTY IMAGE --
		if (!cropNow)
            return null;
		// else, image have at least one pixel with alpha!=0
		
		// Bottom
		cropNow = false;
		for(cBottom = height-1 ; cBottom > cTop ; cBottom--){
			for(int x=0; x<width ; x++){
				cropNow = (img.getRGB(x,cBottom)&0xff000000) != 0;
                if (cropNow) break;
            }
            if (cropNow) break;
        }
		
		// -- HORIZONTAL CROPPING --
		// Left
		cropNow = false;
		for(cLeft=0; cLeft<width-1 ; cLeft++){
			for(int y = cTop ; y < cBottom ; y++){
				cropNow = (img.getRGB(cLeft,y)&0xff000000) != 0;
                if (cropNow) break;
            }
            if (cropNow) break;
        }
		
		// Right
		cropNow = false;
		for(cRight = width-1; cRight>cLeft ; cRight--){
			for(int y = cTop ; y < cBottom ; y++){
				cropNow = (img.getRGB(cRight,y)&0xff000000) != 0;
                if (cropNow) break;
            }
            if (cropNow) break;
        }
		
		// crop
        img = img.getSubimage(cLeft, cTop, cRight-cLeft+1, cBottom-cTop+1);
		return new Pair<IntPoint,BufferedImage>(new IntPoint(cLeft,cTop),img);
	}
	
	
	//-----------------------------
	// ------- MASS CENTRE -------

	/**
	 * Computes the mass centre based on the alpha channel of the pixels
	 * @param img the image where to compute mass center
	 * @return a UBPoint with the computed massCenter
	 */
	public static final IntPoint calculateMassCentre(BufferedImage img){
        if (img == null) return null;
		int cx=0, cy=0;
		int width = img.getWidth();
		int height = img.getHeight();
        int totalMass = 0;
		for(int i=0;i<width; i++)
			for(int j=0;j<height; j++){
				int alpha = 255-img.getRGB(i, j)&0xff;
                totalMass+=alpha;
				cx += i*alpha;
				cy += j*alpha;
			}
        if (totalMass == 0) return new IntPoint(0,0);
		return new IntPoint((int)cx/totalMass, (int)cy/totalMass);
	}
	
	
    
    
    public static final Color borderPredominance(BufferedImage img){
        int width = img.getWidth();
		int height = img.getHeight();
        HashMap<Integer,Integer> map;
        map = new HashMap<Integer,Integer>(width*2+height*2);
        int color = 0, most = 0;
		for(int x=0;x<width; x+=width-1)
			for(int y=0;y<height; y++){
                int c = img.getRGB(x, y);
                int val = 0;
                if (map.containsKey(c))
                    val = map.get(c);
                val++;
                if (val>most){
                    most = val;
                    color = c;
                }
                map.put(c, val);
            }
        
        for(int y=0;y<height; y+=height-1)
            for(int x=0;x<width; x++){			
                int c = img.getRGB(x, y);
                int val = 0;
                if (map.containsKey(c))
                    val = map.get(c);
                val++;
                if (val>most){
                    most = val;
                    color = c;
                }
                map.put(c, val);
            }
        return new Color(color);
    }
    
    
	
	
	//--------------------------------------------
	// ------ PIXEL and COLOR Manipulation ------
	//--------------------------------------------
	
	/** @return true if the pixel given by x and y is valid on the given image */
	public static final boolean isInside(int x, int y, BufferedImage img){
		return (x>=0 && y >= 0 && x < img.getWidth() && y < img.getHeight());
	}
	
	/** changes the color of the pixel (x,y), if it's a valid pixel of img */
	public static final void setPixel(BufferedImage img, int x, int y, int c){
		if (isInside(x,y,img)) img.setRGB(x, y, c);
	}
	
	/** changes the color RGB components of the pixel (x,y), if it's a valid pixel of img */
	public static final void setPixel(BufferedImage img, int x, int y, int r, int g, int b){
		if (isInside(x,y,img)) img.setRGB(x, y, composeColor(r,g,b));
	}
	
	
	// -------------------------------
	//    Colour component getters
	// -------------------------------
	public static final int getRed(int color){ return (color >> 16)&0xff; }
	public static final int getGreen(int color){ return (color>>8)&0x00ff;}
	public static final int getBlue(int color){ return color & 0x0000ff; }


	/**
	 * Returns the compounded RGB int value of the three components  
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @return the RGB composition value
	 */
	public static final int composeColor(int r, int g, int b){
		return r<<16 | g<<8 | b;
	}

	/**
	 * Returns the normalised value of a colour component,
	 * matching the value in the 0-255 
	 * @param component the component to normalise
	 * @return normalised value
	 */
	public static int normalised(int component){
		if (component <0) return 0;
		else if (component > 255) return 255;
		return component;
	}
    
    
    //	-- ROTATION --
	
	/**
	 * creates a new image witch is the original image rotated by rads radians
	 * @param src the original image
	 * @param rads the radians to rotate it
	 * @return the new image
	 */
	public static BufferedImage getRotated(BufferedImage src, double rads) { 
		if (src == null) return null;
		
		int transparency = src.getColorModel().getTransparency();
		BufferedImage dest =  gc.createCompatibleImage(
				src.getWidth(), src.getHeight(), transparency );
		Graphics2D g2d = dest.createGraphics();
		
		AffineTransform origAT = g2d.getTransform(); // save original transform
		
		// rotate the coord. system of the dest. image around its center
		AffineTransform rot = new AffineTransform(); 
		rot.rotate( -rads, src.getWidth()/2, src.getHeight()/2); 
		g2d.transform(rot); 
		
		g2d.drawImage(src, 0, 0, null);   // copy in the image
		
		g2d.setTransform(origAT);    // restore original transform
		g2d.dispose();
		
		return dest; 
	}
    
    
    public static BufferedImage getFlipedX(BufferedImage im){
        int imWidth = im.getWidth();
        int imHeight = im.getHeight();
        int transparency = im.getColorModel().getTransparency();

        BufferedImage copy =  gc.createCompatibleImage(imWidth, imHeight, transparency);
        Graphics2D g2d = copy.createGraphics();
        // g2d.setComposite(AlphaComposite.Src);

        // draw in the flipped image
        g2d.drawImage(im, imWidth, 0,  0, imHeight, 0, 0,  imWidth, imHeight, null);
        //g2d.drawImage(im, 0, imHeight,  imWidth, 0, 0, 0,  imWidth, imHeight, null);
        g2d.dispose();

        return copy; 
    }
    
    //	-- SQUARE THE IMAGE --
	public static Pair<BufferedImage,Pair<Integer,Integer>> square(BufferedImage src) { 
		
		int transparency = src.getColorModel().getTransparency();
        Pair<Integer,Integer> pt = new Pair<Integer,Integer>(0,0);
        pt.second = Math.max(src.getWidth()-src.getHeight(),0);
        pt.first = Math.max(src.getHeight()-src.getWidth(),0);
        
        int wh = Math.max(src.getWidth(), src.getHeight());
		BufferedImage dest =  gc.createCompatibleImage(
				wh, wh, transparency );
		Graphics2D g2d = dest.createGraphics();
		
		g2d.drawImage(src, pt.first/2, pt.second/2, null);
		g2d.dispose();
		
		return new Pair<BufferedImage,Pair<Integer,Integer>>(dest,pt); 
	}
}
