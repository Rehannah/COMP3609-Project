import java.awt.geom.Rectangle2D;

public class PoisonSprite extends Sprite {

    private Score score;
    private Level1Player player;
    
    public PoisonSprite(Score s){        
        super(ImageManager.loadImage("images/myimages/tiles/poison.png"), s);
        score = s;
        if(score == null){
            System.out.println("sprite");
            
        }
        
    }
    
    public boolean collidesWithPlayer(){
        if(visible == false)
            return true;

        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            setVisible(false);
            return score.decreaseLives();
        }
        return true;
    }

    // public Rectangle2D getBoundingRectangle() {
    //     return new Rectangle2D.Double(x, y+33, getWidth(), getHeight()-33);
    // }

    public void setPlayer(Level1Player p) {
        player = p;
    } 
}
