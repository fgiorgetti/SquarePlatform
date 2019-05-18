package br.com.giorgetti.games.squareplatform.gameobjects.interaction.action;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.BlockingSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.Sprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Blocking door is a sprite that blocks player from passing through it
 * and interaction is done by OpenBlockingDoor object.
 * This is still unfinished work. It needs to be customized as when a BlockingDoor
 * is added to the map, it must also add a OpenBlockingDoor object next to it.
 */
public class BlockingDoor extends InteractiveSprite implements BlockingSprite {

    public BlockingDoor() {
        super();
        this.width = 30;
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
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            map.removeSprite(me);
        }).start();

    }

}
