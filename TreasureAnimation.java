import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import javax.swing.JFrame;


/**
    The TreasureAnimation class creates an animation of a flying bird. 
*/
public class TreasureAnimation {
	private JFrame window;
	
	Animation animation;

	private int x;		// x position of animation
	private int y;		// y position of animation

	private int width;
	private int height;

    private SoundManager soundManager;		// reference to SoundManager to play clip

	private Level2Player player;

	private Image treasureImage;
   private Animation currentAnim;
	private HashMap<String, Animation> animations;
	private boolean isActive;

	public TreasureAnimation(JFrame w, Level2Player player) {
		this.player = player;
		window=w;

		initialiseAnimations();
		currentAnim = animations.get("open");

		isActive=false;
		soundManager = SoundManager.getInstance();	
						// get reference to Singleton instance of SoundManager	}
		
		x = window.getWidth()-500;
		y = window.getHeight()-300;
		playSound();

		height=150;
		width=150;
	}

	public void initialiseAnimations(){
		// load images for animation

		Image animImage1 = ImageManager.loadImage("images/myimages/treasure/treasure chest0000.png");
		Image animImage2 = ImageManager.loadImage("images/myimages/treasure/treasure chest0001.png");
		Image animImage3 = ImageManager.loadImage("images/myimages/treasure/treasure chest0002.png");
		Image animImage4 = ImageManager.loadImage("images/myimages/treasure/treasure chest0003.png");
		Image animImage5 = ImageManager.loadImage("images/myimages/treasure/treasure chest0004.png");
		Image animImage6 = ImageManager.loadImage("images/myimages/treasure/treasure chest0005.png");
		Image animImage7 = ImageManager.loadImage("images/myimages/treasure/treasure chest0006.png");
		Image animImage8 = ImageManager.loadImage("images/myimages/treasure/treasure chest0007.png");

		// create animation object and insert frames

		animations = new HashMap<>();
		animation = new Animation(false);	// play once only
		animation.addFrame(animImage1, 200);
		animation.addFrame(animImage2, 200);
		animation.addFrame(animImage3, 200);
		animation.addFrame(animImage4, 200);
		animation.addFrame(animImage5, 200);
		animation.addFrame(animImage6, 200);
		animation.addFrame(animImage7, 200);
		animation.addFrame(animImage8, 200);
		animations.put("open", animation);
	}

	public boolean isActive() {
		return isActive;
	 }

	public void activate() {
		isActive=true;
		System.out.println("Chest is activated");
	 }


	public void draw(Graphics2D g2) {
		g2.drawImage(animation.getImage(), x, y, width, height, null);

		if(currentAnim != null){
			if(currentAnim.isStillActive()) {
				currentAnim.update();
				System.out.println("anim updated");
			}
			else {
				currentAnim.start();
				System.out.println("anim started");
			}
				
        	treasureImage = currentAnim.getImage();
		}
	}

	public void update() {
		if (!window.isVisible ()) return;

		if (!animation.isStillActive()) {
			stopSound();
			return;
		}
		else{
			currentAnim.update();
		}

		
	}

	public void playSound() {
		soundManager.playSound("birdSound", true);
	}


	public void stopSound() {
		soundManager.stopSound("birdSound");
	}


	public boolean collidesWithPlayer () {
		Rectangle2D.Double myRect = getBoundingRectangle();
		Rectangle2D.Double playerRect = player.getBoundingRectangle();
		
		if (myRect.intersects(playerRect)) {
			System.out.println ("Collision with player!");
			return true;
		}
		else
			return false;
	}

	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, width, height);
	}	

}
