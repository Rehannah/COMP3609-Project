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

   boolean isActive;

   private Score score;
   private int lives=3;

   public KnifePirate (JFrame w, Level2Player player, Score s) {
      window = w;
      this.score = s;

      width = 230;
      height = 230;

      x = window.getWidth()-350;
      y = window.getHeight()/2-50;

      dx = 2;
      dy = 0;

      this.player = player;

      pirateImage = ImageManager.loadImage ("images/myimages/pirates/knife/idle/3_3-PIRATE_IDLE_000.png");
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
      if (dx<0)
         return 1; //left
      else
         return 2; //right
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
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/attack left/3_3-PIRATE_ATTACK_006.png"), 150);
		animations.put("attackLeft", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk/3_3-PIRATE_WALK_006.png"), 150);
		animations.put("walk", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/walk left/3_3-PIRATE_WALK_006.png"), 150);
		animations.put("walkLeft", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/knife/idle left/3_3-PIRATE_IDLE_006.png"), 150);
		animations.put("idleLeft", anim);
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

   public void movePattern() {
      x = x + dx;
      // y = y + dy;

      int Wwidth = window.getWidth();

   
      if (x>=Wwidth - (width+200) || x<=500){
         dx=dx*-1;
      }

      x=x+dx;
   }


   public void move() {

     if (!window.isVisible ()) return;

      if (collidesWithPlayer()) {
         if (getDirection()==2) {
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
         movePattern();
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