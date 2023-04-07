import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.JFrame;

public class Sprite implements Cloneable{
    
    private static final int DX = 8;
    
    private int x;			// x-position of sprite
    private int y;			// y-position of sprite
    //private int position;
 
    Graphics2D g2; 
    private Image image;
    
   
    public Sprite(Image im){
        image = im;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public void setX(int n) {
        x = n;
    }

    public void setY(int n) {
        x = n;
    }

    public Object getClone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    
}
