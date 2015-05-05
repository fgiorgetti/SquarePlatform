package br.com.giorgetti.games.squareplatform.tiles;

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

    /**
     * Stores the tileSet with all images that can be referenced to
     * by the tile map. The Integer key is associated with the tileSetIdMap,
     * which in turns is associated with a String unique ID. The reason for that is
     * to save space used by the tile map to represent the tileset which a given tile
     * belongs to.
     */
    private Map<Integer, TileSet> tileSetMap;
    private Map<String, Integer> tileSetIdMap;
    private String[][] map;
    private String[] sprites;

    /**
     * Loads the tile map from the given path. First attempts to
     * read as a resource and if not possible, then tries as a regular file.
     *
     * Initial model is supposed to be as:
     * ROWS=100 # Number of rows based on the tilemap height
     * COLS=500 # Number of columns based on the tilemap width
     * WIDTH=32 # Width in pixels for the tiles
     * HEIGHT=32 # Height in pixels for the tiles
     * 1,Terrain,tileset/terrain.jpg # 1 = tile set id, Terrain = tile set name, tileset/terrain.jpg = image path
     * 2,Sprites,tileset/sprites.jpg
     * PLAYER=50,50 # Player initial position on the map (x,y)
     * SPRITES=100,50,Enemy1 200,30,Enemy2 # 100 = x, 50 = y, Enemy1 = Java Class Representing the Sprite
     * 1,1,0 1,1,0 1,1,0 # First row of the map. 1 = Tile set ID, 1 = Tile Position ID in the tileset, 0 = tile type
     * 1,1,0 1,1,0 1,1,0 # Second row of the map. 1 = Tile set ID, 1 = Tile Position ID in the tileset, 0 = tile type
     * TBD
     *
     * @param tileMapPath
     */
    public void loadTileMap(String tileMapPath) {

    }

    private String[] getTileInfo(String tileMapInfo) {
        return tileInfo.split(SEPARATOR);
    }

}
