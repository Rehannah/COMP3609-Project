import java.awt.geom.Rectangle2D;

public class NutritiousSprite extends Sprite {

    private Score score;
    private Level1Player player;
       
    public NutritiousSprite(Score s){
        super(ImageManager.loadImage("images/myimages/tiles/nutritious.png"),s);
        score = s;
    }
    
    public boolean collidesWithPlayer(){
        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            setVisible(false);
            if(score == null){
                // System.out.println("null");
                
            }
            else
            score.increasePoints();
        }
        return true;
    }

    public Rectangle2D getBoundingRectangle() {
        return new Rectangle2D.Double(x, y+33, getWidth(), getHeight()-33);
    }

    public void setPlayer(Level1Player p) {
        player = p;
    }   

    
}
