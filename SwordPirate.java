import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Image;
import java.util.HashMap;

public class SwordPirate {

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

   boolean isActive;

   public SwordPirate (JFrame w, Level2Player player) {
      window = w;

      width = 200;
      height = 200;

      random = new Random();

      x = window.getWidth()/2;
      y = window.getHeight()/2;

      dx = 1;
      dy = 1;

      this.player = player;

      pirateImage = ImageManager.loadImage ("images/myimages/pirates/1_entity_000_IDLE_000.png");
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
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/attack1_entity_000_ATTACK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/attack1_entity_000_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/attack1_entity_000_ATTACK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/attack1_entity_000_ATTACK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/attack1_entity_000_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/attack1_entity_000_ATTACK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/attack1_entity_000_ATTACK_006.png"), 150);
		animations.put("attack", anim);
		
		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/idle/1_entity_000_IDLE_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/idle/1_entity_000_IDLE_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/idle/1_entity_000_IDLE_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/idle/1_entity_000_IDLE_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/idle/1_entity_000_IDLE_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/idle/1_entity_000_IDLE_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/sword/idle/1_entity_000_IDLE_006.png"), 150);
		animations.put("idle", anim);
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
   }


   public void move() {

     if (!window.isVisible ()) return;

     chase();

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

   
   public boolean collidesWithplayer() {
      Rectangle2D.Double myRect = getBoundingRectangle();
      Rectangle2D.Double playerRect = player.getBoundingRectangle();
      
      return myRect.intersects(playerRect); 
   }

}