package br.com.giorgetti.games.squareplatform.gamestate;

import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public interface GameState extends KeyListener {

    void update();
    void draw(Graphics2D g);
    void notifySwitchedOff();
    void notifySwitchedOn();

    default void destroy() {}
}
