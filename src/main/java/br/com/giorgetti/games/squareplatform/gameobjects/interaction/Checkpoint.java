package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.main.GamePanel;

import java.awt.*;

public class Checkpoint extends AutoInteractiveSprite {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 200;

    private boolean executed = false;

    // Time to display checkpoint on screen in ms
    private static final int DISPLAY_TIME = 1000;
    private static final int INC_SIZE_TIME = 16;
    private static final int MAX_SIZE = 32;

    private long startDisplay;
    private long startIncrease;
    private int size = 8;

    public Checkpoint() {

        this.width = WIDTH;
        this.height = HEIGHT;
        this.ySpeed = 0;

    }

    @Override
    public void executeInteraction() {

        if ( executed ) {
            return;
        }

        executed = true;
        startDisplay = System.currentTimeMillis();
        startIncrease = startDisplay;
        map.checkpoint(getX() + getHalfWidth(), getY() - getHalfHeight() - map.getPlayer().getHalfHeight() );

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

            g.setColor(Color.RED);
            g.drawRect(getX() - getHalfWidth() - map.getX(),
                    GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                    getWidth(), getHeight());
            /*
            */

            return;

        }

        if ( !executed ) {
            return;
        }

        // Checkpoint can be removed
        long curTime = System.currentTimeMillis();
        if ( curTime - startDisplay > DISPLAY_TIME ) {
            map.removeSprite(this);
            return;
        }

        if ( curTime - startIncrease > INC_SIZE_TIME && size < MAX_SIZE ) {
            startIncrease = curTime;
            size++;
        }

        Font originalFont = g.getFont();

        int drawX = GamePanel.WIDTH / 2 - size * 2;
        int drawY = GamePanel.HEIGHT / 3;

        g.setFont(new Font("TimesRoman", Font.BOLD, size));
        g.setColor(Color.black);
        g.drawString("Checkpoint", drawX - 1, drawY - 1);

        g.setColor(Color.white);
        g.drawString("Checkpoint", drawX, drawY);

        g.setFont(originalFont);

    }
}
