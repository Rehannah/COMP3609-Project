import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import java.awt.Image;
import java.util.HashMap;

public class BirdPirate implements Pirate{

   private GameWindow window;

   private int x;
   private int y;

   private int width;
   private int height;

   private int dx;		// increment to move along x-axis
   private int dy;		// increment to move along y-axis

   private Level2Player player;
   private SoundManager soundManager;
  
   private boolean soundPlayed;

   private Image pirateImage;
   private Animation currentAnim;
	private HashMap<String, Animation> animations;

   boolean isActive;

   private int gracePeriod;
   private int lives=3;
   private Image birdImage;

   private int t; //time counter
   public BirdPirate (GameWindow w, Level2Player player) {
      window = w;

      width = 150;
      height = 150;

      dx = 10;
      dy = 6;

      x = window.getWidth()+600;
      y = 200;

      this.player = player;

      soundManager = SoundManager.getInstance();

      soundPlayed = false;

      isActive = true;

      initialiseAnimations();
		currentAnim = animations.get("fly");

      t=0;
   }

   public boolean isActive() {
      return isActive;
   }

   public int getX() {
      return x;
   }

   public int getDirection() {
      if (player.getX() <= this.getX())
         return 1; //left
      else
         return 2; //right
   }

   public int getLives() {
      return lives;
   }

   public void activate() {
      isActive=true;
   }
   
	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
      
		Image stripImage = ImageManager.loadImage("images/pirates/bird/Little Bird Fly Forward.png");
		anim = stripAnimation(stripImage, anim, 4);
		animations.put("fly", anim);

      anim = new Animation(true);
      stripImage = ImageManager.loadImage("images/pirates/bird/Little Bird Fly Forward Left.png");
		anim = stripAnimation(stripImage, anim, 4);
		animations.put("flyLeft", anim);

      anim = new Animation(true);
      stripImage = ImageManager.loadImage("images/pirates/bird/Little Bird Light Attack.png");
      anim = stripAnimation(stripImage, anim, 6);
      animations.put("attack", anim);

      anim = new Animation(true);
      stripImage = ImageManager.loadImage("images/pirates/bird/Little Bird Light Attack Left.png");
      anim = stripAnimation(stripImage, anim, 6);
      animations.put("attackLeft", anim);
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
         birdImage = frameImage;
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

      if (y >= player.getY()-100)
	  y = y - dy;
      else
      if (y <= player.getY()-100)
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
      t++;
       
       if (lives<=0) {
          isActive=false;
       }
       
       if (collidesWithPlayer()) {
          if (getDirection()==2) {
             currentAnim = animations.get("attack");
          }
          else{
             currentAnim = animations.get("attackLeft");
          }
          
          if ( birdImage ==currentAnim.getImage()) {
             player.hurt();
          }
       }
       else{
          if (getDirection()==2) {
             currentAnim = animations.get("fly");
          }
          else{
             currentAnim = animations.get("flyLeft");
          }
       }
 
       if (t<=250) {
          chase();
       }
       else{
          flee();
       }       
       
       if (x>window.getWidth()+600) {
          x=window.getWidth()+600;
          t=0;
       }
       else{
          if (x<=-600){
             x=-600;
             t=0;
          }
       }
    }

   public void decreaseLives(){
      lives--;
      if (lives<=0) {
         isActive=false;
      }
   }

   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, width, height);
   }

   public Rectangle2D.Double getAttackedRectangle() {
      return new Rectangle2D.Double (x, y, width, height);
   }
   
   public boolean collidesWithPlayer() {
      Rectangle2D.Double myRect = getBoundingRectangle();
      Rectangle2D.Double playerRect = player.getBoundingRectangle();
      
      return myRect.intersects(playerRect); 
   }
}