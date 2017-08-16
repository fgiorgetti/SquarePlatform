package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.Animation;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 23/07/17.
 */
public class EnemyFly extends Enemy {

    private static final int Y_SPEED = 1;
    private static final int MAX_DISTANCE_Y = 10;
    private static final int MAX_DISTANCE_X = 150;

    private int distanceMovedY = 0;
    private int distanceMovedX = 0;

    public boolean ignoreYUpdate = false;

    @Override
    public int getMaxHealth() {
        return 100;
    }

    @Override
    public int getHitDamage() {
        return 25;
    }

    public EnemyFly() {
        super();
        this.width = 26;
        this.height = 20;
        this.xSpeed = 10 * Math.random() >= 5? 1:-1;
        this.ySpeed = Y_SPEED;

        loadAnimation(Animation.newAnimation(SpriteState.WALKING.name(),
                "/sprites/enemies/enemyfly.png",
                26).delay(100));

        setAnimation(SpriteState.WALKING.name());
    }

    @Override
    public boolean isBlockedLeft() {
        if ( super.isBlockedLeft() ) {
            this.xSpeed = 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean isBlockedRight() {
        if (super.isBlockedRight()) {
            this.xSpeed = -1;
            return true;
        }
        return false;
    }

    @Override
    public void update(TileMap map) {

        super.update(map);

        if ( isDead() ) {
            return;
        }

        distanceMovedX += Math.abs(this.xSpeed);

        if ( spriteBlockedLeft || isAtLeftMost(getX()) ) {
            this.xSpeed = 1;
            distanceMovedX = 0;
        } else if ( spriteBlockedRight || isAtRightMost(getX())) {
            this.xSpeed = -1;
            distanceMovedX = 0;
        } else if ( distanceMovedX >= MAX_DISTANCE_X ) {
            this.xSpeed = this.xSpeed * -1;
            distanceMovedX = 0;
        }

        // Skip one frame to reduce flying speed
        ignoreYUpdate = !ignoreYUpdate;
        if ( ignoreYUpdate ) {
            return;
        }

        distanceMovedY += Math.abs(this.ySpeed);
        if ( distanceMovedY >= MAX_DISTANCE_Y) {
            distanceMovedY = 0;
            this.ySpeed = this.ySpeed * -1;
        }

    }

    @Override
    public void setY(int newY) {
        this.y = newY;
    }

    @Override
    public void draw(Graphics2D g) {

        if ( hasPlayerCollision() && !isDead() ) {
            g.setColor(Color.RED);
            g.drawString("Hurting player", getX() - getHalfWidth() - map.getX(), GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY()-25);
        }

        if ( getXSpeed() > 0 ) {
            g.drawImage(getCurrentAnimation(), getLeftX(), getTopY(), getWidth(), getHeight(), null);
        } else {
            g.drawImage(getCurrentAnimation(), getRightX(), getTopY(), -getWidth(), getHeight(), null);
        }

        /*
        g.setColor(Color.RED);
        g.drawRect(getX() - getHalfWidth() - map.getX(),
                    GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                    getWidth(), getHeight());
        */

    }
}
