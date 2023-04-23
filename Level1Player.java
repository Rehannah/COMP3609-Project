import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
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
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/1.png"), 30);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/2.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/3.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/4.png"), 300);
		animations.put("jump", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/4.png"), 200);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/5.png"), 150);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/6.png"), 100);
		animations.put("fall", anim);

		anim = new Animation(false);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/1.png"), 30);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/2.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/3.png"), 300);
		anim.addFrame(ImageManager.loadImage("images/myimages/boy/Jump/4.png"), 300);
		animations.put("shortJump", anim);
		
	}


	// public Point collidesWithTile(int newX, int newY) {

	// 	int playerWidth = playerImage.getWidth(null);
	// 	int offsetY = tileMap.getOffsetY();
	// 	int xTile = tileMap.pixelsToTiles(newX + playerWidth - tileMap.getOffsetX());
	// 	int yTile = tileMap.pixelsToTiles(newY - offsetY);

	// 	if (tileMap.getTile(xTile, yTile) != null) {
	// 		Point tilePos = new Point (xTile, yTile);
	// 		System.out.println("tile exist: Xtile: "+xTile);
	// 		System.out.println("Ytile: "+yTile);
	// 		return tilePos;
	// 	}
	// 	else {
	// 		return null;
	// 	}
	// }


	public Point collidesWithTileDown (int newX, int newY) {

		int playerWidth = playerImage.getWidth(null);
		int playerHeight = playerImage.getHeight(null);
		int offsetY = tileMap.getOffsetY();
		int yTileFrom = tileMap.pixelsToTiles(y - offsetY);
		
		// System.out.println("newY: "+newY);
		
		// System.out.println("Ytile From: "+yTileFrom);
		int yTileTo = tileMap.pixelsToTiles(newY - offsetY + playerHeight);
		// System.out.println("Y ttile to: "+yTileTo);

		for (int yTile=yTileFrom; yTile<=yTileTo; yTile++) {
			int xTile = tileMap.pixelsToTiles(newX - tileMap.getOffsetX());
			//System.out.println("offset: "+tileMap.getOffsetX());
		// System.out.println("Xtile: "+xTile);
			// System.out.println("x, y "+xTile + " "+ yTile);
			if (tileMap.getTile(xTile, yTile) != null) {

				if(newY < window.getHeight() - TILE_SIZE - getImage().getHeight(null)){
					// System.out.println("1st working");
					if(newY < yTile * TILE_SIZE +tileMap.getOffsetY() + 32)					
					//System.out.println("first");
						return new Point (xTile, yTile);
					// System.out.println("no return");
					return null;
				}
				// System.out.println("regular x "+xTile + " y "+yTile);
				return new Point (xTile, yTile);

			}
			else {
				xTile = tileMap.pixelsToTiles(newX - tileMap.getOffsetX());
				if (tileMap.getTile(xTile+1, yTile) != null) {
					int leftSide = (xTile + 1) * TILE_SIZE;
					if ((newX + playerWidth-tileMap.getOffsetX()) > leftSide) {
						if(newY < window.getHeight() - TILE_SIZE - getImage().getHeight(null)){
							// System.out.println("1st work");
							if(newY < yTile * TILE_SIZE +tileMap.getOffsetY() + 32)					
							//System.out.println("first");
								return new Point (xTile+1, yTile);
							else
								return null;
						}
						// System.out.println("second");
						return new Point (xTile+1, yTile);
					}
				}
			}
		}
		return null;
	}


	public Point collidesWithTileUp (int newX, int newY) {

		int playerWidth = playerImage.getWidth(null);
		int offsetY = tileMap.getOffsetY();
		int xTile = tileMap.pixelsToTiles(newX-tileMap.getOffsetX());
		int yTileFrom = tileMap.pixelsToTiles(y - 1 - offsetY);
		int yTileTo = tileMap.pixelsToTiles(newY -1 - offsetY);
			
		for (int yTile=yTileFrom; yTile>=yTileTo; yTile--) {
			// System.out.println("x "+ xTile + " y "+yTile);
			if (tileMap.getTile(xTile, yTile) != null) {
				Point tilePos = new Point (xTile, yTile);
				System.out.println("up collide 1");
				return tilePos;
			}
			else {
				if (tileMap.getTile(xTile+1, yTile) != null) {
					int leftSide = (xTile + 1) * TILE_SIZE;
					if (newX -tileMap.getOffsetX() + playerWidth > leftSide) {
						Point tilePos = new Point (xTile+1, yTile);
						System.out.println("up collide 2");
						return tilePos;
					}
				}
			}							
		}
		return null;
	}
	
	

	public Point collidesWithTile(int newX, int newY) {

		int playerWidth = playerImage.getWidth(null);
		int playerHeight = playerImage.getHeight(null);

			int fromX = Math.min (x, newX);
		int fromY = Math.min (y, newY);
		int toX = Math.max (x, newX);
		int toY = Math.max (y, newY);
		// System.out.println("from y  " +fromY + " toY " + toY);
			
		int fromTileX = tileMap.pixelsToTiles (fromX + Math.abs(tileMap.getOffsetX()));
		int fromTileY = tileMap.pixelsToTiles (fromY - tileMap.getOffsetY());
		int toTileX = tileMap.pixelsToTiles (toX +Math.abs(tileMap.getOffsetX()) + playerWidth - 1);
		int toTileY = tileMap.pixelsToTiles (toY - tileMap.getOffsetY());

		for (int x=fromTileX; x<=toTileX; x++) {
			for (int y=fromTileY; y<=toTileY; y++) {
				// System.out.println("X " +x + "y " + y);
			
				if (tileMap.getTile(x, y) != null) {
					// System.out.println("X " +x + "y " + y);
					Point tilePos = new Point (x, y);
					return tilePos;
				}
			}
		}
		
		return null;
	}
	


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
			// System.out.println (": Collision going right");
			int playerWidth = playerImage.getWidth(null);
			x = ((int) tilePos.getX()) * TILE_SIZE - playerWidth; // keep flush with left side of tile
		}
		else if (isInAir()) {
			// System.out.println("In the air. Starting to fall.");
			fall();
		}
	}		 


	public boolean isInAir() {

		int playerHeight;
		Point tilePos;

		if (!jumping && !inAir) {   
			playerHeight = playerImage.getHeight(null);
			tilePos = collidesWithTile(x, y + playerHeight + 1); 	// check below player to see if there is a tile
			
			if (tilePos == null){				   	// there is no tile below player, so player is in the air
				return true;
			}
			else							// there is a tile below player, so the player is on a tile
				return false;
				
		}
		return false;
	}


	private void fall() {
		jumping = false;
		inAir = true;
		timeElapsed = 0;
		currentAnim = animations.get("fall");
		goingUp = false;
		goingDown = true;

		startY = y;
		initialVelocity = 0;
	}


	public void jump () { 
		if (!window.isVisible ()) 
			return;
		
		if(y < dimension.height - (TILE_SIZE + getImage().getHeight(null)))
			return;
		
		jumping = true;
		currentAnim = animations.get("jump");
		currentAnim.start();
		timeElapsed = 0;

		goingUp = true;
		goingDown = false;

		startY = y;
		initialVelocity = 100;
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

		if (jumping || inAir) {
			// System.out.println("inAir");
			distance = (int) (initialVelocity * timeElapsed - 4.9 * timeElapsed * timeElapsed);
			newY = startY - distance;

			if (newY > y && goingUp) {
				goingUp = false;
				goingDown = true;
				// System.out.println("Goig down");
				currentAnim.stop();
				currentAnim = animations.get("fall");
			}

			if (goingUp) {
				Point tilePos = collidesWithTileUp (x, newY);	
				if (tilePos != null) {				// hits a tile going up
					// System.out.println ("Jumping: Collision Going Up!");

					setY(((int) tilePos.getY()) * TILE_SIZE + tileMap.getOffsetY() + TILE_SIZE - 63);		//tiles that can be collided with going up are narrower
					fall();
				}
				else{
					y = newY;
					//System.out.println ("Jumping: No collision.");
				}
			}
			else if (goingDown) {			
				Point tilePos = collidesWithTileDown (x, newY);	
					if (tilePos != null) {				// hits a tile going down
						// System.out.println ("Jumping: Collision Going Down!");
						int playerHeight = playerImage.getHeight(null);
						goingDown = false;

						//int offsetY = tileMap.getOffsetY();
						//int topTileY = ((int) tilePos.getY()) * TILE_SIZE + offsetY;

						y = dimension.height - (tileMap.getHeight() - ((int) tilePos.getY())) * TILE_SIZE - playerImage.getHeight(null);
						//System.out.print( dimension.height+ " "+tileMap.getHeight()+" "+ (int) tilePos.getY()+ " " + playerImage.getHeight(null)+"Y: "+y);
						jumping = false;
						inAir = false;
						// System.out.println ("Reachg rounf");

						currentAnim = animations.get("run");
					}
					else {
						y = newY;
						//System.out.println ("Jumping: No collision.");
					}
			}
		}
		if (isInAir()){
			// System.out.println("inair");
			fall();
		}
		
	// return true;
	}

	
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


	public Rectangle2D getBoundingRectangle() {
		
		return new Rectangle2D.Double(x - tileMap.getOffsetX(),y,getImage().getWidth(null), getImage().getHeight(null));
	}
}