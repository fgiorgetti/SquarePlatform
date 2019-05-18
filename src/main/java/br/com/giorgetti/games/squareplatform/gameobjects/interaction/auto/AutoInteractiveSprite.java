package br.com.giorgetti.games.squareplatform.gameobjects.interaction.auto;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.MovableSprite;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

/**
 * Base class for sprites that are triggered automatically when player
 * walks through the given sprite.
 *
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
