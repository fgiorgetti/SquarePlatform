package br.com.giorgetti.games.squareplatform.tiles;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of tiles defined through a single
 * image. All tiles within the TileSet are supposed to have
 * the same size.
 *
 * Created by fgiorgetti on 5/1/15.
 */
public class TileSet {

    private Map<Integer,Tile> tileSet;
    private BufferedImage imageSet;
    private int spriteWidth;
    private int spriteHeight;
    private int numTiles;

    public TileSet(String resourceImagePath, int spriteWidth, int spriteHeight, int numTiles) {

        try {
            this.imageSet = ImageIO.read(this.getClass().getResourceAsStream(resourceImagePath));
        } catch (IOException e) {
            this.imageSet = new BufferedImage(0,0,BufferedImage.TYPE_INT_RGB);
        }

        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numTiles = numTiles;

        tileSet = new HashMap<Integer, Tile>();

        loadTileSet();
    }

    /**
     * Load tiles from the full image into the tileSet map.
     */
    private void loadTileSet() {

        // Loop through all image rows
        int tilesRead = 0;
        out:
        for ( int row = 0; row < imageSet.getHeight() / spriteHeight; row++ ) {
            for ( int col = 0 ; col < imageSet.getWidth() / spriteWidth ; col++ ) {

                // If read total number of sprites available, stop reading
                if ( tilesRead == numTiles ) {
                    break out;
                }

                // Creating and loading new tile
                Tile tile = new Tile(imageSet.getSubimage(col * spriteWidth, row * spriteHeight, spriteWidth, spriteHeight),
                                Tile.TileType.BLOCKED);
                tileSet.put(tilesRead++, tile);
            }
        }
    }

    /**
     * Returns the tile at the given position.
     *
     * Notice that positions are defined from first row, first column
     * to the right and bottom. In example, if you have a tile image with two
     * rows and four columns:
     *  - The first tile at the first row will get position 0.
     *  - The second tile at the second row will get position 5.
     *
     * @param position
     * @return
     */
    public Tile getTile(String position) {
       return getTile(Integer.parseInt(position));
    }
    public Tile getTile(int position) {
        return tileSet.get(position);
    }
}
