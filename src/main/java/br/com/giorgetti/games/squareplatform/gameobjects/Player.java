package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.player.PlayerState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;
import br.com.giorgetti.games.squareplatform.tiles.Tile.TileType;

import java.awt.*;

/**
 * Created by fgiorgetti on 7/30/15.
 */
public class Player extends Sprite {

    private static final int MAX_XSPEED = 8;
    private static final int FALL_SPEED = -6, JUMP_SPEED = +18;
	private int playerX, playerY;
    private int playerXSpeed, playerYSpeed = FALL_SPEED;
    private long accelerationStarted, deaccelerationStarted, jumpingStarted;
    private static int ACCELERATION_DELAY = 100, DEACCELERATION_DELAY = 50, FALL_DELAY = 50;
    private static int ACCELERATION_RATE = 2, DEACCELERATION_RATE = 4, FALL_RATE = -4;
    private static final int PLAYER_HEIGHT_UP = 20, PLAYER_HEIGHT_CROUCH = 10;
    private static final int PLAYER_WIDTH = 16;
    
    private int playerWidth = PLAYER_WIDTH, playerHeight = PLAYER_HEIGHT_UP;

    private PlayerState state;
    private SpriteDirection direction;
    private Animation[] animations; // Animation indexed by state
    
    private TileMap map = null;
    
    // DONE -> Handle acceleration and deacceleration X
    // DONE -> Added player height and width
    // DONE -> Handle movement from player class, not level state...
    // DONE -> Collision as player moves
    // DONE -> Block if in contact with Block tile
    // Gravity
    // Jumping
    // Crouching
    
    @Override
    public void update(TileMap map) {

        this.map = map;
        
        // If not blocked right and moving right or 
        // if not blocked left and moving left
        setPlayerX(getPlayerX() + getPlayerXSpeed());
        setPlayerY(getPlayerY() + getPlayerYSpeed());

        //System.out.printf("Player X / Y = %d / %d\n", getPlayerX(), getPlayerY());
    }

    @Override
    public void draw(Graphics2D g) {

    	// Left line on player
        g.setColor(isBlockedLeft()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerLeftX(), getPlayerTopY(), getPlayerLeftX(), getPlayerBottomY());

        // Right line
        g.setColor(isBlockedRight()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerRightX(), getPlayerTopY(), getPlayerRightX(), getPlayerBottomY());
        
        // Top line
        g.setColor(isBlockedTop()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerLeftX(), getPlayerTopY(), getPlayerRightX(), getPlayerTopY());

        // Bottom line
        g.setColor(isBlockedBottom()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerLeftX(), getPlayerBottomY(), getPlayerRightX(), getPlayerBottomY());

        g.setColor(Color.GREEN);
        g.drawOval(getPlayerLeftX()-1, getPlayerTopY()-1, 1, 1);
        g.drawOval(getPlayerRightX()-1, getPlayerTopY()-1, 1, 1);
        g.drawOval(getPlayerLeftX()-1, getPlayerBottomY()-1, 1, 1);
        g.drawOval(getPlayerRightX()-1, getPlayerBottomY()-1, 1, 1);
        
    }

	public int getPlayerTopY() {
		return GamePanel.HEIGHT-getPlayerY()-getHalfPlayerHeight()+map.getY();
	}

	public int getPlayerBottomY() {
		return getPlayerTopY() + getPlayerHeight();//-1;
	}

	public int getPlayerLeftX() {
		return getPlayerX()-getHalfPlayerWidth()-map.getX();
	}
	
	public int getPlayerRightX() {
		return getPlayerX() + getHalfPlayerWidth() - map.getX();
	}

    private int getHalfPlayerHeight() {
		return getPlayerHeight() / 2;
	}

	private int getHalfPlayerWidth() {
		return getPlayerWidth() / 2;
	}

	public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
    	
    	int oldPlayerX = this.playerX;
    	
        if ( map != null && playerX > map.getCols() * map.getWidth() - getHalfPlayerWidth() - 1 ) {
            this.playerX = map.getCols() * map.getWidth() - getHalfPlayerWidth() - 1;
        } else if ( playerX < getHalfPlayerWidth() ) {
            this.playerX = getHalfPlayerWidth();
        } else {
            this.playerX = playerX;
        }
        
        // If map not yet provided
        if ( this.map == null ) {
        	return;
        }
        
        // Moving right
        if ( this.playerX > oldPlayerX ) {
        	isBlockedRight();
    	// Moving left
        } else if ( this.playerX < oldPlayerX ) {
        	isBlockedLeft();
        }
        
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
    	
    	int oldPlayerY = this.playerY;
    	
        if ( map != null && playerY > map.getRows() * map.getHeight() - getHalfPlayerHeight()) {
            this.playerY = map.getRows() * map.getHeight() - getHalfPlayerHeight();
        } else if ( playerY < getHalfPlayerHeight() + 1) {
            this.playerY = getHalfPlayerHeight() + 1;
        } else {
            this.playerY = playerY;
        }
        
        if ( this.map == null ) {
        	return;
        }
        
        // Jumping
        //System.out.printf("PYSpeed = %d - STATE = %s\n", this.playerYSpeed, this.state);
        if ( this.playerYSpeed > 0 ) {
        	
        	if ( isBlockedTop() ) {
        		setPlayerYSpeed(0); // stop jumping
        	}
        	        	
        	fall();
        	
        // Falling
        } else if ( this.playerYSpeed < 0 ) {
        	
        	if ( !isBlockedBottom() ) {
        		this.state = PlayerState.FALLING;
        		System.out.println("I am falllingggggg !!!!!");
        	} else if ( this.state == PlayerState.FALLING ) {
        		this.state = PlayerState.IDLE;
        		this.playerYSpeed = FALL_SPEED;
        	}
        	if ( this.playerY-1 == getHalfPlayerHeight() ) {
        		System.out.println("You lose"); // Fell in a hole
        	}
        	
        } else if ( this.state == PlayerState.JUMPING ) {
    		fall();
        }
        
    }

    public int getPlayerXSpeed() {
        return this.playerXSpeed;
    }
    public int getPlayerYSpeed() {
        return this.playerYSpeed;
    }
    public void setPlayerXSpeed(int xSpeed) {
        this.playerXSpeed += xSpeed;
        if ( playerXSpeed > MAX_XSPEED )
            this.playerXSpeed = MAX_XSPEED;
        else if ( playerXSpeed < -MAX_XSPEED )
            this.playerXSpeed = -MAX_XSPEED;
    }
    public void setPlayerYSpeed(int ySpeed) {
    	
    	if ( ySpeed == 0 ) {
    		this.playerYSpeed = 0;
    	}
    	
    	if ( ( ySpeed > 0 ) 
    			|| ( ySpeed < 0 ) ) {
    		this.playerYSpeed += ySpeed;
    	} else {
    		this.playerYSpeed = 0;
    	}
        
        if ( playerYSpeed > JUMP_SPEED )
            playerYSpeed = JUMP_SPEED;
        else if ( playerYSpeed < FALL_SPEED )
            playerYSpeed = FALL_SPEED;
        
    }

	public int getPlayerWidth() {
		return playerWidth;
	}

	public void setPlayerWidth(int playerWidth) {
		this.playerWidth = playerWidth;
	}

	public int getPlayerHeight() {
		return playerHeight;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public SpriteDirection getDirection() {
		return direction;
	}

	public void setDirection(SpriteDirection direction) {
		this.direction = direction;
	}

	public void accelerate() {

		long elapsed = System.currentTimeMillis() - accelerationStarted;
		
		// If player inverted the direction, reset the speed to 0
		if ( this.playerXSpeed > 0 && this.direction == SpriteDirection.LEFT ) {
			this.playerXSpeed = 0;
		} else if ( this.playerXSpeed < 0 && this.direction == SpriteDirection.RIGHT ) {
			this.playerXSpeed = 0;
		}
		
		// Not enough time passed
		if ( elapsed < ACCELERATION_DELAY ) {
			return;
		}

		// Accelerate
		this.playerXSpeed += ACCELERATION_RATE * getDirectionFactor();
		if ( this.playerXSpeed * getDirectionFactor() > MAX_XSPEED ) {
			this.playerXSpeed = MAX_XSPEED * getDirectionFactor();
		}
		this.accelerationStarted = System.currentTimeMillis();
		
	}

	public void deaccelerate() {

		if ( this.playerXSpeed == 0 ) {
			return;
		}
		
		long elapsed = System.currentTimeMillis() - deaccelerationStarted;
		if ( elapsed < DEACCELERATION_DELAY ) {
			return;
		}
		
		this.playerXSpeed -= DEACCELERATION_RATE * getDeaccelerationFactor();
		if ( this.playerXSpeed * getDirectionFactor() <= 0 ) {
			this.playerXSpeed = 0;
		}
		this.deaccelerationStarted = System.currentTimeMillis();
		
	}
	
	public void crouch() {
		
		if ( this.playerHeight == PLAYER_HEIGHT_CROUCH ) {
			return;
		}
		
		this.playerHeight = PLAYER_HEIGHT_CROUCH;
		this.playerY -= PLAYER_HEIGHT_CROUCH / 2;
		
	}
	
	public void standup() {

		if ( this.playerHeight == PLAYER_HEIGHT_UP ) {
			return;
		}

		this.playerHeight = PLAYER_HEIGHT_UP;
		this.playerY += PLAYER_HEIGHT_CROUCH / 2;
		
	}
	
	public void jump() {
		
		if ( this.state == PlayerState.JUMPING || 
				this.state == PlayerState.FALLING ) {
			return;
		}
		
		setPlayerYSpeed(JUMP_SPEED);
		this.jumpingStarted = System.currentTimeMillis();
		this.state = PlayerState.JUMPING;
		
	}
	
	public void fall() {
		
		long elapsed = System.currentTimeMillis() - jumpingStarted;
		if ( elapsed < FALL_DELAY ) {
			return;
		}
		
		setPlayerYSpeed(FALL_RATE);
		this.jumpingStarted = System.currentTimeMillis();
		
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
		
		if ( playerXSpeed > 0 ) {
			return 1;
		} else if ( playerXSpeed < 0 ) {
			return -1;
		}
		
		return 0;
		
	}

	/**
	 * Validates whether player is allowed to move left
	 * @return
	 */
	public boolean isBlockedLeft() {
	
        // Checks if right corners of player are blocked
        boolean leftTopBlocked    = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        boolean leftBottomBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        
        // If tiles at left corners are not blocking tiles, then it is ok to move left
        if ( !leftTopBlocked && !leftBottomBlocked ) {
        	return false;
        }
        
        // Right X for tile on left side
        int tileRx = (map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())) * map.getWidth() + map.getWidth();// left side of tile on the right
        
        // Sets player X based on right side of blocking tile on the left
        this.playerX = (tileRx + getHalfPlayerWidth());
        
        return true;
        
	}

	/**
	 * Validates whether player is allowed to move right
	 * @return
	 */
	public boolean isBlockedRight() {

        // Checks if right corners of player are blocked
        boolean rightTopBlocked    = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        boolean rightBottomBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        
        // If tiles at right corners are not blocking tiles, then it is ok to move right
        if ( !rightTopBlocked && !rightBottomBlocked ) {
        	return false;
        }
        
        // Left X for tile on right side
        int tileLx = (map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())) * map.getWidth()-1;// left side of tile on the right
        
        // Sets player X based on left side of blocking tile on the right
        this.playerX = (tileLx - getHalfPlayerWidth());
        
        return true;
        
	}

	/**
	 * Validates whether player is allowed to move up
	 * @return
	 */
	public boolean isBlockedTop() {
		
        // Checks if top corners of player are blocked
        boolean leftTopBlocked    = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        boolean rightTopBlocked   = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        
        // If tiles at top corners are not blocking tiles, then it is ok to move up
        if ( !leftTopBlocked && !rightTopBlocked ) {
        	return false;
        }
        
        // Bottom Y for tile on upper side
        int tileBy = (map.getPlayerRowAt(getPlayerY() + getHalfPlayerHeight())) * map.getHeight() - map.getHeight() - 1;// bottom y of upper tile
        
        // Sets player Y based on bottom side of blocking tile on the top
        this.playerY = (tileBy - getHalfPlayerHeight());
        
        return true;
        
	}

	/**
	 * Validates whether player is allowed to move down
	 * @return
	 */
	public boolean isBlockedBottom() {
		
        // Checks if bottom corners of player are blocked
        boolean leftBottomBlocked    = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        boolean rightBottomBlocked   = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        
        // If tiles at bottom corners are not blocking tiles, then it is ok to move down
        if ( !leftBottomBlocked && !rightBottomBlocked ) {
        	return false;
        }
        
        // Top Y for tile on bottom side
        int tileTy = (map.getPlayerRowAt(getPlayerY() - getHalfPlayerHeight())) * map.getHeight();// top y of bottom tile
        
        // Sets player Y based on bottom side of blocking tile on the top
        this.playerY = (tileTy + getHalfPlayerHeight());
        
        return true;
        
	}

}
