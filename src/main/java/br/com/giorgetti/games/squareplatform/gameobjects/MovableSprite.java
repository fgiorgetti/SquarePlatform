package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class MovableSprite extends Sprite {

    // Movement ( left, right)
    public static final int MAX_XSPEED = 3;
    public static int ACCELERATION_DELAY = 300;
    public static int DEACCELERATION_DELAY = 300;
    public static int ACCELERATION_RATE = 1;
    public static int DEACCELERATION_RATE = 1;
    public static int DEACCELERATION_RATE_SNOW = 1; // A value of 1 would be nice for a snow stage

    // Falling
    public static final int FALL_SPEED = -10;
    public static int FALL_RATE = -1;
    public static int FALL_DELAY = 50;

    // Jumping
    public static final int JUMP_SPEED = 18;

    protected int xSpeed;
    protected int ySpeed = FALL_SPEED;
    protected long jumpingStarted;
    protected SpriteDirection direction;
    protected boolean jumping; // Set to true while key is pressed


    @Override
    public void update(TileMap map) {

        this.map = map;
        setX(getX() + getXSpeed());
        setY(getY() + getYSpeed());

        // Fell in a whole off screen
        if ( getY()+getHalfHeight() <= 0 ) {
            //System.out.println(this.getClass().getSimpleName() + " - good bye");
            map.removeSprite(this);
        }

    }

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

    @Override
    public void setY(int newY) {

        // Use the common logic
        super.setY(newY);

        if ( this.map == null ) {
            return;
        }

        // Player customization for Y

        // Jumping
        //System.out.printf("YSpeed = %d - STATE = %s\n", this.ySpeed, this.state);
        if ( this.ySpeed > 0 ) {

            if ( isBlockedTop() ) {
                setYSpeed(0); // stop jumping
            }

            fall();

            // Falling
        } else if ( this.ySpeed < 0 ) {

            if ( !isBlockedBottom() ) {
                setState(SpriteState.FALLING);
                fall();
                //System.out.println("I am falllingggggg !!!!!");
            } else if ( this.state == SpriteState.FALLING ) {
                if ( this.getXSpeed() != 0 ) {
                    setState(SpriteState.WALKING);
                } else {
                    setState(SpriteState.IDLE);
                }
                this.ySpeed = FALL_SPEED;
            }

        } else if ( this.state == SpriteState.JUMPING ) {
            fall();
        }

    }

    public SpriteState getState() {
        return state;
    }

    public void setState(SpriteState state) {
        if ( this.state == state ) {
            return;
        }
        this.state = state;
    }

    public void jump() {

        if ( this.state == SpriteState.JUMPING ||
                this.state == SpriteState.FALLING ) {
            return;
        }

        //System.out.println("Before jumping y speed: " + getYSpeed());
        setYSpeed(JUMP_SPEED);
        this.jumpingStarted = System.currentTimeMillis();
        setState(SpriteState.JUMPING);
        this.jumping = true;

    }

    public void jumpReleased() {
        this.jumping = false;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isJumpingOrFalling() {
        return getState() == SpriteState.JUMPING
                || getState() == SpriteState.FALLING;
    }

    public void fall() {

        long elapsed = System.currentTimeMillis() - jumpingStarted;
        if ( elapsed < FALL_DELAY ) {
            return;
        }
        setYSpeed(FALL_RATE);
        //System.out.println(getYSpeed());
        this.jumpingStarted = System.currentTimeMillis();

    }

}
