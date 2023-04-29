import java.awt.Image;
import java.awt.Graphics2D;
import java.util.HashMap;
import javax.swing.JFrame;


/**
    The TreasureAnimation class creates an animation of an opening treasure chest. 
*/
public class TreasureAnimation {
	private GameWindow window;
	
	// Animation animation;

	private int x;		// x position of animation
	private int y;		// y position of animation

	private int width;
	private int height;

	private Image treasureImage;
    private Animation currentAnim;
	private HashMap<String, Animation> animations;

	public TreasureAnimation(GameWindow w) {
		window=w;

		initialiseAnimations();
		currentAnim = null;
		
		x = window.getWidth()+70;
		y = window.getHeight()-390;
		treasureImage = ImageManager.loadImage("images/treasure/treasure chest0000.png");
		height=100;
		width=100;
	}

	public void initialiseAnimations(){
		// load images for animation

		Image animImage1 = ImageManager.loadImage("images/treasure/treasure chest0000.png");
		Image animImage2 = ImageManager.loadImage("images/treasure/treasure chest0001.png");
		Image animImage3 = ImageManager.loadImage("images/treasure/treasure chest0002.png");
		Image animImage4 = ImageManager.loadImage("images/treasure/treasure chest0003.png");
		Image animImage5 = ImageManager.loadImage("images/treasure/treasure chest0004.png");
		Image animImage6 = ImageManager.loadImage("images/treasure/treasure chest0005.png");
		Image animImage7 = ImageManager.loadImage("images/treasure/treasure chest0006.png");

		// create animation object and insert frames

		animations = new HashMap<>();
		Animation animation = new Animation(false);	// play once only
		animation.addFrame(animImage1, 200);
		animation.addFrame(animImage2, 200);
		animation.addFrame(animImage3, 200);
		animation.addFrame(animImage4, 200);
		animation.addFrame(animImage5, 200);
		animation.addFrame(animImage6, 200);
		animation.addFrame(animImage7, 200);
		// animation.addFrame(animImage8, 200);
		animations.put("open", animation);
	}

	public void activate() {
		if(currentAnim == null){
			currentAnim = animations.get("open");
			SoundManager.getInstance().playSound("win", false);
			currentAnim.start();
		}
	}


	public void draw(Graphics2D g2) {
		if(currentAnim != null && currentAnim.isStillActive()) {
			g2.drawImage(currentAnim.getImage(), x, y, width, height, null);
		}
		g2.drawImage(treasureImage, x, y, width, height, null);
	}

	public void update() {
		if (!window.isVisible ()) return;

		if(currentAnim != null && currentAnim.isStillActive()) 
				currentAnim.update();
	}

    public void draw(Graphics2D g2, double scaleX, double scaleY, double scaleHeight, int factor) {
		if(currentAnim != null){
			if(currentAnim.isStillActive())			
				treasureImage = currentAnim.getImage();
			else	
				window.winGame();
		}

		int x2 = x-factor*(int)Math.round(scaleX*x);
		int y2 = y - factor*(int)Math.round(scaleY*y);
		int h =(int)Math.round(Math.pow(scaleHeight,factor)*height);
		int w = (int)(width*1.0/height*h);		
		
		g2.drawImage(treasureImage, x2, y2, w, h, null);
	}
}