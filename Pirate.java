import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Pirate {
   public boolean isActive();
	public void initialiseAnimations();
   public void draw (Graphics2D g2);
   public void move();
   public Rectangle2D.Double getBoundingRectangle();
   public Rectangle2D.Double getAttackedRectangle();
   public boolean collidesWithPlayer();
   public void decreaseLives();
}