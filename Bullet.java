import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Image;
import java.util.HashMap;

public class Bullet {

   private JFrame window;

   private int x;
   private int y;

   private int width;
   private int height;

   private int dx;		// increment to move along x-axis
   private int dy;		// increment to move along y-axis

   private Random random;

   private Level2Player player;
   private Captain captain;
   private SoundManager soundManager;
  
   private boolean soundPlayed;

   private Image bulletImage;
   private Animation currentAnim;
	private HashMap<String, Animation> animations;

   boolean isActive;

   private Score s;

   public Bullet (JFrame w, Level2Player player, Captain captain, Score s) {
      window = w;

      this.player = player;
      this.captain = captain;
      this.s = s;

      width = 80;
      height = 20;

      random = new Random();

      x = captain.getX();
      y = captain.getY()+ (captain.getY()/3);

      dx = 20;
      dy = 0;
      
      bulletImage = ImageManager.loadImage ("images/myimages/bullet.png");
      soundManager = SoundManager.getInstance();

      soundPlayed = false;

      isActive = true;

   }

   public boolean isActive() {
      return isActive;
   }

   public void deActivate() {
		isActive = false;
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


   public void draw (Graphics2D g2) {
      if (isActive)
         g2.drawImage(bulletImage, x, y, width, height, null);
   }


   private void chase() {

      if (x > player.getX())
	  x = x - dx;
      else
      if (x < player.getX())
 	  x = x + dx;

    //   if (y > player.getY())
	//   y = y - dy;
    //   else
    //   if (y < player.getY())
 	//   y = y + dy;
   }


   public void move() {

     if (!window.isVisible ()) return;
      chase();
      if (collidesWithPlayer()){
         deActivate();
         // s.decreasePoints();
         s.decreaseLives();
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