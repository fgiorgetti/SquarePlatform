package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.main.GamePanel;

import java.awt.*;

public class Checkpoint extends AutoInteractiveSprite {

    private static final int WIDTH = 10;
    private static final int HEIGHT = GamePanel.HEIGHT;

    private boolean executed = false;

    public Checkpoint() {

        this.width = WIDTH;
        this.height = HEIGHT;

    }

    @Override
    public void executeInteraction() {

        if ( executed ) {
            return;
        }

        executed = true;
        map.checkpoint(getX(), getY());

    }

    @Override
    public void draw(Graphics2D g) {

        if ( map.isEditMode() ) {
            g.setColor(Color.black);
            g.drawString("CheckPoint", getX() - getHalfWidth() - map.getX() - 1,
                    GamePanel.HEIGHT - getY() + map.getY() - 1);
            g.setColor(Color.white);
            g.drawString("CheckPoint", getX() - getHalfWidth() - map.getX(),
                    GamePanel.HEIGHT - getY() + map.getY());
        }

    }
}
