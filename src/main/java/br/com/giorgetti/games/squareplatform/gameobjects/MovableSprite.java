package br.com.giorgetti.games.squareplatform.gameobjects;

import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.FALL_SPEED;
import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.JUMP_SPEED;
import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.MAX_XSPEED;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class MovableSprite extends Sprite {

    protected int xSpeed;
    protected int ySpeed = FALL_SPEED;
    protected SpriteDirection direction;

    public int getXSpeed() {
        return this.xSpeed;
    }

    public int getYSpeed() {
        return this.ySpeed;
    }

    public SpriteDirection getDirection() {
        return direction;
    }

    public void setDirection(SpriteDirection direction) {
        this.direction = direction;
    }

    /**
     * Returns 1 if going right or -1 if left.
     * @return
     */
    public int getDirectionFactor() {
        if ( direction == SpriteDirection.RIGHT ) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * If speed positive, returns 1 meaning going right.
     * If negative, returns -1 meaning its going left.
     * If zero, then return 0, meaning it is idle.
     *
     * @return
     */
    public int getDeaccelerationFactor() {

        if ( xSpeed > 0 ) {
            return 1;
        } else if ( xSpeed < 0 ) {
            return -1;
        }

        return 0;

    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed += xSpeed;
        if ( this.xSpeed > MAX_XSPEED )
            this.xSpeed = MAX_XSPEED;
        else if ( this.xSpeed < -MAX_XSPEED )
            this.xSpeed = -MAX_XSPEED;
    }

    public void setYSpeed(int ySpeed) {

        if ( ySpeed == 0 ) {
            this.ySpeed = 0;
            return;
        }

        this.ySpeed += ySpeed;

        if ( this.ySpeed > JUMP_SPEED )
            this.ySpeed = JUMP_SPEED;
        else if ( this.ySpeed < FALL_SPEED )
            this.ySpeed = FALL_SPEED;

    }

}
