package br.com.giorgetti.games.squareplatform.gamestate;

import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Interface that can be implemented by any possible state in the game.
 * An active state is responsible for handling the interaction with the player.
 * Created by fgiorgetti on 5/1/15.
 */
public interface GameState extends KeyListener {

    void update();
    void draw(Graphics2D g);
    void notifySwitchedOff();
    void notifySwitchedOn();

    default void destroy() {}
}
