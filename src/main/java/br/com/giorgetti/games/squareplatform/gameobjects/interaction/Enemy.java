package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

public abstract class Enemy extends MovableSprite {

    private int health;

    public abstract int getMaxHealth();

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

    @Override
    public void update(TileMap map) {

        super.update(map);

        /*
        // If set to try by any of the sprites on map, do not change it to false
        boolean blockedBottom = (getYSpeed() < 0 && by <= sty && by >= sby) &&
                ((rx > slx && rx < srx)
                        || (lx < srx && lx > slx));
                        */
    }
}
