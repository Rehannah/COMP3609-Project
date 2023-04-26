import java.awt.geom.Rectangle2D;

public class LevelSprite extends Sprite{

    private Level1Player player;

    public LevelSprite() {
        super(ImageManager.loadImage("images/tiles/nextLevel.png"), null);
        visible = true;
    }

    public boolean collidesWithPlayer() {
        if (visible == false)
            return true;
        
        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            player.idle();
            map.changeLevel();
        }
        return true;
    }

    public Rectangle2D getBoundingRectangle() {
        return new Rectangle2D.Double(x+0.85*getWidth(), y, getWidth(), getHeight());
    }
    

    public void setPlayer(Level1Player p) {
        player = p;
    }  

    public void setMap(TileMap tm) {
        map = tm;
    }   

    
}
