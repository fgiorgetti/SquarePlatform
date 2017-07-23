package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class AutoInteractiveSprite extends MovableSprite {

    public abstract void executeInteraction();

    @Override
    public void update(TileMap map) {
        super.update(map);
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
