package br.com.giorgetti.games.squareplatform.gamestate.title;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OptionState implements GameState {

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.blue);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString("Here the options will be displayed", GamePanel.WIDTH/3, 100);

    }

    @Override
    public void notifySwitchedOff() {

    }

    @Override
    public void notifySwitchedOn() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            GamePanel.gsm.switchGameState(new TitleState());
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
