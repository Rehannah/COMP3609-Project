import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.awt.Image;

public class Level2Player {

	private static final int DX = 8;	// amount of X pixels to move in one keystroke
	
	private JFrame window;		// reference to the JFrame on which player is drawn
	private GamePanel panel;

	private int x;			// x-position of player's sprite
	private int y;			// y-position of player's sprite
	//private int position;

	private Dimension dimension;

	private Image playerImage;
	private Animation currentAnim;
	private HashMap<String, Animation> animations;

	private boolean jumping;
	private int initialVelocityX;
	private int initialVelocityY;

	private int direction;
	private double timeElapsed;
	private int startY;
	private int startX;

	public Level2Player (JFrame window) {
		this.window = window;

		playerImage = ImageManager.loadImage("images/boy/Idle/1.png");	  
		dimension = window.getSize();
		initialiseAnimations();
		currentAnim = animations.get("idle");
		x = window.getWidth()/4;
		y = window.getHeight()-400;

		jumping = false;
		initialVelocityX = 30;
		initialVelocityY = 75;

		direction=2;
	}

	public Level2Player (JFrame window, GamePanel p) {
		this.window = window;
		panel = p;

		playerImage = ImageManager.loadImage("images/boy/Idle/1.png");	  
		dimension = window.getSize();
		initialiseAnimations();
		currentAnim = animations.get("idle");
		x = window.getWidth()/4;
		y = window.getHeight()-400;
		
		jumping = false;
		initialVelocityX = 30;
		initialVelocityY = 75;

		direction=2;
	}

	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/boy/Run/1.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/boy/Run/2.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/boy/Run/3.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/boy/Run/4.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/boy/Run/5.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/boy/Run/6.png"), 150);
		animations.put("runRight", anim);
		
		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/boy/RunLeft/1.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/boy/RunLeft/2.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/boy/RunLeft/3.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/boy/RunLeft/4.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/boy/RunLeft/5.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/boy/RunLeft/6.png"), 150);
		animations.put("runLeft", anim);

		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/boy/Idle/1.png"), 150);
		animations.put("idle", anim);

		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/boy/IdleLeft/1.png"), 150);
		animations.put("idleLeft", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/boy/Throw/1.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/Throw/2.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/Throw/3.png"), 100);
		animations.put("throw", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/boy/ThrowLeft/1.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/ThrowLeft/2.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/boy/ThrowLeft/3.png"), 100);
		animations.put("throwLeft", anim);
		
		// anim = new Animation(false);
		// anim.addFrame(ImageManager.loadImage("images/boy/Jump/1.png"), 50);
		// anim.addFrame(ImageManager.loadImage("images/boy/Jump/2.png"), 300);
		// anim.addFrame(ImageManager.loadImage("images/boy/Jump/3.png"), 300);
		// anim.addFrame(ImageManager.loadImage("images/boy/Jump/4.png"), 300);
		// anim.addFrame(ImageManager.loadImage("images/boy/Jump/5.png"), 150);
		// anim.addFrame(ImageManager.loadImage("images/boy/Jump/6.png"), 100);
		// animations.put("jump", anim);
		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/boy/Jump/2.png"), 160);
		anim.addFrame(ImageManager.loadImage("images/boy/Jump/3.png"), 160);
		anim.addFrame(ImageManager.loadImage("images/boy/Jump/4.png"), 160);
		anim.addFrame(ImageManager.loadImage("images/boy/Jump/5.png"), 160);
		//anim.addFrame(ImageManager.loadImage("images/boy/Jump/6.png"), 100);
		animations.put("jump", anim);

		// anim = new Animation(false);
		// anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/1.png"), 50);
		// anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/2.png"), 300);
		// anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/3.png"), 300);
		// anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/4.png"), 300);
		// anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/5.png"), 150);
		// anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/6.png"), 100);
		// animations.put("jumpLeft", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/2.png"), 160);
		anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/3.png"), 160);
		anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/4.png"), 160);
		anim.addFrame(ImageManager.loadImage("images/boy/JumpLeft/5.png"), 160);
		//anim.addFrame(ImageManager.loadImage("images/boy/Jump/6.png"), 100);
		animations.put("jumpLeft", anim);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Image getImage() {
		return playerImage;
	}

	public synchronized void move (int direction) {

		if(jumping){
			updateJumping();
			return;
		}

		if(direction == -1)
			return;
				
	    if (!window.isVisible ()) 
			return;
			

		if (direction == 1) { //left
			this.direction=direction;
			currentAnim = animations.get("runLeft");
			if(!currentAnim.isStillActive())
				currentAnim.start();

			x = x - DX;
		}
		else{
			if (direction == 2) { //right
				this.direction=direction;
				currentAnim = animations.get("runRight");
				if(!currentAnim.isStillActive())
					currentAnim.start();

				x = x + DX;

			}
			else{
				if(direction == 3){
					currentAnim = animations.get("idleLeft");
					currentAnim.start();
				}
				else{
					if(direction == 4){
						currentAnim = animations.get("idle");
						currentAnim.start();
					}
					else if(direction == 5){
						jump();
					}
				} 
			}
		}
		if (x < 70) {			// stuck within the left bounds
			x = 70;
		}
		if (x > window.getWidth()-getImage().getWidth(null)) {			// stuck within the right bounds
			x = window.getWidth()-getImage().getWidth(null);
		}
	}	

	private void jump () { 
		if(!window.isVisible()) 
			return;

		if(direction%2 == 0){
			if(x+292 > dimension.width - getImage().getWidth(null) - 20)	//not enough space to jump
				return;	
		}
		else if(x -292 < 70)
			return;		//not enough space to jump
		
		jumping = true;
		if(direction % 2 == 0)
			currentAnim = animations.get("jump");
		else
			currentAnim = animations.get("jumpLeft");
		currentAnim.start();

		timeElapsed = 0;		

		startY = y;
		startX = x;
	}

	private void updateJumping() {

		if (!window.isVisible ()) 
			return;
		
		timeElapsed += 0.5;
		
		int oldX = x;
		int oldY = y;
		int dx, dy;
		int playerHeight = getImage().getHeight(null);	 		// below floor; extrapolate to bring player on top of floor.
      	
      	dx = (int) (initialVelocityX * timeElapsed);
		dy = (int) (initialVelocityY * timeElapsed - 4.9 * timeElapsed * timeElapsed);

		y = startY - dy;			// y is the height at which ball is thrown
      		
        if (y > window.getHeight() - 200 - playerHeight){// System.out.println ("Y = " + y);
	    	int amountOver = y - (window.getHeight() - 200 - playerHeight); 
	    	y = window.getHeight() - 200 - playerHeight;
	    	// System.out.println ("New Y = " + y);
            double fractionOver = (amountOver * 1.0) / (y - oldY);
	    	timeElapsed = timeElapsed - (1 - fractionOver) * 0.5;
	    	dx = (int) (initialVelocityX * timeElapsed);
			jumping = false;
		}
		
		if(direction % 2 == 0)
			x = startX + dx;
		else
			x = startX - dx;		
        
		timeElapsed = timeElapsed + 0.5;
		
		if(!jumping)
			currentAnim.stop();
	}

	

	public void startThrow(){
		if(direction % 2 == 0)
			currentAnim = animations.get("throw");
		else 
			currentAnim = animations.get("throwLeft");
		currentAnim.start();
	}
	
	
	public void draw(Graphics2D g2){
		
		Image oldImage = playerImage;
		if(currentAnim != null){
			if(currentAnim.isStillActive())
				currentAnim.update();
			else{
				if(direction % 2 == 0)
					currentAnim = animations.get("idle");
				else 
					currentAnim = animations.get("idleLeft");
				currentAnim.start();
			}
			playerImage = currentAnim.getImage();
		}
		if((playerImage.equals(ImageManager.loadImage("images/boy/Throw/2.png")) ||
		    playerImage.equals(ImageManager.loadImage("images/boy/ThrowLeft/2.png"))) &&
		    oldImage != playerImage){

			panel.addCoconut();		
		}
		
		g2.drawImage(playerImage, x, y, null);
	}
	
	public boolean collidesWithPlayer (int x, int y) {
		Rectangle2D.Double myRectangle = getBoundingRectangle();
		return myRectangle.contains(x, y);
	}

	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, playerImage.getWidth(null), playerImage.getHeight(null));
	}	

	public int getDirection() {
		return direction;
	}
}