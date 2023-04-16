import java.awt.geom.Rectangle2D;

public class PoisonSprite extends Sprite {

    private Score score;
    private Level1Player player;
    
    public PoisonSprite(Score s){        
        super(ImageManager.loadImage("images/myimages/tiles/poison.png"), s);
        score = s;
    }
    
    public boolean collidesWithPlayer(){
        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            setVisible(false);
            // System.out.println("Less lives");;
            return score.decreaseLives();
        }
        return false;
    }

    public Rectangle2D getBoundingRectangle() {
        return new Rectangle2D.Double(x, y+33, getWidth(), getHeight()-33);
    }

    public void setPlayer(Level1Player p) {
        player = p;
    } 
}
