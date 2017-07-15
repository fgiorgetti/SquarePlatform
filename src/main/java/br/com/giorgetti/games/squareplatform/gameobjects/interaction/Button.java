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

    public Button() {
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    @Override
    public void executeInteraction() {
        System.out.println("Executed action");
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
        g.drawString("Colliding? " + hasPlayerCollision(), 10, 30);
        g.drawString("X/Y " + getX() + "/" + getY(), 10, 50);
        g.drawString("Player X/Y " + map.getPlayerX() + "/" + map.getPlayerY(), 10, 70);
        g.drawString("Player LX/RX " + map.getPlayerLeftX() + "/" + map.getPlayerRightX(), 10, 90);
        g.drawString("Player TY/BY " + map.getPlayerTopY() + "/" + map.getPlayerBottomY(), 10, 110);
        g.drawRect(getLeftX(), getTopY(), getWidth(), getHeight());
        */
    }

}
