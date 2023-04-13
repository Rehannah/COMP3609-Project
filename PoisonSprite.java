public class PoisonSprite extends Sprite {

    public PoisonSprite(Score s){        
        super(ImageManager.loadImage("images/myimages/tiles/poison.png"), s);
    }
    
    public boolean collidesWithPlayer(){
        setVisible(false);
        if(score.decreaseLives())
            return true;    
        return false;
    } 
}
