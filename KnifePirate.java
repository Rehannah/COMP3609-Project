import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import java.awt.Image;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class KnifePirate implements Pirate{

   private JFrame window;

   private int x;
   private int y;

   private double xFracLeft;
   private double xFracRight;
   private double yFrac;
   private double widthFrac;
   private double heightFrac;

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

   

   

   public KnifePirate (JFrame w, Level2Player player, Score s) {
      window = w;
      this.score = s;

      height = 230;
      x = window.getWidth();
      y = window.getHeight()-400;

      dx = 4;
      dy = 0;

      this.player = player;

      pirateImage = ImageManager.loadImage ("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_000.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT);
      soundManager = SoundManager.getInstance();
      xFracLeft = 659.0/1324;
      xFracRight = 280.0/1324;
      yFrac = 280.0/1253;
      widthFrac = 385.0/1324;
      heightFrac = 840.0/1253;
      

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

	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_000.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_001.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_002.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_003.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_004.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_005.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_006.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		animations.put("attack", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_000.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_001.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_002.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_003.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_004.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_005.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_006.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		animations.put("attackLeft", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_000.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_001.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_002.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_003.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_004.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_005.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_006.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		animations.put("walk", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_000.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_001.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_002.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_003.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_004.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_005.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_006.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		animations.put("walkLeft", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_000.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_001.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_002.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_003.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_004.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_005.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_006.png").getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT), 150);
		animations.put("idleLeft", anim);
	}


   public void activate() {
      isActive=true;
   }

   public void draw (Graphics2D g2) {
      
      g2.drawImage(pirateImage, x, y, null);
      
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


   public void move() {

     if (!window.isVisible ()) return;

     chase();

      if (collidesWithPlayer()) {
         lives--;
         if (lives<=0) {
            isActive=false;
         }
         if (getDirection()==1) {
            currentAnim = animations.get("attackLeft");
         }
         else {
            currentAnim = animations.get("attack");
         }
         Image imageLeft = ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_005.png");
         Image imageRight = ImageManager.loadImage("images/myimages/pirates/knife/attack/3_3-PIRATE_ATTACK_005.png");
         if (pirateImage ==imageLeft || pirateImage ==imageRight)
            score.decreaseLives();
      }
      else{
         if (getDirection()==1) {
            currentAnim = animations.get("walkLeft");
         }
         else {
            currentAnim = animations.get("walk");
         }
      }   
   }


   public Rectangle2D.Double getBoundingRectangle() {
      int width = pirateImage.getWidth(null);
         if(getDirection() == 1){

            Rectangle2D.Double rect = new Rectangle2D.Double (x+xFracLeft*width, y+yFrac*height, widthFrac*width, heightFrac*height);
            // System.out.println("pirate"+rect);
            return rect;
         }
         else{
            Rectangle2D.Double rect = new Rectangle2D.Double (x+xFracRight*width, y+yFrac*height, widthFrac*width, heightFrac*height);
            // System.out.println("pirate"+rect);
            return rect; 
         }
            
   }

   
   public boolean collidesWithPlayer() {
      Rectangle2D.Double myRect = getBoundingRectangle();
      Rectangle2D.Double playerRect = player.getBoundingRectangle();
      
      return myRect.intersects(playerRect); 
   }

}