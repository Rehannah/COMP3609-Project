import java.awt.geom.Rectangle2D;

public class SlowSprite extends Sprite {
    private Level1Player player;
       
    public SlowSprite(){
        
        super(ImageManager.loadImage("images/tiles/slow.png"),null);        
        visible = true;
    }
    
    public boolean collidesWithPlayer(){
        if (visible == false)
            return true;
        
        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            setVisible(false);
            SoundManager.getInstance().playSound("nutritious", false);
            map.startSlow();
        }
        return true;
    }

    public Rectangle2D getBoundingRectangle()  {
        return new Rectangle2D.Double(x+40, y+90, getWidth() - 80, getHeight()-90);
    }

    public void setPlayer(Level1Player p) {
        player = p;
    }   

    public void setMap(TileMap tm) {
        map = tm;
    }   

}
