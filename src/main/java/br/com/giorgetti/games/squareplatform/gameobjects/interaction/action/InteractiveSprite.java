package br.com.giorgetti.games.squareplatform.gameobjects.interaction.action;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.Sprite;

import java.awt.event.KeyEvent;

/**
 * Base class that must be implemented for all sprites that provide
 * any sort of interaction with player.
 *
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
