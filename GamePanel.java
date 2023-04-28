import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics2D;
import java.awt.Image;
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
	private boolean wonGame;
	private int offsetX;
	private int offsetY;
	private double scaleX;
	private double scaleY;
	private double scaleHeight;
	private int factor;
	private Image backgroundImage;
	
	public GamePanel (GameWindow window, Score s) {
		this.window = window;
		this.s=s;
		soundManager = SoundManager.getInstance();
	}

	public void createGameEntities() {		
		soundManager.playSound("l2background", true);
		wonGame = false;
		backgroundImage = ImageManager.loadImage ("images/background/pirateship.gif");
		player = new Level2Player(window, this);
		numCoconuts = s.getLives();
		coconuts = new ArrayList<Coconut>(numCoconuts);
		for(int i=0; i< numCoconuts; i++){
			Coconut c = new Coconut(window, player);
			coconuts.add(c);
		}
		swordPirate = new SwordPirate(window, player, s);
		knifePirate = new KnifePirate(window, player, s);
		captain = new Captain(window, player, s);
		bird = new BirdPirate(window, player, s);
		s.resetLives();
	}


	public void gameUpdate() {

		if(wonGame){
			winUpdate();
			return;
		}

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
		for(int i=0; i<coconuts.size(); i++)
			if(coconuts.get(i).isActive() == false)
					player.startThrow();
		
	}

	public ArrayList<Coconut> getCoconuts() {
		return coconuts;
	}

	public void addCoconut(){
		if(coconuts!= null){
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
			if (knifePirate.getLives()<=0 && swordPirate.getLives()<=0 && bird.getLives()<=0 && captain.getLives()==3) {
				captain.activate();
			}

			if (captain.getLives()<=0) {
				winGame();
			}
		}
	}

	public void gameRender(Graphics2D imageContext) {

		if(wonGame){
			winRender(imageContext);
			return;
		}
		imageContext.drawImage(backgroundImage, 0, 0, window.pWidth+350, window.pHeight+80, null);
			
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

	public void winGame(){
		int pWidth = window.getBounds().width;
		int pHeight = window.getBounds().height;
		window.setMotion(false);
		wonGame = true;
		offsetX = 70;
		offsetY = 16;
		scaleX = offsetX*1.0/pWidth;
		scaleY = offsetY*1.0/pHeight;
		scaleHeight = (pHeight-offsetY)*1.0/pHeight;
		factor = 4;	
		player.setWonGame(true);	
		player.idle();
		treasure = new TreasureAnimation(window);
	}

	private void winUpdate() {
		if(factor>=1)
			return;
			
		int width = window.getScreenWidth();

		if(player.getX() > width-300){
			player.move(4);
			treasure.activate();
		}
		else{
			if(player.getX() > window.getScreenWidth() - 450)
				player.move(6);			
			else 
				player.move(2);	
			
		}
		treasure.update();
			
	}


	private void winRender(Graphics2D imageContext) {
		if(factor >= 1){
			imageContext.drawImage(backgroundImage, 0, 0, window.getWidth()+(factor)*offsetX, window.getHeight()+(factor)*offsetY, null);
			treasure.draw(imageContext, scaleX, scaleY, scaleHeight, 5-factor);
			player.draw(imageContext, scaleX, scaleY, scaleHeight,5-factor);
			factor--;
		}
		else{		
			imageContext.drawImage(backgroundImage, 0, 0, window.getWidth(), window.getHeight(), null);
			treasure.draw(imageContext, scaleX, scaleY, scaleHeight, 5);
			player.draw(imageContext, scaleX, scaleY, scaleHeight, 5);
		}
	}

}