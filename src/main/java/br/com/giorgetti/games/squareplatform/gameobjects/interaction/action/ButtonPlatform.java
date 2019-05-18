package br.com.giorgetti.games.squareplatform.gameobjects.interaction.action;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.Animation;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Push button that draws 3 tiles to the right
 * or remove them if pressed twice.
 *
 * Created by fgiorgetti on 08/07/17.
 */
public class ButtonPlatform extends InteractiveSprite {

    private final int TILESET = 1;
    private final int TILE    = 1;
    private final int HEIGHT = 32;
    private final int WIDTH = 24;

    private boolean executing = false;
    private boolean isOn = false;

    protected enum ButtonState {
        OFF, TESTING, ON, FAIL;
    }

    public ButtonPlatform() {
        this.width = WIDTH;
        this.height = HEIGHT;

        loadAnimation(Animation.newAnimation(ButtonState.OFF.name(),
                "/sprites/objects/button.png",
                24).onlyOnce());

        loadAnimation(Animation.newAnimation(ButtonState.TESTING.name(),
                "/sprites/objects/button_testing.png",
                24).delay(250).onlyOnce());

        loadAnimation(Animation.newAnimation(ButtonState.ON.name(),
                "/sprites/objects/button_on.png",
                24).onlyOnce());

        loadAnimation(Animation.newAnimation(ButtonState.FAIL.name(),
                "/sprites/objects/button_fail.png",
                24).delay(500).onlyOnce());

        setAnimation(ButtonState.OFF.name());

    }

    @Override
    public void executeInteraction() {

        if (executing) {
            return;
        }

        executing = true;

        final int colRight = map.getColAt(getX() + map.getWidth() );
        final int rowBelow = map.getRowAt(getY() - map.getHeight() );

        new Thread(() -> {

            setAnimation(ButtonState.TESTING.name());
            if ( !isOn ) {
                for (int i = 0; i < 3; i++) {
                    map.addTileAt(rowBelow, colRight + i, TILESET, TILE, 2);
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                }
                setAnimation(ButtonState.ON.name());
            } else {
                for (int i = 2; i >= 0; i--) {
                    map.removeTileAt(rowBelow, colRight + i);
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                }
                setAnimation(ButtonState.FAIL.name());
            }

            isOn = !isOn;

            executing = false;
        }).start();

    }

    @Override
    public void update(TileMap map) {

    }

    @Override
    public void draw(Graphics2D g) {

        g.drawImage(getCurrentAnimation(), getLeftX(), getTopY(), getWidth(), getHeight(), null);

        if ( debug ) {
            g.setColor(Color.green);
            g.drawString("IsOnScreen? " + isOnScreen(), 10, 10);
            g.drawString("Colliding? " + hasPlayerCollision(), 10, 30);
            g.drawString("X/Y " + getX() + "/" + getY(), 10, 50);
            g.drawString("Player X/Y " + map.getPlayerX() + "/" + map.getPlayerY(), 10, 70);
            g.drawString("Player LX/RX " + map.getPlayerLeftX() + "/" + map.getPlayerRightX(), 10, 90);
            g.drawString("Player TY/BY " + map.getPlayerTopY() + "/" + map.getPlayerBottomY(), 10, 110);
            g.drawString("Map X/Y " + map.getX() + "/" + map.getY(), 10, 130);
            g.drawRect(getLeftX(), getTopY(), getWidth(), getHeight());
        }
    }

}
