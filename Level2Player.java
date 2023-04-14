import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.awt.Image;

public class Level2Player {

	private static final int DX = 8;	// amount of X pixels to move in one keystroke
	private static final int DY = 32;	// amount of Y pixels to move in one keystroke

	private JFrame window;		// reference to the JFrame on which player is drawn
	private BackgroundManager bgManager;

	private int x;			// x-position of player's sprite
	private int y;			// y-position of player's sprite
	//private int position;

	private Graphics2D g2;
	private Dimension dimension;

	private Image playerImage;
	private Animation currentAnim;
	private HashMap<String, Animation> animations;


	public Level2Player (JFrame window) {
		this.window = window;

		playerImage = ImageManager.loadImage("images/myimages/boy/Idle/1.png");	  
		dimension = window.getSize();
		initialiseAnimations();
		currentAnim = animations.get("run");
		x = window.getSize().width;
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
		animations.put("run", anim);		
	}

	public int getXPos() {
		return x;
	}

	public int getYPos() {
		return y;
	}

	public Image getImage() {
		return playerImage;
	}

	public synchronized void move (int direction) {;
	    if (!window.isVisible ()) 
			return;

		if (direction == 1) {
			x = x - DX;
			if (x < 2) {			// stuck within the right bounds
				x = 2;
			}
		}
		else{
			if (direction == 2) {
				x = x + DX;
			}
		}
	}	

	public void draw(Graphics2D g2){
		// // draw the background first
		// bgManager.draw (g2);
		// draw player

		g2.drawImage(getImage(), x, y, null);
	}
  
	public boolean collidesWithPlayer (int x, int y) {
		Rectangle2D.Double myRectangle = getBoundingRectangle();
		return myRectangle.contains(x, y);
	}

	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, playerImage.getWidth(null), playerImage.getHeight(null));
	}	

}