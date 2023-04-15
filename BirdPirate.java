import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import java.util.Random;
import java.awt.Image;
import java.util.HashMap;

public class BirdPirate {

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

   public BirdPirate (JFrame w, Level2Player player) {
      window = w;

      width = 150;
      height = 150;

      random = new Random();

      dx = 5;
      dy = 5;

      y = 200;

      this.player = player;

      soundManager = SoundManager.getInstance();

      soundPlayed = false;

      isActive = true;

      initialiseAnimations();
		currentAnim = animations.get("fly");
   }

   public boolean isActive() {
      return isActive;
   }
   
	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
      
		Image stripImage = ImageManager.loadImage("images/myimages/pirates/bird/Little Bird Fly Forward.png");
		anim = stripAnimation(stripImage, anim, 4);
		animations.put("fly", anim);


      anim = new Animation(true);
      stripImage = ImageManager.loadImage("images/myimages/pirates/bird/Little Bird Light Attack.png");
      anim = stripAnimation(stripImage, anim, 6);
      animations.put("attack", anim);
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

      if (y >= player.getY())
	  y = y - dy;
      else
      if (y <= player.getY())
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
      
      if (collidesWithplayer()) {
         currentAnim = animations.get("attack");
      }
      else{
         currentAnim = animations.get("fly");
         chase();
      }

   //   if (Math.abs (x - player.getX()) < 50 && !soundPlayed) {
	// // soundManager.playClip ("ghostSound", true);
   //      soundPlayed = true;
   //   }

   //   if (Math.abs (x - player.getX()) > 80 && soundPlayed) {
	// // soundManager.stopClip ("ghostSound");
   //      soundPlayed = false;
   //   }

   //   if (Math.abs (y - player.getY()) < 50 && !soundPlayed) {
	// // soundManager.playClip ("ghostSound", true);
   //      soundPlayed = true;
   //   }

   //   if (Math.abs (y - player.getY()) > 80 && soundPlayed) {
	// // soundManager.stopClip ("ghostSound");
   //      soundPlayed = false;
   //   }
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