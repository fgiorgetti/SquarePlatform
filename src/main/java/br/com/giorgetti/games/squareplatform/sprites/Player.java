package br.com.giorgetti.games.squareplatform.sprites;

import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 7/30/15.
 */
public class Player implements Sprite {

    private int playerX, playerY;
    private int playerXSpeed, playerYSpeed;
    private TileMap map = null;

    @Override
    public void update(TileMap map) {

        this.map = map;
        setPlayerX(getPlayerX() + getPlayerXSpeed());
        setPlayerY(getPlayerY() + getPlayerYSpeed());

    }

    @Override
    public void draw(Graphics2D g) {

    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        if ( map != null && playerX > map.getCols() * map.getWidth() ) {
            this.playerX = map.getCols() * map.getWidth();
        } else if ( playerX < 0 ) {
            this.playerX = 0;
        } else {
            this.playerX = playerX;
        }
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        if ( map != null && playerY > map.getRows() * map.getHeight()) {
            this.playerY = map.getRows() * map.getHeight();
        } else if ( playerY < 0 ) {
            this.playerY = 0;
        } else {
            this.playerY = playerY;
        }
    }

    public int getPlayerXSpeed() {
        return this.playerXSpeed;
    }
    public int getPlayerYSpeed() {
        return this.playerYSpeed;
    }
    public void setPlayerXSpeed(int xSpeed) {
        this.playerXSpeed = xSpeed;
        if ( playerXSpeed > 4 )
            this.playerXSpeed = 4;
        else if ( playerXSpeed < -4 )
            this.playerXSpeed = -4;
    }
    public void setPlayerYSpeed(int ySpeed) {
        this.playerYSpeed = ySpeed;
        if ( playerYSpeed > 4 )
            playerYSpeed = 4;
        else if ( playerYSpeed < -4 )
            playerYSpeed = -4;
    }

}
