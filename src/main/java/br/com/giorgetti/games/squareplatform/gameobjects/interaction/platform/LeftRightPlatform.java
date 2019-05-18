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
 * Platforms that moves from left to right and allows movable
 * sprites to interact with it.
 */
public class LeftRightPlatform extends MovableSprite implements BlockingSprite {

    private static BufferedImage image;
    private static final int X_SPEED = 1;
    private static final int MAX_DISTANCE = 100;

    private int distanceMoved = 0;

    static {
        try {
            image = ImageIO.read(UpDownPlatform.class.getResourceAsStream("/sprites/objects/platform.png"));
        } catch (IOException e) {
            image = null;
        }
    }
    public LeftRightPlatform() {
        super();
        this.width = 79;
        this.height = 20;
        this.xSpeed = X_SPEED;
        this.ySpeed = 0;
    }

    @Override
    public void update(TileMap map) {

        super.update(map);
        distanceMoved += Math.abs(this.xSpeed);

        if ( distanceMoved >= MAX_DISTANCE ) {
            distanceMoved = 0;
            this.xSpeed = this.xSpeed * -1;
        }

    }

    @Override
    public void setY(int newY) {
        this.y = newY;
    }

    @Override
    public void setX(int newX) {
        this.x = newX;
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
