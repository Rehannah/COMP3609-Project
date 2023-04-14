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
	private Level2Player player;
	public SwordPirate swordPirate;
	public KnifePirate knifePirate;

	private JFrame window;		// reference to the JFrame on which player is drawn
	private BufferedImage image;
 	private Image backgroundImage;

	public GamePanel (JFrame window) {
		this.window = window;

		image = new BufferedImage (400, 400, BufferedImage.TYPE_INT_RGB);
		backgroundImage = ImageManager.loadImage ("images/myimages/background/level2bg.jpg");
	}

	public void createGameEntities() {
		player = new Level2Player(window);
		swordPirate = new SwordPirate(window, player);
		knifePirate = new KnifePirate(window, player);
	}


	public void gameUpdate() {
		if (swordPirate!=null && swordPirate.isActive()) {
			swordPirate.move();
		}

		if (knifePirate !=null && knifePirate.isActive()) {
			knifePirate.move();
		}
		// if (birdPirate.isActive) {
		// 	birdPirate.move();
		// }
		// if (captain.isActive) {
		// 	captain.move();
		// }

	}


	public void updatePlayer (int direction) {

		if (player != null) {
			player.move(direction);
		}
	}


	public Level2Player getPlayer() {
		return player;
	}

}