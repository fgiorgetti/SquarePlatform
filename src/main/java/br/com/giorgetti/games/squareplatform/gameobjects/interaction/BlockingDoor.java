package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.BlockingSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.Sprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

public class BlockingDoor extends InteractiveSprite implements BlockingSprite {

    public BlockingDoor() {
        super();
        this.width = 32;
        this.height = 32;
    }

    @Override
    public void update(TileMap map) {
    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.GRAY);
        g.fillRect(getX() - getHalfWidth() - map.getX(),
                GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                getWidth(), getHeight());

    }

    @Override
    public void executeInteraction() {

        final Sprite me = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                map.removeSprite(me);
            }
        }).start();

    }

}
