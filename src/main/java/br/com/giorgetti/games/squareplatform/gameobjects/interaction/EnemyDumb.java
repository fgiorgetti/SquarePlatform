package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 23/07/17.
 */
public class EnemyDumb extends MovableSprite {

    public EnemyDumb() {
        super();
        this.width = 15;
        this.height = 10;
        this.xSpeed = 10 * Math.random() >= 5? 1:-1;
    }

    @Override
    public boolean isBlockedLeft() {
        return updateDirection(super.isBlockedLeft());
    }

    private boolean updateDirection(boolean blocked) {
        if ( blocked ) {
            this.xSpeed = this.xSpeed * - 1;
        }
        return blocked;
    }

    @Override
    public boolean isBlockedRight() {
        return updateDirection(super.isBlockedRight());
    }


    @Override
    public void draw(Graphics2D g) {

        if ( hasPlayerCollision() ) {
            g.setColor(Color.RED);
            g.drawString("Hurting player", getX() - getHalfWidth() - map.getX(), GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY()-25);
        }

        g.setColor(Color.RED);
        g.drawRect(getX() - getHalfWidth() - map.getX(),
                    GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                    getWidth(), getHeight());

    }
}
