package br.com.giorgetti.games.squareplatform.gameobjects.interaction.action;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.Sprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Opens (currently) all BlockingDoor objects in the map.
 * This must be changed to receive a BlockingDoor in its constructor
 * and when a BlockingDoor is added into the map, an instance of this object
 * is also added close to it.
 */
public class OpenBlockingDoor extends InteractiveSprite {

    private static final Class<? extends InteractiveSprite> CLASSTOINTERACT = BlockingDoor.class;
    public OpenBlockingDoor() {
        super();
        this.width = 8;
        this.height = 8;
    }

    @Override
    public void update(TileMap map) {
    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.drawOval(getX() - getHalfWidth() - map.getX() - 1,
                GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY() - 1,
                getWidth(), getHeight());

        g.setColor(Color.GRAY);
        g.drawOval(getX() - getHalfWidth() - map.getX(),
                GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                getWidth(), getHeight());

    }

    @Override
    public void executeInteraction() {

        for ( Sprite s : map.getGameObjects() ) {
            if ( !CLASSTOINTERACT.isInstance(s) ) {
                continue;
            }
            BlockingDoor b = (BlockingDoor) s;
            b.executeInteraction();
        }

    }

}
