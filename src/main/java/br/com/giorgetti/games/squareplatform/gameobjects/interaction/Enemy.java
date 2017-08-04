package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.Player;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

public abstract class Enemy extends MovableSprite {

    private int health;

    public abstract int getMaxHealth();
    public abstract int getHitDamage();

    public Enemy() {
        health = getMaxHealth();
    }

    public void damage(int damage) {

        if ( isDead() ) {
            return;
        }

        this.health -= damage;

        if (isDead()) {
            die();
        }

    }

    private boolean isDead() {
        return health <= 0;
    }

    protected void die() {
       this.ySpeed = 0;
       fall();
    }

    public boolean isPlayerJumpingOver(Player p) {

        if ( !p.isJumpingOrFalling() ) {
            return false;
        }

        // Player is supposed to be updated already
        int ny = p.getY();
        int nx = p.getX();
        int y = p.getY() - p.getYSpeed();
        int x = p.getX() - p.getXSpeed();

        // Discover the linear function to validate if player is falling above
        int slope = nx-x == 0 ? 0 : ( ny - y ) / ( nx - x );
        int b = ny - ( slope * nx );

//        System.out.printf("ny = %d - y = %d / nx = %d - x = %d | slope = %d - b = %d\n",
 //               ny, y, nx, x, slope, b);

        // f(x) = slope*x + b
        // Now we can trace the player line and check if it crosses a given line

        // f(x) = slope*x + b
        // sty  = slope * x + b   -> finds X at given STY
        int sty = getY() - getHalfHeight();
        int xAtSty = slope == 0 ? nx:( sty - b ) / slope;

        int slx = getX() - getHalfWidth();
        int srx = getX() + getHalfWidth();

        return xAtSty + p.getHalfWidth() >= slx && xAtSty - getHalfWidth() <= srx;

    }

    @Override
    public void update(TileMap map) {

        if ( isDead() ) {

            fall();
            this.y += getYSpeed();

            checkOffScreenBottom(map);

        } else {
            super.update(map);
        }

        if ( hasPlayerCollision() ) {
            map.getPlayer().damage(getHitDamage());
        }

    }
}
