import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics2D;
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
	// private ArrayList pirates;

	private Coconut coconut;

	private ArrayList<Coconut> coconuts;

	private JFrame window;		// reference to the JFrame on which player is drawn
	
	private Score s;
	
	public GamePanel (JFrame window, Score s) {
		this.window = window;

		coconut=null;
		coconuts=null;

		this.s=s;
	}

	public void createGameEntities() {
		coconuts = new ArrayList<Coconut>();
		player = new Level2Player(window);
		swordPirate = new SwordPirate(window, player, s);
		knifePirate = new KnifePirate(window, player, s);
		captain = new Captain(window, player, s);
		bird = new BirdPirate(window, player, s);

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

		if (coconuts!=null && coconuts.size()!=0) {
			for (int i=0; i<coconuts.size(); i++) {
				Coconut c = coconuts.get(i);
				if (c.isActive()) {
					c.update();
				}
			}
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

	public void throwCoconut(){
		Coconut c =  new Coconut(window, player);
		c.activate();
		coconuts.add(c);
		// System.out.println("coco active");
	}

	public ArrayList<Coconut> getCoconuts() {
		return coconuts;
	}

	public Coconut getCoconut() {
		return coconut;
	}

	public void gameRender(Graphics2D imageContext) {
		if (player != null) {
			player.draw(imageContext);
		}
		else{
			createGameEntities();
		}

		if (swordPirate.isActive()) {
			swordPirate.draw(imageContext);
		}
		if (knifePirate.isActive()) {
			knifePirate.draw(imageContext);
		}
		if (bird.isActive()) {
			bird.draw(imageContext);
		}
		if (captain.isActive()) {
			captain.draw(imageContext);
		}

		if (coconuts != null && coconuts.size()!=0) {
			for (int i=0; i<coconuts.size(); i++) {
				Coconut c = coconuts.get(i);
				if (c!=null && c.isActive()) {
					c.draw(imageContext);
				}
			}
		}

		// Coconut coconut;
		// coconut = panel.getCoconut();
		// if (coconut!=null && coconut.isActive()) {
		// 	coconut.draw(imageContext);
		// }
		
	}

}