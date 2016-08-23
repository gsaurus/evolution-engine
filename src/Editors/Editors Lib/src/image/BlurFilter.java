package image;

/**
 * A lowpass convolution filter
 * @author Gil Costa
 */
public class BlurFilter extends AConvolutionFilter {
	public BlurFilter(){
		// instantiate the kernel
		mx = new float[9];
		mx[0]= 1; mx[1]=2; mx[2]=1;
		mx[3]= 2; mx[4]=1; mx[5]=2;
		mx[6]= 1; mx[7]=2; mx[8]=1;
		scale(1/13f);
		mxW=3;
		mxH=3;
	}
}
