package br.com.giorgetti.games.squareplatform.gamestate;

import br.com.giorgetti.games.squareplatform.gamestate.levels.LevelStateManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class GameStateManager implements KeyListener {

    public GameState currentState = new LevelStateManager("/maps/level1.dat");

    public void gameLoop(Graphics2D g) {

        currentState.update();
        currentState.draw(g);

    }

    public boolean isRunning() {
        return true;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        currentState.keyPressed(e);

    }

    public void keyReleased(KeyEvent e) {

    }

}
