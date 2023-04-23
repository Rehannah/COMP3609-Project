import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class LevelSprite extends Sprite{

    private Level1Player player;

    public LevelSprite() {
        super(ImageManager.loadImage("images/myimages/tiles/nextLevel.png"), null);
        visible = true;
    }

    public boolean collidesWithPlayer() {
        if (visible == false)
            return true;
        
        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            map.changeLevel();
        }
        return true;
    }

    public Rectangle2D getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, getWidth(), getHeight());
    }
    

    public void setPlayer(Level1Player p) {
        player = p;
    }  

    public void setMap(TileMap tm) {
        map = tm;
    }   

    
}
