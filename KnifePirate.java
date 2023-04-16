import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Image;
import java.util.HashMap;

public class KnifePirate {

   private JFrame window;

   private int x;
   private int y;

   private int width;
   private int height;

   private int dx;		// increment to move along x-axis
   private int dy;		// increment to move along y-axis

   private Random random;

   private Level2Player player;
   private SoundManager soundManager;
  
   private boolean soundPlayed;

   private Image pirateImage;
   private Animation currentAnim;
	private HashMap<String, Animation> animations;

   private Coconut coconut;

   boolean isActive;

   public KnifePirate (JFrame w, Level2Player player) {
      window = w;
      this.coconut = coconut;

      width = 230;
      height = 230;

      random = new Random();

      x = window.getWidth()/2;
      y = window.getHeight()/2-50;

      dx = 2;
      dy = 1;

      this.player = player;

      pirateImage = ImageManager.loadImage ("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_000.png");
      soundManager = SoundManager.getInstance();

      soundPlayed = false;

      isActive = true;

      initialiseAnimations();
		currentAnim = animations.get("idle");
   }

   public boolean isActive() {
      return isActive;
   }

   
	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_006.png"), 150);
		animations.put("attack", anim);
		
		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_006.png"), 150);
		animations.put("idle", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_006.png"), 150);
		animations.put("walk", anim);
	}


   public void draw (Graphics2D g2) {

      g2.drawImage(pirateImage, x, y, width, height, null);
      
      if(currentAnim != null){
			if(currentAnim.isStillActive())
				currentAnim.update();
			else
				currentAnim.start();
         pirateImage = currentAnim.getImage();
		}

   }


   private void chase() {

      if (x > player.getX())
	  x = x - dx;
      else
      if (x < player.getX())
 	  x = x + dx;

      if (y > player.getY())
	  y = y - dy;
      else
      if (y < player.getY())
 	  y = y + dy;
   }


   private void flee() {

      if (x > player.getX())
	  x = x + dx;
      else
      if (x < player.getX())
 	  x = x - dx;

      if (y > player.getY())
	  y = y + dy;
      else
      if (y < player.getY())
 	  y = y - dy;

     if (y<=window.getHeight()/2)
      y = window.getHeight()/2;
   }


   public void move() {

     if (!window.isVisible ()) return;

      if (collidesWithPlayer()) {
         currentAnim = animations.get("attack");
      }
      else{
         currentAnim = animations.get("walk");
         flee();
      }

      if (x<150) {
         x=150;
      }
      if (x > window.getWidth() - 250) {
         x = window.getWidth() - 250;
      }

      if (Math.abs (x - player.getX()) < 50 && !soundPlayed) {
      // soundManager.playClip ("ghostSound", true);
         soundPlayed = true;
      }

      if (Math.abs (x - player.getX()) > 80 && soundPlayed) {
      // soundManager.stopClip ("ghostSound");
         soundPlayed = false;
      }

      if (Math.abs (y - player.getY()) < 50 && !soundPlayed) {
      // soundManager.playClip ("ghostSound", true);
         soundPlayed = true;
      }

      if (Math.abs (y - player.getY()) > 80 && soundPlayed) {
      // soundManager.stopClip ("ghostSound");
         soundPlayed = false;
      }
   }


   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, width, height);
   }

   
   public boolean collidesWithPlayer() {
      Rectangle2D.Double myRect = getBoundingRectangle();
      Rectangle2D.Double playerRect = player.getBoundingRectangle();
      
      return myRect.intersects(playerRect); 
   }

}