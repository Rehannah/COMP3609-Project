import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel {
   
	private SoundManager soundManager;
	private Level2Player player;
	public SwordPirate swordPirate;
	public KnifePirate knifePirate;
	public Captain captain;
	public BirdPirate bird;
	private ArrayList pirates;

	private Coconut coconut;

	private ArrayList<Coconut> coconuts;

	private JFrame window;		// reference to the JFrame on which player is drawn
	
	
	public GamePanel (JFrame window) {
		this.window = window;

		coconut=null;
		coconuts=null;
	}

	public void createGameEntities() {
		// coconuts = new ArrayList<Coconut>();
		player = new Level2Player(window);
		swordPirate = new SwordPirate(window, player);
		knifePirate = new KnifePirate(window, player);
		captain = new Captain(window, player);
		bird = new BirdPirate(window, player);

		// pirates.add(swordPirate);
		// pirates.add(knifePirate);
		// pirates.add(bird);
		// pirates.add(captain);
	}


	public void gameUpdate() {
		if (swordPirate!=null && swordPirate.isActive()) {
			swordPirate.move();
		}

		if (knifePirate !=null && knifePirate.isActive()) {
			knifePirate.move();
		}
		if (bird!=null && bird.isActive) {
			bird.move();
		}
		if (captain !=null && captain.isActive) {
			captain.move();
		}

	}


	public void updatePlayer (int direction) {

		if (player != null) {
			player.move(direction);
		}
	}


	public Level2Player getPlayer() {
		return player;
	}

	// public void throwCoconut(){
	// 	if (coconut==null) {
	// 		coconut =  new Coconut(window, player, pirates);
	// 		// soundManager.playClip("pew", false);
	// 		coconuts.add(coconut);
	// 		if (coconuts.size()>3) {
	// 			coconuts.remove(coconuts.size());
	// 		}
	// 	}
	// }

}