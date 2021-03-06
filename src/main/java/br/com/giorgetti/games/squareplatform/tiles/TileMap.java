package br.com.giorgetti.games.squareplatform.tiles;

import br.com.giorgetti.games.squareplatform.exception.ErrorConstants;
import br.com.giorgetti.games.squareplatform.exception.InvalidMapException;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.MovableSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.player.Player;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.Sprite;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.action.InteractiveSprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.media.Camera;
import br.com.giorgetti.games.squareplatform.tiles.Tile.TileType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * This class stores the current tile map definition, allowing the map
 * to be drawn on the screen, serialized into a String, and loaded from 
 * a Text File.
 * 
 * Created by fgiorgetti on 5/1/15.
 */
public class TileMap {

    /**
     * Separator character used to split information for multiple content
     * like background, sprites, tilesets and the map itself.
     */
    public static final String PROP_SEPARATOR = ",";
    public static final String INFO_SEPARATOR = " ";

    /**
     * Positions for each information when reading the tile sets
     * 0 = the integer id
     * 1 = the name associated with the id
     * 2 = resource path for the tileset
     * 3 = tile width
     * 4 = tile height
     * 5 = number of tiles
     */
    private static final int POS_TILESET_ID        = 0;
    private static final int POS_TILESET_NAME      = 1;
    private static final int POS_TILESET_PATH      = 2;
    private static final int POS_TILESET_WIDTH     = 3;
    private static final int POS_TILESET_HEIGHT    = 4;
    private static final int POS_TILESET_NUM_TILES = 5;

    /**
     * Information for each sprite that will be added to the map
     * will have:
     * 0 = X position on the map
     * 1 = Y position on the map
     * 2 = the Java class representing the Sprite
     */
    private static final int POS_SPRITE_X = 0;
    private static final int POS_SPRITE_Y = 1;
    private static final int POS_SPRITE_CLASS = 2;

    /**
     * Backgrounds are represented by 2 values, being:
     * 0 = the percentage of the player speed which background will move
     * 1 = background image path
     */
    private static final int POS_BG_SPEED = 0;
    private static final int POS_BG_PATH  = 1;

    /**
     * Stores the tileSet with all images that can be referenced to
     * by the tile map. The Integer key is associated with the tileSetIdMap,
     * which in turns is associated with a String unique ID. The reason for that is
     * to save space used by the tile map to represent the tileset which a given tile
     * belongs to.
     */
    private Map<String, TileSet> tileSetMap;
    private Map<String, String> tileSetIdMap;

    // Dimensions
    private int rows, cols, height, width, maxRowsOnScreen, maxColsOnScreen, maxX, maxY;
    private int rowOffset, colOffset, x, y;

    // Backgrounds
    private List<Background> backgrounds;

    // Player initial position
    private static final int POS_PLAYER_X = 0;
    private static final int POS_PLAYER_Y = 1;

    // Player
    private Sprite player;
    private int lastCheckpointX, lastCheckpointY;

    // Sprites in game
    private Sprite[] gameObjects = new Sprite[0];

    /**
     * Stores identification for each sprite on the map
     * X,Y,C X,Y,C X,Y,C
     * | | |-> Implementing Sprite object
     * | |-> Initial Y coordinate in the map
     * |-> Initial X coordinate in the map
     *
      */
    private ArrayList<String[]> sprites;

    /**
     * Tile map, defining all tiles in the map
     * [A] W,X,Y,Z W,X,Y,Z W,X,Y,Z ...
     *  |  | | | |
     *  |  | | | |-> The tile type (NORMAL, BLOCKED)
     *  |  | | |-> The tile position id in the tileset
     *  |  | |-> Tile Set Map Integer ID
     *  |  |-> Column number
     *  |-> Row number
     *
     *
     * The POS_MAP_* variables define which information is provided
     * on the map for each tile.
     * 0 = column number
     * 1 = the tile set id
     * 2 = tile position on the tileset
     * 3 = type for the tile (blocked, normal, ...)
     */
    public static final int POS_MAP_COLUMN     = 0;
    public static final int POS_MAP_TILESET_ID = 1;
    public static final int POS_MAP_TILEPOS_ID = 2;
    public static final int POS_MAP_TILE_TYPE  = 3;

    private Map<Integer,Map<Integer,String[]>> map = new HashMap<>();
    private int lastGameObjectIdx;

    private Camera camera;
    private boolean editMode = false;

    public TileMap() {}

    public TileMap(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * Loads the tile map from the given path. First attempts to
     * read as a resource and if not possible, then tries as a regular file.
     *
     * Initial model is supposed to be as:
     *
     * ROWS=100 # Number of rows based on the tilemap height
     * COLS=500 # Number of columns based on the tilemap width
     * WIDTH=32 # Width in pixels for the tiles
     * HEIGHT=32 # Height in pixels for the tiles
     * BG=10,background/sky.jpg 25,background/city.jpg# 10 = percent of player speed
     * TS=1,Terrain,tileset/terrain.jpg,32,32,10 2,Sprites,tileset/sprites.jpg,32,32,5 # 1 = tile set id, Terrain = tile set name, tileset/terrain.jpg = image path, 32 = width, 32 = height, 10 = number of tiles
     * SPRITES=100,50,Enemy1 200,30,Enemy2 # 100 = x, 50 = y, Enemy1 = Java Class Representing the Sprite
     * PLAYER_POS=50,50 # Player initial position on the map (x,y)
     * [0] 0,1,1,0 1,1,1,0 2,1,1,0 # [0] = First row of the map, 0 = First column, 1 = Tile set ID, 1 = Tile Position ID in the tileset, 0 = tile type
     * [2] 0,1,1,0 1,1,1,0 2,1,1,0 # [2] = Third row of the map, 0 = First column, 1 = Tile set ID, 1 = Tile Position ID in the tileset, 0 = tile type
     *
     * @param tileMapPath
     */
    public void loadTileMap(String tileMapPath, Sprite player) throws InvalidMapException {

        // Opening the tile map file
        BufferedReader reader = null;
        try {
            if ( !editMode ) {
                reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(tileMapPath)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources" + tileMapPath)));
            }
            this.player = player;
        } catch (Exception e1) {
            System.err.println("Unable to load tile map: " + tileMapPath);
            //e1.printStackTrace();
            throw new InvalidMapException();
        }

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {

                // Ignoring comments
                if ( line.trim().startsWith("#")) {
                    continue;
                }

                // Found a variable
                if ( line.contains("=")) {
                    readVariables(line);
                // Found map definition
                } else if ( line.startsWith("[")) {
                    readMapEntry(line);
                }


            }
        } catch (IOException e) {
            System.err.println("I/O Error while reading: " + tileMapPath);
            System.exit(ErrorConstants.IO_ERROR_READING_TILE_MAP);
        }

        // Once map has been read, initialize the Camera
        if ( !editMode ) {
            this.camera = new Camera(this, (Player) player);
        }
    }

    /**
     * Read variable content from the tile map line
     * @param line
     */
    private void readVariables(String line) {

        String[] info = line.split("=");
        String key   = info[0];
        String value = info[1];

        String[] arr;
        switch (key) {
            case "ROWS":
                rows = Integer.parseInt(value);
                break;
            case "COLS":
                cols = Integer.parseInt(value);
                break;
            case "WIDTH":
                width = Integer.parseInt(value);
                maxX = width * cols - GamePanel.WIDTH;
                maxColsOnScreen = GamePanel.WIDTH / width;
                break;
            case "HEIGHT":
                height = Integer.parseInt(value);
                maxY = height * rows - GamePanel.HEIGHT;
                maxRowsOnScreen = 1 + ( GamePanel.HEIGHT / height );
                break;
            case "BG":
                // Loading backgrounds
                backgrounds = new ArrayList<Background>();
                for ( String bgInfo : value.split(INFO_SEPARATOR) ) {
                    String[] bg = bgInfo.split(PROP_SEPARATOR);
                    backgrounds.add(new Background(bg[POS_BG_PATH], bg[POS_BG_SPEED]));
                }
                break;
            case "TS":
                tileSetIdMap = new HashMap<String, String>();
                tileSetMap   = new HashMap<String, TileSet>();
                for ( String tileSetInfo : value.split(INFO_SEPARATOR) ) {
                    arr = tileSetInfo.split(PROP_SEPARATOR);
                    tileSetIdMap.put(arr[POS_TILESET_NAME], arr[POS_TILESET_ID]);
                    tileSetMap.put(arr[POS_TILESET_ID],
                            new TileSet(
	                            arr[POS_TILESET_PATH],
	                            Integer.parseInt(arr[POS_TILESET_WIDTH]),
	                            Integer.parseInt(arr[POS_TILESET_HEIGHT]),
	                            Integer.parseInt(arr[POS_TILESET_NUM_TILES])));
                }
                break;
            case "SPRITES":

                // Loading sprites
                sprites = new ArrayList<String[]>();
                for ( String sprite : value.split(INFO_SEPARATOR) ) {
                    sprites.add(sprite.split(PROP_SEPARATOR));
                }

                loadGameObjects();

                break;

            case "PLAYER":
                arr = value.split(PROP_SEPARATOR);
                player.setX(Integer.parseInt(arr[POS_PLAYER_X]));
                player.setY(Integer.parseInt(arr[POS_PLAYER_Y]));
                checkpoint(player.getX(), player.getY());
                break;
        }
    }

    private void shiftGameObjectsLeft(int loadedId) {
        synchronized (gameObjects) {
            int idx = -1;
            for ( int i = 0; i < gameObjects.length; i++ ) {
                if ( gameObjects[i] == null ) {
                    continue;
                }

                if ( gameObjects[i].getLoadedId() == loadedId ) {
                    idx = i;
                    break;
                }
            }

            // Not found
            if ( idx == -1 ) {
                return;
            }

            // Remove from persistent object
            if ( editMode ) {
                getSprites().remove(idx);
            }

            // If last one
            if ( idx == gameObjects.length-1 ) {
                gameObjects[idx] = null;
                return;
            }

            for ( int idxRight = idx + 1, idxLeft = idx; idxRight < gameObjects.length; idxRight++, idxLeft++) {
                gameObjects[idxLeft] = gameObjects[idxRight];
                gameObjects[idxRight] = null;
            }
        }
    }

    /**
     * Reloads all sprites from the map
     */
    private void loadGameObjects() {

        // Loading the objects
        this.gameObjects = new Sprite[sprites.size()];
        int idx = 0;
        for ( String[] spriteEntry : sprites ) {
            try {
                Sprite s = (Sprite) Class.forName(spriteEntry[POS_SPRITE_CLASS]).newInstance();
                s.setX(Integer.parseInt(spriteEntry[POS_SPRITE_X]));
                s.setY(Integer.parseInt(spriteEntry[POS_SPRITE_Y]));
                s.setLoadedId(idx);
                gameObjects[idx++] = s;
                s.setMap(this);
            } catch (Exception e) {
                System.err.println("Invalid game object on map: " + spriteEntry[POS_SPRITE_CLASS]);
            }
        }
        this.lastGameObjectIdx = idx;
    }

    private void readMapEntry(String line) {

        String [] rowPositions = line.split(INFO_SEPARATOR);
        String rowStr = rowPositions[0].replaceAll("[\\[\\]]", "");
        int row = Integer.parseInt(rowStr);

        // Adding new row
        map.put(row, new HashMap<Integer, String[]>());

        // Adding columns for the given row
        for ( int i = 1; i < rowPositions.length; i++ ) {
            String[] colInfo = rowPositions[i].split(PROP_SEPARATOR);
            map.get(row).put(Integer.parseInt(colInfo[POS_MAP_COLUMN]), colInfo);
        }

    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<Background> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(List<Background> backgrounds) {
        this.backgrounds = backgrounds;
    }

    public ArrayList<String[]> getSprites() {
        if ( sprites == null ) {
            sprites = new ArrayList<>();
        }
        return sprites;
    }

    public void setSprites(ArrayList<String[]> sprites) {
        this.sprites = sprites;
    }

    /**
     * Adds a new sprite into the Map.
     * Should NOT be used in game.
     * @param x
     * @param y
     * @param name
     */
    public void addSprite(int x, int y, String name) {
        getSprites().add(new String[]{""+x, ""+y, name});
        loadGameObjects();
    }

    public void addSpriteInGame(Sprite sprite) {

        int id = ++lastGameObjectIdx;

        Sprite[] newSprites = new Sprite[gameObjects.length+1];
        System.arraycopy(gameObjects, 0, newSprites, 0, gameObjects.length);

        sprite.setLoadedId(id);
        newSprites[newSprites.length-1] = sprite;

        gameObjects = newSprites;

    }

    public void removeSprite(Sprite sprite) {
        shiftGameObjectsLeft( sprite.getLoadedId() );
    }

    public void removeSpriteAtPlayer() {

       if ( gameObjects == null || gameObjects.length == 0 ) {
           return;
       }

       //System.out.println(String.format("Player -> X = %d | Y = %d", player.getX(), player.getY()));
       for ( Sprite sprite : gameObjects ) {
           if ( sprite == null || !sprite.isOnScreen() ) { // Removed object
               continue;
           }

           //System.out.println(String.format("%-6d -> X = %d | Y = %d", sprite.getLoadedId(), sprite.getX(), sprite.getY()));
           if ( sprite.hasPlayerCollision() ) {
               //System.out.println("removing sprite. loaded id = " + sprite.getLoadedId());
               shiftGameObjectsLeft(sprite.getLoadedId());
           }
       }

    }

    public void addTileAt(int row, int col, int tileSet, int tile, int type) {

        Map<Integer, String[]> newTileRow = new HashMap<Integer, String[]>();
        String[] newTileInfo = new String[4];

        newTileInfo[POS_MAP_COLUMN] = "" + col;
        newTileInfo[POS_MAP_TILESET_ID] = "" + tileSet;
        newTileInfo[POS_MAP_TILEPOS_ID] = "" + tile;
        newTileInfo[POS_MAP_TILE_TYPE] = "" + tile;

        synchronized ( this.map ) {
            if (map.containsKey(row)) {
                map.get(row).put(col, newTileInfo);
            } else {
                newTileRow.put(col, newTileInfo);
                map.put(row, newTileRow);
            }
        }

    }

    public void removeTileAt(int row, int col) {

        if ( !map.containsKey(row) ) {
            return;
        }

        Map<Integer, String[]> tilesRow = map.get(row);

        if ( !tilesRow.containsKey(col) ) {
            return;
        }

        tilesRow.remove(col);

    }

    public Map<Integer, Map<Integer, String[]>> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Map<Integer, String[]>> map) {
        this.map = map;
    }

    public Map<String, TileSet> getTileSetMap() {
        return tileSetMap;
    }

    public void setTileSetMap(Map<String, TileSet> tileSetMap) {
        this.tileSetMap = tileSetMap;
    }

    public Map<String, String> getTileSetIdMap() {
        return tileSetIdMap;
    }

    public void setTileSetIdMap(Map<String, String> tileSetIdMap) {
        this.tileSetIdMap = tileSetIdMap;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getRowOffset() {
        return rowOffset;
    }

    public void setRowOffset(int rowOffset) {
        this.rowOffset = rowOffset;
    }

    public int getColOffset() {
        return colOffset;
    }

    public void setColOffset(int colOffset) {
        this.colOffset = colOffset;
    }

    public int getX() {
        return x;
    }

    public int getRightX() {
        return x + getWidth() + (getWidth() * getMaxColsOnScreen());
    }

    public void setX(int x) {
        if ( x < 0 ) {
            this.x = 0;
        } else if ( x > getMaxX() ) {
            this.x = getMaxX();
        } else {
            this.x = x;
        }
    }

    public int getY() {
        return y;
    }

    public int getTopY() {
        return y + getHeight() + (getHeight() * getMaxRowsOnScreen());
    }

    public void setY(int y) {
        if ( y < 0 ) {
            this.y = 0;
        } else if ( y > getMaxY() ) {
            this.y = getMaxY();
        } else {
            this.y = y;
        }
    }

    public int getMaxRowsOnScreen() {
        return maxRowsOnScreen;
    }

    public int getMaxColsOnScreen() { return maxColsOnScreen; }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        // Serializing TileMap as a String
        /*
        [1] 0,1,1,0 1,1,1,0 2,1,1,0 3,1,1,0 4,1,1,0 5,1,1,0 6,1,1,0 7,1,0,0 8,1,2,0 9,1,3,0 10,1,3,0 11,1,3,0 12,1,3,0 13,1,3,0 14,1,3,0
        [3] 5,1,4,0 6,1,4,0 7,1,4,0
        [3] 5,1,4,0 6,1,4,0 7,1,4,0
        [4] 12,1,4,0
        */
        sb.append("ROWS=").append(getRows()).append("\n")
                .append("COLS=").append(getCols()).append("\n")
                .append("WIDTH=").append(getWidth()).append("\n")
                .append("HEIGHT=").append(getHeight()).append("\n")
                .append("BG=").append(getBackgroundsAsString()).append("\n")
                .append("TS=").append(getTileSetsAsString()).append("\n")
                .append(getSpritesAsString().trim().isEmpty()? "":"SPRITES=").append(getSpritesAsString())
                .append("PLAYER=").append(player.getX()).append(PROP_SEPARATOR).append(player.getY()).append("\n")
                .append(getMapAsString());
        


        return sb.toString();

    }

    private String getMapAsString() {

        StringBuilder s = new StringBuilder();
        TreeSet<Integer> lines = new TreeSet<Integer>(getMap().keySet());


        for ( int line : lines ) {
            s.append("[").append(line).append("]");
            for ( int col : new TreeSet<Integer>(getMap().get(line).keySet()) ) {
                String[] info = getMap().get(line).get(col);
                s.append(TileMap.INFO_SEPARATOR);
                for ( int i = 0 ; i < info.length ; i++ ) {
                    if ( i > 0 )
                        s.append(TileMap.PROP_SEPARATOR);
                    s.append(info[i]);
                }
            }
            s.append("\n");
        }

        return s.toString();

    }

    /**
     * Serializes the tile sets
     * @return
     */
    private String getTileSetsAsString() {

        StringBuilder s = new StringBuilder();

        for ( String name : getTileSetIdMap().keySet() ) {

            if ( s.length() > 0 )
                s.append(TileMap.INFO_SEPARATOR);

            String id = getTileSetIdMap().get(name);
            TileSet t = getTileSetMap().get(id);

            s.append(id).append(TileMap.PROP_SEPARATOR)
                    .append(name).append(TileMap.PROP_SEPARATOR)
                    .append(t.getImagePath()).append(TileMap.PROP_SEPARATOR)
                    .append(t.getSpriteWidth()).append(TileMap.PROP_SEPARATOR)
                    .append(t.getSpriteHeight()).append(TileMap.PROP_SEPARATOR)
                    .append(t.getNumTiles());

        }

        return s.toString();
    }

    /**
     * Serialize the backgrounds defined on this map
     * @return
     */
    private String getBackgroundsAsString() {
        StringBuilder s = new StringBuilder();
        for ( Background b : getBackgrounds() ) {
            if ( s.length() > 0)
                s.append(TileMap.INFO_SEPARATOR);
            s.append(b.toString());
        }
        return s.toString();
    }

    /**
     * Returns a String that can be used to Serialize
     * the sprites defined on the map.
     *
     * @return
     */
    private String getSpritesAsString() {

        StringBuilder s = new StringBuilder();
        for ( String[] sprite: getSprites() ) {
            if ( s.length() > 0)
                s.append(TileMap.INFO_SEPARATOR);

            for ( int i = 0 ; i < sprite.length ; i++ ) {

                if ( i > 0 )
                    s.append(PROP_SEPARATOR);

                s.append(sprite[i]);

            }
        }
        return s.append("\n").toString();

    }

    public int getPlayerRow() {
        return (( player.getY() ) / getHeight()) + 1;
    }

    public int getRowAt(int y) {
        return (( y ) / getHeight()) + 1;
    }

    public int getPlayerCol() {
        return player.getX() / getWidth();
    }

    public int getColAt(int x) {
        return x  / getWidth();
    }

    public int getPlayerX() {
        return this.player.getX();
    }

    public int getPlayerLeftX() {
        return this.player.getX()-player.getHalfWidth();
    }

    public int getPlayerRightX() {
        return this.player.getX()+player.getHalfWidth();
    }

    public int getPlayerY() {
        return this.player.getY();
    }

    public int getPlayerTopY() {
        return this.player.getY()+player.getHalfHeight();
    }

    public int getPlayerBottomY() {
        return this.player.getY()-player.getHalfHeight();
    }

    public void update() {

        player.update(this);

        if ( editMode ) {
            setX(player.getX() - GamePanel.WIDTH / 2);
            setY(player.getY() - GamePanel.HEIGHT / 2);
        } else {
            camera.update();
        }

        setColOffset(getX() / getWidth());
        setRowOffset(getY() / getHeight() + 1);

        if ( !editMode ) {
            updateSprites();
        }

    }

    private void updateSprites() {

        // Ignore if no sprites available on the map
        if ( gameObjects == null || gameObjects.length == 0 ) {
            return;
        }

        for ( Sprite sprite : gameObjects ) {
            if ( sprite == null ) {
                continue;
            }
            sprite.update(this);
        }

    }

    public void interactiveAction(KeyEvent key) {

        // Ignore if no sprites available on the map
        if ( gameObjects == null || gameObjects.length == 0 ) {
            return;
        }

        for ( Sprite sprite : gameObjects ) {
            if ( sprite == null ) {
                continue;
            }
            if ( ! (sprite instanceof InteractiveSprite) ) {
                continue;
            }
            ((InteractiveSprite) sprite).interact(key);
        }

    }

    public void draw(Graphics2D g) {
       draw(g, false);
    }
    public void draw(Graphics2D g, boolean editMode) {

        // Drawing backgrounds
        for ( Background bg : getBackgrounds() ) {
            bg.draw(g, getX(), getY());
        }

        // Drawing map on the screen...
        synchronized ( this.map ) {

            for ( int row : getMap().keySet() ) {

                if ( row < getRowOffset() || row > getRowOffset() + getMaxRowsOnScreen() )
                    continue;

                for ( int col : getMap().get(row).keySet() ) {

                    if ( col < getColOffset() || col > getColOffset() + getMaxColsOnScreen()+1 )
                        continue;

                    // Drawing the tile map
                    Tile t = getTile(row, col);

                    g.drawImage(t.getTileImage(),
                            col * getWidth() - getX(),
                            GamePanel.HEIGHT - row * getHeight() + getY(),
                            null);

                    if ( editMode && t.getType() == Tile.TileType.BLOCKED ) {
                        g.setColor(Color.black);
                        g.fillRect(
                                col * getWidth() - getX() + (getWidth() / 2) - 2,
                                GamePanel.HEIGHT - row * getHeight() + getY() + (getHeight() / 2) - 10,
                                10, 15
                        );
                        g.setColor(Color.white);
                        g.drawString("B",
                                col * getWidth() - getX() + (getWidth() / 2),
                                GamePanel.HEIGHT - row * getHeight() + getY() + (getHeight() / 2)
                        );
                    }
                }
            }
        }

        drawSprites(g);

        if ( !editMode ) {
            g.setColor(Color.white);
            g.drawString("Score: " + ((Player) player).getScore(), 9, 14);
            g.setColor(Color.red);
            g.drawString("Score: " + ((Player) player).getScore(), 10, 15);

            g.setColor(Color.white);
            g.drawString("Lifes: " + ((Player) player).getLifes(), 99,14);
            g.setColor(Color.red);
            g.drawString("Lifes: " + ((Player) player).getLifes(), 100, 15);
        }

    }

    private void drawSprites(Graphics2D g) {

        // Ignore if no sprites available on the map
        if ( gameObjects == null || gameObjects.length == 0 ) {
            return;
        }

        // Draw the sprite
        for ( Sprite s : gameObjects ) {
            if ( s == null ) {
                continue;
            }
            if ( ! s.isOnScreen() ) {
                continue;
            }
            s.draw(g);
        }

    }

    public Tile getTile(int row, int col) {

		try {
			String[] arr = getMap().get(row).get(col);
	
			Tile t = getTileSetMap().get(arr[TileMap.POS_MAP_TILESET_ID]).getTile(arr[TileMap.POS_MAP_TILEPOS_ID]);
			t.setType(Tile.TileType.fromType(arr[TileMap.POS_MAP_TILE_TYPE]));
			
			return t;
		} catch (Exception e) {
			Tile t = new Tile(null, TileType.NORMAL);
			return t;
		}
		
	}

	public void checkpoint(int x, int y) {
        this.lastCheckpointX = x;
        this.lastCheckpointY = y;
    }

    public void recoverLastCheckpoint() {
        this.player.setX(lastCheckpointX);
        this.player.setY(lastCheckpointY);
        ((MovableSprite) this.player).setXSpeed(0);
        // Resets the camera
        setX(player.getX() - GamePanel.WIDTH / 2);
        setY(player.getY() - GamePanel.HEIGHT / 2);
        //((MovableSprite) this.player).incYSpeed(0);
    }

    public Player getPlayer() {
        return (Player) this.player;
    }

    public Sprite[] getGameObjects() {
        return this.gameObjects;
    }


    public boolean isEditMode() {
        return editMode;
    }

}
