package br.com.giorgetti.games.squareplatform.gameobjects.interaction.platform;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.BlockingSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.MovableSprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Platform that moves up and down in the map. Movable sprites
 * can interact with the Platform.
 */
public class UpDownPlatform extends MovableSprite implements BlockingSprite {

    private static BufferedImage image;
    private static final int Y_SPEED = 1;
    private static final int MAX_DISTANCE = 100;

    private int distanceMoved = 0;

    static {
        try {
            image = ImageIO.read(UpDownPlatform.class.getResourceAsStream("/sprites/objects/platform.png"));
        } catch (IOException e) {
            image = null;
        }
    }

    public UpDownPlatform() {
        super();
        this.width = 79;
        this.height = 20;
        this.ySpeed = Y_SPEED;
    }

    @Override
    public void update(TileMap map) {

        super.update(map);
        distanceMoved += Math.abs(this.ySpeed);

        if ( distanceMoved >= MAX_DISTANCE ) {
            distanceMoved = 0;
            this.ySpeed = this.ySpeed * -1;
        }

    }

    @Override
    public void setY(int newY) {
        this.y = newY;
    }

    @Override
    public void draw(Graphics2D g) {

        if ( image != null ) {
            g.drawImage(image, getLeftX(), getTopY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.orange);
            g.fillRect(getX() - getHalfWidth() - map.getX(),
                    GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                        getWidth(), getHeight());
        }

    }

}
