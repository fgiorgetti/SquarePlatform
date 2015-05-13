package br.com.giorgetti.games.squareplatform.tiles;

import br.com.giorgetti.games.squareplatform.exception.ErrorConstants;
import br.com.giorgetti.games.squareplatform.main.GamePanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
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
    private int playerX, playerY;

    // Stores identification for each sprite on the map
    private List<String[]> sprites;

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

    private Map<Integer,Map<Integer,String[]>> map = new HashMap<Integer, Map<Integer, String[]>>();

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
    public void loadTileMap(String tileMapPath) {

        // Opening the tile map file
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(getClass().getResource(tileMapPath).toURI())));
        } catch (Exception e1) {
            System.err.println("Unable to load tile map: " + tileMapPath);
            System.exit(ErrorConstants.INVALID_TILE_MAP_PATH);
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
                maxRowsOnScreen = GamePanel.HEIGHT / height;
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
                break;
            case "PLAYER":
                arr = value.split(PROP_SEPARATOR);
                playerX = Integer.parseInt(arr[POS_PLAYER_X]);
                playerY = Integer.parseInt(arr[POS_PLAYER_Y]);
                break;
        }
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

    public int getPlayerX() {
        System.out.println("Player X = " + playerX);
        return playerX;
    }

    public void setPlayerX(int playerX) {
        if ( playerX > cols * width ) {
            this.playerX = cols * width;
        } else if ( playerX < 0 ) {
            this.playerX = 0;
        } else {
            this.playerX = playerX;
        }
    }

    public int getPlayerY() {
        System.out.println("Player Y = " + playerY);
        return playerY;
    }

    public void setPlayerY(int playerY) {
        if ( playerY > rows * height - GamePanel.HEIGHT ) {
            this.playerY = rows * height - GamePanel.HEIGHT;
        } else if ( playerY < 0 ) {
            this.playerY = 0;
        } else {
            this.playerY = playerY;
        }
    }

    public List<String[]> getSprites() {
        return sprites;
    }

    public void setSprites(List<String[]> sprites) {
        this.sprites = sprites;
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
        System.out.println("Map Y = " + y);
        return y;
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

    public void setMaxRowsOnScreen(int maxRowsOnScreen) {
        this.maxRowsOnScreen = maxRowsOnScreen;
    }

    public int getMaxColsOnScreen() {
        return maxColsOnScreen;
    }

    public void setMaxColsOnScreen(int maxColsOnScreen) {
        this.maxColsOnScreen = maxColsOnScreen;
    }
}
