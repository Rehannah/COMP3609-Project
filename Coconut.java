import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
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
	private int xPos, yPos; 	// location from which to generate projectile

   	private int initialVelocityX = 35;
   	private int initialVelocityY = 30;

   	private Dimension dimension;

   	double timeElapsed;
	boolean active;

    private Image coconutImage;
    private Animation currentAnim;
	private HashMap<String, Animation> animations;
	private int direction;

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
		anim.addFrame(ImageManager.loadImage("images/boy/Coconut/1.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/Coconut/2.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/Coconut/3.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/Coconut/4.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/Coconut/5.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/Coconut/6.png"), 100);
		animations.put("roll", anim);


		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/coconut split.png"), 150);
		animations.put("split", anim);
	}
 
	
	public boolean isActive() {
		return active;
	}

	public void activate() {
		
		xPos = player.getX()+20;
		yPos = player.getY() + 20;
		x = xPos;
		y = yPos;
		if (player.getDirection() % 2 == 0)
			direction = 1;
		else
			direction = -1;
		active = true;
		timeElapsed = 0;
		currentAnim.start();
	}
	

	public void deActivate() {
		active = false;
	}


   	public void update () {  
		int oldY = y;
		int dx, dy;

      	if (!window.isVisible ()) return;

      	dx = (int) (initialVelocityX * timeElapsed);
		dy = (int) (initialVelocityY * timeElapsed - 4.9 * timeElapsed * timeElapsed);

		y = yPos - dy;			// y is the height at which ball is thrown
      		
        if (y > window.getHeight() - 200 - YSIZE) {	 		// below floor; extrapolate to bring ball on top of floor.
	    	int amountOver = y - (200 - YSIZE); 
	    	y = 200 - YSIZE;
            double fractionOver = (amountOver * 1.0) / (y - oldY);
	    	timeElapsed = timeElapsed - (1 - fractionOver) * 0.5;
	    	dx = (int) (initialVelocityX * timeElapsed);
      		deActivate();
		}
		
		x = xPos + dx*direction;

		if (x < -XSIZE || x > dimension.width) {		// outside left and right boundaries
	    	deActivate();
			return;
		}
        
		timeElapsed = timeElapsed + 0.5;
    }
	

   	public void draw (Graphics2D g2) { 

		if (!active)
			return;
			
		if(currentAnim != null) {
			if(currentAnim.isStillActive())
				currentAnim.update();
			else{
				deActivate();
				return;
			}
			coconutImage = currentAnim.getImage();
		}

		g2.drawImage(coconutImage, x, y, XSIZE, YSIZE, null);
   	}


	public Rectangle2D.Double getBoundingRectangle() {		
		return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
	}	

    public boolean collidesWithPirate(ArrayList<Pirate> pirates) {        

        for (int i=0;i<pirates.size();i++) {
			Pirate pirate = pirates.get(i);				
			if (getBoundingRectangle().intersects(pirate.getAttackedRectangle())){
				deActivate();
				pirate.decreaseLives();
				return true;
			}
		}
		return false;
    }

}