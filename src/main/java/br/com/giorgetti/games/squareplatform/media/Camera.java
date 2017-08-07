package br.com.giorgetti.games.squareplatform.media;

import br.com.giorgetti.games.squareplatform.gameobjects.Player;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

/**
 * Adjusts map coordinates based on player position,
 * but instead of keeping player centered on screen
 * it offers allows setting a percentage from the center
 * that wont cause map to move.
 *
 */
public class Camera {

    private final Player player;
    private final TileMap map;

    private static final int X_LIMIT = GamePanel.WIDTH / 100 * 15;
    private static final int Y_LIMIT = GamePanel.HEIGHT / 100 * 15;
    private final int minPlayerXLeft;
    private final int minPlayerXRight;
    private final int minPlayerYBottom;
    private final int minPlayerYTop;

    public Camera(TileMap map) {
        this(map, null);
    }
    public Camera(TileMap map, Player player) {
        this.map = map;
        this.player = player;
        this.minPlayerXLeft = GamePanel.WIDTH/2+X_LIMIT;
        this.minPlayerXRight = map.getMaxX() + GamePanel.WIDTH/2 - X_LIMIT;
        this.minPlayerYBottom = GamePanel.HEIGHT/2+Y_LIMIT;
        this.minPlayerYTop = map.getMaxY() + GamePanel.HEIGHT/2 - Y_LIMIT;
    }

    public void update() {

        if ( this.player == null ) {
            return;
        }

        int mapCenterX = map.getX() + GamePanel.WIDTH / 2;
        int mapCenterY = map.getY() + GamePanel.HEIGHT / 2;

        int diffX = player.getX() - mapCenterX;
        int diffY = player.getY() - mapCenterY;

        boolean minLeft = player.getX() > minPlayerXLeft || (player.getX() < minPlayerXLeft && map.getX() > 0);
        boolean minRight = player.getX() < minPlayerXRight || (player.getX() > minPlayerXRight && map.getX() < map.getMaxX());

        boolean minBottom = player.getY() > minPlayerYBottom || ( player.getY() < minPlayerYBottom && map.getY() > 0);
        boolean minTop = player.getY() < minPlayerYTop || (player.getY() > minPlayerYTop && map.getY() < map.getMaxY());

        //System.out.println("Math.abs(diffX) > X_LIMIT = " + (Math.abs(diffX) > X_LIMIT) );
        //System.out.println("minLeft  = " + minLeft);
        //System.out.println("minRight  = " + minRight);

        if (  Math.abs(diffX) > X_LIMIT && minLeft && minRight ) {
            int speed = 0;
            if ( player.getXSpeed() != 0 ) {
                speed = player.getXSpeed();
            } else {
                speed = diffX > 0 ? 1:-1;
            }
            map.setX(map.getX() + speed);
        }

        if ( Math.abs(diffY) > Y_LIMIT && minBottom && minTop ) {
            int speed = 0;
            if ( player.isJumpingOrFalling() ) {
                speed = player.getYSpeed();
            } else {
                speed = ( diffY > 0 ? 1 : -1 );
            }
            map.setY(map.getY() + speed);
        }

    }

}
