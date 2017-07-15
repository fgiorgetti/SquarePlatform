package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.Sprite;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class InteractiveSprite extends Sprite {

    public abstract void executeInteraction();

    public int getInteractionKeyCode() {
        return KeyEvent.VK_SPACE;
    }

    public void interact(KeyEvent key) {

        //System.out.println("Entered");
        if ( !isOnScreen() ) {
            //System.out.println("! On Screen");
            return;
        }

        //System.out.println("hasCollision? " + hasPlayerCollision());
        //System.out.println("keyCode == interactionkeycode? " + (key.getKeyCode() == getInteractionKeyCode()));
        if ( hasPlayerCollision() && key.getKeyCode() == getInteractionKeyCode() ) {
            executeInteraction();
        }

    }

}
