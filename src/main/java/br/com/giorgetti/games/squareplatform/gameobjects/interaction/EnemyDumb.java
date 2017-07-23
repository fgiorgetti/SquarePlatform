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
    public void update(TileMap map) {

        super.update(map);

        //System.out.printf("X=%d/Y=%d - XSPEED = %d\n", getX(), getY(), getXSpeed());
        if ( isBlockedLeft() || isBlockedRight() || getX() - getHalfWidth() == 0 ) {
            this.xSpeed = this.xSpeed * -1;
        }

    }

    @Override
    public void draw(Graphics2D g) {

        if ( hasPlayerCollision() ) {
            g.drawString("Hurting player", getX(), getY()+25);
        }

        g.setColor(Color.RED);
        g.drawRect(getX() - getHalfWidth() - map.getX(),
                    GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                    getWidth(), getHeight());

    }
}
