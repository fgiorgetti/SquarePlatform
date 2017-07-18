package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.Sprite;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.event.KeyEvent;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class AutoInteractiveSprite extends Sprite {

    public abstract void executeInteraction();

    @Override
    public void update(TileMap map) {
        interact();
    }

    public void interact() {

        if ( !isOnScreen() ) {
            return;
        }

        if ( hasPlayerCollision() ) {
            executeInteraction();
        }

    }

}
