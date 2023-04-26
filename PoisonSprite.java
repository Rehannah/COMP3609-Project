import java.awt.geom.Rectangle2D;

public class PoisonSprite extends Sprite {

    private Score score;
    private Level1Player player;
    
    public PoisonSprite(Score s){        
        super(ImageManager.loadImage("images/tiles/poison.png"), s);
        score = s;
    }
    
    public boolean collidesWithPlayer(){
        if(visible == false)
            return true;

        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            setVisible(false);
            SoundManager.getInstance().playSound("boyHurt", false);
            return score.decreaseLives();
        }
        return true;
    }

    public Rectangle2D getBoundingRectangle()  {
        return new Rectangle2D.Double(x+40, y+33, getWidth() - 80, getHeight()-33);
    }

    public void setPlayer(Level1Player p) {
        player = p;
    } 

    public void setMap(TileMap tm) {
        map = tm;
    } 
}
