import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

public class Captain {

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
   private ArrayList<Bullet> bullets;

   private Score s;
   public Captain (JFrame w, Level2Player player, Score s) {
      window = w;

      bullets = new ArrayList<Bullet>();
      this.s=s;
      
      width = 250;
      height = 250;

      random = new Random();

      x = window.getWidth()-350;
      y = window.getHeight()/2-50;

      dx = 0;
      dy = 0;

      this.player = player;

      pirateImage = ImageManager.loadImage ("images/myimages/pirates/captain/idle/2_entity_000_IDLE_000.png");
      soundManager = SoundManager.getInstance();

      soundPlayed = false;

      isActive = true;

      initialiseAnimations();
		currentAnim = animations.get("idle");
   }

   public boolean isActive() {
      return isActive;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public int getDirection() {
      if (player.getX() <= this.getX())
         return 1; //left
      else
         return 2; //right
   }
   
	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_000.png"), 450);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_002.png"), 155);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_003.png"), 155);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_006.png"), 150);
		animations.put("attack", anim);
		
      anim = new Animation(true);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_000.png"), 450);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_002.png"), 155);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_003.png"), 155);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_006.png"), 150);
		animations.put("attackLeft", anim);

		anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle/2_entity_000_IDLE_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle/2_entity_000_IDLE_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle/2_entity_000_IDLE_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle/2_entity_000_IDLE_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle/2_entity_000_IDLE_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle/2_entity_000_IDLE_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle/2_entity_000_IDLE_006.png"), 150);
		animations.put("idle", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle left/2_entity_000_IDLE_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle left/2_entity_000_IDLE_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle left/2_entity_000_IDLE_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle left/2_entity_000_IDLE_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle left/2_entity_000_IDLE_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle left/2_entity_000_IDLE_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/idle left/2_entity_000_IDLE_006.png"), 150);
		animations.put("idleLeft", anim);

      anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/walk/2_entity_000_WALK_000.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/walk/2_entity_000_WALK_001.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/walk/2_entity_000_WALK_002.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/walk/2_entity_000_WALK_003.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/walk/2_entity_000_WALK_004.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/walk/2_entity_000_WALK_005.png"), 150);
      anim.addFrame(ImageManager.loadImage("images/myimages/pirates/captain/walk/2_entity_000_WALK_006.png"), 150);
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
      Image imageLeft = ImageManager.loadImage("images/myimages/pirates/captain/attack left/2_entity_000_ATTACK_005.png");
      Image imageRight = ImageManager.loadImage("images/myimages/pirates/captain/attack/2_entity_000_ATTACK_005.png");
      if (pirateImage==imageLeft || pirateImage==imageRight) {
         Bullet bullet = new Bullet(window, player, this, s);
         bullets.add(bullet);
      }
      if (bullets.size()!=0){
         for (int i=0; i<bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet!=null && bullet.isActive()) {
               bullet.draw(g2);
            }
            if (bullet.isActive()==false) {
               bullets.remove(i);
            }
         }
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

      if (getDirection()==1) {
         currentAnim = animations.get("attackLeft");
      }
         
      else {
         currentAnim = animations.get("attack");
      }

      if (bullets.size()!=0){
         for (int i=0; i<bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet!=null && bullet.isActive()) {
               bullet.move();
            }
            if (bullet.isActive()==false) {
               bullets.remove(i);
            }
         }
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