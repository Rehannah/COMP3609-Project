import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JFrame;
import java.awt.Image;
import java.util.HashMap;

public class SwordPirate implements Pirate{

   private JFrame window;

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

   private Score score;
   private int lives=3;

   private double xFracLeft;
   private double xFracRight;
   private double yFrac;
   private double widthFrac;
   private double heightFrac;

   public SwordPirate (JFrame w, Level2Player player, Score score) {
      window = w;

      width = 250;
      height = 250;

      x = window.getWidth();
      y = window.getHeight()-400;

      xFracLeft = 659.0/1324;
      xFracRight = 280.0/1324;
      yFrac = 280.0/1253;
      widthFrac = 385.0/1324;
      heightFrac = 840.0/1253;

      dx = 8;
      dy = 0;

      this.player = player;
      this.score = score;

      soundManager = SoundManager.getInstance();

      soundPlayed = false;

      isActive = false;

      initialiseAnimations();
		currentAnim = animations.get("walk");
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
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_006.png"), 150);
		animations.put("attack", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_006.png"), 150);
		animations.put("attackLeft", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk/1_entity_000_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk/1_entity_000_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk/1_entity_000_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk/1_entity_000_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk/1_entity_000_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk/1_entity_000_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk/1_entity_000_WALK_006.png"), 150);
		animations.put("walk", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk left/1_entity_000_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk left/1_entity_000_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk left/1_entity_000_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk left/1_entity_000_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk left/1_entity_000_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk left/1_entity_000_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/pirates/sword/walk left/1_entity_000_WALK_006.png"), 150);
		animations.put("walkLeft", anim);
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



   public void move() {
      if (!window.isVisible ()) return;

      if (collidesWithPlayer()) {
         if (getDirection()==1) 
            currentAnim = animations.get("attackLeft");
         else
            currentAnim = animations.get("attack");
            
         Image imageLeft = ImageManager.loadImage("images/pirates/sword/attack left/1_entity_000_ATTACK_004.png");
         Image imageRight = ImageManager.loadImage("images/pirates/sword/attack/1_entity_000_ATTACK_004.png");
         if (pirateImage ==imageLeft || pirateImage ==imageRight) 
            score.decreaseLives();
      }
      else{
         if (getDirection()==2) 
            currentAnim = animations.get("walk");
         else 
            currentAnim = animations.get("walkLeft");
      }
      chase();
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