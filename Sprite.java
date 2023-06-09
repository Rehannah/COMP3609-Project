import java.awt.Image;
import java.awt.geom.Rectangle2D;

public abstract class Sprite implements Cloneable{
    
    protected static final int DX = 8;
    
    protected int x;			// x-position of sprite
    protected int y;			// y-position of sprite
    protected boolean visible; 
    protected TileMap map; 
    protected Image image;
    protected Score score;
    protected Level1Player player;

    public Sprite(Image im, Score s){
        image = im;
        visible = true;
        score = s;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setX(int n) {
        x = n;
    }

    public void setY(int n) {
        y = n;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public Object getClone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public abstract void setPlayer(Level1Player p);

    public abstract void setMap(TileMap tm);
    
    public abstract boolean collidesWithPlayer();

    public abstract Rectangle2D getBoundingRectangle();
    
}
