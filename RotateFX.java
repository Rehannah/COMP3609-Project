import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.lang.Math;

public class RotateFX {

	private int x;
	private int y;

	private float angle;			// angle controls the amount of rotation


	public RotateFX () {
		angle = 20;				// set to 20 degrees
	}


	public void draw (Graphics2D g2, BufferedImage image, int x, int y, int w, int h) {

		int width, height;

		width = image.getWidth();		// find width of image
		height = image.getHeight();	    // find height of image

		BufferedImage dest = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dest.createGraphics();
   		AffineTransform origAT = g2d.getTransform();  // save original transform
  
    	// rotate the coordinate system of the destination image around its center    
		AffineTransform rotation = new AffineTransform(); 
    	rotation.rotate(Math.toRadians(angle*-1), width/2, height/2); 
    	g2d.transform(rotation); 

    	g2.drawImage(dest, x, y, w, h, null);

        g2d.setTransform(origAT);    		// restore original transform    	
   		g2d.dispose();
	}
}