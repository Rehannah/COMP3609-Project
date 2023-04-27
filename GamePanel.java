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
	private SwordPirate swordPirate;
	private KnifePirate knifePirate;
	private Captain captain;
	private BirdPirate bird;
	private int numCoconuts;

	private ArrayList<Coconut> coconuts;

	private GameWindow window;		// reference to the JFrame on which player is drawn
	
	private Score s;

	private TreasureAnimation treasure;
	
	public GamePanel (GameWindow window, Score s) {
		this.window = window;
		this.s=s;
	}

	public void createGameEntities() {
		numCoconuts = s.getLives()+1;
		coconuts = new ArrayList<Coconut>(numCoconuts);
		player = new Level2Player(window, this);
		swordPirate = new SwordPirate(window, player, s);
		knifePirate = new KnifePirate(window, player, s);
		captain = new Captain(window, player, s);
		bird = new BirdPirate(window, player, s);
		treasure = new TreasureAnimation(window, player);
		s.resetLives();
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
		int size = coconuts.size();
		if(size == 0)
			player.startThrow();
		else if(size <= numCoconuts){
			for(int i=0; i<size; i++)
				if(coconuts.get(i).isActive() == false)
					player.startThrow();
		}
	}

	public ArrayList<Coconut> getCoconuts() {
		return coconuts;
	}

	public void addCoconut(){
		if(coconuts!= null){
			if(coconuts.size() < numCoconuts){			
				Coconut c =  new Coconut(window, player);
				c.activate();	
				coconuts.add(c);
				return;
			}
			for(int i=0; i< coconuts.size(); i++){
				Coconut c = coconuts.get(i);
				if(!c.isActive()){
					c.activate();
					return;
				}
			}
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

		if (treasure!=null && treasure.isActive()) {
			treasure.draw(imageContext);
		}
	}

}