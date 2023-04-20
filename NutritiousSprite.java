import java.awt.geom.Rectangle2D;

public class NutritiousSprite extends Sprite {

    private Score score;
    private Level1Player player;
       
    public NutritiousSprite(Score s){
        
        super(ImageManager.loadImage("images/myimages/tiles/nutritious.png"),s);
        if(s == null){
            System.out.println("sp1");
            
        }
        // image = ImageManager.loadImage(null);
        visible = true;
        score = s;
        if(score == null){
            System.out.println("sp");
            
        }
        
    }
    
    public boolean collidesWithPlayer(){
        if (visible == false)
            return true;
        
        if(getBoundingRectangle().intersects(player.getBoundingRectangle())){
            setVisible(false);
            score.increasePoints();
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
