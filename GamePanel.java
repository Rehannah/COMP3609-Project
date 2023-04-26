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

	private ArrayList<Coconut> coconuts;

	private JFrame window;		// reference to the JFrame on which player is drawn
	
	private Score s;

	public TreasureAnimation treasure;
	
	public GamePanel (JFrame window, Score s) {
		this.window = window;
		this.s=s;
	}

	public void createGameEntities() {
		coconuts = new ArrayList<Coconut>();
		player = new Level2Player(window, this);
		swordPirate = new SwordPirate(window, player, s);
		knifePirate = new KnifePirate(window, player, s);
		captain = new Captain(window, player, s);
		bird = new BirdPirate(window, player, s);
		treasure = new TreasureAnimation(window, player);
	}


	public void gameUpdate() {

		ArrayList<Pirate> pirates = new ArrayList<>(4);

		if (knifePirate!=null && knifePirate.isActive()) {
			knifePirate.move();
			pirates.add(knifePirate);
		}

		if (swordPirate !=null && swordPirate.isActive()) {
			swordPirate.move();
			pirates.add(swordPirate);
		}
		if (bird!=null && bird.isActive) {
			bird.move();
			pirates.add(bird);
		}
		if (captain !=null && captain.isActive) {
			captain.move();
			pirates.add(captain);
		}

		updatePlayer(-1);
		if (coconuts!=null && coconuts.size()!=0) {
			for (int i=0; i<coconuts.size(); i++) {
				Coconut c = coconuts.get(i);
				if (c.isActive()) {
					c.update();
					if(c.collidesWithPirate(pirates))
						s.increasePoints();
				}
			}
		}

		if (treasure!=null && treasure.isActive()) {
			System.out.println("treasure updates");
			treasure.update();
		}

		nextPirate();
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
		player.startThrow();
	}

	public ArrayList<Coconut> getCoconuts() {
		return coconuts;
	}

	public void addCoconut(Coconut c){
		if(coconuts!= null){
			coconuts.add(c);
		}

	}

	public void nextPirate() {
		if (knifePirate!=null) {
			if (knifePirate.isActive()==false && swordPirate.getLives()==3) {
				swordPirate.activate();
			}
			if (knifePirate.getLives()<=0 && swordPirate.getLives()<=0 && bird.getLives()==3) {
				bird.activate();
			}
			if (knifePirate.getLives()<=0 && swordPirate.getLives()<=0 && bird.getLives()<=0 && captain.getLives()==3) {
				captain.activate();
			}

			if (captain.getLives()<=0) {
				treasure.activate();
				System.out.println("Chest activated treasure");
			}
		}
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
	}

}