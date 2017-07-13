package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.Sprite;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class InteractiveSprite extends Sprite {

    private boolean canInteract = false;

    public abstract void executeInteraction();

    public abstract int getInteractionKeyCode();

    public void interact() {

        if ( canInteract ) {
            executeInteraction();
        }

    }

    public void checkCollision() {

        if ( getX() >= this.map.getPlayerLeftX() &&
                getX() <= this.map.getPlayerRightX() &&
                getY() >= this.map.getPlayerBottomY() &&
                getY() <= this.map.getPlayerTopY() ) {
            canInteract = true;
        } else {
            canInteract = false;
        }

    }
}
