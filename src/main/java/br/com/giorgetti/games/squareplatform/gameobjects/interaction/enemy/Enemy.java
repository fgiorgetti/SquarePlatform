package br.com.giorgetti.games.squareplatform.gameobjects.interaction.enemy;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.MovableSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.player.Player;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

/**
 * Default behavior for all Enemies in the game.
 * They can damage the player, die when falling off the screen or when
 * player jumps over them.
 */
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

    protected boolean isDead() {
        return health <= 0;
    }

    protected void die() {
       this.ySpeed = 0;
       this.xSpeed = 0;
       fall();
    }

    public boolean isPlayerJumpingOver(Player p) {

        if ( !p.isJumpingOrFalling() ) {
            return false;
        }

        // Player is supposed to be updated already
        int ny = p.getY() - p.getHalfHeight();
        int nx = p.getX();
        int y = p.getY() - p.getHalfHeight() - p.getYSpeed();
        int x = p.getX() - p.getXSpeed();

        // New Y must be smaller than Y
        if ( ny >= y ) {
            return false;
        }

        // Discover the linear function to validate if player is falling above
        // Gets player previous position (x,y) and new position (nx,ny)
        // Then discover the f(x) so we can find what is the player's X
        // at a given Sprite's Y.
        int slope = nx-x == 0 ? 0 : ( ny - y ) / ( nx - x );
        int b = ny - ( slope * nx );

        // f(x) = slope*x + b
        // Now we can trace the player line and check if it crosses a given line
        // To use it, replace f(x) by the sprite's y
        // and then we will be able to figure out Player's X at a given
        // Sprite's Y coordinate.

        // f(x) = slope*x + b
        // sty  = slope * x + b   -> finds X at given STY
        int sty = getY() + getHalfHeight();
        int xAtSty = slope == 0 ? nx:( sty - b ) / slope;

        int slx = getX() - getHalfWidth();
        int srx = getX() + getHalfWidth();

        //System.out.printf("Player Y = %d / NY = %d / STY = %d\n", y, ny, sty);
        return y > sty && ny <= sty && xAtSty + p.getHalfWidth() >= slx && xAtSty - getHalfWidth() <= srx;

    }

    @Override
    public void update(TileMap map) {

        if ( isDead() ) {

            fall();
            this.y += getYSpeed();

            checkOffScreenBottom(map);

            return;

        } else {
            super.update(map);
        }

        if ( hasPlayerCollision() ) {
            map.getPlayer().damage(getHitDamage());
        }

    }
}
