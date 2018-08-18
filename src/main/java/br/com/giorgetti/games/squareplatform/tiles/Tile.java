package br.com.giorgetti.games.squareplatform.tiles;

import java.awt.image.BufferedImage;

/**
 * Each of the tile that is drawn to the screen. A tile can be blocking or non-blocking.
 *
 * Created by fgiorgetti on 5/1/15.
 */
public class Tile {

    private BufferedImage tileImage;

    // Tile position in the tileSet
    private int tilePositionId;

    private TileType type;

    public enum TileType {

        NORMAL(0), BLOCKED(1);

        int type;

        TileType(int type) {
           this.type = type;
        }

        public static TileType fromType(int type) {
            if ( type == 0 )
                return NORMAL;
            else
                return BLOCKED;
        }
        public static TileType fromType(String type) {
            if ( "0".equals(type) ) {
                return NORMAL;
            }

            return BLOCKED;
        }

        public int getType() {
            return this.type;
        }

    }

    public Tile(BufferedImage image, TileType type) {

        this.tileImage = image;
        this.type = type;

    }

    public int getWidth() {
        return tileImage.getWidth();
    }

    public int getHeight() {
        return tileImage.getHeight();
    }

    /* Getters and Setters */

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }

    public void setTileImage(BufferedImage tileImage) {
        this.tileImage = tileImage;
    }

    public int getTilePositionId() {
        return tilePositionId;
    }

    public void setTilePositionId(int tilePositionId) {
        this.tilePositionId = tilePositionId;
    }

}
