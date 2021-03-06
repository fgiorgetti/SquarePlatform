package br.com.giorgetti.games.squareplatform.gameobjects.sprite;

import br.com.giorgetti.games.squareplatform.tiles.TileMap;

/**
 * Provides common behavior to all sprites that move through the
 * screen, performing collision detecting, blocking movement and etc.
 *
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
    public static int FALL_DELAY = 45;

    // Jumping
    public static final int JUMP_SPEED = 8;

    protected int xSpeed;
    protected int ySpeed = FALL_SPEED;
    protected long jumpingStarted;
    protected SpriteDirection direction;
    protected boolean jumping; // Set to true while key is pressed

    protected boolean spriteBlockedBottom;
    protected boolean spriteBlockedLeft;
    protected boolean spriteBlockedRight;
    protected boolean spriteBlockedTop;

    @Override
    public void update(TileMap map) {

        this.map = map;

        setX(getX() + getXSpeed());
        setY(getY() + getYSpeed());
        checkBlockingSprites(getX()-getXSpeed(), getXSpeed(), getY()-getYSpeed(), getYSpeed());

        // Test if sprite is off at bottom of the screen
        checkOffScreenBottom(map);


    }

    protected void checkOffScreenBottom(TileMap map) {
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
        this.xSpeed = xSpeed;
    }
    public void incXSpeed(int xSpeed) {
        this.xSpeed += xSpeed;
        if ( this.xSpeed > MAX_XSPEED )
            this.xSpeed = MAX_XSPEED;
        else if ( this.xSpeed < -MAX_XSPEED )
            this.xSpeed = -MAX_XSPEED;
    }

    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
    public void incYSpeed(int ySpeed) {

        if ( ySpeed == 0 ) {
            this.ySpeed = 0;
            return;
        }

        this.ySpeed += ySpeed;

        if ( this.ySpeed > getJumpSpeed())
            this.ySpeed = getJumpSpeed();
        else if ( this.ySpeed < FALL_SPEED )
            this.ySpeed = FALL_SPEED;

    }

    protected int getJumpSpeed() {
        return JUMP_SPEED;
    }

    @Override
    public void setX(int newX) {

        int oldX = this.x;
        super.setX(newX);

        if ( map == null ) {
            return;
        }

        // Moving right
        if ( this.x > oldX && isBlockedRight() ) {
            // Left X for tile on right side
            int tileLx = (map.getColAt(getX() + getHalfWidth())) * map.getWidth();// left side of tile on the right
            // Sets X based on left side of blocking tile on the right
            // Suppose sprite is at X=27 its width is 10 (half=5)
            // The tile (width 16) at X=32 (or col 3) (player x + player half 5 = 32) is blocked
            // Each tile on X axis goes from 0 to 31 so player X must be 32 - 5 - 1 so they dont collide
            this.x = (tileLx - getHalfWidth() - 1);
            // Moving left
        } else if ( this.x < oldX && isBlockedLeft() ) {
            // Right X for tile on left side
            int tileRx = (map.getColAt(getX()-getHalfWidth())) * map.getWidth() + map.getWidth();// left side of tile on the right
            // Sets X based on right side of blocking tile on the left
            this.x = (tileRx + getHalfWidth());
        }

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
                // Bottom Y for tile on upper side
                int tileBy = (map.getRowAt(getY() + getHalfHeight())) * map.getHeight() - map.getHeight();// bottom y of upper tile
                // Sets player Y based on bottom side of blocking tile on the top
                // Considering bottom is 0 and tile width is 16, it is drawn from 0 to 15 so at y 16 it is the tile above
                // Tile is drawn from 0 to 15 so Player cannot be at tile bottom (ex 16 - 5 half player = 11)
                // Instead player must be drawn from 10 to 15 to avoid colliding with tile above
                this.y = (tileBy - getHalfHeight() - 1);
                setYSpeed(0); // stop jumping
            }

            fall();

            // Falling
        } else if ( this.ySpeed < 0 ) {

            if ( !isBlockedBottom() ) {
                if (!spriteBlockedBottom) {
                    setState(SpriteState.FALLING);
                }
                fall();
            } else {

                if ( getState() == SpriteState.JUMPING ) {
                    setState(SpriteState.FALLING);
                }

                // Top Y for tile on bottom side
                int tileTy = (map.getRowAt(getY() - getHalfHeight())) * map.getHeight();// top y of bottom tile
                // Sets Y based on bottom side of blocking tile on the top
                this.y = (tileTy + getHalfHeight());
                this.ySpeed = FALL_RATE;

                if ( this.state == SpriteState.FALLING ) {
                    if ( this.getXSpeed() != 0 ) {
                        setState(SpriteState.WALKING);
                    } else {
                        setState(SpriteState.IDLE);
                    }
                    this.ySpeed = FALL_RATE;
                }
            }

        } else if ( this.state == SpriteState.JUMPING ) {
            fall();
        }

    }

    protected void checkBlockingSprites(int x, int xSpeed, int y, int ySpeed) {

        if ( this instanceof BlockingSprite) {
            return;
        }

        // Check against other game objects
        int olx = x - getHalfWidth();
        int orx = x + getHalfWidth();
        int oty = y + getHalfHeight();
        int oby = y - getHalfHeight();
        int nx = x + xSpeed;
        int ny = y + ySpeed;
        int lx = x + xSpeed - getHalfWidth();
        int rx = x + xSpeed + getHalfWidth();
        int ty = y + ySpeed + getHalfHeight();
        int by = y + ySpeed - getHalfHeight();

        spriteBlockedTop = false;
        spriteBlockedBottom = false;
        spriteBlockedLeft = false;
        spriteBlockedRight = false;

        for ( Sprite s : map.getGameObjects() ) {

            if ( !(s instanceof BlockingSprite) ) {
                continue;
            }

            int slx = s.getX() - s.getHalfWidth();
            int srx = s.getX() + s.getHalfWidth();
            int sty = s.getY() + s.getHalfHeight();
            int sby = s.getY() - s.getHalfHeight();

            boolean blockedBottom = (getYSpeed() < 0 && by <= sty && by >= sby) &&
                    ((rx > slx && rx < srx)
                            || (lx < srx && lx > slx));

            // If set to true by any of the sprites on map, do not change it to false
            boolean blockedTop = ( oty <= sby && (oty >= sby || ty >= sby) && y <= sty ) && (
                    ( rx >= slx && rx < srx )
                            || ( lx < srx && lx >= slx )
            );

            // If set to true by any of the sprites on map, do not change it to false
            boolean blockedRight = x < slx && rx >= slx && rx < srx && (
                        ( y >= sby && y <= sty ) // y within sprite
                                || ( oty >= sby && oty <=sty ) // top y within sprite
                                || ( oby >= sby && y < sty ) // bottom y within sprite
                                || ( oby <= sby && oty >= sty ) // sprite bigger than blocking object
                );

            // If set to true by any of the sprites on map, do not change it to false
            boolean blockedLeft = x > srx && lx <= srx && lx > slx && (
                        (y >= sby && y <= sty) // y within sprite
                                || (oty >= sby && oty <= sty) // top y within sprite
                                || (oby >= sby && y < sty) // bottom y within sprite
                                || (oby <= sby && oty >= sty) // sprite bigger than blocking object
                );

            if ( blockedRight && !spriteBlockedRight ) {
                spriteBlockedRight = true;
                setXSpeed(0);
                setX(slx-getHalfWidth()-1);
            } else if ( blockedLeft && !spriteBlockedLeft ) {
                spriteBlockedLeft = true;
                setXSpeed(0);
                setX(srx+1+getHalfWidth());
            } else if ( blockedBottom && !spriteBlockedBottom ) {

                spriteBlockedBottom = true;

                if ( isJumpingOrFalling() ) {
                    if (getXSpeed() != 0) {
                        setState(SpriteState.WALKING);
                    } else {
                        setState(SpriteState.IDLE);
                    }
                }

                setYSpeed(FALL_RATE);

                if (s instanceof MovableSprite) {
                    MovableSprite ms = (MovableSprite) s;
                    setY(sty + getHalfHeight() + ms.getYSpeed());
                    setX(getX() + ms.getXSpeed());
                } else {
                    setY(sty + getHalfHeight());
                }


            } else if ( blockedTop && !spriteBlockedTop ) {
                spriteBlockedTop = true;
                setYSpeed(FALL_RATE);
                fall();
                setY(sby-getHalfHeight()-1);
            }

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
        incYSpeed(getJumpSpeed());
        this.jumpingStarted = System.currentTimeMillis();
        setState(SpriteState.JUMPING);
        this.jumping = true;

    }

    public void bounce() {

        //System.out.println("Before jumping y speed: " + getYSpeed());
        setYSpeed(3);
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
        incYSpeed(FALL_RATE);
        //System.out.println(getYSpeed());
        this.jumpingStarted = System.currentTimeMillis();

    }

}
