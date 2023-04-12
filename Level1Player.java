import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

public class Level1Player{			

	private static final int DX = 8;	// amount of X pixels to move in one keystroke
	private static final int DY = 32;	// amount of Y pixels to move in one keystroke

	private static final int TILE_SIZE = 96;

	private JFrame window;		// reference to the JFrame on which player is drawn
	private TileMap tileMap;
	private BackgroundManager bgManager;

	private int x;			// x-position of player's sprite
	private int y;			// y-position of player's sprite
	//private int position;

	private Graphics2D g2;
	private Dimension dimension;

	private Image playerImage;
	private Animation currentAnim;
	private HashMap<String, Animation> animations;

	private boolean jumping;
	private int timeElapsed;
	private int startY;

	private boolean goingUp;
	private boolean goingDown;

	private boolean inAir;
	private int initialVelocity;
	private int startAir;

	public Level1Player (JFrame window, TileMap t, BackgroundManager b) {
		this.window = window;

		tileMap = t;			// tile map on which the player's sprite is displayed
		bgManager = b;			// instance of BackgroundManager

		goingUp = goingDown = false;
		inAir = false;
		playerImage = ImageManager.loadImage("images/myimages/boy/Idle/1.png");	  
		dimension = window.getSize();
		initialiseAnimations();	  
		currentAnim = animations.get("run");
		x = window.getSize().width;
	}


	public void initialiseAnimations(){
		animations = new HashMap<>();
		Animation anim = new Animation(true);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/1.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/2.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/3.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/4.png"), 175);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/5.png"), 125);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Run/6.png"), 150);
		animations.put("run", anim);
		
		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/1.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/2.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/3.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/4.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/5.png"), 100);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/6.png"), 100);
		animations.put("jump", anim);
		
	}


	public Point collidesWithTile(int newX, int newY) {

		int playerWidth = playerImage.getWidth(null);
		int offsetY = tileMap.getOffsetY();
		int xTile = tileMap.pixelsToTiles(newX);
		int yTile = tileMap.pixelsToTiles(newY - offsetY);

		if (tileMap.getTile(xTile, yTile) != null) {
			Point tilePos = new Point (xTile, yTile);
			return tilePos;
		}
		else {
			return null;
		}
	}


	public Point collidesWithTileDown (int newX, int newY) {

		int playerWidth = playerImage.getWidth(null);
		int playerHeight = playerImage.getHeight(null);
		int offsetY = tileMap.getOffsetY();
		// int xTile = tileMap.pixelsToTiles(newX);
		// System.out.println("Xtile: "+xTile);
		int yTileFrom = tileMap.pixelsToTiles(y - offsetY);
		//System.out.println("Ytile From: "+yTileFrom);
		int yTileTo = tileMap.pixelsToTiles(newY - offsetY + playerHeight);
		//System.out.println("Y ttile to: "+yTileTo);
		for (int yTile=yTileFrom; yTile<=yTileTo; yTile++) {
			int xTile = tileMap.pixelsToTiles(newX - tileMap.getOffsetX());
			int xTile2 = tileMap.pixelsToTiles(newX + getImage().getWidth(null) - tileMap.getOffsetX());
			//System.out.println("offset: "+tileMap.getOffsetX());
		// System.out.println("Xtile: "+xTile);
		// 	System.out.println("x, y "+xTile + " "+ yTile);
			if (tileMap.getTile(xTile, yTile) != null) {
					Point tilePos = new Point (xTile, yTile);
					//System.out.println("first");
					return tilePos;
			}
			else {
				xTile = tileMap.pixelsToTiles(newX - tileMap.getOffsetX());
				if (tileMap.getTile(xTile+1, yTile) != null) {
					int leftSide = (xTile + 1) * TILE_SIZE;
					if (newX + playerWidth > leftSide) {
						//System.out.println("second");
						Point tilePos = new Point (xTile+1, yTile);
						return tilePos;
					}
				}
			}
			if (tileMap.getTile(xTile2, yTile) != null) {
				Point tilePos = new Point (xTile2, yTile);
				//System.out.println("first");
				return tilePos;
		}
		else {
			xTile2 = tileMap.pixelsToTiles(newX - tileMap.getOffsetX());
			if (tileMap.getTile(xTile2+1, yTile) != null) {
				int leftSide = (xTile2 + 1) * TILE_SIZE;
				if (newX + playerWidth > leftSide) {
					//System.out.println("second");
					Point tilePos = new Point (xTile+1, yTile);
					return tilePos;
				}
			}
		}
		}
		return null;
	}


	public Point collidesWithTileUp (int newX, int newY) {

		int playerWidth = playerImage.getWidth(null);
		int offsetY = tileMap.getOffsetY();
		int xTile = tileMap.pixelsToTiles(newX);
		int yTileFrom = tileMap.pixelsToTiles(y - offsetY);
		int yTileTo = tileMap.pixelsToTiles(newY - offsetY);
			
		for (int yTile=yTileFrom; yTile>=yTileTo; yTile--) {
			if (tileMap.getTile(xTile, yTile) != null) {
				Point tilePos = new Point (xTile, yTile);
				return tilePos;
			}
			else {
				if (tileMap.getTile(xTile+1, yTile) != null) {
					int leftSide = (xTile + 1) * TILE_SIZE;
					if (newX + playerWidth > leftSide) {
						Point tilePos = new Point (xTile+1, yTile);
						return tilePos;
					}
				}
			}							
		}
		return null;
	}
	
	/*

	public Point collidesWithTile(int newX, int newY) {

		int playerWidth = playerImage.getWidth(null);
		int playerHeight = playerImage.getHeight(null);

			int fromX = Math.min (x, newX);
		int fromY = Math.min (y, newY);
		int toX = Math.max (x, newX);
		int toY = Math.max (y, newY);

		int fromTileX = tileMap.pixelsToTiles (fromX);
		int fromTileY = tileMap.pixelsToTiles (fromY);
		int toTileX = tileMap.pixelsToTiles (toX + playerWidth - 1);
		int toTileY = tileMap.pixelsToTiles (toY + playerHeight - 1);

		for (int x=fromTileX; x<=toTileX; x++) {
			for (int y=fromTileY; y<=toTileY; y++) {
				if (tileMap.getTile(x, y) != null) {
					Point tilePos = new Point (x, y);
					return tilePos;
				}
			}
		}
		
		return null;
	}
	*/


	public synchronized void move (int direction) {
	    Point tilePos = null;
			
	    if (!window.isVisible ()) 
			return;
		 	
		if (direction == 2) {	
			tilePos = collidesWithTile(x, y);			
		}
		else if (direction == 3 && !jumping) {	
			jump();
			return;
		}
		
		if (tilePos != null) {  
			System.out.println (": Collision going right");
			int playerWidth = playerImage.getWidth(null);
			x = ((int) tilePos.getX()) * TILE_SIZE - playerWidth; // keep flush with left side of tile
		}
		else if (isInAir()) {
			System.out.println("In the air. Starting to fall.");
			fall();
		}
	}		 


	public boolean isInAir() {

		int playerHeight;
		Point tilePos;

		if (!jumping && !inAir) {   
			playerHeight = playerImage.getHeight(null);
			tilePos = collidesWithTile(x, y + playerHeight + 1); 	// check below player to see if there is a tile
			
			if (tilePos == null)				   	// there is no tile below player, so player is in the air
				return true;
			else							// there is a tile below player, so the player is on a tile
				return false;
		}
		return false;
	}


	private void fall() {
		jumping = false;
		inAir = true;
		timeElapsed = 0;

		goingUp = false;
		goingDown = true;

		startY = y;
		initialVelocity = 0;
	}


	public void jump () { 
		if (!window.isVisible ()) 
			return;

		jumping = true;
		// currentAnim = animations.get("jump");
		// currentAnim.start();
		timeElapsed = 0;

		goingUp = true;
		goingDown = false;

		startY = y;
		initialVelocity = 80;
	}

	public void idle() {
		playerImage = ImageManager.loadImage("images/myimages/boy/Idle/1.png");	  
	}


	public void update () {
		int distance = 0;
		int newY = 0;
		
		if(currentAnim != null){
			if(currentAnim.isStillActive())
				currentAnim.update();
			else
				currentAnim.start();
			playerImage = currentAnim.getImage();
		}		
		
		timeElapsed++;

		if (jumping) {
			//System.out.println("inAir");
			distance = (int) (initialVelocity * timeElapsed - 3.5 * timeElapsed * timeElapsed);
			newY = startY - distance;

			if (newY > y && goingUp) {
				goingUp = false;
				goingDown = true;
			}

			if (goingUp) {
				Point tilePos = collidesWithTileUp (x, newY);	
				if (tilePos != null) {				// hits a tile going up
					System.out.println ("Jumping: Collision Going Up!");

					int offsetY = tileMap.getOffsetY();
					int topTileY = ((int) tilePos.getY()) * TILE_SIZE + offsetY;
					int bottomTileY = topTileY + TILE_SIZE;

					y = bottomTileY;
					fall();
				}
				else{
					y = newY;
					System.out.println ("Jumping: No collision.");
				}
			}
			else if (goingDown) {			
				Point tilePos = collidesWithTileDown (x, newY);	
					if (tilePos != null) {				// hits a tile going up
						//System.out.println ("Jumping: Collision Going Down!");
						int playerHeight = playerImage.getHeight(null);
						goingDown = false;

						//int offsetY = tileMap.getOffsetY();
						//int topTileY = ((int) tilePos.getY()) * TILE_SIZE + offsetY;

						y = dimension.height - (tileMap.getHeight() - ((int) tilePos.getY())) * TILE_SIZE - playerImage.getHeight(null);
						//System.out.print( dimension.height+ " "+tileMap.getHeight()+" "+ (int) tilePos.getY()+ " " + playerImage.getHeight(null)+"Y: "+y);
						jumping = false;
						inAir = false;
					}
					else {
						y = newY;
						//System.out.println ("Jumping: No collision.");
					}
			}
		}
		else if (isInAir()){
			System.out.println("inair");
			fall();
		}
	// return true;
	}

	


	// public void moveUp () {
	// 	if (!window.isVisible ()) 
	// 		return;
	// 	y = y - DY;
	// }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Image getImage() {
		return playerImage;
	}
}