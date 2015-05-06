package br.com.giorgetti.games.squareplatform.tiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class TileMap {

    /**
     * Separator character used to split information for each tile in the
     * map. Currently supports the following format:
     *
     * X,Y,Z
     * | | |
     * | | |-> The tile type (NORMAL, BLOCKED)
     * | |-> The tile position id in the tileset
     * |-> Tile Set Map Integer ID
     */
    public static final String PROP_SEPARATOR = ",";
    public static final String INFO_SEPARATOR = " ";

    private static final int POS_TILESET_ID = 0;
    private static final int POS_TILEPOS_ID = 1;
    private static final int POS_TILE_TYPE  = 2;

    private static final int POS_SPRITE_X = 0;
    private static final int POS_SPRITE_Y = 0;
    private static final int POS_SPRITE_CLASS = 0;

    private static final int POS_BG_SPEED = 0;
    private static final int POS_BG_PATH  = 1;

    /**
     * Stores the tileSet with all images that can be referenced to
     * by the tile map. The Integer key is associated with the tileSetIdMap,
     * which in turns is associated with a String unique ID. The reason for that is
     * to save space used by the tile map to represent the tileset which a given tile
     * belongs to.
     */
    private Map<Integer, TileSet> tileSetMap;
    private Map<String, Integer> tileSetIdMap;

    // Dimensions
    private int rows, cols, height, width;

    // Backgrounds
    private List<String[]> backgrounds;

    // Player initial position
    private int playerX, playerY;

    // Stores identification for each sprite on the map
    private String[] sprites;
    // Tile map, defining all tiles in the map
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
     * TS=1,Terrain,tileset/terrain.jpg 2,Sprites,tileset/sprites.jpg # 1 = tile set id, Terrain = tile set name, tileset/terrain.jpg = image path
     * PLAYER_POS=50,50 # Player initial position on the map (x,y)
     * SPRITES=100,50,Enemy1 200,30,Enemy2 # 100 = x, 50 = y, Enemy1 = Java Class Representing the Sprite
     * [0] 0,1,1,0 1,1,1,0 2,1,1,0 # [0] = First row of the map, 0 = First column, 1 = Tile set ID, 1 = Tile Position ID in the tileset, 0 = tile type
     * [2] 0,1,1,0 1,1,1,0 2,1,1,0 # [2] = Third row of the map, 0 = First column, 1 = Tile set ID, 1 = Tile Position ID in the tileset, 0 = tile type
     *
     * @param tileMapPath
     */
    public void loadTileMap(String tileMapPath) {

        // Opening the tile map file

    }

    private String[] getTileInfo(String tileMapInfo) {
        return tileMapInfo.split(PROP_SEPARATOR);
    }

}
