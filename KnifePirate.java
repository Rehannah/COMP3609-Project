import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JFrame;
import java.awt.Image;
import java.util.HashMap;

public class KnifePirate implements Pirate{

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

   private int lives=3;

   private double xFracLeft;
   private double xFracRight;
   private double yFrac;
   private double widthFrac;
   private double heightFrac;
   private int gracePeriod;

   public KnifePirate (GameWindow w, Level2Player player) {
      window = w;

      width = 250;
      height = 250;

      x = window.getWidth();
      y = window.getHeight()-425;

      xFracLeft = 659.0/1324;
      xFracRight = 280.0/1324;
      yFrac = 280.0/1253;
      widthFrac = 385.0/1324;
      heightFrac = 840.0/1253;

      dx = 8;
      dy = 0;

      this.player = player;

      soundManager = SoundManager.getInstance();

      soundPlayed = false;
      isActive = true;

      initialiseAnimations();
		currentAnim = animations.get("walk");
      gracePeriod = 20;
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
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_005.png"), 150);
        anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_006.png"), 150);
		animations.put("attack", anim);

        anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_005.png"), 150);
        anim.addFrame(ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_006.png"), 150);
		animations.put("attackLeft", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk/3_3-PIRATE_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk/3_3-PIRATE_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk/3_3-PIRATE_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk/3_3-PIRATE_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk/3_3-PIRATE_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk/3_3-PIRATE_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk/3_3-PIRATE_WALK_006.png"), 150);
		animations.put("walk", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk left/3_3-PIRATE_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk left/3_3-PIRATE_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk left/3_3-PIRATE_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk left/3_3-PIRATE_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk left/3_3-PIRATE_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk left/3_3-PIRATE_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/pirates/knife/walk left/3_3-PIRATE_WALK_006.png"), 150);
		animations.put("walkLeft", anim);
	}


   public void draw (Graphics2D g2) {
      g2.drawImage(pirateImage, x, y, width, height, null);      
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



   public void move() {

      if (!window.isVisible ()) return;

      gracePeriod++;

      if (collidesWithPlayer()) {       

         if (getDirection()==1) {
            currentAnim = animations.get("attackLeft");
         }
         else{
            currentAnim = animations.get("attack");
         }
         Image imageLeft = ImageManager.loadImage("images/pirates/knife/attack left/3_3-PIRATE_ATTACK_005.png");
         Image imageRight = ImageManager.loadImage("images/pirates/knife/attack/3_3-PIRATE_ATTACK_005.png");
         if (gracePeriod > 20 && (pirateImage ==imageLeft || pirateImage ==imageRight)) {
            gracePeriod = 0;
            player.hurt();
         }
      }
      else{
         if (getDirection()==2) {
            currentAnim = animations.get("walk");
         }
         else {
            currentAnim = animations.get("walkLeft");
         }
      }

      if(currentAnim != null){
			if(currentAnim.isStillActive())
				currentAnim.update();
			else
				currentAnim.start();
         pirateImage = currentAnim.getImage();
		}

     chase();

   }

   public void decreaseLives(){
      soundManager.stopSound("knifeHurt");
      soundManager.playSound("knifeHurt", false);
      lives--;
      if (lives<=0) {
         isActive=false;
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

   public Rectangle2D.Double getAttackedRectangle() {
         if(getDirection() == 1)
            return new Rectangle2D.Double (x+xFracLeft*width, y+yFrac*height, widthFrac*width, heightFrac*height);
         else
            return new Rectangle2D.Double (x+xFracRight*width, y+yFrac*height, widthFrac*width, heightFrac*height);
   }

}
