package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public class Coin extends AutoInteractiveSprite {

    private final int HEIGHT = 10;
    private final int WIDTH = 5;
    private final int SCORE = 10;

    public Coin() {
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    @Override
    public void executeInteraction() {
        map.getPlayer().addScore(SCORE);
        map.removeSpriteAtPlayer();
    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.black);
        g.fillOval(getX() - getHalfWidth() - map.getX() - 1, GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY() + 1, WIDTH, HEIGHT);
        g.setColor(Color.yellow);
        g.fillOval(getX() - getHalfWidth() - map.getX(), GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(), WIDTH, HEIGHT);

    }

}
