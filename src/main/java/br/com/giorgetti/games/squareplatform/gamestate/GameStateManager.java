package br.com.giorgetti.games.squareplatform.gamestate;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class GameStateManager implements KeyListener {

    public GameState temporaryState = null;
    public GameState currentState = null;
    public GameState previousState = null;
    public GameState initialState = null;

    public GameStateManager(GameState initialGameState) {
        this.currentState = initialGameState;
        this.initialState = initialGameState;
    }
    public void gameLoop(Graphics2D g) {

        if ( temporaryState == null ) {
            currentState.update();
        }
        currentState.draw(g);

        if ( temporaryState != null ) {
            temporaryState.update();
            temporaryState.draw(g);
        }

    }

    public void addTemporaryState(GameState state) {
        this.temporaryState = state;
    }
    public void cleanTemporaryState() {
        this.temporaryState = null;
    }

    public void switchGameState(GameState state) {
        this.previousState = this.currentState;
        this.currentState = state;
    }

    public void revertGameState() {
        if ( this.previousState == null ) {
            return;
        }
        this.currentState = this.previousState;
    }

    public boolean isRunning() {
        return true;
    }

    public void keyTyped(KeyEvent e) {
        if ( temporaryState != null ) {
            temporaryState.keyTyped(e);
        } else {
            currentState.keyTyped(e);
        }
    }

    public void keyPressed(KeyEvent e) {
        if ( temporaryState != null ) {
            temporaryState.keyPressed(e);
        } else {
            currentState.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if ( temporaryState != null ) {
            temporaryState.keyReleased(e);
        } else {
            currentState.keyReleased(e);
        }
    }

}
