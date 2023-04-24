import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JFrame;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Image;

public class Level2Player {

	private static final int DX = 8;	// amount of X pixels to move in one keystroke
	private static final int DY = 32;	// amount of Y pixels to move in one keystroke

	private JFrame window;		// reference to the JFrame on which player is drawn
	private GamePanel panel;
	private BackgroundManager bgManager;

	private int x;			// x-position of player's sprite
	private int y;			// y-position of player's sprite
	//private int position;

	private Dimension dimension;

	private Image playerImage;
	private Animation currentAnim;
	private HashMap<String, Animation> animations;

	private int direction;

	public Level2Player (JFrame window) {
		this.window = window;

		playerImage = ImageManager.loadImage("images/myimages/boy/Idle/1.png");	  
		dimension = window.getSize();
		initialiseAnimations();
		currentAnim = animations.get("idle");
		x = window.getWidth()/4;
		y = window.getHeight()-400;

		direction=2;
	}

	public Level2Player (JFrame window, GamePanel p) {
		this.window = window;
		panel = p;

		playerImage = ImageManager.loadImage("images/myimages/boy/Idle/1.png");	  
		dimension = window.getSize();
		initialiseAnimations();
		currentAnim = animations.get("idle");
		x = window.getWidth()/4;
		y = window.getHeight()-400;

		direction=2;
	}

	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/1.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/2.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/3.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/4.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/5.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/6.png"), 150);
		animations.put("runRight", anim);
		
		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/1.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/2.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/3.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/4.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/5.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/6.png"), 150);
		animations.put("runLeft", anim);

		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Idle/1.png"), 150);
		animations.put("idle", anim);

		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/IdleLeft/1.png"), 150);
		animations.put("idleLeft", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Throw/1.png"), 75);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Throw/2.png"), 75);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Throw/3.png"), 75);
		animations.put("throw", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/ThrowLeft/1.png"), 75);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/ThrowLeft/2.png"), 75);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/ThrowLeft/3.png"), 75);
		animations.put("throwLeft", anim);
		
		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/1.png"), 50);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/2.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/3.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/4.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/5.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/6.png"), 100);
		animations.put("jump", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/JumpLeft/1.png"), 50);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/JumpLeft/2.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/JumpLeft/3.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/JumpLeft/4.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/JumpLeft/5.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/JumpLeft/6.png"), 100);
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
		this.direction=direction;

	    if (!window.isVisible ()) 
			return;
			

		if (direction == 1) { //left
			currentAnim = animations.get("runLeft");
			if(!currentAnim.isStillActive())
				currentAnim.start();

			x = x - DX;
			if (x < 290) {			// stuck within the left bounds
				x = 290;
			}
		}
		else{
			if (direction == 2) { //right
				currentAnim = animations.get("runRight");
				if(!currentAnim.isStillActive())
					currentAnim.start();

				x = x + DX;
				if (x > window.getWidth()-getImage().getWidth(null)) {			// stuck within the right bounds
					x = window.getWidth()-getImage().getWidth(null);
				}

			}
			else{
				if(direction == 3){
					currentAnim = animations.get("idleLeft");
					currentAnim.start();
				}
				else if(direction == 4){
					currentAnim = animations.get("idle");
					currentAnim.start();
				}
			}
		}
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
		if((playerImage.equals(ImageManager.loadImage("images/myimages/boy/Throw/2.png")) ||
		    playerImage.equals(ImageManager.loadImage("images/myimages/boy/ThrowLeft/2.png"))) &&
		    oldImage != playerImage){

			Coconut c =  new Coconut(window, this);
			c.activate();	
			panel.addCoconut(c);		
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


// import java.awt.Dimension;
// import java.awt.Graphics2D;
// import javax.swing.JFrame;

// import java.awt.geom.AffineTransform;
// import java.awt.geom.Rectangle2D;
// import java.awt.image.AffineTransformOp;
// import java.awt.image.BufferedImage;
// import java.util.HashMap;
// import java.awt.Image;

// public class Level2Player {

// 	private static final int DX = 8;	// amount of X pixels to move in one keystroke
// 	private static final int DY = 32;	// amount of Y pixels to move in one keystroke

// 	private JFrame window;		// reference to the JFrame on which player is drawn
// 	private BackgroundManager bgManager;

// 	private int x;			// x-position of player's sprite
// 	private int y;			// y-position of player's sprite
// 	//private int position;

// 	private Graphics2D g2;
// 	private Dimension dimension;

// 	private Image playerImage;
// 	private Image idleImage;
// 	private Animation currentAnim;
// 	private HashMap<String, Animation> animations;

// 	private int direction;

// 	public Level2Player (JFrame window) {
// 		this.window = window;

// 		idleImage = ImageManager.loadImage("images/myimages/boy/Idle/1.png");	
// 		playerImage = idleImage;  
// 		dimension = window.getSize();
// 		initialiseAnimations();
// 		currentAnim = animations.get("idle");
// 		currentAnim.start();
// 		x = window.getWidth()/4;
// 		y = window.getHeight()-400;

// 		direction=2;
// 	}

// 	public void initialiseAnimations(){
// 		animations = new HashMap<>();
// 		Animation anim = new Animation(false);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/1.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/2.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/3.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/4.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/5.png"), 125);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/6.png"), 150);
// 		animations.put("runRightOnce", anim);

// 		anim = new Animation(true);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/1.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/2.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/3.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/4.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/5.png"), 125);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/6.png"), 150);
// 		animations.put("runRight", anim);
		
// 		anim = new Animation(false);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/1.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/2.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/3.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/4.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/5.png"), 125);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/6.png"), 150);
// 		animations.put("runLeftOnce", anim);

// 		anim = new Animation(true);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/1.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/2.png"), 150);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/3.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/4.png"), 175);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/5.png"), 125);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/RunLeft/6.png"), 150);
// 		animations.put("runLeft", anim);

// 		anim = new Animation(false);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Throw/1.png"), 50);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Throw/2.png"), 75);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Throw/3.png"), 50);
// 		animations.put("throw", anim);

		

// 		anim = new Animation(true);
// 		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Idle/1.png"), 150);
// 		animations.put("idle", anim);

// 	}

// 	public int getX() {
// 		return x;
// 	}

// 	public int getY() {
// 		return y;
// 	}

// 	public Image getImage() {
// 		return playerImage;
// 	}

// 	public synchronized void move (int direction) {
// 		this.direction=direction;

// 	    if (!window.isVisible ()) 
// 			return;

// 		if(direction == 0){
// 			currentAnim = animations.get("idle");
// 			currentAnim.start();
// 		}
// 		else{
// 			if (direction == 1) { //left once
// 				currentAnim = animations.get("runLeft");				
// 				currentAnim.start();
// 				x = x - DX;
// 				if (x < 290) 		// stuck within the left bounds
// 					x = 290;
// 			}
// 			else{
// 				if (direction == 2) { //right once 
// 					currentAnim = animations.get("runRight");
// 					currentAnim.start();
// 					x = x + DX;
// 					if (x > window.getWidth()-getImage().getWidth(null)) 			// stuck within the right bounds
// 						x = window.getWidth()-getImage().getWidth(null);					

// 				}
// 				// else{
// 				// 	if(direction == 3){	//left continuously
// 				// 		currentAnim = animations.get("runLeft");
// 				// 		currentAnim.start();
// 				// 		x = x - DX;
// 				// 		if (x < 290) 			// stuck within the left bounds
// 				// 			x = 290;
// 				// 	}
// 				// 	else{
// 				// 		if (direction == 4) { //right continuously 
// 				// 			currentAnim = animations.get("runRight");
// 				// 			currentAnim.start();
// 				// 			x = x + DX;
// 				// 			if (x > window.getWidth()-getImage().getWidth(null)) 			// stuck within the right bounds
// 				// 				x = window.getWidth()-getImage().getWidth(null);
// 				// 		}
// 				// 	}
// 				// }
// 			}
// 		}
// 	}	

// 	public void draw(Graphics2D g2){
// 		g2.drawImage(playerImage, x, y, null);
		
// 		if(currentAnim != null){
// 			if(currentAnim.isStillActive())
// 				currentAnim.update();
// 			else{
// 				currentAnim = animations.get("idle");
// 				currentAnim.start();
// 			}
// 			playerImage = currentAnim.getImage();
// 		}
// 	}
	
// 	public boolean collidesWithPlayer (int x, int y) {
// 		Rectangle2D.Double myRectangle = getBoundingRectangle();
// 		return myRectangle.contains(x, y);
// 	}

// 	public Rectangle2D.Double getBoundingRectangle() {
// 		return new Rectangle2D.Double (x, y, playerImage.getWidth(null), playerImage.getHeight(null));
// 	}	

// 	public int getDirection() {
// 		return direction;
// 	}
// }