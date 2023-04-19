import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Coconut {  //Projectile Motion

   	private static final int XSIZE = 10;
   	private static final int YSIZE = 10;

   	private JFrame window;
	private Level2Player player;
   	private int x = 0;
   	private int y = 200;
   	private int dx = 2;
   	private int dy = 2;
	private int xPos, yPos;			// location from which to generate projectile
   	private int initialVelocityX = 25;
   	private int initialVelocityY = 25;

   	private Dimension dimension;

   	double timeElapsed;
	boolean active;

    private Image coconutImage;
    private Animation currentAnim;
	private HashMap<String, Animation> animations;
    private ArrayList pirates;

	public Coconut (JFrame w, Level2Player player, ArrayList pirates) {
        window = w;
        this.player = player;
        this.pirates=pirates;

		active = false;
		timeElapsed = 0;
      	dimension = window.getSize();
		xPos = 25;
		yPos = 425 - YSIZE;

        initialiseAnimations();
		currentAnim = animations.get("roll");
	}


	public boolean isActive() {
		return active;
	}

    public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
      
		Image stripImage = ImageManager.loadImage("images/myimages/coconut.png");
		anim = stripAnimation(stripImage, anim, 5);
		animations.put("roll", anim);


      anim = new Animation(true);
      stripImage = ImageManager.loadImage("images/myimages/coconut split.png");
      animations.put("split", anim);
	}

    // load animation from strip file
   public Animation stripAnimation(Image stripImage, Animation anim, int num) {
    int imageWidth = (int) stripImage.getWidth(null) / num;
        int imageHeight = stripImage.getHeight(null);

        for (int i=0; i<num; i++) {
            BufferedImage frameImage = new BufferedImage (imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage, 
                    0, 0, imageWidth, imageHeight,
                    i*imageWidth, 0, (i*imageWidth)+imageWidth, imageHeight,
                    null);

            anim.addFrame(frameImage, 100);
        }
    return anim;
    }
 
	public void activate() {
		xPos = player.getX();
		yPos = player.getY();
		active = true;
		timeElapsed = 0;
	}
	

	public void deActivate() {
		active = false;
	}


   	public void update () {  

		int oldY;

      		if (!window.isVisible ()) return;
     
		timeElapsed = timeElapsed + 0.5;

      		x = (int) (initialVelocityX * timeElapsed);

		oldY = y;
      		y = (int) (initialVelocityY * timeElapsed - 4.9 * timeElapsed * timeElapsed);

      		if (y > 0)
	 		y = yPos - y;			// yPos is the height at which ball is thrown
      		else
         		y = yPos + y * -1;

         	if (x < -XSIZE || x > dimension.width) {		// outside left and right boundaries
	    		active = false;
			return;
		}

         	if (y > 425 - YSIZE) {	 		// below floor; extrapolate to bring ball on top of floor.
	    		System.out.println ("Y = " + y);
	    		int amountOver = y - (425 - YSIZE); 
	    		y = 425 - YSIZE;
	    		System.out.println ("New Y = " + y);
            		double fractionOver = (amountOver * 1.0) / (y - oldY);
	    		timeElapsed = timeElapsed - (1 - fractionOver) * 0.5;
	    		x = (int) (initialVelocityX * timeElapsed);
            		active = false;
		}
         }
	

   	public void draw (Graphics2D g2) { 

		if (!active)
			return;
			
		if (player.getDirection() == 2)		// going right: add x to xPos
            g2.drawImage(coconutImage, x+xPos, y, XSIZE, YSIZE, null);
		else					// going left: subtract x from xPos
            g2.drawImage(coconutImage, xPos-x, y, XSIZE, YSIZE, null);
   	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, coconutImage.getWidth(null), coconutImage.getHeight(null));
	}	

    // public boolean collidesWithPirate() {
    //     boolean collidesWith = false;
    //     Rectangle2D.Double myRect = getBoundingRectangle();

    //     for (int i=0;i<=pirates.size();i++) {
    //         Object pirate = pirates.get(i);
    //         Rectangle2D.Double pirateRect = pirate.getBoundingRectangle();
    //     }
        
    //     if (myRect.intersects(coconutRect) || myRect.intersects(coconutRect); 
    // }

}