package br.com.giorgetti.games.squareplatform.media;

import br.com.giorgetti.games.squareplatform.gameobjects.player.Player;
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
    private static final int Y_LIMIT = GamePanel.HEIGHT / 100 * 20;
    private static final int X_STOP_LIMIT = GamePanel.WIDTH / 100 * 2;
    private static final int Y_STOP_LIMIT = GamePanel.HEIGHT / 100 * 4;
    private final int minPlayerXLeft;
    private final int minPlayerXRight;
    private final int minPlayerYBottom;
    private final int minPlayerYTop;
    private boolean chasePlayer = false;
    private int chaseSpeedX = 0;
    private int chaseSpeedY = 0;

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

        // Test if need to chase X
        if ( Math.abs(diffX) > X_LIMIT && minLeft && minRight ) {
            chasePlayer = true;
            if ( player.getXSpeed() != 0 ) {
                chaseSpeedX = player.getXSpeed();
            } else {
                chaseSpeedX = diffX > 0 ? 1:-1;
            }
        } else {
            if ( Math.abs(diffX) <= X_STOP_LIMIT ) {
                chaseSpeedX = 0;
            }
        }

        // Test if needs to chase Y
        if ( Math.abs(diffY) > Y_LIMIT && minBottom && minTop ) {
            chasePlayer = true;
            if ( player.getYSpeed() > 0 && diffY > 0 ) {
                chaseSpeedY = player.getYSpeed();
            } else if ( player.getYSpeed() < 0 && diffY < 0 && Math.abs(diffY) > Y_LIMIT  ) {
                chaseSpeedY = player.getYSpeed();
            } else {
                chaseSpeedY = ( diffY > 0 ? 1 : -1 );
            }
        } else {
            if ( Math.abs(diffY) <= Y_STOP_LIMIT ) {
                chaseSpeedY = 0;
            }
        }

        if ( chaseSpeedX == 0 && chaseSpeedY == 0 ) {
            chasePlayer = false;
        }

        // Reached minimum distance to center of screen
        if ( ! chasePlayer ) {
            return;
        }

        map.setX(map.getX() + chaseSpeedX);
        map.setY(map.getY() + chaseSpeedY);

    }

}
