package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.player.PlayerState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 7/30/15.
 */
public class Player extends Sprite {

	
    private static final int MAX_XSPEED = 8;
	private int playerX, playerY;
    private int playerXSpeed, playerYSpeed;
    private long accelerationStarted, deaccelerationStarted;
    private static int ACCELERATION_DELAY = 100, DEACCELERATION_DELAY = 50;
    private static int ACCELERATION_RATE = 2, DEACCELERATION_RATE = 4;
    
    private int playerWidth = 16, playerHeight = 20;

    private PlayerState state;
    private SpriteDirection direction;
    private Animation[] animations; // Animation indexed by state
    
    private TileMap map = null;

    
    // DONE -> Handle acceleration and deacceleration X
    // DONE -> Added player height and width
    // Handle movement from player class, not level state...
    // Collision as player moves
    // Block if in contact with Block tile
    // Gravity
    // Jumping
    // Crouching
    
    @Override
    public void update(TileMap map) {

        this.map = map;
        setPlayerX(getPlayerX() + getPlayerXSpeed());
        setPlayerY(getPlayerY() + getPlayerYSpeed());

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.drawRect(getPlayerLeftX(), getPlayerTopY(), getPlayerWidth(), getPlayerHeight());

    }

	private int getPlayerTopY() {
		return GamePanel.HEIGHT-getPlayerY()-getHalfPlayerHeight()+map.getY();
	}

	private int getPlayerLeftX() {
		return getPlayerX()-getHalfPlayerWidth()-map.getX();
	}
	
	private int getPlayerBottomY() {
		return getPlayerTopY() + getPlayerHeight();
	}

	private int getPlayerRightX() {
		return getPlayerLeftX() + getPlayerWidth();
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
        if ( map != null && playerX > map.getCols() * map.getWidth() - getHalfPlayerWidth() - 1 ) {
            this.playerX = map.getCols() * map.getWidth() - getHalfPlayerWidth() - 1;
        } else if ( playerX < getHalfPlayerWidth() ) {
            this.playerX = getHalfPlayerWidth();
        } else {
            this.playerX = playerX;
        }
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        if ( map != null && playerY > map.getRows() * map.getHeight() - getHalfPlayerHeight()) {
            this.playerY = map.getRows() * map.getHeight() - getHalfPlayerHeight();
        } else if ( playerY < getHalfPlayerHeight() + 1) {
            this.playerY = getHalfPlayerHeight() + 1;
        } else {
            this.playerY = playerY;
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
        this.playerYSpeed += ySpeed;
        if ( playerYSpeed > 12 )
            playerYSpeed = 12;
        else if ( playerYSpeed < -12 )
            playerYSpeed = -12;
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

	public void setPlayerHeight(int playerHeight) {
		this.playerHeight = playerHeight;
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
		
		// Increase speed now
		if ( elapsed > ACCELERATION_DELAY ) {
			this.playerXSpeed += ACCELERATION_RATE * getDirectionFactor();
			if ( this.playerXSpeed * getDirectionFactor() > MAX_XSPEED ) {
				this.playerXSpeed = MAX_XSPEED * getDirectionFactor();
			}
			this.accelerationStarted = System.currentTimeMillis();
		}
		
	}
	
	public void deaccelerate() {

		if ( this.playerXSpeed == 0 ) {
			return;
		}
		
		long elapsed = System.currentTimeMillis() - deaccelerationStarted;
		if ( elapsed > DEACCELERATION_DELAY ) {
			this.playerXSpeed -= DEACCELERATION_RATE * getDeaccelerationFactor();
			if ( this.playerXSpeed * getDirectionFactor() <= 0 ) {
				this.playerXSpeed = 0;
			}
			this.deaccelerationStarted = System.currentTimeMillis();
		}
		
	}
	
	public int getDirectionFactor() {
		if ( direction == SpriteDirection.RIGHT ) {
			return 1;
		} else {
			return -1;
		}
	}

	public int getDeaccelerationFactor() {
		
		if ( playerXSpeed > 0 ) {
			return 1;
		} else if ( playerXSpeed < 0 ) {
			return -1;
		}
		
		return 0;
		
	}
	
}
