import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.JFrame;

public abstract class Sprite implements Cloneable{
    
    protected static final int DX = 8;
    
    protected int x;			// x-position of sprite
    protected int y;			// y-position of sprite
    protected boolean visible; 
    protected Graphics2D g2; 
    protected Image image;
    protected Score score;

    public Sprite(Image im, Score s){
        image = im;
        visible = true;
        score = s;
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

    public boolean isVisible() {
        return visible;
    }

    public void setX(int n) {
        x = n;
    }

    public void setY(int n) {
        y = n;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public Object getClone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean collidesWithPlayer;
}
