import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class TileMapManager {

    private ArrayList<Image> tiles;

    private GameWindow window;
    private Score score;

    // host sprites used for cloning
    private PoisonSprite poisonSprite;
    private NutritiousSprite nutritiousSprite;
    private LevelSprite levelSprite;


    public TileMapManager(GameWindow window, Score s) {
        this.window = window;
        
        score = s;
        poisonSprite = new PoisonSprite(score);
        nutritiousSprite = new NutritiousSprite(score);
        levelSprite = new LevelSprite();
        loadTileImages();
    }


     public TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList<String> lines = new ArrayList<String>();
        int mapWidth = 0;
        int mapHeight = 0;

        // read every line in the text file into the list

        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                mapWidth = Math.max(mapWidth, line.length());
            }
        }

        // parse the lines to create a TileMap
        mapHeight = lines.size();

        TileMap newMap = new TileMap(window, mapWidth, mapHeight);
        for (int y=0; y<mapHeight; y++) {
            String line = lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == '!') {
                    addSprite(newMap, poisonSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, nutritiousSprite, x, y);
                }
                else if (ch == '0') {
                    addSprite(newMap, levelSprite, x, y);
                }
            }
        }

        return newMap;
    }



    private void addSprite(TileMap map, Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Sprite sprite = (Sprite)hostSprite.getClone();
            // center the sprite
            sprite.setX(map.tilesToPixels(tileX));
            sprite.setY(map.tilesToPixels(tileY+1) - sprite.getHeight());
            // bottom-justify the sprite
            
            // add it to the map
            map.addSprite(sprite);
        }
    }
    
    // code for loading sprites and images
    public void loadTileImages() {
        File file;

        tiles = new ArrayList<Image>();
        char ch = 'A';
        while (true) {
            String filename = "images/tiles/tile_" + ch + ".png";
	        file = new File(filename);
            if (!file.exists()) 
                break;            
	        else{
                Image tileImage = new ImageIcon(filename).getImage();
                tiles.add(tileImage);
            }
            ch++;
        }
    }
}