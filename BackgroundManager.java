/* BackgroundManager manages many backgrounds (wraparound images 
   used for the game's background). 

   Backgrounds 'further back' move slower than ones nearer the
   foreground of the game, creating a parallax distance effect.

   When a sprite is instructed to move left or right, the sprite
   doesn't actually move, instead the backgrounds move in the 
   opposite direction (right or left).

*/

import java.awt.Graphics2D;
import javax.swing.JFrame;


public class BackgroundManager {

	private String bgImages[] = {"images/background/1sky.png",
								"images/background/2cloud.png",
								"images/background/3mountain.png",
								"images/background/4mist.png",
								"images/background/5hill.png",
								"images/background/6forest.png",
								"images/background/7foreground.png"};

  	private int moveAmount[] = {0, 1, 2, 2, 3, 4, 8};  
						// pixel amounts to move each background left or right

  	private Background[] backgrounds;
  	private int numBackgrounds;

  	public BackgroundManager(JFrame window) {
    	numBackgrounds = bgImages.length;
    	backgrounds = new Background[numBackgrounds];

    	for (int i = 0; i < numBackgrounds; i++) {
       		backgrounds[i] = new Background(window, bgImages[i], moveAmount[i]);
    	}
  	} 


  	private void moveRight() { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveRight();
  	}

	private void moveRightSlower() {
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveRightSlower();
	}

	public void update(){
		moveRight();
	}

	public void updateSlower(){
		moveRightSlower();
	}

  	// The draw method draws the backgrounds on the screen. The
  	// backgrounds are drawn from the back to the front.
  	
	public void draw (Graphics2D g2) { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].draw(g2);
  	}
}

