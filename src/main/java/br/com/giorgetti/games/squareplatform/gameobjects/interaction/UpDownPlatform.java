package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.BlockingSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

public class UpDownPlatform extends MovableSprite implements BlockingSprite {

    private static final int Y_SPEED = 1;
    private static final int MAX_DISTANCE = 100;

    private int distanceMoved = 0;

    public UpDownPlatform() {
        super();
        this.width = 80;
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

        g.setColor(Color.orange);
        g.fillRect(getX() - getHalfWidth() - map.getX(),
                GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                getWidth(), getHeight());

    }
}
