package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.BlockingSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

public class LeftRightPlatform extends MovableSprite implements BlockingSprite {

    private static final int X_SPEED = 1;
    private static final int MAX_DISTANCE = 100;

    private int distanceMoved = 0;

    public LeftRightPlatform() {
        super();
        this.width = 90;
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

        g.setColor(Color.orange);
        g.fillRect(getX() - getHalfWidth() - map.getX(),
                GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                getWidth(), getHeight());

    }

}
