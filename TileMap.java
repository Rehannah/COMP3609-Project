import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
    The TileMap class contains the data for a tile-based
    map, including Sprites. Each tile is a reference to an
    Image. Images are used multiple times in the tile map.
    map.
*/

public class TileMap {

    private static final int TILE_SIZE = 96;
    private static final int DX = 8;

    private Image[][] tiles;
    private int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private int offsetY;
    private int offsetX;

    private ArrayList sprites;
    private Level1Player player;

    BackgroundManager bgManager;

    private GameWindow window;
    private Dimension dimension;

    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap(GameWindow window, int width, int height) {

        this.window = window;
        dimension = window.getSize();

        screenWidth = dimension.width;
        screenHeight = dimension.height;

        mapWidth = width;
        mapHeight = height;

        // get the y offset to draw all sprites and tiles
        offsetY = screenHeight - tilesToPixels(mapHeight);
        offsetX = 0;        
       	
        bgManager = new BackgroundManager (window);

        tiles = new Image[mapWidth][mapHeight];
        for(int i=0; i<mapWidth; i++)
            for(int j=0; j<mapHeight; j++)
                tiles[i][j] = null;
        
	    player = new Level1Player (window, this, bgManager);
        sprites = new ArrayList();
        
        Image playerImage = player.getImage();
        int playerHeight = playerImage.getHeight(null);

        int x, y;
        x = (dimension.width / 2 - playerImage.getWidth(null)/2);	// position player in middle of screen
        y = dimension.height - (TILE_SIZE + playerHeight);

        player.setX(x);
        player.setY(y);
    }


    //Gets the width of this TileMap (number of pixels across).
    public int getWidthPixels() {
	    return tilesToPixels(mapWidth);
    }

    public int getOffsetX(){
        return offsetX;
    }
    
    //Gets the width of this TileMap (number of tiles across).    
    public int getWidth() {
        return mapWidth;
    }
    
    //Gets the height of this TileMap (number of tiles down).
    public int getHeight() {
        return mapHeight;
    }

    public int getOffsetY() {
	    return offsetY;
    }

    /**
        Gets the tile at the specified location. Returns null if
        no tile is at the location or if the location is out of
        bounds.
    */
    public Image getTile(int x, int y) {
        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight)
            return null;        
        else 
            return tiles[x][y];        
    }

    //Sets the tile at the specified location.
    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }

    /*
        Gets an Iterator of all the Sprites in this map,
        excluding the player Sprite.
    */
    public Iterator getSprites() {
        return sprites.iterator();
    }

    //Class method to convert a pixel position to a tile position.
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }

    //Class method to convert a pixel position to a tile position.
    public static int pixelsToTiles(int pixels) {
        return (int)Math.floor((float)pixels / TILE_SIZE);
    }

    //Class method to convert a tile position to a pixel position.
    public static int tilesToPixels(int numTiles) {
        return numTiles * TILE_SIZE;
    }


    public void update() {
        offsetX -= DX;
        if((screenWidth - tilesToPixels(mapWidth)) > offsetX){
            offsetX = screenWidth - tilesToPixels(mapWidth); 
            player.idle(); 
            int x, y;
            Image playerImage = player.getImage();
            x = (dimension.width / 2 - playerImage.getWidth(null)/2);	// position player in middle of screen
            y = dimension.height - (TILE_SIZE + playerImage.getHeight(null));
            player.setX(x);
            player.setY(y);      
        }
        else{
            bgManager.update();
            player.update();
            Iterator iter = getSprites();
            while(iter.hasNext()){
                Sprite sprite = (Sprite)iter.next();
                if(!sprite.collidesWithPlayer())
                window.endGame();
            }
        }
    }

    //Draws the specified TileMap.
    public void draw(Graphics2D g2){
        // draw the background first
        bgManager.draw (g2);
        
        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<mapHeight; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = getTile(x, y);
                if (image != null) {
                    g2.drawImage(image, tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY, null);
                }
            }
        }
        
        // draw sprites
        Iterator i = getSprites();
        while (i.hasNext()) {            
            Sprite sprite = (Sprite)i.next();
            int x = Math.round(sprite.getX()) + offsetX;
            if(x >=-sprite.getWidth() && x < screenWidth && sprite.isVisible()){
                int y = Math.round(sprite.getY()) + offsetY;
                g2.drawImage(sprite.getImage(),x, y, null);
            }
        }

        // draw player
        g2.drawImage(player.getImage(), Math.round(player.getX()), Math.round(player.getY()), null);
    }


    public void jump(){        
        player.jump();
    }   


    public void addSprite(Sprite sprite) {
        sprite.setPlayer(player);
        sprite.setMap(this);
        sprites.add(sprite);
    }


    public void changeLevel() {
        // Graphics2D g2 = (Graphics2D)window.getGraphics();
        // Image nextLevel = ImageManager
        SoundManager sm = SoundManager.getInstance();
        sm.stopSound("l1background");
        window.increaseLevel();
    }
}