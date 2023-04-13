public class NutritiousSprite extends Sprite {
       
    public NutritiousSprite(Score s){
        super(ImageManager.loadImage("images/myimages/tiles/nutritious.png"),s);
    }
    
    public boolean collidesWithPlayer(){
        setVisible(false);
        score.increasePoints();
        return true;
    }   
}
