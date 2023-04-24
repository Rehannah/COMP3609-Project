import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Coconut {  //Projectile Motion

   	private static final int XSIZE = 40;
   	private static final int YSIZE = 40;

   	private JFrame window;
	private Level2Player player;
   	private int x;
   	private int y;
   	private int dx = 2;
   	private int dy = 2;
	private int xPos, yPos; 	// location from which to generate projectile

   	private int initialVelocityX = 50;
   	private int initialVelocityY = 10;

   	private Dimension dimension;

   	double timeElapsed;
	boolean active;

    private Image coconutImage;
    private Animation currentAnim;
	private HashMap<String, Animation> animations;

	public Coconut (JFrame w, Level2Player player) {
        window = w;
        this.player = player;
        // this.pirates=pirates;

		active = false;
		timeElapsed = 0;
      	dimension = window.getSize();
		

        initialiseAnimations();
		currentAnim = animations.get("roll");
	}

    public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Coconut/1.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Coconut/2.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Coconut/3.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Coconut/4.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Coconut/5.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Coconut/6.png"), 100);
		animations.put("roll", anim);


		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/coconut split.png"), 150);
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
 
	
	public boolean isActive() {
		return active;
	}

	public void activate() {
		
		xPos = player.getX()+20;
		yPos = player.getY() + 20;
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

      	y = yPos - y;			// y is the height at which ball is thrown
      		
        if (x < -XSIZE || x > dimension.width) {		// outside left and right boundaries
	    	deActivate();
			return;
		}
		// System.out.println ("x = " + x + " y "+y);
        if (y > window.getHeight() - 200 - YSIZE) {	 		// below floor; extrapolate to bring ball on top of floor.
	    	// System.out.println ("Y = " + y);
	    	int amountOver = y - (200 - YSIZE); 
	    	y = 200 - YSIZE;
	    	// System.out.println ("New Y = " + y);
            double fractionOver = (amountOver * 1.0) / (y - oldY);
	    	timeElapsed = timeElapsed - (1 - fractionOver) * 0.5;
	    	x = (int) (initialVelocityX * timeElapsed);
            deActivate();
		}
    }
	

   	public void draw (Graphics2D g2) { 

		if (!active)
			return;
			
		if(currentAnim != null) {
			if(currentAnim.isStillActive())
				currentAnim.update();
			else
				currentAnim.start();
			coconutImage = currentAnim.getImage();
		}

		if(coconutImage == null)
		System.out.println("null im");

		if (player.getDirection() == 2)		// going right: add x to xPos
            g2.drawImage(coconutImage, x+xPos, y, XSIZE, YSIZE, null);
		else					// going left: subtract x from xPos
            g2.drawImage(coconutImage, xPos-x, y, XSIZE, YSIZE, null);
		
   	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
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