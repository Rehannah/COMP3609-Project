import javax.swing.JFrame;
import javax.swing.JPanel;
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

	public TreasureAnimation treasure;
	
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
		treasure = new TreasureAnimation(window, player);
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
					coconut.update();
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
		coconut =  new Coconut(window, player);
		coconut.activate();
		coconuts.add(coconut);
	}

	public ArrayList<Coconut> getCoconuts() {
		return coconuts;
	}

	public Coconut getCoconut() {
		return coconut;
	}

	public void nextPirate() {
		if (swordPirate!=null) {
			if (swordPirate.isActive()==false && knifePirate.getLives()==3) {
				knifePirate.activate();
			}
			if (swordPirate.getLives()<=0 && knifePirate.getLives()<=0 && bird.getLives()==3) {
				bird.activate();
			}
			if (swordPirate.getLives()<=0 && knifePirate.getLives()<=0 && bird.getLives()<=0 && captain.getLives()==3) {
				captain.activate();
			}

			if (captain.getLives()<=0) {
				treasure.activate();
			}
		}
	}
}