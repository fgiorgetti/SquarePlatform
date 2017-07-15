package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public class Button extends InteractiveSprite {

    private final int HEIGHT = 10;
    private final int WIDTH = 10;

    private boolean executing = false;
    private boolean isOn = false;

    public Button() {
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    @Override
    public void executeInteraction() {
        //System.out.println("Executed action");

        if (executing) {
            return;
        }

        executing = true;

        final int colRight = map.getColAt(getX() + map.getWidth() );
        final int rowBelow = map.getRowAt(getY() - map.getHeight() );

        new Thread(new Runnable() {

            @Override
            public void run() {

                if ( !isOn ) {
                    for (int i = 0; i < 3; i++) {
                        map.addTileAt(rowBelow, colRight + i, 1, 8, 1);
                        try { Thread.sleep(500); } catch (InterruptedException e) {}
                    }
                } else {
                    for (int i = 2; i >= 0; i--) {
                        map.removeTileAt(rowBelow, colRight + i);
                        try { Thread.sleep(500); } catch (InterruptedException e) {}
                    }
                }

                isOn = !isOn;

                executing = false;
            }

        }).start();

    }

    @Override
    public void update(TileMap map) {

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.black);
        g.fillOval(getX() - getHalfWidth() - map.getX() - 1, GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY() + 1, WIDTH, HEIGHT);
        g.setColor(Color.red);
        g.fillOval(getX() - getHalfWidth() - map.getX(), GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(), WIDTH, HEIGHT);

        /*
        g.setColor(Color.green);
        g.drawString("IsOnScreen? " + isOnScreen(), 10, 10);
        g.drawString("Colliding? " + hasPlayerCollision(), 10, 30);
        g.drawString("X/Y " + getX() + "/" + getY(), 10, 50);
        g.drawString("Player X/Y " + map.getPlayerX() + "/" + map.getPlayerY(), 10, 70);
        g.drawString("Player LX/RX " + map.getPlayerLeftX() + "/" + map.getPlayerRightX(), 10, 90);
        g.drawString("Player TY/BY " + map.getPlayerTopY() + "/" + map.getPlayerBottomY(), 10, 110);
        g.drawString("Map X/Y " + map.getX() + "/" + map.getY(), 10, 130);
        g.drawRect(getLeftX(), getTopY(), getWidth(), getHeight());
        */
    }

}
