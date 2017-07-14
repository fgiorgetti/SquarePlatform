package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public class Button extends InteractiveSprite {

    private final int HEIGHT = 10;
    private final int WIDTH = 10;

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void executeInteraction() {
    }

    @Override
    public int getInteractionKeyCode() {
        return 0;
    }

    @Override
    public void interact() {

    }

    @Override
    public void update(TileMap map) {

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.black);
        g.fillOval(getX() - map.getX() - 1, GamePanel.HEIGHT - getY() + map.getY() + 1, WIDTH, HEIGHT);
        g.setColor(Color.red);
        g.fillOval(getX() - map.getX(), GamePanel.HEIGHT - getY() + map.getY(), WIDTH, HEIGHT);

    }

}
