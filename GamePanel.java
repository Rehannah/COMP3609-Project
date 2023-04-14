import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel {
   

	private SoundManager soundManager;
	public Level2Player l2Player;
	private boolean isRunning;

	private Thread gameThread;

	private JFrame window;		// reference to the JFrame on which player is drawn
	private BufferedImage image;
 	private Image backgroundImage;

	public GamePanel (JFrame window) {
		this.window = window;

		image = new BufferedImage (400, 400, BufferedImage.TYPE_INT_RGB);
		backgroundImage = ImageManager.loadImage ("images/myimages/background/level2bg.jpg");

	}


	public void createGameEntities() {
		l2Player = new Level2Player(window);
	}

	public void gameUpdate() {

		// for (int i=0; i<NUM_ALIENS; i++) {
		// 	aliens[i].move();
		// }

		// if (ghostsActive) {
		// 	ghost1.move();
		// 	ghost2.move();
		// }
	}


	public void updatePlayer (int direction) {

		if (l2Player != null) {
			l2Player.move(direction);
		}

	}


	public void gameRender() {

		

		// render the l2Player

		// render the aliens

		// if (aliens != null) {
		// 	for (int i=0; i<NUM_ALIENS; i++)
		// 		aliens[i].draw(imageContext);
       	// 	}

		// // render the ghosts

		// if (ghost1 != null && ghostsActive) {
		// 	ghost1.draw(imageContext);
		// }

		// if (ghost2 != null && ghostsActive) {
		// 	//ghost2.draw(imageContext);
		// }


	}


	// public void dropAlien() {

	// 	if (!alienDropped) {	// start the game thread
	// 		gameThread = new Thread(this);
	// 		gameThread.start();
	// 		//soundManager.playClip("background", true);
	// 		alienDropped = true;
	// 	}

	// }


	// public void activateGhost() {
	// 	if (ghostsActive)
	// 		ghostsActive = false;
	// 	else
	// 		ghostsActive = true;
	// }


	// public boolean isOnAlien (int x, int y) {
		
	// 	for (int i=0; i<NUM_ALIENS; i++) {
	// 		if (aliens[i].isOnAlien(x, y))
	// 			return true;
	// 	}

	// 	return false;
	// }

}